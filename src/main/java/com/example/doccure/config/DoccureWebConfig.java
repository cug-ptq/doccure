package com.example.doccure.config;

import com.example.doccure.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DoccureWebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自己的拦截器并设置拦截的请求路径
        InterceptorRegistration registration = registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/**");
        registration.excludePathPatterns("/login","/login/auth","/doctor-profile",
                "/register","/doctor-register","/register/patient","/register/doctor","/query/registerResult",
                "/blank-page","doctor-profile",
                "/index","/doctors","/admin/login",
                "/**/fonts/*.eot","/**/fonts/*.ttf","/**/fonts/*.woff","/**/fonts/*.woff2",
                "/**/*.html",
                "/**/js/*.js",
                "/**/js/**/*.js",
                "/**/css/*.css",
                "/**/img/*.png",
                "/**/img/*.jpg",
                "/**/img/**/*.jpg",
                "/**/img/**/*.png",
                "/**/plugins/**/*.js",
                "/**/plugins/**/*.css",
                "/**/plugins/**/**/*.js",
                "/**/plugins/**/**/*.css",
                "/**/plugins/**/*.eot","/**/plugins/**/*.svg","/**/plugins/**/*.ttf",
                "/**/plugins/**/*.woff","/**/plugins/**/*.woff2"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
//        registry.addResourceHandler("/info/**").addResourceLocations("file:D:/upload/");
        registry.addResourceHandler("/info/**").addResourceLocations("file:/root/upload/");
    }
}