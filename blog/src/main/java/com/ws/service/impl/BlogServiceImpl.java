package com.ws.service.impl;

import com.ws.dao.BlogRepository;
import com.ws.po.Blog;
import com.ws.po.Type;
import com.ws.service.BlogService;
import com.ws.util.MarkdownUtils;
import com.ws.util.MyBeanUtils;
import com.ws.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * 博客服务实现
 *
 * @author wangsen
 * @date 2022/03/08
 */
@Service
@CacheConfig(cacheNames = "blog")
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;

    public BlogServiceImpl(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
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
        return blogRepository.getById(id);
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
        Blog blog = blogRepository.getById(id);
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        blogRepository.updateViews(id);
        return b;
    }

    /**
     * 博客列表-分页查询
     *
     * @param pageable 可分页
     * @param blog     博客
     * @return {@link Page}<{@link Blog}>
     */
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll((root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                predicates.add(cb.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
            }
            if (blog.getTypeId() != null) {
                predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
            }
            if (blog.isRecommend()) {
                predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
            }
            cq.where(predicates.toArray(new Predicate[0]));
            return null;
        }, pageable);
    }

    /**
     * 博客列表
     *
     * @param pageable 可分页
     * @return {@link Page}<{@link Blog}>
     */
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll((root, cq, cb) -> {
            Join<Object, Object> join = root.join("tags");
            return cb.equal(join.get("id"), tagId);
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    @CachePut(cacheNames = "blog", key = "T(String).valueOf(#blog.id)")
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    @CachePut(cacheNames = "blog", key = "T(String).valueOf(#id)")
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.getById(id);
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    /*根据id删除blog*/
    @CacheEvict(cacheNames = "blog", key = "T(String).valueOf(#id)")
    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
