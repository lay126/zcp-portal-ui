package com.skcc.cloudz.zcp.configuration.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.skcc.cloudz.zcp.configuration.web.interceptor.AddOnServiceMetaDataInterceptor;
import com.skcc.cloudz.zcp.configuration.web.interceptor.UserNamespaceInterceptor;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public AddOnServiceMetaDataInterceptor addOnServiceMetaDataInterceptor() {
        AddOnServiceMetaDataInterceptor addOnServiceMetaDataInterceptor = new AddOnServiceMetaDataInterceptor();
        return addOnServiceMetaDataInterceptor;
    }

    @Bean
    public UserNamespaceInterceptor userNamespaceInterceptor() {
        UserNamespaceInterceptor userNamespaceInterceptor = new UserNamespaceInterceptor();
        return userNamespaceInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(addOnServiceMetaDataInterceptor())
            .addPathPatterns(new String[] { "/*", "/**/*" })
            .excludePathPatterns(new String[] { "/static/**", "/error/**", "/common/**" });
        registry.addInterceptor(userNamespaceInterceptor())
            .addPathPatterns(new String[] { "/my/*", "/", "/main", "/management/**", "/alert/**", "/guide/**" })
            .excludePathPatterns(new String[] { "/static/**", "/error/**", "/common/**" });
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/*").allowedOrigins("*").allowedMethods("GET, POST, PUT, DELETE")
                .allowedHeaders("Content-Type").allowCredentials(false).maxAge(3600);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/management/namespace/pop/popNamespaceDel.html")
                .setViewName("content/management/namespace/pop/popNamespaceDel");

        registry.addViewController("/management/namespace/pop/popUserCreate.html")
                .setViewName("content/management/namespace/pop/popUserCreate");

        registry.addViewController("/common/popup/popup").setViewName("common/popup/popup");
    }
}
