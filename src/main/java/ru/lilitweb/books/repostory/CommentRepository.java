package ru.lilitweb.books.repostory;

import org.springframework.data.repository.CrudRepository;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Comment;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByBook(Book book);
}
