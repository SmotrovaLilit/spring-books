package ru.lilitweb.books.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.*;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.service.BookService;
import ru.lilitweb.books.service.LocalisationService;

import java.util.LinkedHashMap;

@ShellComponent
@ShellCommandGroup("Book")
public class BookCrud {
    private final LocalisationService localisation;
    private BookService bookService;

    @Autowired
    public BookCrud(LocalisationService localisation, BookService bookService) {
        this.localisation = localisation;
        this.bookService = bookService;
    }

    @ShellMethod("Create book")
    public int bookCreate(Book book) {
        bookService.add(book);
        return book.getId();
    }

    @ShellMethod("Update book")
    public void bookUpdate(@ShellOption Book book, @ShellOption int id) {
        book.setId(id);
        bookService.update(book);
    }

    @ShellMethod("Delete book")
    public void bookDelete(@ShellOption int id) {
        bookService.delete(bookService.getById(id));
    }

    @ShellMethod("List books")
    public Table bookList() {
        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("id", "ID");
        headers.put("title", "Title");
        headers.put("year", "Year");
        headers.put("description", "Description");
        headers.put("author", "Author");
        headers.put("genres", "Genres");
        TableModel table = new BeanListTableModel<>(bookService.getAll(), headers);
        return new TableBuilder(table)
                .addFullBorder(BorderStyle.fancy_light_double_dash)
                .build();
    }
}
