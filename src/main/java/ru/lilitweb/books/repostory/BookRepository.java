package ru.lilitweb.books.repostory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.lilitweb.books.domain.Author;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;

import java.util.List;

public interface BookRepository extends PagingAndSortingRepository<Book, String> {
    List<Book> findByGenresIn(List<Genre> genres);

    List<Book> findByAuthor(Author author);

    List<Book> findAll();

    Page<Book> findByTitleContainsOrAuthorFullnameContains(String title, String author, Pageable pageable);
}
