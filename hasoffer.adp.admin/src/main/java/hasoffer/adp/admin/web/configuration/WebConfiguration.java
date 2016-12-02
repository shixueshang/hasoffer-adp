package hasoffer.adp.admin.web.configuration;

import hasoffer.adp.admin.web.interceptor.ContextInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.io.IOException;
import java.util.Arrays;

@Configuration
@EnableWebMvc
@ComponentScan("hasoffer.adp.admin.web.controller")
public class WebConfiguration extends WebMvcConfigurerAdapter {

    /**
     * 遇到Header:
     * Accept:application/json
     * 返回的数据自动转换为 json
     *
     * @return
     */
    @Bean
    public ViewResolver viewResolver1() {
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setDefaultViews(Arrays.asList(new MappingJackson2JsonView()));

        resolver.setOrder(1);

        return resolver;
    }


    /**
     * 文件上传
     * @return
     * @throws IOException
     */
    @Bean(name="multipartResolver")
    public CommonsMultipartResolver getResolver() throws IOException {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();

        resolver.setDefaultEncoding("utf-8");
        resolver.setMaxUploadSizePerFile(104857600);//10MB
        resolver.setMaxInMemorySize(4096);

        return resolver;
    }

    /**
     * 创建视图解析器(JSP)
     *
     * @return ViewResolver
     */
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/jsp/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(InternalResourceView.class);

        resolver.setOrder(2);
        return resolver;
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.ignoreAcceptHeader(false);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ContextInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
        registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
    }
}
