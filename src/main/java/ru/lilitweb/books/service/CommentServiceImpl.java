package ru.lilitweb.books.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Comment;
import ru.lilitweb.books.domain.User;
import ru.lilitweb.books.repostory.CommentRepository;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment addComment(User user, Book book, String message) {
        Comment comment = new Comment(message, user, book);
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public List<Comment> bookComments(Book book) {
        return commentRepository.findByBook(book);
    }
}
