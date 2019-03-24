package ru.lilitweb.books.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.lilitweb.books.config.WebConfig;
import ru.lilitweb.books.domain.Author;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;
import ru.lilitweb.books.rest.conveter.BookDtoToBookConverter;
import ru.lilitweb.books.rest.dto.BookDto;
import ru.lilitweb.books.service.BookService;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
@Import(WebConfig.class)
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
                Sort.by(Sort.Direction.ASC, "createdAt")
        );
        when(bookService.search(Optional.empty(), pageRequest)).thenReturn(new PageImpl<>(Collections.singletonList(book)));
        MvcResult mvcResult = this.mvc.perform(get("/api/v1/books?sort=asc,createdAt"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void viewBook() throws Exception {
        String id = "1";
        Book book = getTestBook();
        book.setId(id);
        when(bookService.getById(id)).thenReturn(Optional.of(book));
        this.mvc.perform(get("/api/v1/book/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

    }

    @Test
    public void update() throws Exception {
        List<String> genres = Arrays.asList("поэзия", "new");
        String id = "1";
        BookDto bookDto = new BookDto(
                id,
                "Руслан и Людмила new",
                "author new",
                2011,
                "Описание new",
                genres);
        when(bookService.getById(id)).thenReturn(Optional.of(getTestBook()));

        this.mvc.perform(MockMvcRequestBuilders.put("/api/v1/book/" + id).
                contentType(MediaType.APPLICATION_JSON_UTF8).
                content(toJson(bookDto))).
                andExpect(status().isOk());
        verify(bookService, atLeastOnce()).update(new BookDtoToBookConverter().convert(bookDto));
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
    public void deleteBook() throws Exception {
        String id = "1";

        this.mvc.perform(delete("/api/v1/book/" + id))
                .andExpect(status().isNoContent());

        verify(bookService, atLeastOnce()).delete(id);
    }

    @Test
    public void create() throws Exception {
        List<String> genres = Arrays.asList("поэзия", "new");
        String id = "1";
        BookDto bookDto = new BookDto(
                id,
                "Руслан и Людмила new",
                "author new",
                2011,
                "Описание new",
                genres);


        this.mvc.perform(MockMvcRequestBuilders.post("/api/v1/book/").
                contentType(MediaType.APPLICATION_JSON_UTF8).
                content(toJson(bookDto))).
                andExpect(status().isCreated());

        verify(bookService, atLeastOnce()).add(new BookDtoToBookConverter().convert(bookDto));
    }

    private static String toJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
