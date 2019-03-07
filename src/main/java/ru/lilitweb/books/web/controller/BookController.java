package ru.lilitweb.books.web.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.service.BookService;
import ru.lilitweb.books.web.ResourceNotFoundException;
import ru.lilitweb.books.web.conveter.BookFormToBookConverter;
import ru.lilitweb.books.web.conveter.BookToBookFormConverter;
import ru.lilitweb.books.web.form.BookForm;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public String index(@RequestParam("page") Optional<Integer> page,
                        @RequestParam("search") Optional<String> searchValue,
                        HttpServletRequest request,
                        Model model) {
        int pageNumber = page.orElse(1);
        int countOnPage = 10;
        Page<Book> bookPage = bookService.
                search(searchValue, PageRequest.of(
                        (pageNumber - 1),
                        countOnPage,
                        Sort.by(Sort.Direction.DESC, "createdAt")
                ));


        List<Integer> pageNumbers = new ArrayList<>();
        if (bookPage.getTotalPages() > 0) {
            pageNumbers = IntStream.rangeClosed(1, bookPage.getTotalPages())
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("books", bookPage.
                get().
                collect(Collectors.toList())
        );
        model.addAttribute("bookPage", bookPage);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("search", searchValue.orElse(""));
        model.addAttribute("currentUrlWithoutPage",
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .replaceQueryParam("page")
                        .toUriString());
        return "book/index";
    }

    @GetMapping("/book/{id}")
    public String view(@PathVariable String id, Model model) {
        Book book = bookService.getById(id).orElseThrow(ResourceNotFoundException::new);
        model.addAttribute("book", book);
        return "book/view";
    }

    @GetMapping("/book/{id}/edit")
    public String edit(@PathVariable String id,
                       Model model,
                       BookToBookFormConverter converter) {
        Book book = bookService.getById(id).orElseThrow(ResourceNotFoundException::new);
        if (!model.containsAttribute("bookForm")) {
            model.addAttribute("bookForm", converter.convert(book));
        }
        model.addAttribute("id", id);
        model.addAttribute("listgenres", bookService.getAvailableGenres());
        return "book/edit";
    }

    @PostMapping("/book/{id}")
    public String update(@PathVariable String id, @ModelAttribute("bookForm") @Valid BookForm bookForm,
                         BindingResult result,
                         RedirectAttributes attr,
                         BookFormToBookConverter converter) {
        bookService.getById(id).orElseThrow(ResourceNotFoundException::new);

        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.bookForm", result);
            attr.addFlashAttribute("bookForm", bookForm);
            return "redirect:/book/" + id + "/edit";
        }
        Book book = converter.convert(bookForm);
        Objects.requireNonNull(book).setId(id);
        bookService.update(book);
        return "redirect:/book/" + id;
    }

    @GetMapping("/book/create")
    public String create(Model model) {
        if (!model.containsAttribute("bookForm")) {
            model.addAttribute("bookForm", new BookForm());
        }
        model.addAttribute("listgenres", bookService.getAvailableGenres());
        return "book/create";
    }

    @PostMapping("/book")
    public String store(@ModelAttribute("bookForm") @Valid BookForm bookForm,
                        BindingResult result,
                        RedirectAttributes attr,
                        BookFormToBookConverter bookFormToBookConverter) {
        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.bookForm", result);
            attr.addFlashAttribute("bookForm", bookForm);
            return "redirect:/book/create";
        }
        Book book = bookFormToBookConverter.convert(bookForm);
        bookService.add(book);
        return "redirect:/book/" + Objects.requireNonNull(book).getId();
    }

    @DeleteMapping(value = "/book/{id}", produces = "application/json")
    @ResponseBody
    public void delete(@PathVariable String id) {
        bookService.delete(id);
    }
}
