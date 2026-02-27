package com.FT.FinanceTracker.config.init;

import com.FT.FinanceTracker.entity.User;
import com.FT.FinanceTracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (adminEmail == null || adminEmail.isEmpty() || adminPassword == null || adminPassword.isEmpty()) {
            System.out.println("ADMIN USER CREATION SKIPPED (Credentials not configured)");
            return;
        }

        boolean exists = userRepository.findByEmail(adminEmail).isPresent();

        if (!exists) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(User.Role.ADMIN);
            admin.setStatus(User.Status.ACTIVE);

            userRepository.save(admin);

            System.out.println("ADMIN USER CREATED: " + adminEmail);
        } else {
            System.out.println("ADMIN USER ALREADY EXISTS: " + adminEmail);
        }
    }
}
