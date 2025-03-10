package com.nvc.spring_boot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
public class StaticResourcesWebConfiguration implements WebMvcConfigurer {
    @Value("${upload-file.base-uri}")
    private String baseUri;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/storage/**").addResourceLocations(baseUri);
    }
}
