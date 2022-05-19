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
public class Ad implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 广告id
     */
    @TableId(value = "ad_id")
    private String adId;

    /**
     * 广告标题
     */
    private String adTitle;

    /**
     * 广告url
     */
    private String adImgUrl;

    /**
     *  广告url链接
     */
    private String adLinkUrl;

    /**
     * 广告排序，越小越在后面
     */
    private Integer adSort;

    /**
     * 广告类型
     */
    private String adTypeId;

    /**
     * 广告开始时间
     */
    private Date adBeginTime;

    /**
     * 广告结束时间
     */
    private Date adEndTime;

    /**
     * 添加广告时间
     */
    private Date adAddTime;


}
