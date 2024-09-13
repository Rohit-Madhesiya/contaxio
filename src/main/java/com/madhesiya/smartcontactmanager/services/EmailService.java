package com.madhesiya.smartcontactmanager.services;

import java.io.InputStream;
import java.io.File;

public interface EmailService {

  // send email to single person
  void sendEmail(String to, String subject, String message);

  // send email to multiple person
  void sendEmail(String to[], String subject, String message);

  // void sendEmailWithHtml
  void sendEmailWithHtml(String to, String subject, String htmlContent);

  // void send email with file
  void sendEmailWithFile(String to, String subject, String message, File file);

  // send email with file input stream
  void sendEmailWithFileWithStream(String to, String subject, String message, InputStream inputStream);

}
