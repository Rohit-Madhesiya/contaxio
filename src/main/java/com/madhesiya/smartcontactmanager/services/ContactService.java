package com.madhesiya.smartcontactmanager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.madhesiya.smartcontactmanager.entities.Contact;
import com.madhesiya.smartcontactmanager.entities.User;

public interface ContactService {

  Contact save(Contact contact);

  Contact getById(String id);

  Optional<Contact> update(Contact contact);

  List<Contact> getAll();

  void delete(String id);

  Page<Contact> searchByName(String name, int page, int size, String sortBy, String order, User user);

  Page<Contact> searchByEmail(String email, int page, int size, String sortBy, String order, User user);

  Page<Contact> searchByPhoneNumber(String phoneNumber, int page, int size, String sortBy, String order, User user);

  List<Contact> getByUserId(String userId);

  Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction);

}
