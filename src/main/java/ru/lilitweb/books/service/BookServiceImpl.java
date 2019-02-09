package ru.lilitweb.books.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.lilitweb.books.dao.BookDao;
import ru.lilitweb.books.domain.Book;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private BookDao bookDao;

    @Autowired
    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public void add(Book book) {
        bookDao.insert(book);
    }

    @Override
    public void update(Book book) {
        bookDao.update(book);
    }

    @Override
    public Book getById(int id) {
        return bookDao.getById(id);
    }

    @Override
    public List<Book> getAll() {
        List<Book> books = bookDao.getAll();
        bookDao.loadGenresFetchModeSelect(books);
        return books;
    }

    @Override
    public void delete(int id) {
        bookDao.delete(id);
    }
}
