package ru.lilitweb.books.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.lilitweb.books.domain.Author;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;
import ru.lilitweb.books.repostory.BookRepository;

import java.util.Arrays;
import java.util.Collections;
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
                new Author("some book author"));

        bookService.add(book);
        verify(repository, atLeastOnce()).save(book);
    }

    @Test
    void update() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new Author("some book author"));

        bookService.add(book);
        verify(repository, atLeastOnce()).save(book);
    }

    @Test
    void getById() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new Author("some book author"));
        book.setId("1");
        when(repository.findById("1")).thenReturn(Optional.of(book));
        Book returnedBook = bookService.getById("1").orElse(null);

        assertEquals(returnedBook, book);
    }

    @Test
    void getAllByAuthor() {
        Author author = new Author("some book author");
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                author);
        when(repository.findByAuthor(author)).thenReturn(Collections.singletonList(book));
        List<Book> books = bookService.getAllByAuthor(author);
        assertEquals(book, books.get(0));
    }

    @Test
    void getAllByGenres() {
        List<Genre> genres = Arrays.asList(new Genre("Поэзия"), new Genre("Ужастик"));
        Author author = new Author("some book author");
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                author);
        book.setGenres(genres);
        when(repository.findByGenresIn(genres)).thenReturn(Collections.singletonList(book));
        List<Book> books = bookService.getAllByGenres(genres);
        assertEquals(book, books.get(0));
    }

    @Test
    void getAll() {
        Author author = new Author("some book author");
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                author);

        when(repository.findAll()).thenReturn(Collections.singletonList(book));
        List<Book> books = bookService.getAll();
        assertEquals(book, books.get(0));
    }

    @Test
    void delete() {
        Author author = new Author("some book author");
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                author);
        bookService.delete(book);
        verify(repository, atLeastOnce()).delete(book);
    }

    @org.junit.Test
    public void paginate() throws Exception {
        throw new Exception();
    }

    @org.junit.Test
    public void deleteById() throws Exception {
        throw new Exception();
    }
}
