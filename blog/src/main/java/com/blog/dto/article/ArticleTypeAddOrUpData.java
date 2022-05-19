package com.blog.dto.article;

import com.blog.service.AddArticleInterface;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @program: blog
 * @description:
 **/
@Data
public class ArticleTypeAddOrUpData {

    private String articleTypeId;



    /**
     * 文章分类名称
     */
    @NotBlank(message = "文章类型分类名称,不能为空",groups = {AddArticleInterface.class})
    private String articleTypeName;

    /*
     * 文章分类排序，越小越靠前
     */
    @NotNull(message = "文章排序,不能为空")
    private Integer articleTypeSort;

    /**
     * 文章分类父id
     */
    private String articleTypeParentId;

}
