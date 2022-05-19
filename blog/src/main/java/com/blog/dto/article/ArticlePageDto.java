package com.blog.dto.article;

import com.blog.dto.Base.BasePageDto;
import lombok.Data;

/**
 * @program: blog
 * @description:
 **/
@Data
public class ArticlePageDto extends BasePageDto {
    /**
     * 文章名
     */
    private String articleTitle;
}
