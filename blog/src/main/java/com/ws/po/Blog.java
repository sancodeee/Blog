package com.ws.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 博客实体
 *
 * @author wangsen
 * @date 2022/03/08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_blog")
@ApiModel(value = "博客实体类")
public class Blog implements Serializable {

    private static final long serialVersionUID = -5541812433648018526L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Long id;

    @NotEmpty(message = "博客标题不能为空")
    @Size(max = 100, message = "博客标题长度不能超过100个字符")
    @ApiModelProperty(value = "博客标题")
    private String title;

    /**
     * 博客文本内容
     * MyBatis-Plus 自动处理大文本，无需 @Lob 注解
     */
    @NotEmpty(message = "博客内容不能为空")
    @ApiModelProperty(value = "博客文本内容")
    private String content;

    @Size(max = 255, message = "博客首图URL长度不能超过255个字符")
    @ApiModelProperty(value = "博客首图")
    private String firstPicture;

    @Size(max = 50, message = "博客标志位长度不能超过50个字符")
    @ApiModelProperty(value = "博客标志位")
    private String flag;

    @ApiModelProperty(value = "博文浏览次数")
    private Integer views;

    @ApiModelProperty(value = "是否开启赞赏")
    private boolean appreciation;

    @ApiModelProperty(value = "转载声明 是否转载")
    private boolean shareStatement;

    @ApiModelProperty(value = "是否允许评论标志位")
    private boolean commentabled;

    @ApiModelProperty(value = "是否发布")
    private boolean published;

    @ApiModelProperty(value = "是否推荐")
    private boolean recommend;

    /**
     * 创建时间
     * MyBatis-Plus 自动处理 Date 类型，无需 @Temporal 注解
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     * MyBatis-Plus 自动处理 Date 类型，无需 @Temporal 注解
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 分类ID（外键）
     */
    @ApiModelProperty(value = "分类ID")
    private Long typeId;

    /**
     * 用户ID（外键）
     */
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /**
     * 分类
     * MyBatis-Plus 不自动处理关联关系，需要手动查询
     * 使用 @TableField(exist = false) 标记为非数据库字段
     */
    @ApiModelProperty(value = "分类")
    @TableField(exist = false)
    private Type type;

    /**
     * 标签列表
     * MyBatis-Plus 不自动处理多对多关系，需要手动处理
     * 使用 @TableField(exist = false) 标记为非数据库字段
     */
    @ApiModelProperty(value = "标签")
    @TableField(exist = false)
    private List<Tag> tags = new ArrayList<>();

    /**
     * 用户作者
     * MyBatis-Plus 不自动处理关联关系，需要手动查询
     * 使用 @TableField(exist = false) 标记为非数据库字段
     */
    @ApiModelProperty(value = "用户作者")
    @TableField(exist = false)
    private User user;

    /**
     * 评论列表
     * MyBatis-Plus 不自动处理关联关系，需要手动查询
     * 使用 @TableField(exist = false) 标记为非数据库字段
     */
    @ApiModelProperty(value = "评论")
    @TableField(exist = false)
    private List<Comment> comments = new ArrayList<>();

    /**
     * 标签id（非数据库字段，用于前端展示）
     */
    @ApiModelProperty(value = "标签id")
    @TableField(exist = false)
    private String tagIds;

    @Size(max = 200, message = "博客描述长度不能超过200个字符")
    @ApiModelProperty(value = "描述")
    private String description;

    public void init() {
        this.tagIds = tagsToIds(this.getTags());
    }

    private String tagsToIds(List<Tag> tags) {
        if (!tags.isEmpty()) {
            StringBuilder ids = new StringBuilder();
            boolean flag = false;
            for (Tag tag : tags) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        } else {
            return tagIds;
        }
    }
}
