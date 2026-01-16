package com.ws.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 分类
 *
 * @author wangsen
 * @date 2022/03/08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_type")
public class Type implements Serializable {

    private static final long serialVersionUID = 1387125026528110194L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "分类名称不能为空")
    private String name;

    /**
     * 关联的博客列表
     * MyBatis-Plus 不自动处理关联关系，需要手动查询
     * 使用 @TableField(exist = false) 标记为非数据库字段
     */
    @TableField(exist = false)
    private List<Blog> blogs = new ArrayList<>();


}
