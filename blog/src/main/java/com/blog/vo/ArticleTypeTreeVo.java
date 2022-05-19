package com.blog.vo;

import com.blog.pojo.Article;
import lombok.Data;

import java.util.List;

/**
 * @program: blog
 * @description:
 **/
@Data
public class ArticleTypeTreeVo {

    private String articleTypeId;
    private String articleTypeName;
    private List<Article> articleList;
    private List<ArticleTypeTreeVo> articleTypeTreeVoList;
}
