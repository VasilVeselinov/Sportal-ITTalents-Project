package sportal.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static sportal.util.GlobalConstants.*;

@Configuration
@EnableWebMvc
@ComponentScan
public class WebResourcesConfiguration implements WebMvcConfigurer {

    // Vasko : please fix me, if you change directory
    private static final String UPLOAD_PATH_FOR_PICTURES =
            System.getProperty("user.home") + "\\Desktop\\" + PACKAGE_FOR_PICTURES;
    private static final String UPLOAD_PATH_FOR_VIDEOS =
            System.getProperty("user.home") + "\\Desktop\\" + PACKAGE_FOR_VIDEOS;
    private static final String SAVE_PATH_FOR_LOGFILE =
            System.getProperty("user.home") + "\\Desktop\\" + PACKAGE_FOR_LOG_FILES;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "/" + PACKAGE_FOR_PICTURES + "/**",
                "/" + PACKAGE_FOR_VIDEOS + "/**",
                "/" + PACKAGE_FOR_LOG_FILES + "/**",
                "/static/css/**",
                "/static/img/**",
                "/static/js/**")
                .addResourceLocations(
                        "file:" + UPLOAD_PATH_FOR_PICTURES + "\\",
                        "file:" + UPLOAD_PATH_FOR_VIDEOS + "\\",
                        "file:" + SAVE_PATH_FOR_LOGFILE + "\\",
                        "classpath:/static/css/",
                        "classpath:/static/img/",
                        "classpath:/static/js/");
    }
}
