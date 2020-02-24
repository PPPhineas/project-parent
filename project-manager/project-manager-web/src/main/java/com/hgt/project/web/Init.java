package com.hgt.project.web;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author karl xavier
 * @version 0.1
*/
@SpringBootApplication(scanBasePackages = "com.hgt.project")
@MapperScan(basePackages = "com.hgt.project.dao.mapper")
@ComponentScan(basePackages = "com.hgt.project")
@EnableSwagger2
public class Init {

    public static void main(String[] args) {
        SpringApplication.run(Init.class, args);
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 最大单页条数
        paginationInterceptor.setLimit(200);
        return paginationInterceptor;
    }

}
