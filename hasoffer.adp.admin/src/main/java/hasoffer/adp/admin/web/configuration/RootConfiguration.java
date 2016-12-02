package hasoffer.adp.admin.web.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@PropertySource("classpath:/message.properties")
@ComponentScan(basePackages={"hasoffer.adp"},
        excludeFilters={
            @ComponentScan.Filter(type=FilterType.ANNOTATION, value=EnableWebMvc.class)
        })
public class RootConfiguration {

    @Autowired
    Environment env;

    private String imagePathDir;

    private String pvRequestUrl;

    @Bean
    public MessageSource messageSource(){
        ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
        ms.setBasename("message");
        return ms;
    }

    public String getImagePathDir() {
        if(imagePathDir == null){
            imagePathDir = env.getProperty("icon_url");
        }
        return imagePathDir;
    }

    public void setImagePathDir(String imagePathDir) {
        this.imagePathDir = imagePathDir;
    }

    public String getPvRequestUrl() {
        if(pvRequestUrl == null){
            pvRequestUrl = env.getProperty("pv_request_url");
        }
        return pvRequestUrl;
    }

    public void setPvRequestUrl(String pvRequestUrl) {
        this.pvRequestUrl = pvRequestUrl;
    }
}
