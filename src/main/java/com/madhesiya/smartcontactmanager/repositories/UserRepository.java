package com.madhesiya.smartcontactmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.madhesiya.smartcontactmanager.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  // extra methods for db operations
  // custom query methods
  // custom finder methods
}
