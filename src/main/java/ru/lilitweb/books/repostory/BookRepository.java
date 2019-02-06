package ru.lilitweb.books.repostory;

import org.springframework.data.repository.CrudRepository;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;
import ru.lilitweb.books.domain.User;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findByGenresIn(List<Genre> genres);
    List<Book> findByAuthor(User author);
    List<Book> findAll();
}
