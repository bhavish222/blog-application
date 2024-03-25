package io.mountblue.BlogApplication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        jdbcUserDetailsManager.setUsersByUsernameQuery("select name, password, enabled from user where name=?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select username, role from role where username=?");
        return jdbcUserDetailsManager;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                configurer ->
                        configurer
                                    .requestMatchers("/","/css/**","/post**","/signup","/register","/login-page").permitAll()
                                    .requestMatchers("/newpost").hasAnyRole("ADMIN","AUTHOR")
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
        return http.build();
    }
}