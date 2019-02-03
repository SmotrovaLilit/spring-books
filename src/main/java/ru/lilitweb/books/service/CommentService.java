package ru.lilitweb.books.service;

import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Comment;
import ru.lilitweb.books.domain.User;

import java.util.List;

public interface CommentService {
    Comment addComment(User user, Book book, String message);
    List<Comment> bookComments(Book book);
}
