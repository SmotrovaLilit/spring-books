package ru.lilitweb.books.repostory;

import org.springframework.stereotype.Repository;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@SuppressWarnings("JpaQlInspection")
public class UserRepositoryJpa implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void insert(User user) {
        em.persist(user);
    }

    @Override
    @Transactional
    public void update(User user) {
        em.merge(user);
    }

    @Override
    public User getById(int id) {
        return em.find(User.class, id);
    }

    @Override
    public List<User> getAll() {
        TypedQuery<User> query = em.createQuery(
                "select g from User g",
                User.class);
        return query.getResultList();
    }

    @Override
    public void delete(User user) {
        em.remove(user);
    }
}
