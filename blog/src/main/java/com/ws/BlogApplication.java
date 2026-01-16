package com.ws;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@EnableCaching
@SpringBootApplication
@EnableSwagger2
@MapperScan("com.ws.dao")  // 扫描 MyBatis Mapper 接口
public class BlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
        log.info("---启动成功！---");
    }

}
