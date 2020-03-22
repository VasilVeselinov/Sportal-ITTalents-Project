package sportal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sportal.model.service.IAuthService;

import static sportal.util.GlobalConstants.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private IAuthService authService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String[] URL_FOR_NON_LOGGED_USER = {
            "/", "/articles/{id}", "/articles/top_5_read_today", "/articles/the_category/{id}", "/articles/search",
            "/categories/all", "/comments/all/{id}", "/comments/{id}", "/emails/registration_confirm", "/favicon.ico"
    };

    private static final String[] URL_RESOURCES = {
            "/static/css/*", "/static/img/*", "/static/js/*",
            "/" + PACKAGE_FOR_PICTURES + "/*", "/" + PACKAGE_FOR_VIDEOS + "/*", "/" + PACKAGE_FOR_LOG_FILES + "/*"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/users/registration", "/users/login", "/after_registration",
                            "favicon.ico")
                        .anonymous()
                    .antMatchers(URL_RESOURCES)
                        .permitAll()
                    .antMatchers(HttpMethod.GET, URL_FOR_NON_LOGGED_USER)
                        .permitAll()
                    .anyRequest()
                    	.authenticated()
                .and()
                    .formLogin()
                        .loginPage("/users/login")
                            .permitAll()
                            .usernameParameter("username")
                            .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                            .permitAll()
                .and()
                    .exceptionHandling()
                        .accessDeniedPage("/unauthorized");
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return this.authService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.authService).passwordEncoder(this.passwordEncoder);
    }
}
