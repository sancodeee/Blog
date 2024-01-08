package com.ws.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogQuery {

    /**
     * 标题
     */
    private String title;
    /**
     * 类型ID
     */
    private Long typeId;
    /**
     * 是否推荐
     */
    private boolean recommend;

}
