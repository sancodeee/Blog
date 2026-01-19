package com.ws.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.po.Comment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Comment Mapper 接口
 *
 * @author wangsen
 * @date 2022/03/08
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 根据博客ID查询顶级评论（父评论为空）
     *
     * @param blogId 博客ID
     * @return 评论列表
     */
    @Select("SELECT * FROM t_comment WHERE blog_id = #{blogId} AND parent_comment_id IS NULL ORDER BY create_time DESC")
    List<Comment> findByBlogIdAndParentCommentNull(@Param("blogId") Long blogId);

    /**
     * 根据博客ID删除该博客的所有评论（包括子评论）
     *
     * @param blogId 博客ID
     * @return 影响行数
     */
    @Delete("DELETE FROM t_comment WHERE blog_id = #{blogId}")
    int deleteByBlogId(@Param("blogId") Long blogId);

}
