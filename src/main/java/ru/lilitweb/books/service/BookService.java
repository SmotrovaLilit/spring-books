package ru.lilitweb.books.service;

import ru.lilitweb.books.domain.Author;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;

import java.util.List;

public interface BookService {
    void add(Book book);
    void update(Book book);
    Book getById(String id);
    List<Book> getAllByAuthor(Author author);
    List<Book> getAllByGenres(List<Genre> genres);

    List<Book> getAll();

    void delete(Book book);
}
