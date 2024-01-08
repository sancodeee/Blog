package com.ws.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
@Entity(name = "t_blog")
@ApiModel(value = "博客实体类")
@Table
public class Blog implements Serializable {

    private static final long serialVersionUID = -5541812433648018526L;

    @Id
    @ApiModelProperty(value = "主键id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "博客标题")
    private String title;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    @ApiModelProperty(value = "博客文本内容")
    private String content;

    @ApiModelProperty(value = "博客首图")
    private String firstPicture;

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

    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @ApiModelProperty(value = "分类")
    @ManyToOne
    private Type type;

    @ApiModelProperty(value = "标签")
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private List<Tag> tags = new ArrayList<>();

    @ApiModelProperty(value = "用户作者")
    @ManyToOne
    private User user;

    @ApiModelProperty(value = "评论")
    @OneToMany(mappedBy = "blog")
    private List<Comment> comments = new ArrayList<>();

    @ApiModelProperty(value = "标签id")
    @Transient
    private String tagIds;

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
