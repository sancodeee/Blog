package com.ws.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.po.Blog;
import com.ws.po.Type;
import com.ws.service.BlogService;
import com.ws.service.TypeService;
import com.ws.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String types(@RequestParam(defaultValue = "1") Integer pageNum,
                        @RequestParam(defaultValue = "8") Integer pageSize,
                        @PathVariable Long id, Model model) {
        List<Type> types = typeService.listTypeTop(10000);
        if (id == -1 && !types.isEmpty()) {
            id = types.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        Page<Blog> page = new Page<>(pageNum, pageSize);
        model.addAttribute("types", types);
        model.addAttribute("page", blogService.listBlog(page, blogQuery));
        model.addAttribute("activeTypeId", id);
        return "types";
    }

}
