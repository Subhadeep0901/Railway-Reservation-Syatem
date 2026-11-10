package com.example.railway.config;

import com.example.railway.model.AdminUser;
import com.example.railway.repository.AdminUserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    public CommandLineRunner createDefaultAdmin(
            AdminUserRepository repository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (repository.findByUsername("admin").isEmpty()) {

                AdminUser admin = new AdminUser();

                admin.setUsername("admin");
                admin.setPassword(
                        passwordEncoder.encode("Admin@123"));

                repository.save(admin);

                System.out.println("Default admin created!");
            }
        };
    }
}