package com.blog.service.impl;

import com.blog.pojo.UserCollectionArticle;
import com.blog.mapper.UserCollectionArticleMapper;
import com.blog.service.UserCollectionArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户收藏的文章 服务实现类
 * </p>
 *
 * @author blog
 * @since 2022-04-16
 */
@Service
public class UserCollectionArticleServiceImpl extends ServiceImpl<UserCollectionArticleMapper, UserCollectionArticle> implements UserCollectionArticleService {

}
