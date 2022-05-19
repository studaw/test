package com.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.pojo.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.vo.CommentVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author blog
 * @since 2022-04-06
 */
public interface CommentService extends IService<Comment> {

    IPage<CommentVo> getArticleCommentList(Page<CommentVo> commentVoPage, String articleId);
}
