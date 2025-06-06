package com.coderstack.gymmatrix.config;
import com.coderstack.gymmatrix.interceptor.AdminInterceptor;
import com.coderstack.gymmatrix.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/dashboard/**");
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/api/gym/**");
    }
}
