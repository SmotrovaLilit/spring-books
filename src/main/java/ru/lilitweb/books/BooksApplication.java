package ru.lilitweb.books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.function.Function;

@SpringBootApplication
@EnableMongoRepositories
public class BooksApplication {
    public static void main(String[] args) {
        SpringApplication.run(BooksApplication.class, args);
    }
}

