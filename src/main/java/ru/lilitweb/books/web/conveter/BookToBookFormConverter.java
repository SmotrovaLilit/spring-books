package ru.lilitweb.books.web.conveter;

import org.springframework.core.convert.converter.Converter;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.web.form.BookForm;

import java.util.ArrayList;
import java.util.List;


public class BookToBookFormConverter implements Converter<Book, BookForm> {
    @Override
    public BookForm convert(Book source) {
        List<String> genres = new ArrayList<>();
        source.getGenres().forEach(g -> {
            genres.add(g.getName());
        });
        return new BookForm(
                source.getTitle(),
                source.getAuthor().getFullname(),
                source.getYear(),
                source.getDescription(),
                genres
        );
    }
}
