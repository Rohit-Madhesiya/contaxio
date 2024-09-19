package com.madhesiya.smartcontactmanager.forms;

import org.springframework.web.multipart.MultipartFile;

import com.madhesiya.smartcontactmanager.validators.ValidFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactForm {

  @NotBlank(message = "Name is required")
  private String name;

  @Email(message = "Invalid email address [xyz@gmail.com]")
  private String email;

  @NotBlank(message = "Phone number is required")
  @Pattern(regexp = "^[0-9]{10}$", message = "Invalid Phone Number")
  private String phoneNumber;

  @NotBlank(message = "Address is required")
  private String address;

  private String description;
  private boolean favourite;

  private String websiteLink;
  private String linkedInLink;

  // custom annotation create to validate file-size,resolution
  @ValidFile(message = "Invalid File")
  private MultipartFile contactImg;

  private String picture;
}
