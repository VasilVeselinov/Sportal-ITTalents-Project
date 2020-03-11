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

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private IAuthService authService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String[] urlForNonLoggedUser = {
            "/", "/articles/{id}", "/articles/top_5_read_today", "/articles/the_category/{id}", "/articles/search",
            "/categories/all" , "/comments/all/{id}", "/comments/{id}" , "/emails/registration_confirm"
    };

    private static final String[] urlResources = {
            "/static/css/*", "/static/img/*", "/static/js/*", "/uploadPictures/*"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/users/registration", "/users/login", "/after_registration")
                        .anonymous()
                    .antMatchers(urlResources)
                        .permitAll()
                    .antMatchers(HttpMethod.GET, urlForNonLoggedUser)
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
