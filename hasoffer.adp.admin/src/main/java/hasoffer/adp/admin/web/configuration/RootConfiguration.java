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

    private String domainUrl;

    private String clickUrl;

    private String requestPath;

    private String callbackPath;

    private String adimgPath;

    private String adclickPath;

    private String mailHost;

    private int mailPort;

    private String mailUsername;

    private String mailPassword;

    private String mailFromaddress;

    private String mailToaddress;

    @Bean
    public MessageSource messageSource(){
        ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
        ms.setBasename("message");
        return ms;
    }

    public String getImagePathDir() {
        if(imagePathDir == null){
            imagePathDir = env.getProperty("icon_path");
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

    public String getDomainUrl() {
        if(domainUrl == null){
            domainUrl = env.getProperty("domain_url");
        }
        return domainUrl;
    }

    public void setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
    }

    public String getClickUrl() {
        if (clickUrl == null) {
            clickUrl = env.getProperty("clk_tks");
        }
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getRequestPath() {
        if (requestPath == null) {
            requestPath = env.getProperty("request_path");
        }
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public String getCallbackPath() {
        if (callbackPath == null) {
            callbackPath = env.getProperty("callback_path");
        }
        return callbackPath;
    }

    public void setCallbackPath(String callbackPath) {
        this.callbackPath = callbackPath;
    }

    public String getAdimgPath() {
        if (adimgPath == null) {
            adimgPath = env.getProperty("adimg_path");
        }
        return adimgPath;
    }

    public void setAdimgPath(String adimgPath) {
        this.adimgPath = adimgPath;
    }

    public String getAdclickPath() {
        if (adclickPath == null) {
            adclickPath = env.getProperty("adclick_path");
        }
        return adclickPath;
    }

    public void setAdclickPath(String adclickPath) {
        this.adclickPath = adclickPath;
    }

    public String getMailHost() {
        if (mailHost == null) {
            mailHost = env.getProperty("mail_host");
        }
        return mailHost;
    }

    public void setMailHost(String mailHost) {
        this.mailHost = mailHost;
    }

    public int getMailPort() {
        if (mailPort == 0) {
            mailPort = Integer.parseInt(env.getProperty("mail_port"));
        }
        return mailPort;
    }

    public void setMailPort(int mailPort) {
        this.mailPort = mailPort;
    }

    public String getMailUsername() {
        if (mailUsername == null) {
            mailUsername = env.getProperty("mail_username");
        }
        return mailUsername;
    }

    public void setMailUsername(String mailUsername) {
        this.mailUsername = mailUsername;
    }

    public String getMailPassword() {
        if (mailPassword == null) {
            mailPassword = env.getProperty("mail_password");
        }
        return mailPassword;
    }

    public void setMailPassword(String mailPassword) {
        this.mailPassword = mailPassword;
    }

    public String getMailFromaddress() {
        if (mailFromaddress == null) {
            mailFromaddress = env.getProperty("mail_fromaddress");
        }
        return mailFromaddress;
    }

    public void setMailFromaddress(String mailFromaddress) {
        this.mailFromaddress = mailFromaddress;
    }

    public String getMailToaddress() {
        if (mailToaddress == null) {
            mailToaddress = env.getProperty("mail_toaddress");
        }
        return mailToaddress;
    }

    public void setMailToaddress(String mailToaddress) {
        this.mailToaddress = mailToaddress;
    }
}
