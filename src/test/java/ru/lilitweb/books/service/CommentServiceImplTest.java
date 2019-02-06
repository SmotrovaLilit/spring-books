package ru.lilitweb.books.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Comment;
import ru.lilitweb.books.domain.User;
import ru.lilitweb.books.repostory.CommentRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    private CommentService commentService;

    @Mock
    CommentRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(CommentRepository.class);
        commentService = new CommentServiceImpl(repository);
    }

    @Test
    void addComment() {
        User user = new User("some comment author");
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new User("some book author"));
        String message = "some message";

        Comment comment = commentService.addComment(user, book, message);

        verify(repository, atLeastOnce()).save(new Comment(message, user, book));
        assertEquals(message, comment.getMessage());
        assertEquals(book.getTitle(), comment.getBook().getTitle());
        assertEquals(user.getFullname(), comment.getAuthor().getFullname());
    }

    @Test
    void bookComments() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new User("some book author"));
        when(repository.findByBook(book)).thenReturn(Arrays.asList(new Comment("some message", new User(), new Book())));
        List<Comment> comments = commentService.bookComments(book);
        assertEquals("some message", comments.get(0).getMessage());
    }
}
