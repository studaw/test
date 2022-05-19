package com.blog.controller;

import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.dto.user.UserInfoDto;
import com.blog.pojo.*;
import com.blog.service.*;
import com.blog.utils.CommonPage;
import com.blog.utils.CommonResult;
import com.blog.utils.CommonUtils;
import com.blog.vo.ArticleTypeTreeVo;
import com.blog.vo.ArticleVo;
import com.blog.vo.CommentVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @program: blog
 * @description:
 **/

@Controller
@Slf4j
public class ViewController {

    @Autowired
    UserService userService;
    @Autowired
    ArticleService articleService;
    @Autowired
    ArticleTypeService articleTypeService;
    @Autowired
    ArticleTagListService articleTagListService;
    @Autowired
    ArticleTagService articleTagService;
    @Autowired
    AdTypeService adTypeService;
    @Autowired
    AdService adService;
    @Autowired
    LinkService linkService;
    @Autowired
    UserCollectionArticleService userCollectionArticleService;
    @Autowired
    CommentService commentService;

    /**
     * 获取图像验证码
     *
     * @throws IOException
     */

    /**
     * 用户登录
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/getCaptcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CircleCaptcha captcha = CommonUtils.getCaptcha(request);
        captcha.write(response.getOutputStream());
    }

    @GetMapping("/login")
    public String userLogin(HttpServletRequest request,Model model){
        if(Objects.nonNull(request.getSession().getAttribute("user"))){
            return "redirect:/";
        }
        model.addAttribute("referer", request.getHeader("referer"));
        return "/view/login";
    }

    @PostMapping("/userLogin")
    @ResponseBody
    public CommonResult userLogin(HttpServletRequest request, UserInfoDto userInfoDto){
        HttpSession session = request.getSession();
        String verifyCode = userInfoDto.getVerifyCode();
        if(StrUtil.isBlank(verifyCode)||!verifyCode.equals(session.getAttribute("circleCaptchaCode"))){
            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("验证码不正确");
        }
        User one = userService.getOne(Wrappers.<User>lambdaQuery().
                eq(User::getUsername, userInfoDto.getUsername()), false);
        if(Objects.isNull(one)){
            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("账户不正确");
        }

        //Objects.nonNull 它实际上只是提供给 IDE 来辅助检测代码中是否非法的传递了 null 给一个 parameter
        if(Objects.nonNull(one.getUserFrozen())&&(one.getUserFrozen()==1)){
            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("你的账户被冻结，请联系管理员");
        }
        if(!SecureUtil.md5(one.getUserId()+userInfoDto.getPassword()).equals(one.getPassword())){
            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("密码输入错误");
        }

        session.setAttribute("user",one);
        return CommonResult.success("登陆成功");
    }

    /**
     * 用户注册
     * @param request
     * @param model
     * @return
     */
    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        ServletContext servletContext=request.getServletContext();
//            获取树形结构
        List<ArticleTypeTreeVo> articleTypeList =(List<ArticleTypeTreeVo>) servletContext.getAttribute("articleTypeList");
        if(CollUtil.isEmpty(articleTypeList)) {
            articleTypeList = articleTypeService.getIndexArticleTypeList(null);
            if (CollUtil.isNotEmpty(articleTypeList)) {
                for (ArticleTypeTreeVo articleTypeTreeVo : articleTypeList) {
                    articleTypeTreeVo.setArticleTypeTreeVoList(articleTypeService.getIndexArticleTypeList(articleTypeTreeVo.getArticleTypeId()));
                }
                servletContext.setAttribute("articleTypeList", articleTypeList);
            }
        }

//        热门文章
        List<Article> articleHotList =(List<Article>) servletContext.getAttribute("articleHotList");
        if(CollUtil.isEmpty(articleHotList)) {
            articleHotList = articleService.list(Wrappers.<Article>lambdaQuery()
                    .eq(Article::getArticleHot, 0)
                    .select(Article::getArticleId, Article::getArticleTitle)
                    .last("limit 5"));

            servletContext.setAttribute("articleHotList", articleHotList);
        }
//        热门标签
        List<ArticleTag> articleTagList =(List<ArticleTag>) servletContext.getAttribute("articleTagList");
        if(CollUtil.isEmpty(articleTagList)) {
            articleTagList = articleTagService.list(Wrappers.<ArticleTag>lambdaQuery()
                    .select(ArticleTag::getArticleTagId, ArticleTag::getArticleTagName));
            servletContext.setAttribute("articleTagList", articleTagList);
        }
//广告
        List<Ad> adIndexList =(List<Ad>) servletContext.getAttribute("adIndexList");
        if(CollUtil.isEmpty(adIndexList)) {
            AdType homeAd = adTypeService.getOne(Wrappers.<AdType>lambdaQuery()
                    .eq(AdType::getAdTypeTag, "homeAd")
                    .select(AdType::getAdTypeId), false);
            if (Objects.nonNull(homeAd)) {
                DateTime date = DateUtil.date();
                adIndexList = adService.list(Wrappers.<Ad>lambdaQuery()
                        .eq(Ad::getAdTypeId, homeAd.getAdTypeId())
                        .lt(Ad::getAdBeginTime, date)
                        .gt(Ad::getAdEndTime, date)
                        .select(Ad::getAdId, Ad::getAdLinkUrl, Ad::getAdImgUrl, Ad::getAdTitle)
                        .orderByAsc(Ad::getAdSort));

                servletContext.setAttribute("adIndexList", adIndexList);
            }
        }

//        首页最新文章
        List<ArticleVo> indexArticleList =(List<ArticleVo>) servletContext.getAttribute("indexArticleList");
        if(CollUtil.isEmpty(indexArticleList)) {
            indexArticleList = articleService.getIndexArticleList();

            servletContext.setAttribute("indexArticleList", indexArticleList);
        }
//        友情链接
        List<Link> linkList =(List<Link>) servletContext.getAttribute("linkList");
        if(CollUtil.isEmpty(linkList)){
            linkList =linkService.list(Wrappers.<Link>lambdaQuery().orderByAsc(Link::getLinkSort));
        }
        servletContext.setAttribute("linkList", linkList);

