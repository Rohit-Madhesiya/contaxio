package com.madhesiya.smartcontactmanager.forms;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
  @NotBlank(message = "Username is required")
  @Size(message = "Minimum 3 characters is required", min = 3)
  private String username;
  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email address")
  private String email;
  @Size(min = 8, max = 12, message = "Invalid number")
  private String phoneNumber;
  @NotBlank(message = "Password is required")
  @Size(min = 6, message = "Minimum 6 characters is required")
  private String password;
  @NotBlank(message = "About is required")
  private String about;
}
