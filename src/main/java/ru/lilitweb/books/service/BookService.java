package ru.lilitweb.books.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.lilitweb.books.domain.Author;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface BookService {
    void add(Book book);

    void update(Book book);

    Optional<Book> getById(String id);

    List<Book> getAllByAuthor(Author author);

    List<Book> getAllByGenres(List<Genre> genres);

    List<Book> getAll();

    List<String> getAvailableGenres();

    Page<Book> search(Optional<String> term, Pageable pageable);

    void delete(Book book);

    void delete(String id);
}
