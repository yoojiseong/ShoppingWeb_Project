package com.busanit501.shoppingweb_project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // URL 패턴 /images/** → 로컬 디스크 C:/upload_images/ 폴더 매핑
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///C:/upload_images/");
    }
}
