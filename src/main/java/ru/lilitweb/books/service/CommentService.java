package ru.lilitweb.books.service;

import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Comment;
import ru.lilitweb.books.domain.User;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface CommentService {
    Comment addComment(User user, Book book, String message);
    Comment addAnonimComment(@NotNull  Book book, String message);
    List<Comment> bookComments(Book book);
}
