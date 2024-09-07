package com.madhesiya.smartcontactmanager.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {
  private String username;
  private String email;
  private String phoneNumber;
  private String password;
  private String about;
}
