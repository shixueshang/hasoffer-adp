package hasoffer.adp.api.configuration;

import hasoffer.adp.api.interceptor.ContextInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Arrays;

@Configuration
@EnableWebMvc
@ComponentScan("hasoffer.adp.api.controller")
@EnableScheduling
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


}
