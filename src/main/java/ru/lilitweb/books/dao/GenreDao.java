package ru.lilitweb.books.dao;
import ru.lilitweb.books.domain.Genre;

import java.util.List;

public interface GenreDao {
    int count();
    void insert(Genre genre);
    void update(Genre genre);
    Genre getById(long id);

    List<Genre> getAll();

    void delete(long id);
}
