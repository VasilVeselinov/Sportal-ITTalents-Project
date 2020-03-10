package sportal.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan
public class WebResourcesConfiguration implements WebMvcConfigurer {

    // Vasko : please fix me, if you change directory for upload pictures
    private static final String uploadDirectory = System.getProperty("user.home") + "\\Desktop\\uploadPictures";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploadPictures/**",
                "/static/css/**",
                "/static/img/**",
                "/static/js/**")
                .addResourceLocations("file:" + uploadDirectory + "\\",
                        "classpath:/static/css/",
                        "classpath:/static/img/",
                        "classpath:/static/js/");
    }
}
