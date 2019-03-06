package ru.lilitweb.books.web.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class BookForm {

    @NotBlank(message = "{bookForm.error.title.empty}")
    @Size(min = 3, max=2024, message = "{bookForm.error.title.incorrect}")
    private String title;

    @NonNull
    @NotBlank(message = "{bookForm.error.author.empty}")
    @Size(min = 3, max=2024, message = "{bookForm.error.author.incorrect}")
    private String author;

    @NonNull
    @Min(value = 1900, message = "{bookForm.error.year.incorrect}")
    private int year;

    @NonNull
    @NotBlank(message = "{bookForm.error.description.empty}")
    @Size(min = 3, message = "{bookForm.error.description.incorrect}")
    private String description;

    private List<String> genres = new ArrayList<>();
}
