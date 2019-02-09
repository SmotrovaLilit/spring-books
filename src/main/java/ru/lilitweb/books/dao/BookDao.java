package ru.lilitweb.books.dao;

import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;
import ru.lilitweb.books.domain.User;

import java.util.List;

public interface BookDao {
    int count();
    void insert(Book book);
    void update(Book book);
    Book getById(long id);

    List<Book> getAll();
    List<Book> getAllWithGenres();

    List<Book> getAllByGenres(long[] genres);
    List<Book> getAllByAuthorId(long authorId);

    void loadGenresFetchModeSelect(List<Book> books);
    void loadGenresFetchModeSubSelect(List<Book> books);

    void delete(long id);
}
