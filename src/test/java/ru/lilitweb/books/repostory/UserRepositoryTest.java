package ru.lilitweb.books.repostory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.lilitweb.books.domain.Author;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Comment;
import ru.lilitweb.books.domain.User;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void add() {
        User user = new User("some comment author");
        repository.save(user);
        assertNotEquals("", user.getId());
    }

    @Test
    void update() {
        User user = new User("some comment author");
        mongoTemplate.save(user);
        assertNotEquals("", user.getId());

        user.setFullname("new fullname");
        repository.save(user);
        User foundedUser = repository.findById(user.getId()).orElse(null);
        assertEquals(user, foundedUser);
    }


}
