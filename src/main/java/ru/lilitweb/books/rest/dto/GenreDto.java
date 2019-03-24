package ru.lilitweb.books.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
@AllArgsConstructor
public class GenreDto {
    @NonNull
    private String name;
}
