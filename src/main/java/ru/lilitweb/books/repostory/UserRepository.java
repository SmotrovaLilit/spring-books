package ru.lilitweb.books.repostory;

import org.springframework.data.repository.CrudRepository;
import ru.lilitweb.books.domain.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
}
