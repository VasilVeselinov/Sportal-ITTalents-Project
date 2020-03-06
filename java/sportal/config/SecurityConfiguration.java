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
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private IAuthService authService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                // for not logged user
                .authorizeRequests()
                    .antMatchers("/")
                        .permitAll()
                    .antMatchers("/users/registration", "/users/login", "/after_registration")
                        .anonymous()
                    .antMatchers("/css/*", "/img/*", "/js/*")
                        .permitAll()
                    .antMatchers(HttpMethod.GET,"/articles/{id}", "/articles/top_5_read_today",
                            "/articles/the_category/{id}", "/articles/search")
                        .permitAll()
                    .antMatchers(HttpMethod.GET,"/categories/all")
                        .permitAll()
                    .antMatchers(HttpMethod.GET,"/comments/all/{id}", "/comments/{id}")
                        .permitAll()
                    .antMatchers(HttpMethod.GET, "/emails/registration_confirm")
                        .permitAll()
                    .anyRequest()
                        .authenticated()
                .and()
                    .formLogin()
                        .loginPage("/users/login")
                            .permitAll()
                            .usernameParameter("username")
                            .passwordParameter("password")
                // redirect: after login -> /home
                        .defaultSuccessUrl("/",true)
                .and()
                // redirect: after logout -> /login
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
