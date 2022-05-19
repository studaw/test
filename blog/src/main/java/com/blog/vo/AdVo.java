package com.blog.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @program: blog
 * @description:
 **/
@Data
public class AdVo {
    /**
     * 广告id
     */
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


    private String adTypeTitle;
}
