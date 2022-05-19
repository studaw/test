package com.blog.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.dto.article.PublishArticleActionDto;
import com.blog.pojo.*;
import com.blog.mapper.ArticleMapper;
import com.blog.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.service.ArticleTagListService;
import com.blog.service.CommentService;
import com.blog.service.ReplyService;
import com.blog.utils.CommonException;
import com.blog.utils.CommonExceptionHandler;
import com.blog.utils.CommonResult;
import com.blog.vo.ArticleVo;
import com.sun.xml.internal.ws.api.policy.PolicyResolver;
import jdk.internal.org.objectweb.asm.Handle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.CommunicationException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author blog
 * @since 2022-04-06
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    ArticleService articleService;
    @Autowired
    ArticleTagListService articleTagListService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private ServletContext servletContext;

    @Override
    public IPage<ArticleVo> articleListView(IPage<ArticleVo> articlePage, String articleTitle, String articleTypeId) {
        return articleMapper.articleListView(articlePage,articleTitle,articleTypeId);
    }

    /**
     * 文章列表
     * @param articlePage
     * @param articleTitle
     * @return
     */
    @Override
    public IPage<ArticleVo> articleList(IPage<ArticleVo> articlePage, String articleTitle,String userId) {
        return articleMapper.articleList(articlePage,articleTitle,userId);
    }

    /**
     *
     * @param publishArticleActionDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult publishArticleAction(HttpServletRequest request, PublishArticleActionDto publishArticleActionDto) {
       //保存文章
        User user = (User)request.getSession().getAttribute("user");
        String userId = user.getUserId();

        Article article=new Article();

        article.setArticleId(publishArticleActionDto.getArticleId());
        article.setArticleTypeId(publishArticleActionDto.getArticleTypeId());
        article.setUserId(userId);
        article.setArticleTitle(publishArticleActionDto.getArticleTitle());
        if(StrUtil.isBlank(article.getArticleId())){
            article.setArticleAddTime(DateUtil.date());
        }
        article.setArticleAddTime(DateUtil.date());
        article.setArticleCoverUrl(publishArticleActionDto.getArticleCoverUrl());
        article.setArticleContext(publishArticleActionDto.getArticleContext());
        article.setArticleLookNumber(0);
        article.setArticleGoodNumber(0);
        article.setArticleHot(0);
        article.setArticleCollectionNumber(0);

        if(!articleService.saveOrUpdate(article)){
            return CommonResult.failed("操作失败，请重试");
        }

        //保存标签
        String[] articleTagIds = publishArticleActionDto.getArticleTagIds();

        if(Objects.nonNull(articleTagIds)||articleTagIds.length>0){
            articleTagListService.remove(Wrappers.<ArticleTagList>lambdaQuery().eq(ArticleTagList::getArticleTagId,article.getArticleId()));
        }

        ArrayList<ArticleTagList> arrayList = new ArrayList<>();
        for (String articleTagId : articleTagIds) {
            ArticleTagList articleTagList = new ArticleTagList();
            articleTagList.setArticleId(article.getArticleId());
            articleTagList.setArticleTagId(articleTagId);
            arrayList.add(articleTagList);
        }

        if(!articleTagListService.saveBatch(arrayList,50)){
            throw new CommonException("发布文章标签失败");   //保存回滚操作
        }

        servletContext.removeAttribute("indexArticleList");
        return CommonResult.success("操作成功");
    }

    @Override
    public ArticleVo getArticle(String articleId) {
        return articleMapper.getArticle(articleId);
    }

    @Override
    public IPage<ArticleVo> tagArticleList(Page<ArticleVo> articlePage, String articleTagId) {
        return articleMapper.tagArticleList(articlePage,articleTagId);
    }

    @Override
    public CommonResult delArticle(String articleId) {
        if(!removeById(articleId)){
            return CommonResult.failed("该文章删除");
        }
        List<Comment> commentList = commentService.list(Wrappers.<Comment>lambdaQuery()
                .eq(Comment::getArticleId, articleId)
                .select(Comment::getCommentId));

        if(CollUtil.isNotEmpty(commentList)){
            List<String> stringList = commentList.stream().map(Comment::getCommentId).collect(Collectors.toList());
            commentService.removeById((Serializable) stringList);

            replyService.remove(Wrappers.<Reply>lambdaQuery().in(Reply::getCommentId,commentList));
        }

        return CommonResult.success("删除成功");
    }

    @Override
    public List<ArticleVo> getIndexArticleList() {
        return articleMapper.getIndexArticleList();
    }
}
