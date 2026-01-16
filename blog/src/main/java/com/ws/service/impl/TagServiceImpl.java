package com.ws.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.dao.TagMapper;
import com.ws.po.Tag;
import com.ws.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "tag")
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    public TagServiceImpl(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @CachePut(key = "T(String).valueOf(#tag.id)")
    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        // MyBatis-Plus 使用 insert 方法
        tagMapper.insert(tag);
        return tag;
    }

    @Cacheable(key = "T(String).valueOf(#id)")
    @Transactional
    @Override
    public Tag getTag(Long id) {
        // MyBatis-Plus 使用 selectById 方法
        return tagMapper.selectById(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagMapper.findByName(name);
    }

    @Transactional
    @Override
    public IPage<Tag> listTag(Page<Tag> page) {
        // MyBatis-Plus 使用 selectPage 方法
        return tagMapper.selectPage(page, null);
    }

    @Override
    public List<Tag> listTag() {
        // MyBatis-Plus 使用 selectList 方法
        return tagMapper.selectList(null);
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        // 使用 TagMapper 自定义的 findTop 方法
        Page<Tag> page = new Page<>(1, size);
        IPage<Tag> result = tagMapper.findTop(page);
        return result.getRecords();
    }

    @Override
    public List<Tag> listTag(String ids) { //1,2,3
        // MyBatis-Plus 使用 selectBatchIds 方法
        return tagMapper.selectBatchIds(convertToList(ids));
    }

    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idArray = ids.split(",");
            for (String s : idArray) {
                list.add(new Long(s));
            }
        }
        return list;
    }

    @Cacheable(key = "T(String).valueOf(#id)")
    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagMapper.selectById(id);
        if (t != null) {
            BeanUtils.copyProperties(tag, t);
            // MyBatis-Plus 使用 updateById 方法
            tagMapper.updateById(t);
        }
        return t;
    }

    @CacheEvict(key = "T(String).valueOf(#id)")
    @Transactional
    @Override
    public void deleteTag(Long id) {
        // MyBatis-Plus 使用 deleteById 方法
        tagMapper.deleteById(id);
    }
}
