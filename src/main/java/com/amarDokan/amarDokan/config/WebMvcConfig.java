package com.amarDokan.amarDokan.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${image.upload.path:uploads/img}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path path = Paths.get(uploadPath);
        String absolutePath = path.toFile().getAbsolutePath();
        
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:" + absolutePath + "/", "classpath:/static/img/");
    }
}
