package ru.lilitweb.books.repostory;

import ru.lilitweb.books.domain.Genre;

import java.util.List;

public interface GenreRepository {
    void insert(Genre genre);
    void update(Genre genre);
    Genre getById(int id);

    List<Genre> getAll();

    void delete(Genre genre);
}
