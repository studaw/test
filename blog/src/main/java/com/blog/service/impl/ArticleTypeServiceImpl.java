package com.blog.service.impl;

import com.blog.mapper.ArticleMapper;
import com.blog.pojo.ArticleType;
import com.blog.mapper.ArticleTypeMapper;
import com.blog.service.ArticleTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.vo.ArticleTypeTreeVo;
import com.blog.vo.ArticleTypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author blog
 * @since 2022-04-06
 */
@Service
public class   ArticleTypeServiceImpl extends ServiceImpl<ArticleTypeMapper, ArticleType> implements ArticleTypeService {
    @Autowired
    private ArticleTypeMapper articleTypeMapper;

    @Override
    public List<ArticleTypeVo> articleTypeList(){
        return articleTypeMapper.articleTypList();
    }

    @Override
    public List<ArticleTypeTreeVo> getIndexArticleTypeList(String articleTypeParentId) {
        return articleTypeMapper.getIndexArticleTypeList(articleTypeParentId);
    }
}
