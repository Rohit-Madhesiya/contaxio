package com.madhesiya.smartcontactmanager.controllers;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// import com.madhesiya.smartcontactmanager.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

  // private Logger logger = LoggerFactory.getLogger(UserController.class);

  // @Autowired
  // private UserService userService;

  @GetMapping("/")
  public String index() {
    return "redirect:/home";
  }

  // user dashboard page
  @RequestMapping(value = "/dashboard")
  public String userDashboard() {
    return "user/dashboard";
  }

  // user profile page
  @RequestMapping(value = "/profile")
  public String userProfile(Model model, Authentication authentication) {
    return "user/profile";
  }

  // user add contact page

  // user view contact page

  // user edit contact page

  // user delete contact page

}
