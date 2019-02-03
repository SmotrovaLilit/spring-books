package ru.lilitweb.books.dao;

import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;
import ru.lilitweb.books.domain.User;

import java.util.List;

public interface BookDao {
    int count();
    void insert(Book book);
    void update(Book book);
    Book getById(int id);

    List<Book> getAll();

    List<Book> getAllByGenres(List<Genre> genres);
    List<Book> getAllByAuthor(User author);

    void loadAuthors(List<Book> books, RelatedEntitiesLoader<User> usersLoader);
    void loadGenres(List<Book> books, RelatedEntitiesLoader<Genre> genresLoader);

    void delete(int id);
}
