package ru.lilitweb.books.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.persistence.Entity;

@Data
@RequiredArgsConstructor
public class Book implements Entity {
    private int id;

    @NonNull
    private String title;

    @NonNull
    private int year;

    @NonNull
    private String description;


    @ManyToOne
    @NonNull
    private User author;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres = new ArrayList<>();
}
