package com.ws.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * User Mapper 接口
 *
 * @author wangsen
 * @date 2022/03/08
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名和密码查询用户
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户对象
     */
    @Select("SELECT * FROM t_user WHERE username = #{username} AND password = #{password}")
    User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

}
