package com.ws.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.po.Blog;
import com.ws.vo.BlogQuery;

import java.util.List;
import java.util.Map;

/**
 * 博客服务
 *
 * @author wangsen
 * @date 2022/03/08
 */
public interface BlogService {

    Blog getBlog(Long id);

    Blog getAndConvert(Long id);

    IPage<Blog> listBlog(Page<Blog> pageable, BlogQuery blog);

    IPage<Blog> listBlog(Page<Blog> pageable);

    IPage<Blog> listBlog(Long tagId, Page<Blog> pageable);

    IPage<Blog> listBlog(String query, Page<Blog> pageable);

    List<Blog> listRecommendBlogTop(Integer size);

    Map<String, List<Blog>> archiveBlog();

    Long countBlog();

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);

}
