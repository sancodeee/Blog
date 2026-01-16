package com.ws.web.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ws.po.Blog;
import com.ws.po.User;
import com.ws.service.BlogService;
import com.ws.service.TagService;
import com.ws.service.TypeService;
import com.ws.vo.BlogQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";
    private static final String MESSAGE = "message";


    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(@RequestParam(defaultValue = "1") Integer pageNum,
                        @RequestParam(defaultValue = "5") Integer pageSize,
                        BlogQuery blog, Model model) {
        model.addAttribute("types", typeService.listType());
        Page<Blog> page = new Page<>(pageNum, pageSize);
        model.addAttribute("page", blogService.listBlog(page, blog));
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(@RequestParam(defaultValue = "1") Integer pageNum,
                         @RequestParam(defaultValue = "5") Integer pageSize,
                         BlogQuery blog, Model model) {
        Page<Blog> page = new Page<>(pageNum, pageSize);
        model.addAttribute("page", blogService.listBlog(page, blog));
        return "admin/blogs :: blogList";
    }


    @GetMapping("/blogs/input")
    public String input(Model model) {
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        setTypeAndTag(model);
        log.info("blog对象是否属于Blog类：{}", blogService.getBlog(id) instanceof Blog);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog", blog);
        return INPUT;
    }

    @PostMapping("/blogs")
    public String post(@Valid Blog blog, BindingResult result, RedirectAttributes attributes, HttpSession session, Model model) {
        if (result.hasErrors()) {
            setTypeAndTag(model);
            return INPUT;
        }

        User user = (User) session.getAttribute("user");
        log.info("从 session 获取的 user 对象: {}", user);
        log.info("user.getId(): {}", user != null ? user.getId() : "user is null");
        blog.setUser(user);
        // 设置 userId（MyBatis-Plus 需要 userId 字段）
        if (user != null) {
            blog.setUserId(user.getId());
            log.info("设置 blog.getUserId(): {}", blog.getUserId());
        }
        // 安全处理 type 和 tags
        // 表单提交使用 type.id，需要从 blog.type.getId() 获取 typeId
        if (blog.getType() != null && blog.getType().getId() != null) {
            blog.setTypeId(blog.getType().getId());
            blog.setType(typeService.getType(blog.getTypeId()));
        } else if (blog.getTypeId() != null) {
            blog.setType(typeService.getType(blog.getTypeId()));
        }
        // 处理 tags（tagIds 可能为空字符串）
        String tagIds = blog.getTagIds();
        if (tagIds != null && !tagIds.trim().isEmpty()) {
            blog.setTags(tagService.listTag(tagIds));
        }
        Blog b;
        if (blog.getId() == null) {
            b = blogService.saveBlog(blog);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }
        if (b == null) {
            attributes.addFlashAttribute(MESSAGE, "操作失败");
        } else {
            attributes.addFlashAttribute(MESSAGE, "操作成功");
        }
        return REDIRECT_LIST;
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute(MESSAGE, "删除成功");
        return REDIRECT_LIST;
    }


}
