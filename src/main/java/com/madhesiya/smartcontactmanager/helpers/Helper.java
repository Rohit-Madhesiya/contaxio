package com.madhesiya.smartcontactmanager.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {

  public static String getEmailOfLoggedInUser(Authentication authentication) {

    if (authentication instanceof OAuth2AuthenticationToken) {

      String username = "";

      var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

      String clientId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

      var auth2User = (OAuth2User) authentication.getPrincipal();

      if (clientId.equalsIgnoreCase("google")) {
        // if loggedin with google
        username = auth2User.getAttribute("email");

        System.out.println("Logged in with Google");

      } else if (clientId.equalsIgnoreCase("github")) {
        // if loggedin with github
        username = auth2User.getAttribute("email") != null ? auth2User.getAttribute("email").toString()
            : auth2User.getAttribute("login") + "@gmail.com";

        System.out.println("Logged in with Github");

      }

      return username;

    } else {
      // if loggedin with email
      System.out.println("Logged in with database");
      return authentication.getName();
    }

  }

}
