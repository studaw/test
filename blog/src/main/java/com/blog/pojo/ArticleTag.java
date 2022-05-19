package com.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
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
public class ArticleTag implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 文章标签id
     */
    @TableId(value = "article_tag_id")
    private String articleTagId;

    /**
     * 文章名称
     */
    private String articleTagName;

    /**
     * 添加时间
     */
    private Date articleTagAddTime;


}
