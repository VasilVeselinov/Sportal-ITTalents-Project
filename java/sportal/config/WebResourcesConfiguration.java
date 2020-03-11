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

    // Vasko : please fix me, if you change directory for upload
    private static final String uploadDirectoryForPictures = System.getProperty("user.home") + "\\Desktop\\uploadPictures";
    private static final String uploadDirectoryForVideos = System.getProperty("user.home") + "\\Desktop\\uploadVideos";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "/uploadPictures/**",
                "/uploadVideos/**",
                "/static/css/**",
                "/static/img/**",
                "/static/js/**")
                .addResourceLocations(
                        "file:" + uploadDirectoryForPictures + "\\",
                        "file:" + uploadDirectoryForVideos + "\\",
                        "classpath:/static/css/",
                        "classpath:/static/img/",
                        "classpath:/static/js/");
    }
}
