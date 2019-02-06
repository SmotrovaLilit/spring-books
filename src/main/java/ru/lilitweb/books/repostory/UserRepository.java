package ru.lilitweb.books.repostory;

import ru.lilitweb.books.domain.User;

import java.util.List;

public interface UserRepository {
    void insert(User user);
    void update(User user);
    User getById(long id);

    List<User> getAll();

    void delete(User user);
}
