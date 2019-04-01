package ru.lilitweb.books.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User{
    @Id
    private String id;

    @NonNull
    private String fullname;
}
