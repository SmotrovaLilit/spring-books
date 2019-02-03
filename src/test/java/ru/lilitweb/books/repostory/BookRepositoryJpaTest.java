package ru.lilitweb.books.repostory;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;
import ru.lilitweb.books.domain.User;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@DataJpaTest
@Import(BookRepositoryJpa.class)
class BookRepositoryJpaTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void count() {
    }

    @Test
    void insert() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new User(1));
        bookRepository.insert(book);

        Book foundedBook = entityManager.find(Book.class, book.getId());

        assertTrue(book.getId() > 0);
        assertEquals(book.getTitle(), foundedBook.getTitle());
        assertEquals(book.getYear(), foundedBook.getYear());
        assertEquals(book.getDescription(), foundedBook.getDescription());
        assertEquals(book.getAuthor().getId(), foundedBook.getAuthor().getId());
    }

    @Test
    void update() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new User(1));
        entityManager.persist(book);

        book.setTitle("new title");
        book.setDescription("new description");
        book.setYear(book.getYear() + 1);
        book.setAuthor(new User(2));
        bookRepository.update(book);

        Book foundedBook = entityManager.find(Book.class, book.getId());

        assertEquals(book.getTitle(), foundedBook.getTitle());
        assertEquals(book.getYear(), foundedBook.getYear());
        assertEquals(book.getDescription(), foundedBook.getDescription());
        assertEquals(book.getAuthor().getId(), foundedBook.getAuthor().getId());
    }

    @Test
    void getById() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new User(1));
        entityManager.persist(book);

        Book foundedBook = bookRepository.getById(book.getId());

        assertEquals(book.getTitle(), foundedBook.getTitle());
        assertEquals(book.getAuthor().getFullname(), foundedBook.getAuthor().getFullname());
    }

    @Test
    void getAll() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new User(1));
        entityManager.persist(book);

        List<Book> foundedBooks = bookRepository.getAll();

        Book foundedBook = foundedBooks.get(0);
        assertEquals(book.getTitle(), foundedBook.getTitle());
        assertEquals(book.getAuthor().getFullname(), foundedBook.getAuthor().getFullname());
    }

    @Test
    void getAllByGenres() {
        List<Genre> genres = Arrays.asList(new Genre(1), new Genre(2));
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new User(1));
        book.setGenres(genres);
        entityManager.persist(book);
        entityManager.persist(new Book(
                "Руслан и Людмила новая версия",
                2019,
                "Описание",
                new User(2)));

        List<Book> foundedBooks = bookRepository.getAllByGenres(genres);

        Book foundedBook = foundedBooks.get(0);
        assertEquals(1, foundedBooks.size());
        assertEquals(book.getTitle(), foundedBook.getTitle());
        assertEquals(book.getAuthor().getFullname(), foundedBook.getAuthor().getFullname());
    }

    @Test
    void getAllByAuthorId() {
        User author = new User(1);
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                author);
        entityManager.persist(book);
        entityManager.persist(new Book(
                "Руслан и Людмила новая версия",
                2019,
                "Описание",
                new User(2)));

        List<Book> foundedBooks = bookRepository.getAllByAuthor(author);

        Book foundedBook = foundedBooks.get(0);
        assertEquals(1, foundedBooks.size());
        assertEquals(book.getTitle(), foundedBook.getTitle());
        assertEquals(book.getAuthor().getFullname(), foundedBook.getAuthor().getFullname());
    }


    @Test
    void delete() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new User(1));
        entityManager.persist(book);

        bookRepository.delete(book);

        assertNull(entityManager.find(Book.class, book.getId()));
    }
}
