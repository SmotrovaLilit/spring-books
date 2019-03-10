package ru.lilitweb.books.rest.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;
import ru.lilitweb.books.rest.ResourceId;
import ru.lilitweb.books.rest.conveter.BookPageToBookDtoResponsePageConverter;
import ru.lilitweb.books.rest.dto.response.ResponsePage;
import ru.lilitweb.books.rest.validation.SortBookListConstraint;
import ru.lilitweb.books.service.BookService;
import ru.lilitweb.books.rest.ResourceNotFoundException;
import ru.lilitweb.books.rest.conveter.BookDtoToBookConverter;
import ru.lilitweb.books.rest.conveter.BookToBookDtoConverter;
import ru.lilitweb.books.rest.dto.BookDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/v1")
public class BookController {
    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Options
     *
     * @return allowed method
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> options() {
        return ResponseEntity
                .ok()
                .allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.HEAD, HttpMethod.OPTIONS,
                        HttpMethod.PUT, HttpMethod.DELETE)
                .build();
    }


    @ApiOperation("Book search")
    @GetMapping("/books")
    public ResponseEntity<ResponsePage<BookDto>> index(
            @ApiParam(value = "Sort",
                    required = true,
                    example = "desc,createdAt") @Valid @RequestParam(required = false)
            @SortBookListConstraint Sort sort,
            @Valid @RequestParam(required = false, defaultValue = "1") @Min(1) Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "") String search,
            BookPageToBookDtoResponsePageConverter converter
    ) {
        Page<Book> bookPage = bookService.
                search(!search.equals("") ? Optional.of(search) : Optional.empty(),
                        PageRequest.of(page - 1, size, sort == null ? Sort.unsorted() : sort));


        return ResponseEntity.ok(converter.convert(bookPage));

    }

    @ApiOperation("Information about book")
    @GetMapping("/book/{id}")
    public ResponseEntity<BookDto> view(
            @ApiParam(value = "Book id",
                    required = true,
                    example = "5c803be45a0f745d98a2abf3") @PathVariable String id,
            BookToBookDtoConverter converter) {
        Book book = bookService.getById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException(new ResourceId(id));
        });
        return ResponseEntity.ok(converter.convert(book));
    }

    @ApiOperation(value = "Genre list", response = List.class)
    @GetMapping("/book/genres")
    public ResponseEntity<List<String>> genres() {
        return ResponseEntity.ok(bookService.getAvailableGenres()
                .stream()
                .map(Genre::getName)
                .collect(Collectors.toList()));
    }


    @ApiOperation("Update book")
    @PutMapping("/book/{id}")
    public ResponseEntity<BookDto> update(
            @ApiParam(value = "Book id",
                    required = true,
                    example = "5c803be45a0f745d98a2abf3") @PathVariable String id,
            @RequestBody @Valid BookDto bookDto,
            BookDtoToBookConverter bookDtoToBookConverter, BookToBookDtoConverter bookToBookDtoConverter) {
        bookService.getById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException(new ResourceId(id));
        });

        Book book = bookDtoToBookConverter.convert(bookDto);
        Objects.requireNonNull(book).setId(id);
        bookService.update(book);
        return ResponseEntity.ok(bookToBookDtoConverter.convert(book));
    }


    @ApiOperation("Create book")
    @PostMapping("/book")
    public ResponseEntity<BookDto> create(@RequestBody @Valid BookDto bookDto,
                                          BookDtoToBookConverter converter) {
        Book book = converter.convert(bookDto);
        bookService.add(book);
        bookDto.setId(Objects.requireNonNull(book).getId());

        URI uri = MvcUriComponentsBuilder.fromController(getClass()).path("/book/{id}")
                .buildAndExpand(book.getId()).toUri();
        return ResponseEntity.created(uri).body(bookDto);
    }


    @ApiOperation("Remove book")
    @DeleteMapping(value = "/book/{id}")
    public ResponseEntity<Void> delete(
            @ApiParam(value = "Book id", required = true, example = "5c803") @PathVariable String id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @ApiOperation("Check existing book")
    @RequestMapping(value = "/book/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<?> head(
            @ApiParam(value = "Book id",
                    required = true,
                    example = "5c803be45a0f745d98a2abf3") @PathVariable String id) {
        bookService.getById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException(new ResourceId(id));
        });
        return ResponseEntity.noContent().build();
    }
}
