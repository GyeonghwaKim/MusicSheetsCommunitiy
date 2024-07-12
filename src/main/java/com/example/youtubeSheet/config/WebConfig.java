package com.example.youtubeSheet.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private String resourcePath ="/upload/**";

    private String savePath ="file:///C:/Users/Hwa/springbootImg/sheets/";
    private String profileResourcePath ="/profiles/**";

    private String profileSavePath ="file:///C:/Users/Hwa/springbootImg/sheets/profiles/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath)
                .addResourceLocations(savePath);

        registry.addResourceHandler(profileResourcePath)
                .addResourceLocations(profileSavePath);
    }
}