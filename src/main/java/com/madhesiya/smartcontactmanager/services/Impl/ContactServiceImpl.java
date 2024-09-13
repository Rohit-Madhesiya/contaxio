package com.madhesiya.smartcontactmanager.services.Impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.madhesiya.smartcontactmanager.entities.Contact;
import com.madhesiya.smartcontactmanager.entities.User;
import com.madhesiya.smartcontactmanager.helpers.ResourceNotFoundException;
import com.madhesiya.smartcontactmanager.repositories.ContactRepository;
import com.madhesiya.smartcontactmanager.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService {
  @Autowired
  private ContactRepository contactRepository;

  @Override
  public Contact save(Contact contact) {

    String contactId = UUID.randomUUID().toString();
    contact.setId(contactId);
    return contactRepository.save(contact);
  }

  @Override
  public Contact getById(String id) {
    return contactRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id: " + id));
  }

  @Override
  public Optional<Contact> update(Contact contact) {
    return null;
  }

  @Override
  public List<Contact> getAll() {
    return contactRepository.findAll();
  }

  @Override
  public void delete(String id) {
    Contact contact = contactRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id: " + id));

    contactRepository.delete(contact);
  }

  @Override
  public List<Contact> getByUserId(String userId) {
    return contactRepository.findByUserId(userId);
  }

  @Override
  public Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction) {

    Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    var pageable = PageRequest.of(page, size, sort);
    return contactRepository.findByUser(user, pageable);
  }

  @Override
  public Page<Contact> searchByName(String name, int page, int size, String sortBy, String order, User user) {
    Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    var pageable = PageRequest.of(page, size, sort);
    return contactRepository.findByUserAndNameContaining(user, name, pageable);
  }

  @Override
  public Page<Contact> searchByEmail(String email, int page, int size, String sortBy, String order, User user) {
    Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    var pageable = PageRequest.of(page, size, sort);
    return contactRepository.findByUserAndEmailContaining(user, email, pageable);
  }

  @Override
  public Page<Contact> searchByPhoneNumber(String phoneNumber, int page, int size, String sortBy, String order,
      User user) {
    Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    var pageable = PageRequest.of(page, size, sort);
    return contactRepository.findByUserAndPhoneNumberContaining(user, phoneNumber, pageable);
  }

}
