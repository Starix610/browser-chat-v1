package com.starix.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Tobu
 * @date 2019-12-07 16:24
 */
@Configuration
public class CustomWebMvcConfigurer implements WebMvcConfigurer {


    @Bean
    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }

    @Override
   public void addInterceptors(InterceptorRegistry registry) {
        /**
         * 拦截器按照顺序执行,如果不同拦截器拦截存在相同的URL，前面的拦截器会执行，后面的拦截器将不执行
         */
        registry.addInterceptor(loginInterceptor())
                .excludePathPatterns(
                        "/login.html",
                        "/login",
                        "/register",
                        "/js/**",
                        "/css/**",
                        "/img/**",
                        "/checkUsername")
                .addPathPatterns("/**");

    }

}
