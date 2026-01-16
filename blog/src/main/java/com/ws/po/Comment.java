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
 * 评论实体
 *
 * @author wangsen
 * @date 2022/03/08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = -362529354341985688L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 内容
     */
    private String content;

    private String avatar;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 博客ID（外键）
     */
    private Long blogId;

    /**
     * 父评论ID（外键）
     */
    private Long parentCommentId;

    /**
     * 博客
     * MyBatis-Plus 不自动处理关联关系，需要手动查询
     * 使用 @TableField(exist = false) 标记为非数据库字段
     */
    @TableField(exist = false)
    private Blog blog;

    /**
     * 回复评论列表
     * MyBatis-Plus 不自动处理关联关系，需要手动查询
     * 使用 @TableField(exist = false) 标记为非数据库字段
     */
    @TableField(exist = false)
    private List<Comment> replyComments = new ArrayList<>();

    /**
     * 父评论
     * MyBatis-Plus 不自动处理关联关系，需要手动查询
     * 使用 @TableField(exist = false) 标记为非数据库字段
     */
    @TableField(exist = false)
    private Comment parentComment;

    /**
     * 管理员评论
     */
    private boolean adminComment;

}
