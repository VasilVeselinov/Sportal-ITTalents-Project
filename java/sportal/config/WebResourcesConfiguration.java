package sportal.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static sportal.GlobalConstants.PACKAGE_FOR_PICTURES;
import static sportal.GlobalConstants.PACKAGE_FOR_VIDEOS;

@Configuration
@EnableWebMvc
@ComponentScan
public class WebResourcesConfiguration implements WebMvcConfigurer {

    // Vasko : please fix me, if you change directory for upload
    private static final String UPLOAD_DIRECTORY_FOR_PICTURES =
            System.getProperty("user.home") + "\\Desktop\\" + PACKAGE_FOR_PICTURES;
    private static final String UPLOAD_DIRECTORY_FOR_VIDEOS =
            System.getProperty("user.home") + "\\Desktop\\" + PACKAGE_FOR_VIDEOS;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "/" + PACKAGE_FOR_PICTURES + "/**",
                "/" + PACKAGE_FOR_VIDEOS + "/**",
                "/static/css/**",
                "/static/img/**",
                "/static/js/**")
                .addResourceLocations(
                        "file:" + UPLOAD_DIRECTORY_FOR_PICTURES + "\\",
                        "file:" + UPLOAD_DIRECTORY_FOR_VIDEOS + "\\",
                        "classpath:/static/css/",
                        "classpath:/static/img/",
                        "classpath:/static/js/");
    }
}
