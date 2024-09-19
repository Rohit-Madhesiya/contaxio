package com.madhesiya.smartcontactmanager;

import com.madhesiya.smartcontactmanager.entities.User;
import com.madhesiya.smartcontactmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class SmartcontactmanagerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SmartcontactmanagerApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Value("${spring.security.user.roles}")
	 private List<String> role_list;


	@Override
	public void run(String... args) throws Exception {
		User user=new  User();
		user.setUserId(UUID.randomUUID().toString());
		user.setUsername("springstudent");
		user.setEmail("springstudent@gmail.com");
		user.setPassword(passwordEncoder.encode("springstudent"));
		user.setRoleList(role_list);
		user.setEnabled(true);
		user.setEmailVerified(true);
		user.setPhoneVerified(true);
		user.setAbout("This is dummy user created initially.");

		userRepository.findByEmail("springstudent@gmail.com").ifPresentOrElse(user1->{},()->{
			userRepository.save(user);
			System.out.println("User Created");
		});
	}
}
