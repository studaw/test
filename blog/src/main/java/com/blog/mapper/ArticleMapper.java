package com.blog.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.pojo.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.vo.ArticleTypeVo;
import com.blog.vo.ArticleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author blog
 * @since 2022-04-06
 */
public interface ArticleMapper extends BaseMapper<Article> {




    /**
     * 文章列表
     * @param articlePage
     * @param articleTitle
     * @return
     */
    IPage<ArticleVo> articleList(IPage<ArticleVo> articlePage, @Param("articleTitle") String articleTitle,String userId);

    IPage<ArticleVo> articleListView(IPage<ArticleVo> articlePage, String articleTitle, String articleTypeId);

    ArticleVo getArticle(@Param("articleId") String articleId);

    IPage<ArticleVo> tagArticleList(Page<ArticleVo> articlePage, @Param("articleTagId")String articleTagId);

    List<ArticleVo> getIndexArticleList();

    /**
     * 文章类数量
      * @return
     */

}
