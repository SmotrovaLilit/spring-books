package ru.lilitweb.books.repostory;

import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Comment;

import java.util.List;

public interface CommentRepository {
    void insert(Comment comment);
    void update(Comment comment);
    Comment getById(int id);

    List<Comment> getAllByBook(Book book);

    void delete(Comment comment);
}
