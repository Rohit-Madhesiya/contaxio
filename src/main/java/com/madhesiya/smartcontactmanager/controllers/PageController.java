package com.madhesiya.smartcontactmanager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
