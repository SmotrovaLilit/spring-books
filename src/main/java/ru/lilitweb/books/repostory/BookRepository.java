package ru.lilitweb.books.repostory;

import org.springframework.data.repository.CrudRepository;
import ru.lilitweb.books.domain.Author;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, String> {
    List<Book> findByGenresIn(List<Genre> genres);
    List<Book> findByAuthor(Author author);
    List<Book> findAll();
}
