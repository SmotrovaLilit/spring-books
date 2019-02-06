package ru.lilitweb.books.dao;

import ru.lilitweb.books.domain.User;

import java.util.List;

public interface UserDao extends RelatedEntitiesLoader<User> {
    int count();
    void insert(User user);
    void update(User user);
    User getById(long id);

    List<User> getAll();

    void delete(long id);

    List<User> getByIds(List<Long> ids);
}
