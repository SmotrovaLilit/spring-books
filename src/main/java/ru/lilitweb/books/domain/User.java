package ru.lilitweb.books.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class User implements Entity {
    private int id;

    @NonNull
    private String fullname;

    public User(int id) {
        this.id = id;
    }
}
