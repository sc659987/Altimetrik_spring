package test.altimetrik.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import test.altimetrik.filter.JwtAuthenticationFilter;
import test.altimetrik.filter.NoAuthenticationEntryPoint;
import test.altimetrik.filter.UsernameAndPasswordAuthenticationSuccessHandler;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ComponentScan(basePackages = "test.altimetrik.controller")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final String DEFAULT_SECURED_PATH_PATTERN = "/api/**";
    private final String PROPERTY_SECURED_PATH_PATTERN = "core.rest.framework.security.secured-path-pattern";

    private final List<AuthenticationProvider> authenticationProviders;
    private final Environment environment;
    private final NoAuthenticationEntryPoint unauthorizedHandler;


    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        authenticationProviders.forEach(builder::authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        final String securedPath = environment.getProperty(PROPERTY_SECURED_PATH_PATTERN, DEFAULT_SECURED_PATH_PATTERN);
        httpSecurity.anonymous().disable()
                .csrf().disable()
                .rememberMe().disable()
                .requestCache().disable()
                .formLogin()
                .loginProcessingUrl("/login") //the URL on which the clients should post the login information
                .usernameParameter("username") //the username parameter in the queryString, default is 'username'
                .passwordParameter("password")
                .successHandler(new UsernameAndPasswordAuthenticationSuccessHandler())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(securedPath)
                .authenticated();
        // First the Jwt token then Username and password auth
        httpSecurity.addFilterBefore(new JwtAuthenticationFilter(super.authenticationManagerBean()), UsernamePasswordAuthenticationFilter.class);
    }
}
