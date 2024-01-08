package com.ws.service.impl;

import com.ws.dao.CommentRepository;
import com.ws.po.Comment;
import com.ws.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by("createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId, sort);
        return eachComment(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1) {
            comment.setParentComment(commentRepository.getById(parentCommentId));
        } else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
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
