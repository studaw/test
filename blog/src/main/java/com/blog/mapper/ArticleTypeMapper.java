package com.blog.mapper;

import com.blog.pojo.ArticleType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.vo.ArticleTypeTreeVo;
import com.blog.vo.ArticleTypeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author blog
 * @since 2022-04-06
 */

public interface ArticleTypeMapper extends BaseMapper<ArticleType> {

    List<ArticleTypeVo> articleTypList();

    List<ArticleTypeTreeVo> getIndexArticleTypeList(@Param("articleTypeParentId") String articleTypeParentId);
}
