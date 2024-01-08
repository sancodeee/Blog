package com.ws.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web配置
 *
 * @author wangsen
 * @date 2022/03/08
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 登录拦截器
     *
     * @param registry 注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin", "/admin/login", "/css/**", "/image/**", "/js/**", "/lib/**");
    }
}
