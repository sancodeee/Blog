package com.ws.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.po.Blog;
import com.ws.po.Tag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Blog Mapper 接口
 *
 * @author wangsen
 * @date 2022/03/08
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

    /**
     * 查询推荐的博客（分页）
     *
     * @param page 分页对象
     * @return 博客列表
     */
    @Select("SELECT * FROM t_blog WHERE recommend = 1 ORDER BY create_time DESC")
    IPage<Blog> findTop(Page<Blog> page);

    /**
     * 标题或内容模糊搜索（分页）
     *
     * @param query 搜索关键词
     * @param page  分页对象
     * @return 博客分页列表
     */
    @Select("SELECT * FROM t_blog WHERE title LIKE CONCAT('%', #{query}, '%') OR content LIKE CONCAT('%', #{query}, '%') ORDER BY create_time DESC")
    IPage<Blog> findByQuery(@Param("query") String query, Page<Blog> page);

    /**
     * 更新浏览次数
     *
     * @param id 博客ID
     * @return 影响行数
     */
    @Update("UPDATE t_blog SET views = views + 1 WHERE id = #{id}")
    int updateViews(@Param("id") Long id);

    /**
     * 按年份分组查询
     *
     * @return 年份列表
     */
    @Select("SELECT DISTINCT DATE_FORMAT(update_time, '%Y') as year FROM t_blog ORDER BY year DESC")
    List<String> findGroupYear();

    /**
     * 按年份查询博客
     *
     * @param year 年份
     * @return 博客列表
     */
    @Select("SELECT * FROM t_blog WHERE DATE_FORMAT(update_time, '%Y') = #{year} ORDER BY create_time DESC")
    List<Blog> findByYear(@Param("year") String year);

    // ========== 多对多关联查询方法（Blog-Tag） ==========

    /**
     * 查询博客关联的标签列表
     *
     * @param blogId 博客ID
     * @return 标签列表
     */
    @Select("SELECT t.* FROM t_tag t " +
            "INNER JOIN t_blog_tags bt ON t.id = bt.tags_id " +
            "WHERE bt.blogs_id = #{blogId}")
    List<Tag> selectTagsByBlogId(@Param("blogId") Long blogId);

    /**
     * 查询标签关联的博客ID列表（用于分页查询）
     *
     * @param tagId 标签ID
     * @return 博客ID列表
     */
    @Select("SELECT bt.blogs_id FROM t_blog_tags bt WHERE bt.tags_id = #{tagId}")
    List<Long> selectBlogIdsByTagId(@Param("tagId") Long tagId);

    /**
     * 插入博客-标签关联
     *
     * @param blogId 博客ID
     * @param tagId  标签ID
     * @return 影响行数
     */
    @Insert("INSERT INTO t_blog_tags (blogs_id, tags_id) VALUES (#{blogId}, #{tagId})")
    int insertBlogTag(@Param("blogId") Long blogId, @Param("tagId") Long tagId);

    /**
     * 批量插入博客-标签关联
     *
     * @param blogId 博客ID
     * @param tagIds 标签ID列表
     * @return 影响行数
     */
    int insertBlogTags(@Param("blogId") Long blogId, @Param("tagIds") List<Long> tagIds);

    /**
     * 删除博客的所有标签关联
     *
     * @param blogId 博客ID
     * @return 影响行数
     */
    @Delete("DELETE FROM t_blog_tags WHERE blogs_id = #{blogId}")
    int deleteBlogTags(@Param("blogId") Long blogId);

    /**
     * 根据标签ID查询博客（分页）
     *
     * @param tagId 标签ID
     * @param page  分页对象
     * @return 博客分页列表
     */
    @Select("SELECT b.* FROM t_blog b " +
            "INNER JOIN t_blog_tags bt ON b.id = bt.blogs_id " +
            "WHERE bt.tags_id = #{tagId} " +
            "ORDER BY b.create_time DESC")
    IPage<Blog> selectBlogsByTagId(@Param("tagId") Long tagId, Page<Blog> page);

}
