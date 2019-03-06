package ru.lilitweb.books.web.conveter;

import org.springframework.core.convert.converter.Converter;
import ru.lilitweb.books.domain.Author;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;
import ru.lilitweb.books.web.form.BookForm;

import java.util.ArrayList;
import java.util.List;

public class BookFormToBookConverter implements Converter<BookForm, Book> {
    @Override
    public Book convert(BookForm source) {
        List<Genre> genres = new ArrayList<>();
        source.getGenres().forEach(t -> {
            genres.add(new Genre(t));
        });
        Book book = new Book(
                source.getTitle(),
                source.getYear(),
                source.getDescription(),
                new Author(source.getAuthor())
        );

        book.setGenres(genres);

        return book;
    }
}
