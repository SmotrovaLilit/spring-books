package ru.lilitweb.books.repostory;

import org.springframework.data.repository.CrudRepository;
import ru.lilitweb.books.domain.Genre;

import java.util.List;

public interface GenreRepository extends CrudRepository<Genre, Long> {
}
