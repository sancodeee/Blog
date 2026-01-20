package com.ws.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.po.Blog;
import com.ws.po.Tag;
import com.ws.service.BlogService;
import com.ws.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tags(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "8") Integer pageSize,
                       @PathVariable Long id, Model model) {
        List<Tag> tags = tagService.listTagTop(10000);

        // 如果标签列表为空，返回空分页结果
        if (tags.isEmpty()) {
            model.addAttribute("tags", tags);
            model.addAttribute("page", new Page<Blog>(pageNum, pageSize));
            model.addAttribute("activeTagId", id);
            return "tags";
        }

        // 如果 id 为 -1，使用第一个标签的 ID
        if (id == -1) {
            id = tags.get(0).getId();
        }

        Page<Blog> page = new Page<>(pageNum, pageSize);
        model.addAttribute("tags", tags);
        model.addAttribute("page", blogService.listBlog(id, page));
        model.addAttribute("activeTagId", id);
        return "tags";
    }

}
