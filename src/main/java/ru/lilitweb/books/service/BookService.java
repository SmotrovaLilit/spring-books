package ru.lilitweb.books.service;

import ru.lilitweb.books.domain.Book;

import java.util.List;

public interface BookService {
    void add(Book book);
    void update(Book book);
    Book getById(int id);
    List<Book> getAll();

    void delete(int id);
}
