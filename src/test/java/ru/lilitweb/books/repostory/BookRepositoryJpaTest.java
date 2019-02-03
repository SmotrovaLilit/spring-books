package ru.lilitweb.books.repostory;

import org.junit.jupiter.api.BeforeEach;
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
    private User bookAuthor;

    @BeforeEach
    void setUp() {
        bookAuthor = new User("some book author");
        entityManager.persist(bookAuthor);
    }

    @Test
    void insert() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                bookAuthor);
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
                bookAuthor);
        entityManager.persist(book);
        User anotherUser = new User("some name");
        entityManager.persist(anotherUser);

        book.setTitle("new title");
        book.setDescription("new description");
        book.setYear(book.getYear() + 1);
        book.setAuthor(anotherUser);
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
                bookAuthor);
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
                bookAuthor);
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
                bookAuthor);
        book.setGenres(genres);
        entityManager.persist(book);
        entityManager.persist(new Book(
                "Руслан и Людмила новая версия",
                2019,
                "Описание",
                bookAuthor));

        List<Book> foundedBooks = bookRepository.getAllByGenres(genres);

        Book foundedBook = foundedBooks.get(0);
        assertEquals(1, foundedBooks.size());
        assertEquals(book.getTitle(), foundedBook.getTitle());
        assertEquals(book.getAuthor().getFullname(), foundedBook.getAuthor().getFullname());
    }

    @Test
    void getAllByAuthorId() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                bookAuthor);

        User anotherUser = new User("some name");
        entityManager.persist(anotherUser);

        entityManager.persist(book);
        entityManager.persist(new Book(
                "Руслан и Людмила новая версия",
                2019,
                "Описание",
                anotherUser));

        List<Book> foundedBooks = bookRepository.getAllByAuthor(bookAuthor);

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
                bookAuthor);
        entityManager.persist(book);

        bookRepository.delete(book);

        assertNull(entityManager.find(Book.class, book.getId()));
    }
}
