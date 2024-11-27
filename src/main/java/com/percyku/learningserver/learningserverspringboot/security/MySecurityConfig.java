package com.percyku.learningserver.learningserverspringboot.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    //authenticationProvider bean definition
    @Bean
    public DaoAuthenticationProvider authenticationProvider(MyUserDetailService userService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService); //set the custom user details service
        auth.setPasswordEncoder(passwordEncoder()); //set the password encoder - bcrypt
        return auth;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        return http
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                // 設定 Session 的創建機制
                .sessionManagement(
                        session ->  session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )
                // 設定 CSRF 保護
                //.csrf( csrf ->csrf.disable())
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(createCsrfHandler())
                        .ignoringRequestMatchers("/register")
                )
                // 設定 CORS 跨域
                //.cors(cors -> cors.disable())
                .cors(cors -> cors.configurationSource(createCorsConfig()))
                // 設定 api 的權限控制
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/register","/logoutSuccess","/loginTest").permitAll()
//                        .requestMatchers("/welcome","/welcome2","/loginforlearnsys","/logoutforlearnsys").authenticated()
                        .requestMatchers("/updateProfile","/loginforlearnsys","/logoutforlearnsys").authenticated()
                        .requestMatchers("/api/courses/student/{id}","/api/courses/findByName","/api/courses/enroll/{courseId}").hasRole("STUDENT")
                        .requestMatchers("/api/courses/instructor/{id}","/api/courses").hasRole("INSTRUCTOR")

                        .anyRequest().denyAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logoutforlearnsys")
                        .logoutSuccessUrl("/logoutSuccess")
                        .deleteCookies("JSESSIONID")
                        .deleteCookies("XSRF-TOKEN")
                )
                .build();

    }

    private CsrfTokenRequestAttributeHandler createCsrfHandler() {
        CsrfTokenRequestAttributeHandler csrfHandler = new CsrfTokenRequestAttributeHandler();
        csrfHandler.setCsrfRequestAttributeName(null);

        return csrfHandler;
    }


    private CorsConfigurationSource createCorsConfig(){
        CorsConfiguration config= new CorsConfiguration();
//        config.setAllowedOrigins(List.of("*"));
        config.setAllowedOrigins(List.of("http://localhost:3000","null"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source =new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);

        return source;

    }
}
