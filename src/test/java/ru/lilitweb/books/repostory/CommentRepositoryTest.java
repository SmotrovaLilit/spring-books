package ru.lilitweb.books.repostory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.lilitweb.books.domain.Author;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Comment;
import ru.lilitweb.books.domain.User;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void add() {
        User user = new User("some comment author");
        mongoTemplate.save(user);
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new Author("some book author"));
        mongoTemplate.save(book);

        String message = "some message";

        Comment comment = commentRepository.save(new Comment(message, user, book));
        assertNotEquals("", comment.getId());
        assertEquals(book, comment.getBook());
        assertEquals(user, comment.getAuthor());
    }

    @Test
    void update() {
        User user = new User("some comment author");
        mongoTemplate.save(user);
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new Author("some book author"));

        mongoTemplate.save(book);
        Comment comment = new Comment("some message", user, book);
        mongoTemplate.save(comment);
        assertNotEquals("", comment.getId());


        comment.setMessage("new message");
        commentRepository.save(comment);
        Comment foundedComment = commentRepository.findById(comment.getId()).orElse(null);
        assertNotNull(foundedComment);
        assertEquals(comment, foundedComment);
        assertEquals(book, foundedComment.getBook());
    }


}
