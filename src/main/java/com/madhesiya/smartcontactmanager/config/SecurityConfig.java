package com.madhesiya.smartcontactmanager.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.madhesiya.smartcontactmanager.helpers.Message;
import com.madhesiya.smartcontactmanager.helpers.MessageType;
import com.madhesiya.smartcontactmanager.services.Impl.SecurityCustomUserDetailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Configuration
public class SecurityConfig {
  // user create and login using java code with in memory service

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

      authorize.requestMatchers("/user/**").authenticated();
      // TODO: Test this restriction
      // authorize.requestMatchers("/api/**").authenticated();
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

      formLogin.failureHandler(new AuthenticationFailureHandler() {

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
          if (exception instanceof DisabledException) {
            // user is disabled
            HttpSession session = request.getSession();
            session.setAttribute("message",
                Message.builder()
                    .content("User is disabled, An e-mail with verification link has been sent on the e-mail.")
                    .type(MessageType.red)
                    .build());
            response.sendRedirect("/login");
          } else {
            HttpSession session = request.getSession();
            session.setAttribute("message",
                Message.builder()
                    .content("Invalid username or password.")
                    .type(MessageType.red)
                    .build());
            response.sendRedirect("/login");
          }
        }

      });
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
