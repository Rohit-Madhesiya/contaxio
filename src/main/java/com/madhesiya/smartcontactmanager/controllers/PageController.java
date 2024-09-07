package com.madhesiya.smartcontactmanager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.madhesiya.smartcontactmanager.forms.UserForm;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PageController {

  @RequestMapping("/home")
  public String home(Model model) {
    System.out.println("Home Page Handler..");
    model.addAttribute("name", "Smart Contact Manager");
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
    return new String("contact");
  }

  @GetMapping("/login")
  public String getLoginPage() {
    return new String("login");
  }

  @GetMapping("/register")
  public String getRegisterPage(Model model) {
    UserForm userform = new UserForm();
    // userform.setUsername("Rohit");
    model.addAttribute("userForm", userform);
    return new String("register");
  }

  // Processing Request

  // @PostMapping("/do-register")
  @RequestMapping(value = "/do-register", method = RequestMethod.POST)
  public String processRegister(@ModelAttribute UserForm userForm) {
    System.out.println("Registration Processing...");
    // fetch form data
    System.out.println(userForm);
    // validate form data

    // save data in DB

    // redirect
    return "redirect:/register";
  }

}
