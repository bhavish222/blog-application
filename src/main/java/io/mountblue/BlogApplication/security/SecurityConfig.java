package io.mountblue.BlogApplication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select name, role from roles where name=?");
        return jdbcUserDetailsManager;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        configurer ->
                                configurer
                                        .requestMatchers("/css/**").permitAll()
                                        .requestMatchers("/").permitAll()
                                        .requestMatchers("/newpost").hasRole("ADMIN")
                                        .requestMatchers("/post**").permitAll()
                                        .anyRequest()
                                        .authenticated()
                )
                .formLogin(form ->
                                form.loginPage("/login-page")
                                        .loginProcessingUrl("/authenticateTheUser")
                                        .permitAll()
                );
        return http.build();
    }
}