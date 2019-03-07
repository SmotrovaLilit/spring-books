package ru.lilitweb.books.web.form;

import lombok.*;
import ru.lilitweb.books.web.validation.YearConstraint;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookForm {

    @Size(min = 3, max=2024, message = "{bookForm.error.title.incorrect}")
    private String title;

    @Size(min = 3, max=2024, message = "{bookForm.error.author.incorrect}")
    private String author;

    @YearConstraint(message = "{bookForm.error.year.incorrect}")
    private int year;

    @Size(min = 3, message = "{bookForm.error.description.incorrect}")
    private String description;

    private List<String> genres = new ArrayList<>();
}

