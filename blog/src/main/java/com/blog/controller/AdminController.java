package com.blog.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.system.HostInfo;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import cn.hutool.system.UserInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.dto.Ad.AdDto;
import com.blog.dto.article.ArticlePageDto;
import com.blog.dto.article.ArticleTypeAddOrUpData;
import com.blog.dto.article.ArticleTypeUpdateDto;
import com.blog.dto.user.UserDto;
import com.blog.dto.user.UserListPageDto;
import com.blog.pojo.*;
import com.blog.service.*;
import com.blog.utils.CommonPage;
import com.blog.utils.CommonResult;
import com.blog.vo.AdVo;
import com.blog.vo.ArticleTypeVo;
import com.blog.vo.ArticleVo;
import com.sun.xml.internal.ws.api.policy.PolicyResolver;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/hzh")
@Slf4j
public class AdminController {
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private ArticleTypeService articleTypeService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private UserService userService;
    @Autowired
    private ArticleTagListService articleTagListService;
    @Autowired
    private LinkService linkService;
    @Autowired
    private AdService adService;
    @Autowired
    private AdTypeService adTypeService;
    @Autowired
    private AdminService adminService;

    @GetMapping("/login")
    public String logIn(HttpServletRequest request){
        if(Objects.nonNull(request.getSession().getAttribute("admin"))){
            return "redirect:/hzh/";
        }
        return "/admin/login";
    }

    @PostMapping("/adminLogin")
    @ResponseBody
    public CommonResult adminLogin(HttpServletRequest request,
                                   String adminName,
                                   String adminPassword,
                                   String verifyCode) {
        HttpSession session = request.getSession();
        if (StrUtil.isBlank(verifyCode) || !verifyCode.equals(session.getAttribute("circleCaptchaCode"))) {
            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("验证码不正确");
        }
        Admin admin = adminService.getOne(Wrappers.<Admin>lambdaQuery()
                .eq(Admin::getAdminName, adminName)
                .eq(Admin::getAdminPassword, SecureUtil.md5(adminName + adminPassword)), false);
        if (Objects.isNull(admin)) {
            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("用户名或密码不正确");
        }
        session.setAttribute("admin", admin);
        return CommonResult.success("登录成功");
    }

    /**
     * 管理员退出登录
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public String logOut(HttpServletRequest request){
        request.getSession().removeAttribute("admin");

        return "redirect:/hzh/login";
    }
    /**
     * 管理端-首页
     * @param model
     * @return
     */
    @GetMapping("/")
    public String adminIndex(Model model){
//        系统信息
        OsInfo osInfo = SystemUtil.getOsInfo();//系统信息
        UserInfo userInfo = SystemUtil.getUserInfo();//用户信息
        HostInfo hostInfo = SystemUtil.getHostInfo();//当前主机网络地址信息
        model.addAttribute("osName",userInfo.getName());
        model.addAttribute("hostAddress",hostInfo.getName());

//        文章数量
        int articleTypeCount = articleTypeService.count();
        int articleTagCount = articleTagService.count();
        int articleCount = articleService.count();
        model.addAttribute("articleTypeCount",articleTypeCount);
        model.addAttribute("articleTagCount",articleTagCount);
        model.addAttribute("articleCount",articleCount);

//         用户数量
        int userCount = userService.count();
        model.addAttribute("userCount",userCount);
        return "/admin/index";
    }


