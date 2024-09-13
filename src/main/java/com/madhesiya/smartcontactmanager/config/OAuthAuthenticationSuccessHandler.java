package com.madhesiya.smartcontactmanager.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.madhesiya.smartcontactmanager.entities.Providers;
import com.madhesiya.smartcontactmanager.entities.User;
import com.madhesiya.smartcontactmanager.repositories.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);
  @Value("${spring.security.user.roles}")
  private List<String> role_list;

  @Autowired
  private UserRepository userRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    logger.info("OAuthAuthenticationSuccessHandler");
    var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

    String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

    DefaultOAuth2User auth2User = (DefaultOAuth2User) authentication.getPrincipal();

    User user1 = new User();
    user1.setUserId(UUID.randomUUID().toString());
    user1.setRoleList(role_list);
    user1.setEnabled(true);
    user1.setEmailVerified(true);

    // google login

    if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {

      String email = auth2User.getAttribute("email");
      String name = auth2User.getAttribute("name");
      String picture = auth2User.getAttribute("picture");

      user1.setEmail(email);
      user1.setUsername(name);
      user1.setPassword("password");
      user1.setProfilePic(picture);

      user1.setProvider(Providers.GOOGLE);
      user1.setProviderUserId(auth2User.getName());

      user1.setAbout("This account is created using google.");

    } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {

      // github login
      String email = auth2User.getAttribute("email") != null ? auth2User.getAttribute("email").toString()
          : auth2User.getAttribute("login") + "@gmail.com";

      String name = auth2User.getAttribute("login");
      String picture = auth2User.getAttribute("avatar_url").toString();

      user1.setUsername(name);
      user1.setPassword("password");
      user1.setEmail(email);
      user1.setProfilePic(picture);
      user1.setUserId(name);
      user1.setProvider(Providers.GITHUB);
      user1.setProviderUserId(auth2User.getName());
      user1.setAbout("This account is created using github.");
    }

    // save data in database

    User presentUser = userRepository.findByEmail(user1.getEmail()).orElse(null);
    if (presentUser == null) {
      userRepository.save(user1);
      logger.info("User Saved: " + user1.getEmail());
    }

    new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");

  }

}
