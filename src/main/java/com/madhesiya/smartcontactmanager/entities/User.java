package com.madhesiya.smartcontactmanager.entities;

import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class User implements UserDetails {

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
  @Enumerated(value = EnumType.STRING)
  private Providers provider = Providers.SELF;
  private String providerUserId;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Contact> contacts = new ArrayList<>();

  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> roleList = new ArrayList<>();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // list of roles(ADMIN,USER)
    // collection of SimpleGranterAuthority[roles{ADMIN,USER}]

    Collection<SimpleGrantedAuthority> roles = roleList.stream().map(role -> new SimpleGrantedAuthority(role))
        .collect(Collectors.toList());
    return roles;
  }

}
