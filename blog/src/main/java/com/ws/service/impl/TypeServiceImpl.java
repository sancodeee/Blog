package com.ws.service.impl;

import com.ws.dao.TypeRepository;
import com.ws.po.Type;
import com.ws.service.TypeService;
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

import java.util.List;


@Service
@CacheConfig(cacheNames = "type")
public class TypeServiceImpl implements TypeService {

    private final TypeRepository typeRepository;

    public TypeServiceImpl(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @CachePut(key = "T(String).valueOf(#type.id)")
    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Cacheable(key = "T(String).valueOf(#id)")
    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.getById(id);
    }

    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        Pageable pageable = PageRequest.of(0, size);
        return typeRepository.findTop(pageable);
    }

    @Cacheable(key = "T(String).valueOf(#id)")
    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeRepository.getById(id);
        BeanUtils.copyProperties(type, t);
        return typeRepository.save(t);
    }

    @CacheEvict(key = "T(String).valueOf(#id)")
    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }

}
