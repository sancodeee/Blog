package com.ws.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.dao.BlogMapper;
import com.ws.dao.TagMapper;
import com.ws.dao.TypeMapper;
import com.ws.dao.UserMapper;
import com.ws.po.Blog;
import com.ws.po.Tag;
import com.ws.po.Type;
import com.ws.po.User;
import com.ws.service.BlogService;
import com.ws.util.MarkdownUtils;
import com.ws.util.MyBeanUtils;
import com.ws.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 博客服务实现
 *
 * @author wangsen
 * @date 2022/03/08
 */
@Service
@CacheConfig(cacheNames = "blog")
public class BlogServiceImpl implements BlogService {

    private final BlogMapper blogMapper;
    private final TagMapper tagMapper;
    private final TypeMapper typeMapper;
    private final UserMapper userMapper;

    public BlogServiceImpl(BlogMapper blogMapper, TagMapper tagMapper,
                           TypeMapper typeMapper, UserMapper userMapper) {
        this.blogMapper = blogMapper;
        this.tagMapper = tagMapper;
        this.typeMapper = typeMapper;
        this.userMapper = userMapper;
    }

    /**
     * 根据id查询db
     *
     * @param id id
     * @return {@link Blog}
     */
    @Cacheable(cacheNames = "blog", key = "T(String).valueOf(#id)")
    @Override
    public Blog getBlog(Long id) {
        Blog blog = blogMapper.selectById(id);
        if (blog != null) {
            loadTagsForBlog(blog);
        }
        return blog;
    }

    /**
     * blog格式转换
     *
     * @param id id
     * @return {@link Blog}
     */
    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        // 先使用数据库级别的原子更新（避免并发导致的重复计数）
        blogMapper.updateViews(id);

        Blog blog = blogMapper.selectById(id);
        if (blog == null) {
            return null;
        }

