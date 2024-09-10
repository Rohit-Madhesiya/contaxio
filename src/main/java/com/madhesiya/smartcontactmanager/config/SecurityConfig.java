package com.madhesiya.smartcontactmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.madhesiya.smartcontactmanager.services.Impl.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {
  // user create and login using java code with in memory service
  // @Bean
  // public UserDetailsService userDetailsService() {
  // UserDetails user = User
  // .withDefaultPasswordEncoder()
  // .username("admin")
  // .password("admin123")
  // .roles("ADMIN", "USER")
  // .build();
  // return new InMemoryUserDetailsManager(user);
  // }

  @Autowired
  private SecurityCustomUserDetailService userDetailService;

  @Autowired
  private OAuthAuthenticationSuccessHandler successHandler;

  // Configuration of authentication provider for spring security
  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    // user detail service object
    daoAuthenticationProvider.setUserDetailsService(userDetailService);
    // password encoder object
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    return daoAuthenticationProvider;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    // configuration

    httpSecurity.authorizeHttpRequests(authorize -> {
      // authorize.requestMatchers("/home", "/register", "/services",
      // "/about").permitAll();
      authorize.requestMatchers("/user/**").authenticated();
      authorize.anyRequest().permitAll();
    });

    // form default login
    httpSecurity.formLogin(formLogin -> {

      formLogin.loginPage("/login");
      formLogin.loginProcessingUrl("/authenticate");
      formLogin.successForwardUrl("/user/profile");
      formLogin.failureForwardUrl("/login?error=true");
      formLogin.usernameParameter("email");
      formLogin.passwordParameter("password");
    });

    httpSecurity.csrf(AbstractHttpConfigurer::disable);
    httpSecurity.logout(logoutForm -> {
      logoutForm.logoutUrl("/logout");
      logoutForm.logoutSuccessUrl("/login?logout=true");
    });

    // oauth configuration
    httpSecurity.oauth2Login(oauth -> {
      oauth.loginPage("/login");
      oauth.successHandler(successHandler);
    });

    return httpSecurity.build();

  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
