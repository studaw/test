package com.blog.service;

import com.blog.pojo.ArticleType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.vo.ArticleTypeTreeVo;
import com.blog.vo.ArticleTypeVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author blog
 * @since 2022-04-06
 */
public interface ArticleTypeService extends IService<ArticleType> {

    /**
     * 文章类型列表包括文章数量
     * @return
     */
    List<ArticleTypeVo> articleTypeList();

    List<ArticleTypeTreeVo> getIndexArticleTypeList(String articleTypeParentId);
}
