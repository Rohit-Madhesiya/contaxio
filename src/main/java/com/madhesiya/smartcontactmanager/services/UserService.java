package com.madhesiya.smartcontactmanager.services;

import java.util.List;
import java.util.Optional;

import com.madhesiya.smartcontactmanager.entities.User;

public interface UserService {

  User save(User user);

  Optional<User> getUserById(String id);

  Optional<User> updateUser(User user);

  void deleteUser(String id);

  boolean isUserExist(String userId);

  boolean isUserExistByEmail(String email);

  List<User> getAllUsers();

  User getUserByEmail(String email);

  User getUserByEmailToken(String token);
}
