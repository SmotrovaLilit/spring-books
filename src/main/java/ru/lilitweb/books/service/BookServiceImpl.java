package ru.lilitweb.books.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.repostory.BookRepository;
import ru.lilitweb.books.repostory.GenreRepository;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository  bookRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void add(Book book) {
        bookRepository.insert(book);
    }

    @Override
    public void update(Book book) {
        bookRepository.update(book);
    }

    @Override
    public Book getById(int id) {
        return bookRepository.getById(id);
    }

    @Override
    public List<Book> getAll() {
        List<Book> books = bookRepository.getAll();

        return books;
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }
}
