package ru.lilitweb.books.shell.converter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.lilitweb.books.domain.Author;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.User;

@Component
public class BookConverter implements Converter<String, Book> {

    @Override
    public Book convert(String source) {
        String[] data = source.split("\\|");
        if (data.length != 4) {
            return null;
        }

        String name = data[0];
        int year = Integer.parseInt(data[1]);
        String description = data[2];
        String authorName = data[3];

        return new Book(name, year, description, new Author(authorName));
    }
}
