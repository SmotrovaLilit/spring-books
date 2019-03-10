package ru.lilitweb.books.rest.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;
import ru.lilitweb.books.rest.validation.YearConstraint;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private String id;

    @ApiModelProperty(value = "Title", required = true, example = "Some book")
    @Size(min = 3, max=2024, message = "{bookForm.error.title.incorrect}")
    private String title;

    @ApiModelProperty(value = "Author full name", required = true, example = "Some author")
    @Size(min = 3, max=2024, message = "{bookForm.error.author.incorrect}")
    private String author;

    @ApiModelProperty(value = "Year", required = true, example = "2012")
    @YearConstraint(message = "{bookForm.error.year.incorrect}")
    private int year;

    @ApiModelProperty(value = "Description", required = true, example = "Some description")
    @Size(min = 3, message = "{bookForm.error.description.incorrect}")
    private String description;

    @ApiModelProperty(value = "Genres", required = true, example = "[\"drama\"]")
    private List<String> genres = new ArrayList<>();
}

