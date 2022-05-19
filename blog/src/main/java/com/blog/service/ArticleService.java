package com.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.dto.article.PublishArticleActionDto;
import com.blog.pojo.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.utils.CommonResult;
import com.blog.vo.ArticleVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author blog
 * @since 2022-04-06
 */
public interface ArticleService extends IService<Article> {
    /**
     * 文章列表
     * @param articlePage
     * @param articleTitle
     * @return
     */
    IPage<ArticleVo> articleListView(IPage<ArticleVo > articlePage, String articleTitle,String articleTypeId);

    IPage<ArticleVo> articleList(IPage<ArticleVo > articlePage, String articleTitle,String userId);

    CommonResult publishArticleAction(HttpServletRequest request, PublishArticleActionDto publishArticleActionDto);

    ArticleVo getArticle(String articleId);

    IPage<ArticleVo> tagArticleList(Page<ArticleVo> articlePage, String articleTagId);

    CommonResult delArticle(String articleId);

    List<ArticleVo> getIndexArticleList();
}
