package ru.lilitweb.books.repostory;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import ru.lilitweb.books.domain.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(UserRepositoryJpa.class)
class UserRepositoryJpaTest {
    @Autowired
    UserRepository repository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    void insert() {
        User user = new User("some name");
        repository.insert(user);

        assertTrue(user.getId() > 0);

        User foundedUser = entityManager.find(User.class, user.getId());

        assertEquals(user.getFullname(), foundedUser.getFullname());
    }

    @Test
    void update() {
        User user = new User("some name");
        entityManager.persist(user);

        user.setFullname("new name");
        repository.update(user);

        User foundedUser = entityManager.find(User.class, user.getId());

        assertEquals(user.getFullname(), foundedUser.getFullname());
    }

    @Test
    void getById() {
        User user = new User("some name");
        entityManager.persist(user);

        User foundedUser = repository.getById(user.getId());

        assertEquals(user.getFullname(), foundedUser.getFullname());
    }

    @Test
    void getAll() {
        User user = new User("some name");
        entityManager.persist(user);

        List<User> users = repository.getAll();
        Optional<User> foundedUser = users.stream().filter(u -> u.getId() == user.getId()).findFirst();
        assertTrue(foundedUser.isPresent());
    }

    @Test
    void delete() {
        User user = new User("some name");
        entityManager.persist(user);

        repository.delete(user);

        assertNull(entityManager.find(User.class, user.getId()));
    }
}
