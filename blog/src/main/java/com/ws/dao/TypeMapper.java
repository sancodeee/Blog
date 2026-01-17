package com.ws.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ws.po.Type;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Type Mapper 接口
 *
 * @author wangsen
 * @date 2022/03/08
 */
@Mapper
public interface TypeMapper extends BaseMapper<Type> {

    /**
     * 根据名称查询分类
     *
     * @param name 分类名称
     * @return 分类对象
     */
    @Select("SELECT * FROM t_type WHERE name = #{name}")
    Type findByName(@Param("name") String name);

    /**
     * 按博客数量排序查询分类
     * 注意：由于移除了关联关系，需要在 Service 层手动组装 blogs 数据
     *
     * @param page 分页对象
     * @return 分类列表
     */
    @Select("SELECT * FROM t_type ORDER BY id")
    IPage<Type> findTop(IPage<Type> page);

    /**
     * 查询所有分类及其博客数量
     *
     * @return 分类列表（包含 blogCount）
     */
    @Select("SELECT t.*, " +
            "(SELECT COUNT(*) FROM t_blog WHERE type_id = t.id) AS blog_count " +
            "FROM t_type t " +
            "ORDER BY t.id")
    List<Type> findAllWithBlogCount();

}
