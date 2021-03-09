package io.yzecho.netdisk.config;

import io.yzecho.netdisk.interceptor.LoginHandlerInterceptor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;

/**
 * @author yzecho
 * @desc
 * @date 12/01/2021 18:07
 */
@Configurable
public class WebConfig implements WebMvcConfigurer, ErrorPageRegistrar {

    /**
     * 注册视图解析器
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/temp-file").setViewName("temp-file");
        registry.addViewController("/error400Page").setViewName("error/400");
        registry.addViewController("/error401Page").setViewName("error/401");
        registry.addViewController("/error404Page").setViewName("error/404");
        registry.addViewController("/error500Page").setViewName("error/500");
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 文件最大
        factory.setMaxFileSize(DataSize.parse("1024000KB"));
        // 设置上传数据的总大小
        factory.setMaxRequestSize(DataSize.parse("1024000KB"));
        return factory.createMultipartConfig();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/temp-file", "/error400Page", "/error401Page", "/error404Page", "/error500Page", "/uploadTempFile", "/admin", "/sendCode", "/loginByGitHub", "/login", "/register", "/file/share", "/connection",
                        "/asserts/**", "/**/*.css", "/**/*.js", "/**/*.png ", "/**/*.jpg"
                        , "/**/*.jpeg", "/**/*.gif", "/**/fonts/*", "/**/*.svg");
    }

    /**
     * 配置错误页面
     *
     * @param registry
     */
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/error400Page");
        ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error401Page");
        ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/error404Page");
        ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error500Page");
        registry.addErrorPages(error400Page, error401Page, error404Page, error500Page);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
