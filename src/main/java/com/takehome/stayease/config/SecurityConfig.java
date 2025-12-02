package com.takehome.stayease.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import com.takehome.stayease.service.impl.UserServiceImpl;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
   private final UserServiceImpl userServiceImpl;
   private final JWTAuthenticationFilter jwtAuthenticationFilter;

   @Bean
    SecurityFilterChain sequrityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // disable the CSRF to test from localhost and Postman.
        httpSecurity.csrf(csrf -> csrf.disable());

        httpSecurity.authenticationProvider(authenticationProvider());

        httpSecurity.authorizeHttpRequests(configurer -> configurer
            .requestMatchers( "/api/users/register", "/api/users/login")
                .permitAll()
            .requestMatchers(HttpMethod.GET, "/api/hotels")
                .permitAll()
            .requestMatchers(HttpMethod.PUT, "/api/hotels/*")
                .hasRole("HOTEL_MANAGER")
            .requestMatchers(HttpMethod.DELETE, "api/bookings/*")
                .hasRole("HOTEL_MANAGER")
            .requestMatchers("/api/hotels", "/api/hotels/*", "/api/hotels/**")
                .hasRole("ADMIN")
            .anyRequest()
            .authenticated()
        );

        // *** Force 401 instead of 403 for unauthenticated users ***
        httpSecurity.exceptionHandling(ex -> ex
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        );

        // httpSecurity.httpBasic(Customizer.withDefaults());
        // Do not save the state, instead authenticate with token
        httpSecurity.sessionManagement( configurer ->
            configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        //Invoke JWT before the traditionational authentication
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    AuthenticationManager authenticationManager(
        AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = 
        new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userServiceImpl);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

