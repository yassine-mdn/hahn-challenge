package io.hahn.bookspaceback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BookspaceBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookspaceBackApplication.class, args);
    }

}
