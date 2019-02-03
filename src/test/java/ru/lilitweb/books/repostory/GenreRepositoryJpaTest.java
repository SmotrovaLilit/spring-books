package ru.lilitweb.books.repostory;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import ru.lilitweb.books.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(GenreRepositoryJpa.class)
class GenreRepositoryJpaTest {
    @Autowired
    GenreRepository repository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    void insert() {
        Genre genre = new Genre("some name");
        repository.insert(genre);

        assertTrue(genre.getId() > 0);

        Genre foundedGenre = entityManager.find(Genre.class, genre.getId());

        assertEquals(genre.getName(), foundedGenre.getName());
    }

    @Test
    void update() {
        Genre genre = new Genre("some name");
        entityManager.persist(genre);

        genre.setName("new name");
        repository.update(genre);

        Genre foundedGenre = entityManager.find(Genre.class, genre.getId());

        assertEquals(genre.getName(), foundedGenre.getName());
    }

    @Test
    void getById() {
        Genre genre = new Genre("some name");
        entityManager.persist(genre);

        Genre foundedGenre = repository.getById(genre.getId());

        assertEquals(genre.getName(), foundedGenre.getName());
    }

    @Test
    void getAll() {
        Genre genre = new Genre("some name");
        entityManager.persist(genre);

        List<Genre> genres = repository.getAll();
        Optional<Genre> foundedGenre = genres.stream().filter(u -> u.getId() == genre.getId()).findFirst();
        assertTrue(foundedGenre.isPresent());
    }

    @Test
    void delete() {
        Genre genre = new Genre("some name");
        entityManager.persist(genre);

        repository.delete(genre);

        assertNull(entityManager.find(Genre.class, genre.getId()));
    }
}
