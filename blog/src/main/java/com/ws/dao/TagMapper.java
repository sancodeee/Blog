package com.ws.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ws.po.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Tag Mapper 接口
 *
 * @author wangsen
 * @date 2022/03/08
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 根据名称查询标签
     *
     * @param name 标签名称
     * @return 标签对象
     */
    @Select("SELECT * FROM t_tag WHERE name = #{name}")
    Tag findByName(@Param("name") String name);

    /**
     * 按博客数量排序查询标签
     * 注意：由于移除了关联关系，需要在 Service 层手动组装 blogs 数据
     *
     * @param page 分页对象
     * @return 标签列表
     */
    @Select("SELECT * FROM t_tag ORDER BY id")
    IPage<Tag> findTop(IPage<Tag> page);

    /**
     * 查询所有标签及其博客数量
     *
     * @return 标签列表（包含 blogCount）
     */
    @Select("SELECT t.*, " +
            "(SELECT COUNT(*) FROM t_blog_tags bt WHERE bt.tags_id = t.id) AS blog_count " +
            "FROM t_tag t " +
            "ORDER BY t.id")
    List<Tag> findAllWithBlogCount();

}
