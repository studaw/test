package com.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author blog
 * @since 2022-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ArticleTagList implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 文章对应标签id
     */
    @TableId(value = "article_tag_list_id")
    private String articleTagListId;

    /**
     * 文章id
     */
    private String articleTagId;

    /**
     * 文章标签id
     */
    private String articleId;


}
