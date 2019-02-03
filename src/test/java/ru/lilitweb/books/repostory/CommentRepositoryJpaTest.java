package ru.lilitweb.books.repostory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Comment;
import ru.lilitweb.books.domain.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(CommentRepositoryJpa.class)
class CommentRepositoryJpaTest {
    @Autowired
    CommentRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private Book book;
    private User commentAuthor;
    private User bookAuthor;

    @BeforeEach
    void setUp() {
        bookAuthor = new User("some book author");
        entityManager.persist(bookAuthor);
        book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                bookAuthor);
        entityManager.persist(book);

        commentAuthor = new User("some comment author");
        entityManager.persist(commentAuthor);
    }

    @Test
    void insert() {
        Comment comment = new Comment("some message", commentAuthor, book);
        repository.insert(comment);

        assertTrue(comment.getId() > 0);

        Comment foundedComment = entityManager.find(Comment.class, comment.getId());

        assertEquals(comment.getMessage(), foundedComment.getMessage());
    }

    @Test
    void update() {
        Comment comment = new Comment("some message", commentAuthor, book);
        entityManager.persist(comment);

        comment.setMessage("new mesasge");
        repository.update(comment);

        Comment foundedComment = entityManager.find(Comment.class, comment.getId());

        assertEquals(comment.getMessage(), foundedComment.getMessage());
    }

    @Test
    void getById() {
        Comment comment = new Comment("some message", commentAuthor, book);
        entityManager.persist(comment);

        Comment foundedComment = repository.getById(comment.getId());

        assertEquals(comment.getMessage(), foundedComment.getMessage());
    }

    @Test
    void getAllByBook() {
        Comment comment = new Comment("some message", commentAuthor, book);
        entityManager.persist(comment);
        Book anotherBook = new Book(
                "another book",
                2019,
                "desc",
                bookAuthor);
        entityManager.persist(anotherBook);
        entityManager.persist(new Comment("some message", commentAuthor, anotherBook));

        List<Comment> comments = repository.getAllByBook(book);
        Optional<Comment> foundedComment = comments.stream().filter(u -> u.getId() == comment.getId()).findFirst();
        assertTrue(foundedComment.isPresent());
        assertEquals(1, comments.size());
    }

    @Test
    void delete() {
        Comment comment = new Comment("some message", commentAuthor, book);
        entityManager.persist(comment);

        repository.delete(comment);

        assertNull(entityManager.find(Comment.class, comment.getId()));
    }
}
