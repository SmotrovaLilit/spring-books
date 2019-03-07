package ru.lilitweb.books.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.*;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Comment;
import ru.lilitweb.books.service.BookService;
import ru.lilitweb.books.service.CommentService;
import ru.lilitweb.books.service.LocalisationService;

import java.util.LinkedHashMap;
import java.util.List;

@ShellComponent
@ShellCommandGroup("Book")
public class BookCommands {
    private final LocalisationService localisation;
    private BookService bookService;
    private CommentService commentService;

    @Autowired
    public BookCommands(LocalisationService localisation, BookService bookService, CommentService commentService) {
        this.localisation = localisation;
        this.bookService = bookService;
        this.commentService = commentService;
    }

    @ShellMethod("Create book")
    public String bookCreate(Book book) {
        bookService.add(book);
        return book.getId();
    }

    @ShellMethod("Book comments")
    public Table bookComments(@ShellOption String id) throws Exception {
        Book book = bookService.getById(id).orElseThrow(Exception::new);
        List<Comment> comments = commentService.bookComments(book);
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("id", localisation.getMessage("field.comment.id"));
        headers.put("message", localisation.getMessage("field.comment.message"));
        headers.put("author", localisation.getMessage("field.comment.author"));

        TableModel table = new BeanListTableModel<>(comments, headers);
        return new TableBuilder(table)
                .addFullBorder(BorderStyle.fancy_light_double_dash)
                .build();

    }

    @ShellMethod("Add comment to book")
    public String bookAddComment(@ShellOption String id, @ShellOption String message) throws Exception {
        Book foundedBook = bookService.getById(id).orElseThrow(Exception::new);
        Comment comment = commentService.addAnonimComment(foundedBook, message);
        return comment.getId();
    }

    @ShellMethod("Update book")
    public void bookUpdate(@ShellOption Book book, @ShellOption String id) {
        book.setId(id);
        bookService.update(book);
    }

    @ShellMethod("Delete book")
    public void bookDelete(@ShellOption String id) {
        bookService.getById(id).ifPresent(b -> bookService.delete(b));
    }

    @ShellMethod("List books")
    public Table bookList() {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("id", localisation.getMessage("field.book.id"));
        headers.put("title", localisation.getMessage("field.book.title"));
        headers.put("year", localisation.getMessage("field.book.year"));
        headers.put("description", localisation.getMessage("field.book.description"));
        headers.put("author", localisation.getMessage("field.book.author"));
        headers.put("genres", localisation.getMessage("field.book.genres"));
        TableModel table = new BeanListTableModel<>(bookService.getAll(), headers);
        return new TableBuilder(table)
                .addFullBorder(BorderStyle.fancy_light_double_dash)
                .build();
    }
}