    /**
     *管理端-用户列表
     * @param userListPageDto
     * @param model
     * @return
     */
    @GetMapping("/user/list")
    public String userList(@Valid UserListPageDto userListPageDto, Model model){

        Integer pageNumber = userListPageDto.getPageNumber();
        String userName = userListPageDto.getUserName();

        IPage<User> userPage = new Page<>(pageNumber,20);
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.<User>lambdaQuery().orderByDesc(User::getUserRegisterTime);

        if(StrUtil.isNotBlank(userName)){
            userLambdaQueryWrapper.like(User::getUsername,userName);
            model.addAttribute("userName",userName);
        }

        IPage<User> page = userService.page(userPage, userLambdaQueryWrapper);

        model.addAttribute("userPage", CommonPage.restPage(page));

        return "/admin/userList";
    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @PostMapping("/user/del")
    @ResponseBody
    public CommonResult userDel(String userId,Model model) {

        if (StrUtil.isBlank(userId)) {

            return CommonResult.failed("参数错误，请刷新页面重试");
        }

        if (articleService.count(Wrappers.<Article>lambdaQuery().eq(Article::getUserId, userId)) > 0) {

            return CommonResult.failed("该用户发布过文章，无法删除用户，请冻结用户");
        }

        if (userService.removeById(userId)) {

            return CommonResult.success("删除成功");
        }

        return CommonResult.failed("删除失败");
    }

    @PostMapping("/user/update")
    @ResponseBody
    public CommonResult userUpDate(@Valid UserDto userDto){
        User user = userService.getById(userDto.getUserId());
        if(Objects.isNull(user)){
            return CommonResult.failed("用户id不正确");
        }

        Date userRegisterTime = user.getUserRegisterTime();
        String password = userDto.getPassword();
        if(StrUtil.isNotBlank(password)){
//            用户密码等于注册时间+用户明文密码 然后md5 加密
            user.setPassword(SecureUtil.md5(userRegisterTime+password));
        }else {
            userDto.setPassword(null);
        }

        BeanUtils.copyProperties(userDto,user);
        if(userService.updateById(user)){
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed("修改失败，请重试");
    }

    /**
     * 文章类型列表
     *
     * @return
     */
    @GetMapping("/article/type/list")
    public String articleTypeList(Model model,String articleTypeParentId){
        List<ArticleType> articleType0List = articleTypeService.list(Wrappers.<ArticleType>lambdaQuery().
                isNull(ArticleType::getArticleTypeParentId)
                .or().eq(ArticleType::getArticleTypeParentId, "")//两个条件and连接
                .orderByAsc(ArticleType::getArticleTypeSort));

        LambdaQueryWrapper<ArticleType> queryWrapper = Wrappers.<ArticleType>lambdaQuery()
                .isNotNull(ArticleType::getArticleTypeParentId)
                .ne(ArticleType::getArticleTypeParentId, "")
                .orderByAsc(ArticleType::getArticleTypeSort);
        if (StrUtil.isNotBlank(articleTypeParentId)) {
            queryWrapper.eq(ArticleType::getArticleTypeParentId, articleTypeParentId);
            model.addAttribute("articleTypeName", articleTypeService.getById(articleTypeParentId).getArticleTypeName());
        }

        List<ArticleType> articleType1List = articleTypeService.list(queryWrapper);
        model.addAttribute("articleType0List", articleType0List);
        model.addAttribute("articleType1List", articleType1List);
        return "/admin/articleTypeList";

    }

   @PostMapping("/article/type/addOrUpdata")
   @ResponseBody
    public CommonResult addOrUpdata(@Valid ArticleType articleType){
       servletContext.removeAttribute("articleTypeList");

       if(StrUtil.isNotBlank(articleType.getArticleTypeParentId())
               && StrUtil.isNotBlank(articleType.getArticleTypeId())
               && articleType.getArticleTypeParentId().equals(articleType.getArticleTypeId())){
           return CommonResult.failed("不能将自己分配到自己的目录下");
       }

       String articleTypeId = articleType.getArticleTypeId();

       if(StrUtil.isNotBlank(articleTypeId)){
           if(articleTypeService.updateById(articleType)){
               return CommonResult.success("更新成功");
           }else {
               return CommonResult.failed("更新失败");
           }
       }

       articleType.setArticleTypeAddTime(DateUtil.date());
       if(articleTypeService.save(articleType)){

           return CommonResult.success("添加成功,好兄弟");
       }
       return CommonResult.failed("添加失败");


    }

    @PostMapping("/article/type/update")
    @ResponseBody
    public CommonResult articleTypeUpdate(@Valid ArticleTypeUpdateDto articleTypeUpdateDto) {
        ArticleType articleType = new ArticleType();
        BeanUtils.copyProperties(articleTypeUpdateDto, articleType);

        String articleTypeName = articleType.getArticleTypeName();
        Integer articleTypeSort = articleType.getArticleTypeSort();
        if (StrUtil.isBlank(articleTypeName)) {
            articleType.setArticleTypeName(null);
        }
        if (Objects.isNull(articleTypeSort)) {
            articleType.setArticleTypeSort(null);
        }
        if(StrUtil.isNotBlank(articleType.getArticleTypeParentId()) && StrUtil.isNotBlank(articleType.getArticleTypeId()) && articleType.getArticleTypeParentId().equals(articleType.getArticleTypeId())){
            return CommonResult.failed("不能将自己分配到自己的目录下");
        }

        if (articleTypeService.updateById(articleType)) {
            servletContext.removeAttribute("articleTypeList");
            return CommonResult.success("添加成功");
        }
        return CommonResult.failed("添加失败");
    }

    /**
     *
     * 文章标签删除
     * @param articleTypeId
     * @return
     */
    @PostMapping("/article/type/del")
    @ResponseBody
    public CommonResult articleTypeDel(@NotBlank(message = "文章id不能为空")String articleTypeId){

        if(articleService.count(Wrappers.<Article>lambdaQuery()
                .eq(Article::getArticleId,articleTypeId))>0){
            return CommonResult.failed("请先删除该分类下的所有文章");
        }
        if(articleTypeService.count(Wrappers.<ArticleType>lambdaQuery()
                .eq(ArticleType::getArticleTypeParentId,articleTypeId))>0){
            return CommonResult.failed("请先删除下级分类");
        }
        if(articleTypeService.removeById(articleTypeId)){
            servletContext.removeAttribute("articleTypeList");
            return CommonResult.success("删除成功");
        }

        return CommonResult.failed("删除失败,请重试");
    }

    /**
     * 文章标签
     *
     * @param model
     * @return
     */
    @GetMapping("/article/tag/list")
    public String articleTagList(Model model){
        List<ArticleTag> articleTagList = articleTagService.list(Wrappers.<ArticleTag>lambdaQuery().orderByDesc(ArticleTag::getArticleTagAddTime));
        model.addAttribute("articleTagList",articleTagList);
        return "/admin/articleTagList";
    }

    @PostMapping("/article/tag/addOrUpdata")
    @ResponseBody
    public CommonResult articleTagAddOrUpdata(ArticleTag articleTag){
        servletContext.removeAttribute("articleTagList");
        String articleTagId = articleTag.getArticleTagId();
        if(StrUtil.isNotBlank(articleTagId)){
            if(articleTagService.updateById(articleTag)){
                return CommonResult.success("修改成功");
            }else {
                return CommonResult.failed("修改失败，请重试");
            }
        }

        articleTag.setArticleTagAddTime(DateUtil.date());
        if(articleTagService.save(articleTag)){
            return CommonResult.success("添加成功");
        }else {
            return CommonResult.failed("添加失败,请重试");
        }

    }

    @PostMapping("/article/tag/del")
    @ResponseBody
    public CommonResult articleTagDel(String articleTagId){
        servletContext.removeAttribute("articleTagList");
        if(StrUtil.isBlank(articleTagId)){
           return CommonResult.failed("未获取标签id,请重试");
        }

        if(articleTagListService.count(Wrappers.<ArticleTagList>lambdaQuery().eq(ArticleTagList::getArticleTagId,articleTagId))>0){
            return CommonResult.failed("标签下有对应的文章，请先删除标签下的文章后重试");
        }

        if(articleTagService.removeById(articleTagId)){
            return CommonResult.success("文章标签删除成功");
        }else {
        return CommonResult.failed("文章标签删除失败");
        }

    }

    /**
     *文章列表
     * @param articlePageDto
     * @return
     */
    @GetMapping("/article/list")
    public String articleList(ArticlePageDto articlePageDto,Model model){

        IPage<ArticleVo> articleVoPage = new Page<>(articlePageDto.getPageNumber(), 20);
        IPage<ArticleVo> articleVoIPage=articleService.articleList(articleVoPage,articlePageDto.getArticleTitle(),null);

        model.addAttribute("articleIPage",CommonPage.restPage(articleVoIPage));
//        List<ArticleVo> list = CommonPage.restPage(articleVoIPage).getList();
//
//        for (ArticleVo articleVo : list) {
//            System.out.println(articleVo.getArticleId());
//        }

        if(StrUtil.isNotBlank(articlePageDto.getArticleTitle())){
            model.addAttribute("articleTitle",articlePageDto.getArticleTitle());
        }
        return "/admin/articleList";
    }


    /**
     * 文章删除
     * @param articleId
     * @return
     */
    @PostMapping("/article/del")
    @ResponseBody
    public CommonResult articleDel(String articleId){
        if(StrUtil.isBlank(articleId)){
            return CommonResult.failed("出现问题了，请重试");
        }

        if(articleService.removeById(articleId)){

            return CommonResult.success("删除成功");
        }else {
            return CommonResult.failed("删除失败，检查后重试");
        }
    }

    /**
     * 友情连接列表
     * @param model
     * @return
     */
    @GetMapping("/link/list")
    public String linkList(Model model){
        List<Link> list = linkService.list(Wrappers.<Link>lambdaQuery().orderByAsc(Link::getLinkSort));
        model.addAttribute("linkList",list);

        return "/admin/linkList";
    }

    /**
     * 更新或添加操作
     * @param link
     * @return
     */
    @PostMapping("/link/addOrUpdate")
    @ResponseBody
    public CommonResult linkAddOrUpdate(Link link){
        servletContext.removeAttribute("linkList");
        String linkId = link.getLinkId();
        if(StrUtil.isBlank(linkId)){
            link.setLinkAddTime(DateUtil.date());
            if(linkService.save(link)){
                return CommonResult.success("添加成功");
            }else {
                return CommonResult.failed("添加失败，请重试");
            }
        }
        if(linkService.updateById(link)){
            return CommonResult.success("更新成功");
        }
        return CommonResult.failed("更新失败");
    }
    @PostMapping("/link/del")
    @ResponseBody
    public CommonResult linkDel(String linkId){
        servletContext.removeAttribute("linkList");
        if(StrUtil.isBlank(linkId)){
           return CommonResult.failed("系统出错,请重试");
        }
        if(linkService.removeById(linkId)){
            return CommonResult.success("删除成功");
        }else {
            return CommonResult.failed("删除失败");
        }
    }

    /**
     * 广告管理
     * @param model
     * @return
     */
    @GetMapping("/ad/list")
    private String adList(String adTypeId, Model model){
        List<AdType> adTypeList = adTypeService.list(Wrappers.<AdType>lambdaQuery().orderByAsc(AdType::getAdTypeSort));
        model.addAttribute("adTypeList",adTypeList);
        LambdaQueryWrapper<Ad> adLambdaQueryWrapper = Wrappers.<Ad>lambdaQuery().orderByAsc(Ad::getAdSort);

        if(StrUtil.isNotBlank(adTypeId)){
            adLambdaQueryWrapper.eq(Ad::getAdId,adTypeId);
        }

        List<AdVo> adList = adService.adList(adTypeId);

        model.addAttribute("adVoList",adList);

        return "/admin/adList";
    }

    /**
     * 广告类型管理
     * @param adType
     * @return
     */
    @PostMapping("/ad/type/addOrUpdate")
    @ResponseBody
    public CommonResult addOrUpdate(AdType adType){
        servletContext.removeAttribute("adIndexList");
        String adTypeId = adType.getAdTypeId();

        if(StrUtil.isBlank(adTypeId)){
            adType.setAdTypeAddTime(DateUtil.date());
            if(adTypeService.save(adType)){
                return CommonResult.success("添加成功");
            }else {
                return CommonResult.failed("添加失败");
            }
        }

        if(adTypeService.updateById(adType)){
            return CommonResult.success("修改成功");
        }

        return CommonResult.failed("修改失败");
    }

    @PostMapping("/ad/addOrUpdate")
    @ResponseBody
    public CommonResult addOrUpdate(HttpServletRequest request, AdDto adDto){

        String adTypeId = adDto.getAdId();

        Ad ad = new Ad();
        servletContext.removeAttribute("adIndexList");

        BeanUtils.copyProperties(adDto,ad);

        ad.setAdEndTime(DateUtil.parseDateTime(adDto.getAdEndTime()));
        ad.setAdBeginTime(DateUtil.parseDateTime(adDto.getAdBeginTime()));
//             移除广告缓存
        request.getServletContext().removeAttribute("adIndexList");
        request.getServletContext().removeAttribute("adArticleList");

        if(StrUtil.isBlank(adTypeId)){

            ad.setAdAddTime(DateUtil.date());
            if(adService.save(ad)){
                return CommonResult.success("添加成功");
            }else {
                return CommonResult.failed("添加失败");
            }
        }


        if(adService.updateById(ad)){
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed("修改失败");
    }
    @PostMapping("/ad/del")
    @ResponseBody
    public CommonResult adDel(String adId){
        servletContext.removeAttribute("adIndexList");
        if(StrUtil.isBlank(adId)){
            return CommonResult.failed("系统出错,请重试");
        }
        if(adService.removeById(adId)){
            return CommonResult.success("删除成功");
        }else {
            return CommonResult.failed("删除失败");
        }
    }
}
