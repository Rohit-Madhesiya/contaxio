package com.madhesiya.smartcontactmanager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.madhesiya.smartcontactmanager.entities.User;
import com.madhesiya.smartcontactmanager.forms.UserForm;
import com.madhesiya.smartcontactmanager.helpers.Message;
import com.madhesiya.smartcontactmanager.helpers.MessageType;
import com.madhesiya.smartcontactmanager.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class PageController {
  @Autowired
  private UserService userService;

  @GetMapping("/")
  public String homeInit2(Model model) {
    System.out.println("Home Page Handler..");
    model.addAttribute("name", "Contaxio");
    return "home";
  }

  @RequestMapping("/home")
  public String home(Model model) {
    System.out.println("Home Page Handler..");
    model.addAttribute("name", "Contaxio");
    return "home";
  }

  @RequestMapping("/about")
  public String aboutPage() {
    return "about";

  }

  @RequestMapping("/services")
  public String servicesPage() {
    return "services";
  }

  @GetMapping("/contact")
  public String getContactPage() {
    return "contact";
  }

  @GetMapping("/login")
  public String getLoginPage() {
    return "login";
  }

  @GetMapping("/register")
  public String getRegisterPage(Model model) {
    UserForm userform = new UserForm();
    // userform.setUsername("Rohit");
    model.addAttribute("userForm", userform);
    return "register";
  }

  // Processing Request

  // @PostMapping("/do-register")
  @RequestMapping(value = "/do-register", method = RequestMethod.POST)
  public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult,
      HttpSession session) {
    System.out.println("Registration Processing...");
    // fetch form data
    System.out.println(userForm);
    // validate form data
    if (bindingResult.hasErrors()) {
      return "register";
    }

    // save data in DB
    User user = new User();
    user.setUsername(userForm.getUsername());
    user.setEmail(userForm.getEmail());
    user.setPassword(userForm.getPassword());
    user.setAbout(userForm.getAbout());
    user.setPhoneNumber(userForm.getPhoneNumber());
    user.setProfilePic("");
    user.setEnabled(false);

    userService.save(user);

    // add the message
    System.out.println("User saved.");
    Message message = Message.builder()
        .content("Registration Successful, An email has been sent to verify your account.").type(MessageType.green)
        .build();
    session.setAttribute("message", message);
    // redirect
    return "redirect:/register";
  }

}
