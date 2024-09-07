package com.madhesiya.smartcontactmanager.entities;

import java.util.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

  @Id
  private String userId;
  @Column(nullable = false)
  private String username;
  @Column(unique = true, nullable = false)
  private String email;
  private String phoneNumber;
  private String password;
  @Column(length = 1000)
  private String about;
  @Column(length = 1000)
  private String ProfilePic;
  private boolean enabled = false;
  private boolean emailVerified = false;
  private boolean phoneVerified = false;

  // Via Self, Google,Facebook, Github
  private Providers provider = Providers.SELF;
  private String providerUserId;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Contact> contacts = new ArrayList<>();

}