        return "/view/index";
    }

    @GetMapping("/register")
    public String register(HttpServletRequest request){
        if (Objects.nonNull(request.getSession().getAttribute("user"))) {
            return "redirect:/";
        }

        return "/view/register";
    }

    @PostMapping("/userRegister")
    @ResponseBody
    public CommonResult userRegister(HttpServletRequest request,UserInfoDto userInfoDto){
        HttpSession session = request.getSession();
        String verifyCode = userInfoDto.getVerifyCode();
        if(StrUtil.isBlank(verifyCode)||!verifyCode.equals(session.getAttribute("circleCaptchaCode"))){
            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("验证码不正确");
        }

        if(userInfoDto.getUsername().equals(userInfoDto.getPassword())){

            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("用户名和密码不能一样");
        }

        if(userService.count(Wrappers.<User>lambdaQuery().eq(User::getUsername, userInfoDto.getUsername()))>0){
            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("该用户名已被注册,请重试");
        }
        User user=new User();
        BeanUtil.copyProperties(userInfoDto,user);

        user.setUserId(IdUtil.simpleUUID());
        user.setPassword(SecureUtil.md5(user.getUserId() + user.getPassword()));
        user.setUserFrozen(0);
        user.setUserRegisterTime(DateUtil.date());
        if(userService.save(user)){
            return CommonResult.success("注册成功");
        }
        return CommonResult.failed("注册失败，请重试");
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public String logOut(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return "/";
    }


    @GetMapping("/article/list")
    public String articleListView(Integer pageNumber, String articleTitle, String articleTypeId, Model model) {
        Page<ArticleVo> articlePage = new Page<>(Objects.isNull(pageNumber) ? 1 : pageNumber, 24);
        IPage<ArticleVo> articleVoIPage = articleService.articleListView(articlePage, articleTitle, articleTypeId);

        //文章列表
        model.addAttribute("articleVoIPage", CommonPage.restPage(articleVoIPage));

        //文章分类名称
        if (StrUtil.isNotBlank(articleTypeId)) {
            ArticleType articleType = articleTypeService.getOne(Wrappers.<ArticleType>lambdaQuery().eq(ArticleType::getArticleTypeId, articleTypeId).select(ArticleType::getArticleTypeName), false);
            model.addAttribute("articleTypeName", articleType.getArticleTypeName());
            model.addAttribute("articleTypeId", articleTypeId);
        }

        return "/view/articleList";
    }

    @GetMapping("/tag/article/list")
    public String tagArticleList(String articleTagId, Integer pageNumber, Model model) {
        if (StrUtil.isBlank(articleTagId)) {
            return "redirect:/";
        }
        if (Objects.isNull(pageNumber) || pageNumber < 1) {
            pageNumber = 1;
        }
        Page<ArticleVo> articlePage = new Page<>(Objects.isNull(pageNumber) ? 1 : pageNumber, 24);
        IPage<ArticleVo> articleVoIPage = articleService.tagArticleList(articlePage, articleTagId);
        model.addAttribute("articleVoIPage", CommonPage.restPage(articleVoIPage));

        //获取标签类型
        ArticleTag articleTag = articleTagService.getOne(Wrappers.<ArticleTag>lambdaQuery().eq(ArticleTag::getArticleTagId, articleTagId));
        if (Objects.nonNull(articleTag)) {
            model.addAttribute("articleTagName", articleTag.getArticleTagName());
        }

        model.addAttribute("articleTagId", articleTagId);
        return "/view/tagArticleList";

    }

    @GetMapping("/article")
    public String articleView(HttpServletRequest request, String articleId, Model model) {
        HttpSession session = request.getSession();

        ArticleVo articleVo = articleService.getArticle(articleId);
        if (Objects.isNull(articleVo)) {
            return "redirect:/";
        }

        Article article = articleService.getOne(Wrappers.<Article>lambdaQuery().eq(Article::getArticleId, articleVo.getArticleId()).select(Article::getArticleId, Article::getArticleLookNumber), false);
        //添加查看次数
        Integer articleLookNumber = article.getArticleLookNumber();
        if (Objects.isNull(articleLookNumber) || articleLookNumber < 0) {
            articleLookNumber = 0;
        }
        ++articleLookNumber;
        article.setArticleLookNumber(articleLookNumber);
        articleService.updateById(article);


        //隐藏作者用户名
        String userName = articleVo.getUserName();
        if (StrUtil.isNotBlank(userName)) {
            articleVo.setUserName(CommonUtils.getHideMiddleStr(userName));
        }

        //文章
        model.addAttribute("article", articleVo);

        //文章类型
        if (Objects.nonNull(articleVo) && StrUtil.isNotBlank(articleVo.getArticleTypeId())) {
            ArticleType articleType = articleTypeService.getOne(Wrappers.<ArticleType>lambdaQuery().eq(ArticleType::getArticleTypeId, articleVo.getArticleTypeId()).select(ArticleType::getArticleTypeName, ArticleType::getArticleTypeId), false);
            model.addAttribute("articleType", articleType);
        }

        return "/view/article";
    }

    @GetMapping("/contact")
    public String contact() {
        return "/view/contact";
    }

    /**
     * 捐赠
     *
     * @return
     */
    @GetMapping("/donation")
    public String donation(Model model) {
        return "/view/donation";
    }


    /**
     * 文章点赞
     *
     * @param request
     * @param articleId
     * @return
     */
    @PostMapping("/articleGood")
    @ResponseBody
    public CommonResult articleGood(HttpServletRequest request, String articleId) {
        HttpSession session = request.getSession();
        if (Objects.nonNull(session.getAttribute("articleGoodTime"))) {
            return CommonResult.failed("客官！您已经点过啦");
        }

        Article article = articleService.getById(articleId);
        Integer articleGoodNumber = article.getArticleGoodNumber();
        ++articleGoodNumber;
        article.setArticleGoodNumber(articleGoodNumber);
        if (articleService.updateById(article)) {
            session.setAttribute("articleGoodTime", true);
            return CommonResult.success("点赞成功！");
        }

        return CommonResult.failed("点赞失败");
    }

    /**
     * 收藏文章
     *
     * @param request
     * @param articleId
     * @return
     */
    @PostMapping("/articleCollection")
    @ResponseBody
    public CommonResult articleCollection(HttpServletRequest request, String articleId) {
        User user = (User) request.getSession().getAttribute("user");
        if (Objects.isNull(user)) {
            return CommonResult.failed("客官！您还没有登录呢");
        }

        if(userCollectionArticleService.count(Wrappers.<UserCollectionArticle>lambdaQuery()
                .eq(UserCollectionArticle::getUserId,user.getUserId())
                .eq(UserCollectionArticle::getArticleId,articleId))>0){
            return CommonResult.failed("已经收藏过了");
        }
        UserCollectionArticle userCollectionArticle=new UserCollectionArticle();
        userCollectionArticle.setArticleId(articleId);
        userCollectionArticle.setUserCollectionArticleTime(DateUtil.date());
        userCollectionArticle.setUserId(user.getUserId());

        if(userCollectionArticleService.save(userCollectionArticle)){

            return CommonResult.success("收藏成功可以在个人收藏中心查看");
        }

        Article article=articleService.getById(articleId);
        if(Objects.nonNull(article)){
            Integer articleCollectionNumber = article.getArticleCollectionNumber();
            ++articleCollectionNumber;
        }
        return CommonResult.failed("收藏失败，刷新重试");
    }


    @GetMapping("/article/search")
    public String articleSearch(HttpServletRequest request, Integer pageNumber, String articleTitle, Model model) {
        if (StrUtil.isBlank(articleTitle)) {
            return "/";
        }
        articleTitle = articleTitle.trim();
        model.addAttribute("articleTitle", articleTitle);
        if (Objects.isNull(pageNumber) || pageNumber < 1) {
            pageNumber = 1;
        }
        String ipAddr = CommonUtils.getIpAddr(request);
        ServletContext servletContext = request.getServletContext();
        ConcurrentMap<String, Long> articleSearchMap = (ConcurrentMap<String, Long>) servletContext.getAttribute("articleSearchMap");
        if (CollUtil.isEmpty(articleSearchMap) || Objects.isNull(articleSearchMap.get(ipAddr))) {
            articleSearchMap = new ConcurrentHashMap<>();
            articleSearchMap.put(ipAddr, DateUtil.currentSeconds());
        } else {
            if ((articleSearchMap.get(ipAddr) + 1 > DateUtil.currentSeconds())) {
                return "/view/searchError";
            }
        }
        //查询到的文章列表
        List<Article> articleList = new ArrayList<>();

        //拆分搜索词,查询标签
        List<Word> words = WordSegmenter.seg(articleTitle);
        List<String> titleList = words.stream().map(Word::getText).collect(Collectors.toList());
        titleList.add(articleTitle);
        List<String> articleTagIdList = articleTagService.list(Wrappers.<ArticleTag>lambdaQuery()
                .in(ArticleTag::getArticleTagName, titleList)
                .select(ArticleTag::getArticleTagId)).stream().map(ArticleTag::getArticleTagId).collect(Collectors.toList());
        List<String> articleIdList = new ArrayList<>();
        if (CollUtil.isNotEmpty(articleTagIdList)) {
            articleIdList = articleTagListService.list(Wrappers.<ArticleTagList>lambdaQuery()
                            .in(ArticleTagList::getArticleTagId, articleTagIdList)
                            .select(ArticleTagList::getArticleId)).stream()
                    .map(ArticleTagList::getArticleId).collect(Collectors.toList());

        }

        //分页查询
        IPage<Article> articlePage = new Page<>(pageNumber, 12);
        LambdaQueryWrapper<Article> queryWrapper = Wrappers.<Article>lambdaQuery()
                .like(Article::getArticleTitle, articleTitle)
                .select(Article::getArticleId,
                        Article::getArticleCoverUrl,
                        Article::getArticleCollectionNumber,
                        Article::getArticleLookNumber,
                        Article::getArticleAddTime,
                        Article::getArticleTitle);
        if (CollUtil.isNotEmpty(articleIdList)) {
            queryWrapper.or().in(Article::getArticleId, articleIdList);
        }

        IPage<Article> articleIPage = articleService.page(articlePage, queryWrapper);
        model.addAttribute("articleIPage", CommonPage.restPage(articleIPage));

        //保持搜索时间
        articleSearchMap.put(ipAddr, DateUtil.currentSeconds());
        servletContext.setAttribute("articleSearchMap", articleSearchMap);

        return "/view/articleSearch";
    }


    @PostMapping("/comment/list")
    @ResponseBody
    public CommonResult commentList(HttpServletRequest request, String articleId, Integer pageNumber) {
        if (StrUtil.isBlank(articleId)) {
            return CommonResult.failed("程序出现错误，请刷新页面重试");
        }
        if (Objects.isNull(pageNumber) || pageNumber < 1) {
            pageNumber = 1;
        }
        Page<CommentVo> commentVoPage = new Page<>(pageNumber, 5);
        IPage<CommentVo> commentVoIPage = commentService.getArticleCommentList(commentVoPage, articleId);
        commentVoIPage.getRecords().stream().forEach(commentVo -> {
            commentVo.setUserName(CommonUtils.getHideMiddleStr(commentVo.getUserName()));
        });

        //已经点过赞的评论
        HashMap<String, Long> goodCommentMap = (HashMap<String, Long>) request.getSession().getAttribute("goodCommentMap");
        if (CollUtil.isNotEmpty(goodCommentMap)) {
            List<String> commentIds = goodCommentMap.keySet().stream().collect(Collectors.toList());
            commentVoIPage.getRecords().stream().forEach(commentVo -> {
                if (commentIds.contains(commentVo.getCommentId())) {
                    commentVo.setIsGoodComment(1);
                }
            });
        }

        return CommonResult.success(CommonPage.restPage(commentVoIPage));
    }
}
