package ru.lilitweb.books.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import ru.lilitweb.books.dao.Entity;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class User implements Entity {
    private long id;

    @NonNull
    private String fullName;

    public User(long id) {
        this.id = id;
    }
}
