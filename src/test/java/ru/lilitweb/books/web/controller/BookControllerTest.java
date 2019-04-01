package ru.lilitweb.books.web.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.lilitweb.books.domain.Author;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;
import ru.lilitweb.books.service.BookService;
import ru.lilitweb.books.web.form.BookForm;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    BookService bookService;

    @Test
    public void index() throws Exception {
        Book book = getTestBook();

        PageRequest pageRequest = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        when(bookService.search(Optional.empty(), pageRequest)).thenReturn(new PageImpl<Book>(Collections.singletonList(book)));
        this.mvc.perform(get("/books"))
                .andExpect(view().name("book/index"))
                .andExpect(status().isOk());
    }

    @Test
    public void viewBook() throws Exception {
        String id = "1";
        Book book = getTestBook();
        book.setId(id);
        when(bookService.getById(id)).thenReturn(Optional.of(book));
        this.mvc.perform(get("/book/" + id))
                .andExpect(view().name("book/view"))
                .andExpect(status().isOk());
    }

    @Test
    public void edit() throws Exception {
        String id = "1";
        Book book = getTestBook();
        book.setId(id);
        when(bookService.getById(id)).thenReturn(Optional.of(book));
        this.mvc.perform(get("/book/" + id + "/edit"))
                .andExpect(view().name("book/edit"))
                .andExpect(status().isOk());
    }

    @Test
    public void update_ifSuccess() throws Exception {
        List<String> genres = Arrays.asList("поэзия", "new");
        String id = "1";
        BookForm bookForm = new BookForm(
                "Руслан и Людмила new",
                "author new",
                2011,
                "Описание new",
                genres);
        when(bookService.getById(id)).thenReturn(Optional.of(getTestBook()));
        this.mvc.perform(MockMvcRequestBuilders.post("/book/" + id).
                accept(MediaType.TEXT_HTML).
                param("title", bookForm.getTitle()).
                param("author", bookForm.getAuthor()).
                param("year", String.valueOf(bookForm.getYear())).
                param("description", bookForm.getDescription()).
                param("genres", bookForm.getGenres().toArray(new String[0]))
        ).
                andExpect(status().is3xxRedirection()).
                andExpect(model().hasNoErrors()).
                andExpect(redirectedUrl("/book/" + id));
    }

    private Book getTestBook() {
        List<Genre> genres = Arrays.asList(new Genre("Поэзия"), new Genre("Ужастик"));
        Author author = new Author("some book author");
        String id = "1";
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                author);

        book.setGenres(genres);
        return book;
    }

    @Test
    public void create() throws Exception {
        this.mvc.perform(get("/book/create"))
                .andExpect(view().name("book/create"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteBook() throws Exception {
        String id = "1";

        this.mvc.perform(delete("/book/" + id))
                .andExpect(status().isOk());

        verify(bookService, atLeastOnce()).delete(id);
    }

    @Test
    public void store() throws Exception {
        List<String> genres = Arrays.asList("поэзия", "new");
        String id = "1";
        BookForm bookForm = new BookForm(
                "Руслан и Людмила new",
                "author new",
                2011,
                "Описание new",
                genres);

        this.mvc.perform(MockMvcRequestBuilders.post("/book/store").
                accept(MediaType.TEXT_HTML).
                param("title", bookForm.getTitle()).
                param("author", bookForm.getAuthor()).
                param("year", String.valueOf(bookForm.getYear())).
                param("description", bookForm.getDescription()).
                param("genres", bookForm.getGenres().toArray(new String[0]))
        ).
                andExpect(model().hasNoErrors());
    }
}
