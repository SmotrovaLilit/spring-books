package ru.lilitweb.books.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;
import ru.lilitweb.books.domain.User;
import ru.lilitweb.books.repostory.BookRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    private BookService bookService;

    @Mock
    BookRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(BookRepository.class);
        bookService = new BookServiceImpl(repository);
    }
    @Test
    void add() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new User("some book author"));

        bookService.add(book);
        verify(repository, atLeastOnce()).save(book);
    }

    @Test
    void update() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new User("some book author"));

        bookService.add(book);
        verify(repository, atLeastOnce()).save(book);
    }

    @Test
    void getById() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new User("some book author"));
        book.setId(1);
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        Book returnedBook = bookService.getById(1);

        assertEquals(returnedBook, book);
    }

    @Test
    void getAllByAuthor() {
        User author = new User("some book author");
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                author);
        when(repository.findByAuthor(author)).thenReturn(Arrays.asList(book));
        List<Book> books = bookService.getAllByAuthor(author);
        assertEquals(book, books.get(0));
    }

    @Test
    void getAllByGenres() {
        List<Genre> genres = Arrays.asList(new Genre("Поэзия"), new Genre("Ужастик"));
        User author = new User("some book author");
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                author);
        book.setGenres(genres);
        when(repository.findByGenresIn(genres)).thenReturn(Arrays.asList(book));
        List<Book> books = bookService.getAllByGenres(genres);
        assertEquals(book, books.get(0));
    }

    @Test
    void getAll() {
        User author = new User("some book author");
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                author);

        when(repository.findAll()).thenReturn(Arrays.asList(book));
        List<Book> books = bookService.getAll();
        assertEquals(book, books.get(0));
    }

    @Test
    void delete() {
        User author = new User("some book author");
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                author);
        bookService.delete(book);
        verify(repository, atLeastOnce()).delete(book);
    }
}
