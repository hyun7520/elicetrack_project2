package io.elice.shoppingmall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    String dir = System.getProperty("user.home");


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("file:///C:/Users/spnamji/comfohouse/"); //   file:/// 하고 현재 파일 경로 넣으면 됨.
    }
}