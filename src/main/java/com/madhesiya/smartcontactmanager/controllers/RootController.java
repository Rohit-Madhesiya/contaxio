package com.madhesiya.smartcontactmanager.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.madhesiya.smartcontactmanager.entities.User;
import com.madhesiya.smartcontactmanager.helpers.Helper;
import com.madhesiya.smartcontactmanager.services.UserService;

@ControllerAdvice
public class RootController {

  private Logger logger = LoggerFactory.getLogger(RootController.class);

  @Autowired
  private UserService userService;

  @ModelAttribute
  public void addLoggedInUserInfo(Model model, Authentication authentication) {
    if (authentication == null)
      return;
    System.out.println("Adding logged in user information to the model");
    String username = Helper.getEmailOfLoggedInUser(authentication);
    logger.info("User logger in: {}", username);
    // Fetch data from the DB

    User user = userService.getUserByEmail(username);

    logger.info("name: {}", user.getUsername());
    logger.info("email: {}", user.getEmail());
    logger.info("Profile Picture: {}", user.getProfilePic());
    model.addAttribute("userProfile", user);
  }
}
