package ru.lilitweb.books.service;

import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;
import ru.lilitweb.books.domain.User;

import java.util.List;

public interface BookService {
    void add(Book book);
    void update(Book book);
    Book getById(long id);
    List<Book> getAllByAuthor(User author);
    List<Book> getAllByGenres(List<Genre> genres);

    List<Book> getAll();

    void delete(Book book);
}
