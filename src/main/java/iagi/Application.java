package iagi;

import org.springframework.beans.factory.annotation.Autowired;
import iagi.jwt.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import iagi.jwt.JwtService;

/**
 *
 * @author rais
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Autowired
    public FilterRegistrationBean jwtFilter(JwtService jwtService) {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new JwtFilter(jwtService));
        bean.addUrlPatterns("/api/*");
        return bean;
    }

}
