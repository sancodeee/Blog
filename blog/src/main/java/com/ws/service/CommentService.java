package com.ws.service;

import com.ws.po.Comment;

import java.util.List;

/**
 * 评论服务
 *
 * @author wangsen
 * @date 2022/03/08
 */
public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);

}
