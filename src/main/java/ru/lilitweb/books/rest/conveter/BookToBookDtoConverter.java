package ru.lilitweb.books.rest.conveter;

import org.springframework.core.convert.converter.Converter;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.rest.dto.BookDto;

import java.util.ArrayList;
import java.util.List;


public class BookToBookDtoConverter implements Converter<Book, BookDto> {
    @Override
    public BookDto convert(Book source) {
        List<String> genres = new ArrayList<>();
        source.getGenres().forEach(g -> {
            genres.add(g.getName());
        });
        return new BookDto(
                source.getId(),
                source.getTitle(),
                source.getAuthor().getFullname(),
                source.getYear(),
                source.getDescription(),
                genres
        );
    }
}
