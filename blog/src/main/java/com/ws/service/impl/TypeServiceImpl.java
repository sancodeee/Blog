package com.ws.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.dao.TypeMapper;
import com.ws.po.Type;
import com.ws.service.TypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@CacheConfig(cacheNames = "type")
public class TypeServiceImpl implements TypeService {

    private final TypeMapper typeMapper;

    public TypeServiceImpl(TypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    @CachePut(key = "T(String).valueOf(#type.id)")
    @Transactional
    @Override
    public Type saveType(Type type) {
        // MyBatis-Plus 使用 insert 方法
        typeMapper.insert(type);
        return type;
    }

    @Cacheable(key = "T(String).valueOf(#id)")
    @Transactional
    @Override
    public Type getType(Long id) {
        // MyBatis-Plus 使用 selectById 方法
        return typeMapper.selectById(id);
    }

    @Override
    public Type getTypeByName(String name) {
        return typeMapper.findByName(name);
    }

    @Transactional
    @Override
    public IPage<Type> listType(Page<Type> page) {
        // MyBatis-Plus 使用 selectPage 方法
        return typeMapper.selectPage(page, null);
    }

    @Override
    public List<Type> listType() {
        // MyBatis-Plus 使用 selectList 方法
        return typeMapper.selectList(null);
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        // 使用 TypeMapper 自定义的 findTop 方法
        Page<Type> page = new Page<>(1, size);
        IPage<Type> result = typeMapper.findTop(page);
        return result.getRecords();
    }

    @Cacheable(key = "T(String).valueOf(#id)")
    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeMapper.selectById(id);
        if (t != null) {
            BeanUtils.copyProperties(type, t);
            // MyBatis-Plus 使用 updateById 方法
            typeMapper.updateById(t);
        }
        return t;
    }

    @CacheEvict(key = "T(String).valueOf(#id)")
    @Transactional
    @Override
    public void deleteType(Long id) {
        // MyBatis-Plus 使用 deleteById 方法
        typeMapper.deleteById(id);
    }

}
