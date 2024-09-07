package com.madhesiya.smartcontactmanager.entities;

import java.util.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity(name = "contact")
public class Contact {
  @Id
  private String id;
  private String name;
  private String email;
  private String phoneNumber;
  private String address;
  private String picture;
  @Column(length = 1000)
  private String description;

  private boolean favourite = false;

  private String websiteLink;
  private String linkedInLink;

  @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private List<SocialLink> socialLinks = new ArrayList<>();

  @ManyToOne
  private User user;
}
