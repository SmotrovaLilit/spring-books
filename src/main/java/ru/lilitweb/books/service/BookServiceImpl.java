package ru.lilitweb.books.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.lilitweb.books.domain.Author;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;
import ru.lilitweb.books.repostory.BookRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;
    private List<String> genres = Arrays.asList("Poem", "Crime", "Drama", "Horror", "Paranormal romance", "Poetry");

    @Autowired
    public BookServiceImpl(BookRepository bookDao) {
        this.bookRepository = bookDao;
    }

    @Override
    public void add(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void update(Book book) {
        bookRepository.save(book);
    }

    @Override
    public Optional<Book> getById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> getAllByAuthor(Author author) {
        return bookRepository.findByAuthor(author);
    }

    @Override
    public List<Book> getAllByGenres(List<Genre> genres) {
        return bookRepository.findByGenresIn(genres);
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<String> getAvailableGenres() {
        return genres;
    }

    @Override
    public Page<Book> search(Optional<String> term, Pageable pageable) {
        if (!term.isPresent()) {
            return bookRepository.findAll(pageable);
        }
        return bookRepository.findByTitleContainsOrAuthorFullnameContains(term.get(), term.get(), pageable);
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }

    @Override
    public void delete(String id) {
        bookRepository.deleteById(id);
    }
}
