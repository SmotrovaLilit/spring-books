package ru.lilitweb.books.repostory;

import org.springframework.stereotype.Repository;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;
import ru.lilitweb.books.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@SuppressWarnings("JpaQlInspection")
public class BookRepositoryJpa implements BookRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void insert(Book book) {
        em.persist(book);
    }

    @Override
    @Transactional
    public void update(Book book) {
        em.merge(book);
    }


    @Override
    public Book getById(int id) {
        return em.find(Book.class, id);
    }

    @Override
    public List<Book> getAll() {
        TypedQuery<Book> query = em.createQuery(
                "select b from Book b",
                Book.class);
        return query.getResultList();
    }

    @Override
    public List<Book> getAllByGenres(List<Genre> genres) {
        TypedQuery<Book> query = em.createQuery(
                "select distinct b from Book b join b.genres g where g in :genres",
                Book.class);
        query.setParameter("genres", genres);
        return query.getResultList();
    }

    @Override
    public List<Book> getAllByAuthor(User author) {
        TypedQuery<Book> query = em.createQuery(
                "select b from Book b where b.author = :author",
                Book.class);
        query.setParameter("author", author);
        return query.getResultList();
    }

    @Override
    public void delete(Book book) {
        em.remove(book);
    }
}
