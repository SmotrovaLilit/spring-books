package ru.lilitweb.books.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;


@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Document(collection = "book_comments")
public class Comment {
    @Id
    private String id;

    @NonNull
    private String message;

    @DBRef
    private User author;

    @NonNull
    @DBRef
    private Book book;

    public Comment(String message, User author, Book book) {
        this.message = message;
        this.author = author;
        this.book = book;
    }
}
