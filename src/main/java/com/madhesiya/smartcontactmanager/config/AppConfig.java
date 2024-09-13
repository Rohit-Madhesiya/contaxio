package com.madhesiya.smartcontactmanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
@Profile("dev")
public class AppConfig {

  @Value("${cloudinary.cloud.name}")
  private String cloudName;
  @Value("${cloudinary.api.key}")
  private String apikey;
  @Value("${cloudinary.api.secret}")
  private String apisecret;

  @Bean
  public Cloudinary cloudinary() {

    return new Cloudinary(ObjectUtils.asMap(
        "cloud_name", cloudName,
        "api_key", apikey,
        "api_secret", apisecret));
  }

}
