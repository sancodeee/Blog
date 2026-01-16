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
        if (id == -1 && !tags.isEmpty()) {
            id = tags.get(0).getId();
        }
        Page<Blog> page = new Page<>(pageNum, pageSize);
        model.addAttribute("tags", tags);
        model.addAttribute("page", blogService.listBlog(id, page));
        model.addAttribute("activeTagId", id);
        return "tags";
    }

}
