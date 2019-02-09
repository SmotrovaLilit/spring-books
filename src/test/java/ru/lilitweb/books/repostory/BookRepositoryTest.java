package ru.lilitweb.books.repostory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.lilitweb.books.domain.Author;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;

import java.util.Arrays;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void add() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new Author("some book author"));

        book.setGenres(Collections.singletonList(new Genre("some genre")));
        bookRepository.save(book);
        assertNotEquals("", book.getId());
    }

    @Test
    void update() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new Author("some book author"));

        book.setGenres(Collections.singletonList(new Genre("some genre")));
        mongoTemplate.save(book);
        book = bookRepository.findById(book.getId()).orElse(null);
        assertNotNull(book);

        book.setTitle("new title");
        book.setDescription("new description");
        book.setAuthor(new Author("new author"));
        book.setGenres(Arrays.asList(new Genre("some genre1"), new Genre("some genre2")));
        bookRepository.save(book);

        Book foundedBook = bookRepository.findById(book.getId()).orElse(null);
        assertEquals(book, foundedBook);
    }
}
