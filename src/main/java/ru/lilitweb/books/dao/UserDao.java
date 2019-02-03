package ru.lilitweb.books.dao;

import ru.lilitweb.books.domain.User;

import java.util.List;

public interface UserDao extends RelatedEntitiesLoader<User> {
    int count();
    void insert(User user);
    void update(User user);
    User getById(int id);

    List<User> getAll();

    void delete(int id);

    List<User> getByIds(List<Integer> ids);
}
