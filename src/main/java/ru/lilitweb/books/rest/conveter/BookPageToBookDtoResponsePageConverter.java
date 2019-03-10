package ru.lilitweb.books.rest.conveter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.rest.dto.BookDto;
import ru.lilitweb.books.rest.dto.response.ResponsePage;

import java.util.stream.Collectors;

public class BookPageToBookDtoResponsePageConverter implements Converter<Page<Book>, ResponsePage<BookDto>> {

    @Override
    public ResponsePage<BookDto> convert(Page<Book> source) {
        if (source == null) {
            return  null;
        }
        ResponsePage<BookDto> responsePage = new ResponsePage<BookDto>();
        responsePage.setElements(source.get()
                .map(book -> (new BookToBookDtoConverter()).convert(book))
                .collect(Collectors.toList()));
        responsePage.setPageNumber(source.getNumber());
        responsePage.setSize(source.getSize());
        responsePage.setTotalElements(source.getTotalElements());
        responsePage.setTotalPages(source.getTotalPages());
        return responsePage;
    }
}
