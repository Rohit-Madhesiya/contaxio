package com.madhesiya.smartcontactmanager.services.Impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.madhesiya.smartcontactmanager.entities.User;
import com.madhesiya.smartcontactmanager.helpers.Helper;
import com.madhesiya.smartcontactmanager.helpers.ResourceNotFoundException;
import com.madhesiya.smartcontactmanager.repositories.UserRepository;
import com.madhesiya.smartcontactmanager.services.EmailService;
import com.madhesiya.smartcontactmanager.services.UserService;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Value("${spring.security.user.roles}")
  private List<String> role_list;

  @Autowired
  private EmailService emailService;

  @Autowired
  private Helper helper;

  @Override
  public User save(User user) {
    String userId = UUID.randomUUID().toString();
    user.setUserId(userId);
    // Password encoding will be done here
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    // set the user role
    user.setRoleList(role_list);

    logger.info(user.getProvider().toString());

    String token = UUID.randomUUID().toString();
    user.setEmailToken(token);

    User savedUser = userRepository.save(user);
    token = helper.getEmailVerificationLink(token);

    emailService.sendEmail(savedUser.getEmail(), savedUser.getUsername()+", Verify your account - Contaxio", token);

    return savedUser;
  }

  @Override
  public Optional<User> getUserById(String id) {
    return userRepository.findById(id);
  }

  @Override
  public Optional<User> updateUser(User user) {
    User user2 = userRepository.findById(user.getUserId())
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    user2.setUsername(user.getUsername());
    user2.setEmail(user.getEmail());
    user2.setPassword(user.getPassword());
    user2.setAbout(user.getAbout());
    user2.setPhoneNumber(user.getPhoneNumber());
    user2.setProfilePic(user.getProfilePic());
    user2.setEnabled(user.isEnabled());
    user2.setEmailVerified(user.isEmailVerified());
    user2.setPhoneVerified(user.isPhoneVerified());
    user2.setProvider(user.getProvider());
    user2.setProviderUserId(user.getProviderUserId());
    // save the user in DB
    User save = userRepository.save(user2);
    return Optional.ofNullable(save);
  }

  @Override
  public void deleteUser(String id) {
    User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    userRepository.delete(user);
  }

  @Override
  public boolean isUserExist(String userId) {
    User user = userRepository.findById(userId).orElse(null);
    return user == null ? false : true;
  }

  @Override
  public boolean isUserExistByEmail(String email) {
    User user = userRepository.findByEmail(email).orElse(null);
    return user == null ? false : true;
  }

  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElse(null);
  }

  @Override
  public User getUserByEmailToken(String token) {
    return userRepository.findByEmailToken(token).orElse(null);
  }

}
