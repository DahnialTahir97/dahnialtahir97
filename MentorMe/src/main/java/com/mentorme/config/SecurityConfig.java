package com.mentorme.config;
import com.mentorme.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }
 @Bean DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService service, PasswordEncoder encoder){
  DaoAuthenticationProvider p=new DaoAuthenticationProvider(); p.setUserDetailsService(service); p.setPasswordEncoder(encoder); return p;
 }
 @Bean SecurityFilterChain securityFilterChain(HttpSecurity http, DaoAuthenticationProvider provider) throws Exception {
  return http.authenticationProvider(provider).authorizeHttpRequests(a->a
   .requestMatchers("/", "/register", "/css/**", "/images/**").permitAll()
   .requestMatchers("/admin/**").hasRole("ADMIN")
   .requestMatchers("/tutor/**").hasRole("TUTOR")
   .requestMatchers("/student/**").hasRole("STUDENT")
   .anyRequest().authenticated())
   .formLogin(f->f.loginPage("/login").defaultSuccessUrl("/dashboard",true).permitAll())
   .logout(l->l.logoutSuccessUrl("/?logout").permitAll()).build();
 }
}