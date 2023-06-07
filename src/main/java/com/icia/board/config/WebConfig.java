package com.icia.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceChainRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//웹 인터페이스를 구현하는 WebMVcConfigurer
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private String resourcePath="/upload/**"; // html에서 접근할 경로
    private String savePath = "file:///D:/springboot_img/"; // 실제 파일이 저장된 경로.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler(resourcePath)
                .addResourceLocations(savePath);
    }

}
