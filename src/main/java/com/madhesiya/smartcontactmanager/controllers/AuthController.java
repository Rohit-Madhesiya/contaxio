package com.madhesiya.smartcontactmanager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madhesiya.smartcontactmanager.entities.User;
import com.madhesiya.smartcontactmanager.helpers.Message;
import com.madhesiya.smartcontactmanager.helpers.MessageType;
import com.madhesiya.smartcontactmanager.repositories.UserRepository;
import com.madhesiya.smartcontactmanager.services.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @RequestMapping("/verify-email")
  public String verifyEmail(@RequestParam("token") String emailToken, HttpSession session) {

    User user = userService.getUserByEmailToken(emailToken);
    Message message;
    if (user != null) {
      // user fetched

      user.setEmailVerified(true);
      user.setEnabled(true);
      userRepository.save(user);
      message = Message.builder().content("Account verified successfully.").type(MessageType.green).build();
      System.out.println("Account verified successfully.");

    }
    message = Message.builder().content("User not found, Account verification failed!").type(MessageType.red).build();
    session.setAttribute("message", message);

    return "redirect:/login";

  }
}
