package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置自定义拦截器
 */
@Configuration
@EnableWebMvc
public class MyWebAppConfigurer implements WebMvcConfigurer {

    private static Logger logger = LoggerFactory.getLogger(MyWebAppConfigurer.class);

    public MyWebAppConfigurer() {
        logger.info("加载自定义拦截器...");
    }

    /**
     * 拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
//        registry.addInterceptor(new MyStaticInterceptor())
//                .addPathPatterns("/webRTC/**");
    }

    /**
     * 添加静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/webRTC/**").addResourceLocations("classpath:/webRTC/");
        logger.info("映射静态资源...");
    }
}
