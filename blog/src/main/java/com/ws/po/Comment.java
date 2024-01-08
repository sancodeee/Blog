package com.ws.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
@Entity(name = "t_comment")
@Table
public class Comment implements Serializable {

    private static final long serialVersionUID = -362529354341985688L;

    /**
     * id
     */
    @Id
    @GeneratedValue
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    /**
     * 博客
     */
    @ManyToOne
    private Blog blog;

    /**
     * 回复评论
     */
    @OneToMany(mappedBy = "parentComment")
    private List<Comment> replyComments = new ArrayList<>();

    /**
     * 父评论
     */
    @ManyToOne
    private Comment parentComment;

    /**
     * 管理员评论
     */
    private boolean adminComment;

}
