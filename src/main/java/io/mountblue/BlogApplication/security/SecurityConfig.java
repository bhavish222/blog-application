package io.mountblue.BlogApplication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery("select name, password, 'true' from user where name=?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select username, role from role where username=?");
        return jdbcUserDetailsManager;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                configurer ->
                        configurer
                                .requestMatchers("/newpost","/editpost/**","/postcomment/**").hasAnyRole("ADMIN","AUTHOR")
                                .requestMatchers("/","/css/**","/post**","/signup","/register","/login-page","/api/post**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/editpost/**").hasAnyRole("ADMIN","AUTHOR")
                                .requestMatchers(HttpMethod.POST, "/api/savepost").hasAnyRole("ADMIN","AUTHOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/deletepost/**").hasAnyRole("ADMIN","AUTHOR")
                                .requestMatchers(HttpMethod.POST, "/api/postcomment/post**").hasAnyRole("ADMIN","AUTHOR")
                                .requestMatchers(HttpMethod.DELETE, "/api/deletecomment/comment**").hasAnyRole("ADMIN","AUTHOR")
                                .requestMatchers(HttpMethod.PUT, "/api/updatecomment/comment**").hasAnyRole("ADMIN","AUTHOR")
                                .anyRequest()
                                .authenticated()
                )
                .exceptionHandling(
                        configurer ->
                                configurer
                                        .accessDeniedPage("/access-denied")
                )
                .formLogin(form ->
                        form.loginPage("/login-page")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                )
                .logout(
                        logout ->
                                logout.permitAll()
                );
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
}