package ru.lilitweb.books.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.Genre;
import ru.lilitweb.books.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcTemplate jdbc;

    private final HasOneRelation<Book, User> authorsRelation =
            HasOneRelation.<Book, User>builder()
                    .foreignKeyGetter(book -> book.getAuthor().getId())
                    .relationSetter(Book::setAuthor)
                    .build();

    private final ManyToManyRelation<Book, Genre> genreRelation =
            ManyToManyRelation.<Book, Genre>builder()
                    .table("book_genre")
                    .foreignKey("book_id")
                    .otherKey("genre_id")
                    .relationSetter(Book::setGenres)
                    .build();

    @Autowired
    public BookDaoJdbc(NamedParameterJdbcTemplate jdbcTemplate) {
        jdbc = jdbcTemplate;
    }

    @Override
    public int count() {
        return jdbc.queryForObject("select count(*) from book", new HashMap<>(), Integer.class);
    }

    @Override
    public void insert(Book book) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("year", book.getYear())
                .addValue("description", book.getDescription())
                .addValue("author_id", book.getAuthor().getId());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update("insert into book (title, year, description, author_id) values (:title, :year, :description, :author_id)",
                parameters, keyHolder, new String[]{"id"});
        book.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        if (book.getGenres() != null && book.getGenres().size() > 0) {
            book.getGenres().forEach(genre -> {
                jdbc.update(
                        "insert into book_genre (book_id, genre_id) values(:bookId, :genreId)",
                        new MapSqlParameterSource()
                                .addValue("bookId", book.getId())
                                .addValue("genreId", genre.getId())
                );
            });
        }
    }

    @Override
    public void update(Book book) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("id", book.getId());
        params.put("title", book.getTitle());
        params.put("year", book.getYear());
        params.put("description", book.getDescription());
        params.put("author_id", book.getAuthor().getId());
        jdbc.update("update book set title=:title, year=:year, description=:description, author_id=:author_id where id=:id", params);
        if (book.getGenres() != null && book.getGenres().size() > 0) {
            book.getGenres().forEach(genre -> {
                jdbc.update(
                        "INSERT INTO book_genre (book_id, genre_id) SELECT :bookId, :genreId FROM DUAL WHERE NOT EXISTS (SELECT * FROM book_genre WHERE book_id = :bookId AND genre_id = :genreId)",
                        new MapSqlParameterSource()
                                .addValue("bookId", book.getId())
                                .addValue("genreId", genre.getId())
                );
            });
            jdbc.update(
                    "DELETE from book_genre where book_id=:bookId and not(genre_id in (:genres))",
                    new MapSqlParameterSource()
                            .addValue("bookId", book.getId())
                            .addValue("genres", book.getGenres()
                                    .stream()
                                    .mapToLong(Genre::getId)
                                    .boxed()
                                    .map(String::valueOf)
                                    .collect(Collectors.toList())
                            )
            );

        }
    }

    @Override
    public Book getById(long id) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        return jdbc.queryForObject("select * from book where id = :id", params, new BookMapper());
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("select * from book", new HashMap<String, Book>(), new BookMapper());
    }

    @Override
    public void loadAuthors(List<Book> books, RelatedEntitiesLoader<User> relatedEntitiesLoader) {
        authorsRelation.load(books, relatedEntitiesLoader);
    }

    @Override
    public void loadGenres(List<Book> books, RelatedEntitiesLoader<Genre> genresLoader) {
        genreRelation.load(books, jdbc, genresLoader);
    }

    @Override
    public List<Book> getAllByGenres(long[] genres) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("genres", Arrays.stream(genres)
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.toList()));

        return jdbc.query("select book.* from book inner join book_genre on book.id=book_genre.book_id where book_genre.genre_id in (:genres) group by book.id",
                params, new BookMapper());
    }

    @Override
    public List<Book> getAllByAuthorId(long authorId) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("author_id", authorId);

        return jdbc.query("select * from book where author_id=:author_id", params, new BookMapper());
    }

    @Override
    public void delete(long id) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        jdbc.update("delete from book where id=:id", params);
    }

    private static class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            int id = resultSet.getInt("id");
            int year = resultSet.getInt("year");
            int authorId = resultSet.getInt("author_id");
            String title = resultSet.getString("title");
            String description = resultSet.getString("description");
            return new Book(id, title, year, description, new User(authorId));
        }
    }
}
