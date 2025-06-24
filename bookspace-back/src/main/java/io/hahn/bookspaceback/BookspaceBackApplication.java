package io.hahn.bookspaceback;

import io.hahn.bookspaceback.entity.User;
import io.hahn.bookspaceback.entity.enums.Role;
import io.hahn.bookspaceback.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;


@Slf4j
@SpringBootApplication
@EnableJpaAuditing
public class BookspaceBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookspaceBackApplication.class, args);
    }

    @Bean
    public CommandLineRunner createDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return (args) -> {
            if(userRepository.findByUsername("admin").isPresent()) {
                log.info("Admin already exists");
                return;
            }
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@email.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
            log.info("Admin created");
        };
    }
}
