package ru.lilitweb.books.repostory;

import org.springframework.stereotype.Repository;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@SuppressWarnings("JpaQlInspection")
public class CommentRepositoryJpa implements CommentRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void insert(Comment comment) {
        em.persist(comment);
    }

    @Override
    @Transactional
    public void update(Comment comment) {
        em.merge(comment);
    }

    @Override
    public Comment getById(int id) {
        return em.find(Comment.class, id);
    }

    @Override
    public List<Comment> getAllByBook(Book book) {
        TypedQuery<Comment> query = em.createQuery(
                "select c from Comment c where c.book=:book",
                Comment.class);
        query.setParameter("book", book);
        return query.getResultList();
    }

    @Override
    public void delete(Comment comment) {
        em.remove(comment);
    }
}
