package com.madhesiya.smartcontactmanager.controllers;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.madhesiya.smartcontactmanager.entities.Contact;
import com.madhesiya.smartcontactmanager.entities.User;
import com.madhesiya.smartcontactmanager.forms.ContactForm;
import com.madhesiya.smartcontactmanager.forms.ContactSearchForm;
import com.madhesiya.smartcontactmanager.helpers.AppConstants;
import com.madhesiya.smartcontactmanager.helpers.Helper;
import com.madhesiya.smartcontactmanager.helpers.Message;
import com.madhesiya.smartcontactmanager.helpers.MessageType;
import com.madhesiya.smartcontactmanager.services.ContactService;
import com.madhesiya.smartcontactmanager.services.ImageService;
import com.madhesiya.smartcontactmanager.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

  private Logger logger = LoggerFactory.getLogger(ContactController.class);

  @Autowired
  private ContactService contactService;

  @Autowired
  private UserService userService;

  @Autowired
  private ImageService imageService;

  // add contact page handler
  @RequestMapping("/add")
  public String addContactView(Model model) {
    ContactForm contactForm = new ContactForm();
    model.addAttribute("contactForm", contactForm);
    return "user/add_contact";
  }

  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult,
      Authentication authentication, HttpSession session) {

    // validate form

    if (bindingResult.hasErrors()) {
      Message message = Message.builder().content("Please correct the following errors.").type(MessageType.red)
          .build();
      session.setAttribute("message", message);
      return "user/add_contact";
    }

    // process the form data

    String username = Helper.getEmailOfLoggedInUser(authentication);

    Contact contact = new Contact();

    contact.setName(contactForm.getName());
    contact.setEmail(contactForm.getEmail());
    contact.setAddress(contactForm.getAddress());
    contact.setPhoneNumber(contactForm.getPhoneNumber());
    contact.setDescription(contactForm.getDescription());
    contact.setFavourite(contactForm.isFavourite());
    contact.setWebsiteLink(contactForm.getWebsiteLink());
    contact.setLinkedInLink(contactForm.getLinkedInLink());

    contact.setUser(userService.getUserByEmail(username));

    String filename = UUID.randomUUID().toString();

    // Contact image processing
    String fileURL = imageService.uploadImage(contactForm.getContactImg(), filename);
    contact.setCloudinaryImagePublicId(filename);
    contact.setPicture(fileURL);

    contactService.save(contact);
    Message message = Message.builder().content("You have successfully added a new contact.").type(MessageType.green)
        .build();
    session.setAttribute("message", message);
    return "redirect:/user/contacts/add";
  }

  @RequestMapping
  public String viewContacts(@RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "" + AppConstants.PAGE_SIZE) int size,
      @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
      @RequestParam(value = "direction", defaultValue = "asc") String direction,
      Model model, Authentication authentication) {

    // load all the user contacts

    String username = Helper.getEmailOfLoggedInUser(authentication);
    User user = userService.getUserByEmail(username);

    Page<Contact> pageContact = contactService.getByUser(user, page, size, sortBy, direction);

    model.addAttribute("pageContact", pageContact);
    model.addAttribute("page_size", AppConstants.PAGE_SIZE);

    model.addAttribute("contactSearchForm", new ContactSearchForm());

    return "user/contacts";
  }

  // search handler
  @RequestMapping("/search")
  public String searchHandler(@ModelAttribute ContactSearchForm contactSearchForm,
      @RequestParam(value = "size", defaultValue = "" + AppConstants.PAGE_SIZE) int size,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
      @RequestParam(value = "direction", defaultValue = "asc") String direction,
      Model model,
      Authentication authentication) {

    logger.info("field: {}, keyword: {}", contactSearchForm.getField(), contactSearchForm.getValue());

    String username = Helper.getEmailOfLoggedInUser(authentication);
    User user = userService.getUserByEmail(username);

    Page<Contact> pageContact = null;

    if (contactSearchForm.getField().equalsIgnoreCase("name")) {
      pageContact = contactService.searchByName(contactSearchForm.getValue(), page, size, sortBy, direction, user);
    } else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
      pageContact = contactService.searchByEmail(contactSearchForm.getValue(), page, size, sortBy, direction, user);
    } else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
      pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), page, size, sortBy, direction,
          user);
    }

    model.addAttribute("pageContact", pageContact);
    model.addAttribute("contactSearchForm", contactSearchForm);
    model.addAttribute("page_size", AppConstants.PAGE_SIZE);

    return "user/search";
  }

  @RequestMapping("/delete/{contactId}")
  public String deleteContact(@PathVariable String contactId, HttpSession session) {

    contactService.delete(contactId);

    logger.info("contact deleted: {}", contactId);

    session.setAttribute("message",
        Message.builder()
            .content("Contact is deleted successfully")
            .type(MessageType.green)
            .build());

    return "redirect:/user/contacts";
  }

  // update contact form view
  @GetMapping("/view/{contactId}")
  public String updateContactFormView(
      @PathVariable("contactId") String contactId,
      Model model) {

    var contact = contactService.getById(contactId);
    ContactForm contactForm = new ContactForm();
    contactForm.setName(contact.getName());
    contactForm.setEmail(contact.getEmail());
    contactForm.setPhoneNumber(contact.getPhoneNumber());
    contactForm.setAddress(contact.getAddress());
    contactForm.setDescription(contact.getDescription());
    contactForm.setFavourite(contact.isFavourite());
    contactForm.setWebsiteLink(contact.getWebsiteLink());
    contactForm.setLinkedInLink(contact.getLinkedInLink());
    contactForm.setPicture(contact.getPicture());
    ;
    model.addAttribute("contactForm", contactForm);
    model.addAttribute("contactId", contactId);

    return "user/update_contact_view";
  }

  @RequestMapping(value = "/update/{contactId}", method = RequestMethod.POST)
  public String updateContact(@PathVariable("contactId") String contactId,
      @Valid @ModelAttribute ContactForm contactForm,
      BindingResult bindingResult,
      Model model, HttpSession session) {

    // update the contact
    if (bindingResult.hasErrors()) {
      return "user/update_contact_view";
    }

    var con = contactService.getById(contactId);
    con.setId(contactId);
    con.setName(contactForm.getName());
    con.setEmail(contactForm.getEmail());
    con.setPhoneNumber(contactForm.getPhoneNumber());
    con.setAddress(contactForm.getAddress());
    con.setDescription(contactForm.getDescription());
    con.setFavourite(contactForm.isFavourite());
    con.setWebsiteLink(contactForm.getWebsiteLink());
    con.setLinkedInLink(contactForm.getLinkedInLink());

    // process image:

    if (contactForm.getContactImg() != null && !contactForm.getContactImg().isEmpty()) {
      logger.info("file is not empty");
      String fileName = UUID.randomUUID().toString();
      String imageUrl = imageService.uploadImage(contactForm.getContactImg(), fileName);
      con.setCloudinaryImagePublicId(fileName);
      con.setPicture(imageUrl);
      contactForm.setPicture(imageUrl);

    } else {
      logger.info("file is empty");
    }

    var updateCon = contactService.update(con);
    logger.info("updated contact {}", updateCon);

    session.setAttribute("message",
        Message.builder()
            .content("Contact Updated !!")
            .type(MessageType.green)
            .build());

    return "redirect:/user/contacts/view/" + contactId;
  }

}
