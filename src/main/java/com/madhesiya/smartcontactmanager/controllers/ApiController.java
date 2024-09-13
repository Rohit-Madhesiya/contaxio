package com.madhesiya.smartcontactmanager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.madhesiya.smartcontactmanager.entities.Contact;
import com.madhesiya.smartcontactmanager.services.ContactService;

@RestController
@RequestMapping("/api")
public class ApiController {

  @Autowired
  private ContactService contactService;

  // get contacts

  @GetMapping("/contacts/{contactId}")
  public Contact getContact(@PathVariable String contactId) {

    return contactService.getById(contactId);

  }

}
