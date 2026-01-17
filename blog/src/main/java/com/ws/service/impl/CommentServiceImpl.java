package com.ws.service.impl;

import com.ws.dao.CommentMapper;
import com.ws.po.Comment;
import com.ws.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        List<Comment> comments = commentMapper.findByBlogIdAndParentCommentNull(blogId);
        // 为每个评论加载子评论（因为 MyBatis-Plus 不自动加载关联）
        for (Comment comment : comments) {
            List<Comment> replyComments = loadReplyComments(comment.getId());
            comment.setReplyComments(replyComments);
        }
        return eachComment(comments);
    }

    /**
     * 递归加载回复评论
     */
    private List<Comment> loadReplyComments(Long parentCommentId) {
        // 使用 MyBatis-Plus 查询条件查询子评论
        List<Comment> comments = commentMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Comment>()
                .eq("parent_comment_id", parentCommentId)
                .orderByDesc("create_time")
        );
        for (Comment comment : comments) {
            // 加载父评论信息（用于显示"@某人"）
            // 注意：从数据库查询出来的 comment.getParentComment() 是 null
            // 需要使用 comment.getParentCommentId() 来获取父评论ID
            if (comment.getParentCommentId() != null) {
                Comment parentComment = commentMapper.selectById(comment.getParentCommentId());
                comment.setParentComment(parentComment);
            }
            List<Comment> replyComments = loadReplyComments(comment.getId());
            comment.setReplyComments(replyComments);
        }
        return comments;
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        // 添加空值检查，防止 NullPointerException
        if (comment.getParentComment() != null) {
            Long parentCommentId = comment.getParentComment().getId();
            if (parentCommentId != null && parentCommentId != -1) {
                comment.setParentComment(commentMapper.selectById(parentCommentId));
                // 设置 parentCommentId（MyBatis-Plus 需要此字段来映射到数据库）
                comment.setParentCommentId(parentCommentId);
            } else {
                comment.setParentComment(null);
                comment.setParentCommentId(null);
            }
        } else {
            comment.setParentComment(null);
            comment.setParentCommentId(null);
        }
        comment.setCreateTime(new Date());
        // MyBatis-Plus 使用 insert 方法
        commentMapper.insert(comment);
        return comment;
    }


    /**
     * 循环每个顶级的评论节点
     *
     * @param comments 评论
     * @return {@link List}<{@link Comment}>
     */
    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment, c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     * root根节点，blog不为空的对象集合
     *
     * @param comments 评论
     */
    private void combineChildren(List<Comment> comments) {

        for (Comment comment : comments) {
            List<Comment> replays = comment.getReplyComments();
            for (Comment replay : replays) {
                //循环迭代，找出子代，存放在tempReplays中
                recursively(replay);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplays);
            //清除临时存放区
            tempReplays = new ArrayList<>();
        }
    }

    // 存放迭代找出的所有子代的集合
    private List<Comment> tempReplays = new ArrayList<>();

    /**
     * 递归迭代，剥洋葱
     * <p>
     * 被迭代的对象
     *
     * @param comment 评论
     */
    private void recursively(Comment comment) {
        //顶节点添加到临时存放集合
        tempReplays.add(comment);
        if (!comment.getReplyComments().isEmpty()) {
            List<Comment> replays = comment.getReplyComments();
            for (Comment replay : replays) {
                tempReplays.add(replay);
                if (!replay.getReplyComments().isEmpty()) {
                    recursively(replay);
                }
            }
        }
    }

}
