package com.example.lab4.laba_4.config;

import com.example.lab4.laba_4.service.AccessDeniedHandler;
import com.example.lab4.laba_4.service.CleanerLogoutHandler;
import com.example.lab4.laba_4.service.DaoUserDetailsService;
import com.example.lab4.laba_4.service.SuccessLoginHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@Order(SecurityProperties.IGNORED_ORDER)
@Transactional
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final
    DataSource dataSource;
    final
    private DaoUserDetailsService daoUserDetailsService;
    private Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    private CleanerLogoutHandler cleanerLogoutHandler;
    private SuccessLoginHandler successLoginHandler;
    private AccessDeniedHandler accessDeniedHandler;


    @Autowired
    public SecurityConfig(DataSource dataSource, DaoUserDetailsService daoUserDetailsService, CleanerLogoutHandler cleanerLogoutHandler, SuccessLoginHandler successLoginHandler, AccessDeniedHandler accessDeniedHandler) {
        this.dataSource = dataSource;
        this.daoUserDetailsService = daoUserDetailsService;
        this.cleanerLogoutHandler = cleanerLogoutHandler;
        this.successLoginHandler = successLoginHandler;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(authenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        //TODO
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(daoUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder());
    }


    @Override
    @Transactional
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(successLoginHandler)
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .deleteCookies("JSESSIONID")
                .and()
                .cors()
                .configurationSource(corsConfigurationSource())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(accessDeniedHandler)
                .and()
                .authenticationProvider(authenticationProvider());
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.addAllowedHeader("Access-Control-Allow-Origin: *");
        corsConfiguration.addAllowedHeader("Access-Control-Allow-Methods: GET, POST, PATCH, PUT, DELETE, OPTIONS");
        corsConfiguration.addAllowedHeader("Access-Control-Allow-Headers: Origin, Content-Type, X-Auth-Token");

        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }

//    @Bean
//    GenericFilterBean filter() {
//        return new GenericFilterBean() {
//            @Override
//            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//                System.out.println(servletRequest.getParameter("username"));
//                System.out.println(servletRequest.getParameter("password"));
//                ;
//            }
//        };
//    }
}
