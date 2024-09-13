package com.madhesiya.smartcontactmanager.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.madhesiya.smartcontactmanager.entities.Contact;
import com.madhesiya.smartcontactmanager.entities.User;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
  // extra methods for db operations
  // custom query methods
  // custom finder methods

  // find the contacts
  Page<Contact> findByUser(User user, Pageable pageable);

  @Query("SELECT c FROM contact c WHERE c.user.id= :userId")
  List<Contact> findByUserId(@Param("userId") String userId);

  Page<Contact> findByUserAndNameContaining(User user, String namekeyword, Pageable pageable);

  Page<Contact> findByUserAndEmailContaining(User user, String emailKeyword, Pageable pageable);

  Page<Contact> findByUserAndPhoneNumberContaining(User user, String phoneNumber, Pageable pageable);

}
