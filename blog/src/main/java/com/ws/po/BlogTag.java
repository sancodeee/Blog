package com.ws.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 博客-标签中间表实体
 * 映射 t_blog_tags 表，实现 Blog 与 Tag 的多对多关系
 *
 * @author wangsen
 * @date 2022/03/08
 */
@TableName("t_blog_tags")
public class BlogTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 博客ID（外键指向 t_blog.id）
     */
    @TableField("blogs_id")
    private Long blogId;

    /**
     * 标签ID（外键指向 t_tag.id）
     */
    @TableField("tags_id")
    private Long tagId;

    public BlogTag() {
    }

    public BlogTag(Long blogId, Long tagId) {
        this.blogId = blogId;
        this.tagId = tagId;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlogTag blogTag = (BlogTag) o;

        if (blogId != null ? !blogId.equals(blogTag.blogId) : blogTag.blogId != null) return false;
        return tagId != null ? tagId.equals(blogTag.tagId) : blogTag.tagId == null;
    }

    @Override
    public int hashCode() {
        int result = blogId != null ? blogId.hashCode() : 0;
        result = 31 * result + (tagId != null ? tagId.hashCode() : 0);
        return result;
    }
}
