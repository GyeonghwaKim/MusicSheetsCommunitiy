package com.example.youtubeSheet.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private String postResourcePath ="/localPost/**";

    private String savePostPath ="file:///C:/Users/Hwa/springbootImg/sheets/localPost/";
    private String profileResourcePath ="/localProfile/**";

    private String saveProfilePath ="file:///C:/Users/Hwa/springbootImg/sheets/localProfile/";

    private String s3ProfileResourcePath="/s3Profile/**";

    private String s3PostResourcePath="/s3Post/**";

    @Value("${cloud.aws.s3.url}")
    private String s3SavePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler(postResourcePath)
                .addResourceLocations(savePostPath);

        registry.addResourceHandler(profileResourcePath)
                .addResourceLocations(saveProfilePath);

        registry.addResourceHandler(s3ProfileResourcePath)
                .addResourceLocations(s3SavePath);

        registry.addResourceHandler(s3PostResourcePath)
                .addResourceLocations(s3SavePath);

    }
}