        // 加载关联数据
        loadTagsForBlog(blog);
        loadTypeForBlog(blog);
        loadUserForBlog(blog);

        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        // 手动设置关联对象（BeanUtils.copyProperties 不会复制关联对象）
        b.setUser(blog.getUser());
        b.setType(blog.getType());
        b.setTags(blog.getTags());
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        return b;
    }

    /**
     * 加载博客的标签（多对多关联）
     *
     * @param blog 博客对象
     */
    private void loadTagsForBlog(Blog blog) {
        if (blog != null && blog.getId() != null) {
            List<Tag> tags = blogMapper.selectTagsByBlogId(blog.getId());
            blog.setTags(tags);
            blog.init(); // 更新 tagIds 字段
        }
    }

    /**
     * 加载博客的分类（多对一关联）
     *
     * @param blog 博客对象
     */
    private void loadTypeForBlog(Blog blog) {
        if (blog != null && blog.getTypeId() != null) {
            Type type = typeMapper.selectById(blog.getTypeId());
            blog.setType(type);
        }
    }

    /**
     * 加载博客的作者（多对一关联）
     *
     * @param blog 博客对象
     */
    private void loadUserForBlog(Blog blog) {
        if (blog != null && blog.getUserId() != null) {
            User user = userMapper.selectById(blog.getUserId());
            blog.setUser(user);
        }
    }

    /**
     * 博客列表-分页查询
     *
     * @param page 可分页
     * @param blog     博客
     * @return {@link IPage}<{@link Blog}>
     */
    @Override
    public IPage<Blog> listBlog(Page<Blog> page, BlogQuery blog) {
        // 使用 QueryWrapper 构建动态查询条件（替代 JPA Specification）
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        if (blog.getTitle() != null && !"".equals(blog.getTitle())) {
            queryWrapper.like("title", blog.getTitle());
        }
        if (blog.getTypeId() != null) {
            queryWrapper.eq("type_id", blog.getTypeId());
        }
        if (blog.isRecommend()) {
            queryWrapper.eq("recommend", true);
        }
        queryWrapper.orderByDesc("create_time");
        IPage<Blog> result = blogMapper.selectPage(page, queryWrapper);
        // 为每个博客加载关联数据（user, type, tags）
        for (Blog b : result.getRecords()) {
            loadUserForBlog(b);
            loadTypeForBlog(b);
            loadTagsForBlog(b);
        }
        return result;
    }

    /**
     * 博客列表
     *
     * @param page 可分页
     * @return {@link IPage}<{@link Blog}>
     */
    @Override
    public IPage<Blog> listBlog(Page<Blog> page) {
        // 创建查询条件，按更新时间倒序排序
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("update_time");
        IPage<Blog> result = blogMapper.selectPage(page, queryWrapper);

        // 为每个博客加载关联数据（user, type, tags）
        for (Blog blog : result.getRecords()) {
            loadUserForBlog(blog);
            loadTypeForBlog(blog);
            loadTagsForBlog(blog);
        }
        return result;
    }

    @Override
    public IPage<Blog> listBlog(Long tagId, Page<Blog> page) {
        // 多对多关联查询：通过中间表 t_blog_tags 查询
        IPage<Blog> result = blogMapper.selectBlogsByTagId(tagId, page);
        // 为每个博客加载关联数据（user, type, tags）
        for (Blog blog : result.getRecords()) {
            loadUserForBlog(blog);
            loadTypeForBlog(blog);
            loadTagsForBlog(blog);
        }
        return result;
    }

    @Override
    public IPage<Blog> listBlog(String query, Page<Blog> page) {
        // 使用 BlogMapper 自定义的 findByQuery 方法
        IPage<Blog> result = blogMapper.findByQuery(query, page);
        // 为每个博客加载关联数据（user, type, tags）
        for (Blog blog : result.getRecords()) {
            loadUserForBlog(blog);
            loadTypeForBlog(blog);
            loadTagsForBlog(blog);
        }
        return result;
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        // 使用 BlogMapper 自定义的 findTop 方法
        Page<Blog> page = new Page<>(1, size);
        IPage<Blog> result = blogMapper.findTop(page);
        List<Blog> blogs = result.getRecords();
        // 为每个博客加载关联数据（user, type, tags）
        for (Blog blog : blogs) {
            loadUserForBlog(blog);
            loadTypeForBlog(blog);
            loadTagsForBlog(blog);
        }
        return blogs;
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogMapper.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            List<Blog> blogs = blogMapper.findByYear(year);
            // 为每个博客加载关联数据（user, type, tags）
            for (Blog blog : blogs) {
                loadUserForBlog(blog);
                loadTypeForBlog(blog);
                loadTagsForBlog(blog);
            }
            map.put(year, blogs);
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogMapper.selectCount(null);
    }

    @CachePut(cacheNames = "blog", key = "T(String).valueOf(#blog.id)")
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            // 新增博客
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
            blogMapper.insert(blog);
        } else {
            // 更新博客：先删除旧的标签关联，再保存新的
            blog.setUpdateTime(new Date());
            blogMapper.updateById(blog);
            // 删除旧的标签关联
            blogMapper.deleteBlogTags(blog.getId());
        }

        // 保存标签关联（多对多关系）
        if (blog.getTags() != null && !blog.getTags().isEmpty()) {
            List<Long> tagIds = new ArrayList<>();
            for (Tag tag : blog.getTags()) {
                if (tag.getId() != null) {
                    tagIds.add(tag.getId());
                }
            }
            if (!tagIds.isEmpty()) {
                blogMapper.insertBlogTags(blog.getId(), tagIds);
            }
        }

        return blog;
    }

    @CachePut(cacheNames = "blog", key = "T(String).valueOf(#id)")
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogMapper.selectById(id);
        if (b != null) {
            BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));
            b.setUpdateTime(new Date());
            blogMapper.updateById(b);

            // 更新标签关联：先删除旧的，再插入新的
            if (blog.getTags() != null) {
                blogMapper.deleteBlogTags(id);
                List<Long> tagIds = new ArrayList<>();
                for (Tag tag : blog.getTags()) {
                    if (tag.getId() != null) {
                        tagIds.add(tag.getId());
                    }
                }
                if (!tagIds.isEmpty()) {
                    blogMapper.insertBlogTags(id, tagIds);
                }
            }
        }
        return b;
    }

    /*根据id删除blog*/
    @CacheEvict(cacheNames = "blog", key = "T(String).valueOf(#id)")
    @Transactional
    @Override
    public void deleteBlog(Long id) {
        // 先删除标签关联（多对多关系）
        blogMapper.deleteBlogTags(id);
        // 再删除博客本身
        blogMapper.deleteById(id);
    }

    /**
     * 批量获取博客浏览次数
     *
     * @param ids 博客ID列表
     * @return ID -> 浏览次数的映射
     */
    @Override
    public Map<Long, Integer> getViewsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyMap();
        }

        // 使用 MyBatis-Plus 的批量查询
        List<Blog> blogs = blogMapper.selectBatchIds(ids);

        // 转换为 Map
        return blogs.stream()
                .collect(Collectors.toMap(
                        Blog::getId,
                        Blog::getViews,
                        (v1, v2) -> v1  // 处理重复 key
                ));
    }
}
