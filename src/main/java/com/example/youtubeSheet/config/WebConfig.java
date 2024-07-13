package com.example.youtubeSheet.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private String resourcePath ="/upload/**";

    private String savePath ="file:///C:/Users/Hwa/springbootImg/sheets/";
    private String profileResourcePath ="/localProfile/**";

    private String profileSavePath ="file:///C:/Users/Hwa/springbootImg/sheets/localProfile/";

    private String s3ProfileResourcePath="/s3Profile/**";

    @Value("${cloud.aws.s3.url}")
    private String s3ProfileSavePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler(resourcePath)
                .addResourceLocations(savePath);

        registry.addResourceHandler(profileResourcePath)
                .addResourceLocations(profileSavePath);

        registry.addResourceHandler(s3ProfileResourcePath)
                .addResourceLocations(s3ProfileSavePath);

    }
}