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
import java.util.List;

/**
 * 标签
 *
 * @author wangsen
 * @date 2022/03/08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_tag")
public class Tag implements Serializable {

    private static final long serialVersionUID = 5161271815028452059L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    /**
     * 关联的博客列表
     * MyBatis-Plus 不自动处理多对多关系，需要手动处理
     * 使用 @TableField(exist = false) 标记为非数据库字段
     */
    @TableField(exist = false)
    private List<Blog> blogs = new ArrayList<>();

}
