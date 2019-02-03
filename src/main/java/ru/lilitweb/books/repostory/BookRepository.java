package ru.lilitweb.books.repostory;

import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;
import ru.lilitweb.books.domain.User;

import java.util.List;

public interface BookRepository {
    void insert(Book book);
    void update(Book book);
    Book getById(int id);

    List<Book> getAll();

    List<Book> getAllByGenres(List<Genre> genres);
    List<Book> getAllByAuthor(User author);

    void delete(Book book);
}
