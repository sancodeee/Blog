package com.ws.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户
 *
 * @author wangsen
 * @date 2022/03/08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = -9065991871093999188L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String nickname;

    private String username;

    private String password;

    private String email;

    private String avatar;

    private Integer types;

    private Date createTime;

    private Date updateTime;

    /**
     * 关联的博客列表
     * MyBatis-Plus 不自动处理关联关系，需要手动查询
     * 使用 @TableField(exist = false) 标记为非数据库字段
     */
    @TableField(exist = false)
    private List<Blog> blogs = new ArrayList<>();


}
