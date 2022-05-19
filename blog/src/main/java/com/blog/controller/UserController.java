package com.blog.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.dto.article.PublishArticleActionDto;
import com.blog.pojo.*;
import com.blog.service.*;
import com.blog.utils.CommonPage;
import com.blog.utils.CommonResult;
import com.blog.utils.CommonUtils;
import com.blog.vo.ArticleVo;
import com.blog.vo.CommentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    UploadFileListService uploadFileListService;
    @Autowired
    ArticleService articleService;
    @Autowired
    ArticleTagService articleTagService;
    @Autowired
    ArticleTypeService articleTypeService;
    @Autowired
    ArticleTagListService articleTagListService;
    @Autowired
    UserCollectionArticleService userCollectionArticleService;
    @Autowired
    ReplyService replyService;
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;

    @PostMapping("/uploadFile")
    @ResponseBody
    public String uploadFile(HttpServletRequest request, MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        User user = (User) request.getSession().getAttribute("user");
        if (Objects.isNull(user.getUserPublishArticle()) || user.getUserPublishArticle() != 1) {
            return null;
        }
//        String a="http://localhost:8888";
//        System.out.println(a+uploadFileListService.getUploadFileUrl(file));


        return uploadFileListService.getUploadFileUrl(file);
    }

    @GetMapping("/manager")
    public String userManager() {
        return "/user/userManager";
    }

    @GetMapping("/publishArticle")
    public String publishArticle(HttpServletRequest request,Model model,String articleId){

        User user =(User) request.getSession().getAttribute("user");
        if(Objects.isNull(user)||user.getUserPublishArticle()!=1||Objects.isNull(user.getUserPublishArticle())){
            return "redirecr:/";
        }

        Article article = articleService.getOne(Wrappers.<Article>lambdaQuery().eq(Article::getUserId, user.getUserId()).eq(Article::getArticleId, articleId));
        if (Objects.nonNull(article)) {
            model.addAttribute("article", article);

            //获取文章标签
            List<ArticleTagList> articleTagLists = articleTagListService.list(Wrappers.<ArticleTagList>lambdaQuery()
                    .eq(ArticleTagList::getArticleId, article.getArticleId())
                    .select(ArticleTagList::getArticleTagId));

            if (CollUtil.isNotEmpty(articleTagLists)) {
                List<String> articleTagIdList = articleTagLists.stream().map(ArticleTagList::getArticleTagId).collect(Collectors.toList());
                model.addAttribute("articleTagIdList", articleTagIdList);
            }
            //获取该文章类型的同级类型
            String articleTypeId = article.getArticleTypeId();
            List<ArticleType> articleSameTypeList = articleTypeService.list(Wrappers.<ArticleType>lambdaQuery().eq(ArticleType::getArticleTypeParentId, articleTypeService.getById(articleTypeId).getArticleTypeParentId()));
            model.addAttribute("articleSameTypeList", articleSameTypeList);

            //获取该文章上级类型
            model.addAttribute("articleTypeParentId", articleTypeService.getById(article.getArticleTypeId()).getArticleTypeParentId());
        }

        //获取类型
        List<ArticleType> articleType0List = articleTypeService.list(Wrappers.<ArticleType>lambdaQuery().isNull(ArticleType::getArticleTypeParentId).or().eq(ArticleType::getArticleTypeParentId, "").orderByAsc(ArticleType::getArticleTypeSort));
        model.addAttribute("articleType0List", articleType0List);

        //获取标签
        List<ArticleTag> articleTagList = articleTagService.list();
        model.addAttribute("articleTagList", articleTagList);

        return "/user/publishArticle";
    }
    @PostMapping("/getArticleTypeChild")
    @ResponseBody
    public CommonResult getArticleTypeChild(String articleTypeId) {
        if (StrUtil.isBlank(articleTypeId)) {
            return CommonResult.failed("请选择一级分类");
        }

        List<ArticleType> articleTypeList = articleTypeService.list(Wrappers.<ArticleType>lambdaQuery()
                .eq(ArticleType::getArticleTypeParentId, articleTypeId)
                .select(ArticleType::getArticleTypeId, ArticleType::getArticleTypeName));

        return CommonResult.success(articleTypeList);
    }

    @PostMapping("/publishArticleAction")
    @ResponseBody
    public CommonResult publishArticleAction(HttpServletRequest request,
                                             PublishArticleActionDto publishArticleActionDto,
                                             MultipartFile articleCoverFile) throws IOException {

        User  user =(User) request.getSession().getAttribute("user");
        if(Objects.isNull(user)||Objects.isNull(user.getUserPublishArticle())||user.getUserPublishArticle()!=1){
            return CommonResult.failed("没有发布权限，联系管理员");
        }
        if(Objects.nonNull(articleCoverFile)) {
            BufferedImage red= ImageIO.read(articleCoverFile.getInputStream());
            if(Objects.isNull(red)){
                return CommonResult.failed("请上传图片文件");
            }

            int width=red.getWidth();
            int height= red.getHeight();
            if(width!=300||height!=150){
                return CommonResult.failed("图片像素为300px *150px");
            }

            publishArticleActionDto.setArticleCoverUrl(uploadFileListService.getUploadFileUrl(articleCoverFile));
        }


       return articleService.publishArticleAction(request,publishArticleActionDto);
    }

    /**
     * 我的文章
     * @param request
     * @param pageNumber
     * @param model
     * @return
     */
    @GetMapping("/myArticleList")
    public String myArticleList(HttpServletRequest request, Integer pageNumber, Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (Objects.isNull(pageNumber) || pageNumber < 1) {
            pageNumber = 1;
        }
        Page<ArticleVo> articlePage = new Page<>(pageNumber, 24);
        IPage<ArticleVo> articleVoIPage = articleService.articleList(articlePage, null,user.getUserId());

        model.addAttribute("articleVoIPage", CommonPage.restPage(articleVoIPage));
        return "/user/myArticleList";
    }

    /**
     * 删除文章
     * @param articleId
     * @return
     */
    @PostMapping("/delArticle")
    @ResponseBody
    public CommonResult delArticle(String articleId){
        return articleService.delArticle(articleId);
    }

    @GetMapping("/collection/list")
    public String collectionList(HttpServletRequest request, Integer pageNumber, Model model) {
        User user = (User) request.getSession().getAttribute("user");

        Page<Article> articlePage = new Page<>(pageNumber, 15);

        List<String> articleIdList = userCollectionArticleService.list(Wrappers.<UserCollectionArticle>lambdaQuery()
                        .eq(UserCollectionArticle::getUserId, user.getUserId())
                        .select(UserCollectionArticle::getArticleId)).stream()
                .map(UserCollectionArticle::getArticleId)
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(articleIdList)) {
            LambdaQueryWrapper<Article> wrapper = Wrappers.<Article>lambdaQuery()
                    .in(Article::getArticleId, articleIdList)
                    .select(Article::getArticleId,
                            Article::getArticleAddTime,
                            Article::getArticleCollectionNumber,
                            Article::getArticleGoodNumber,
                            Article::getArticleLookNumber,
                            Article::getArticleCoverUrl,
                            Article::getArticleTitle);
            IPage<Article> articleIPage = articleService.page(articlePage, wrapper);
            model.addAttribute("articleIPage", CommonPage.restPage(articleIPage));
        }


        return "/user/collectionList";
    }

    @PostMapping("/saveComment")
    @ResponseBody
    public CommonResult userSaveComment(HttpServletRequest request, String articleId, String commentContent,String commentId) {
        if (StrUtil.isBlank(articleId) || StrUtil.isBlank(commentContent)) {
            return CommonResult.failed("评论失败，请刷新页面重试");
        }
        if (commentContent.length() < 1 || commentContent.length() > 800) {
            return CommonResult.failed("评论内容在1-800个字符之间！");
        }


        User user = (User) request.getSession().getAttribute("user");
        if (Objects.isNull(user)) {
            return CommonResult.failed("客官！您的登录过期，请从新登录哦");
        }
        String userId = user.getUserId();


        Comment comment = commentService.getOne(Wrappers.<Comment>lambdaQuery().eq(Comment::getUserId, userId).select(Comment::getCommentTime).orderByDesc(Comment::getCommentTime), false);
        if (Objects.nonNull(comment) && Objects.nonNull(comment.getCommentTime())) {
            if ((comment.getCommentTime().getTime() + 10000) > System.currentTimeMillis()) {
                return CommonResult.failed("客官您评论太快啦~~，休息一下吧");
            }
        }

        Comment comment1 = new Comment();
        comment1.setArticleId(articleId);
        comment1.setUserId(userId);
        comment1.setCommentContext(commentContent);
        comment1.setCommentTime(DateUtil.date());
        comment1.setCommentGoodNumber(0);

        if (commentService.save(comment1)) {
            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(comment1, commentVo);
            commentVo.setUserName(
                    userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUserId, commentVo.getUserId()).select(User::getUsername)).getUsername()
            );
            commentVo.setCommentTime(DateUtil.format(comment1.getCommentTime(),"yyyy-MM-dd HH:mm:ss"));
            return CommonResult.success(commentVo);
        }
        return CommonResult.failed("评论失败");
    }

    @PostMapping("/commentReply")
    @ResponseBody
    public CommonResult commentReply(HttpServletRequest request,String commentId,String articleId,String commentContent){
        if (StrUtil.isBlank(commentId) || StrUtil.isBlank(articleId) || StrUtil.isBlank(commentContent)) {
            return CommonResult.failed("评论失败，请刷新页面重试");
        }
        if (commentContent.length() < 1 || commentContent.length() > 800) {
            return CommonResult.failed("评论内容在1-800个字符之间！");
        }


        User user = (User) request.getSession().getAttribute("user");
        if (Objects.isNull(user)) {
            return CommonResult.failed("客官！您的登录过期，请从新登录哦");
        }
        String userId = user.getUserId();


        Reply commentReply = replyService.getOne(Wrappers.<Reply>lambdaQuery()
                .eq(Reply::getCommentReplyId, userId)
                .select(Reply::getCommentReplyTime)
                .orderByDesc(Reply::getCommentReplyTime), false);
        if (Objects.nonNull(commentReply) && Objects.nonNull(commentReply.getCommentReplyTime())) {
            if ((commentReply.getCommentReplyTime().getTime() + 10000) > System.currentTimeMillis()) {
                return CommonResult.failed("客官您评论太快啦~~，休息一下吧");
            }
        }
        return CommonResult.success("评论成功");
    }
}
