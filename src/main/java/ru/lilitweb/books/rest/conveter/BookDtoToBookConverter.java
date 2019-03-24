package ru.lilitweb.books.rest.conveter;

import org.springframework.core.convert.converter.Converter;
import ru.lilitweb.books.domain.Author;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;
import ru.lilitweb.books.rest.dto.BookDto;

import java.util.ArrayList;
import java.util.List;

public class BookDtoToBookConverter implements Converter<BookDto, Book> {
    @Override
    public Book convert(BookDto source) {
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

        book.setId(source.getId());
        book.setGenres(genres);

        return book;
    }
}
