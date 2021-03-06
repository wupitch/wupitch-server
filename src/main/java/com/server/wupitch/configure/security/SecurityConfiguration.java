package com.server.wupitch.configure.security;

import com.server.wupitch.configure.response.exception.CustomAccessDeniedHandler;
import com.server.wupitch.configure.response.exception.CustomAuthenticationEntryPoint;
import com.server.wupitch.configure.security.jwt.JwtAuthenticationFilter;
import com.server.wupitch.configure.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    

    @Value("${mapping.url}")
    private String mappingUrl;


    @Configuration
    public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins(mappingUrl)
                    .allowedMethods("GET", "POST", "OPTIONS", "PUT", "PATCH");
        }
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs/**", "/swagger-resources/**",
                "/swagger-ui/**", "/webjars/**", "/swagger/**","/swagger.json");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/app/sign-in", "/app/sign-up","/app/accounts/kakao","/app/accounts/apple","/app/accounts/nickname/validation", "/app/accounts/email/validation", "/app/accounts/password/validation").permitAll()
                .antMatchers(HttpMethod.GET, "/errors/**","/app/areas", "/app/sports", "/app/extras","/app/accounts/auth/{accountId}/enrollment-agree","/app/accounts/auth/{accountId}/enrollment-deny").permitAll()
                .antMatchers(HttpMethod.PATCH, "/app/accounts/invalid-status/{email}").permitAll()
                .antMatchers(HttpMethod.PATCH, "/app/accounts/role").hasRole("ADMIN")
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}