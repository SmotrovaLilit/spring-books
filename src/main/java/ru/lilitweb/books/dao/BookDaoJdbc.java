package ru.lilitweb.books.dao;

import com.google.common.collect.Lists;
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
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcTemplate jdbc;

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
        return jdbc.queryForObject("select b.*, u.fullname as author_fullname  from book b left join user u on u.id = b.author_id where b.id = :id", params, new BookMapper());
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("select b.* , u.fullname as author_fullname from book b left join user u on b.author_id=u.id", new HashMap<String, Book>(), new BookMapper());
    }

    @Override
    public List<Book> getAllWithGenres() {
        List<Book> books = jdbc.query("select b.*, u.fullname as author_fullname, g.name as genre_name\n" +
                        "from book b\n" +
                        "       left join user u on b.author_id = u.id\n" +
                        "       left join book_genre bg on b.id = bg.book_id\n" +
                        "       left join genre g on bg.genre_id = g.id",
                new HashMap<String, Book>(),
                (resultSet, i) -> {
                    BookMapper defaultBookMapper = new BookMapper();
                    Book book = defaultBookMapper.mapRow(resultSet, i);
                    String genreName = resultSet.getString("genre_name");
                    List<Genre> gList = new ArrayList<>();
                    if (genreName != null) {
                        gList.add(new Genre(genreName));
                    }
                    book.setGenres(gList);
                    return book;
                });
        Map<Long, Book> booksResult = new HashMap<>();

        for (Book book : books) {
            if (booksResult.get(book.getId()) == null) {
                booksResult.put(book.getId(), book);
                continue;
            }
            Book addedBook = booksResult.get(book.getId());
            List<Genre> gList = addedBook.getGenres();
            if (book.getGenres() != null) {
                gList.addAll(book.getGenres());
            }
            booksResult.get(book.getId()).setGenres(gList);
        }

        return new ArrayList<>(booksResult.values());
    }

    private void addGenresToBookList(List<Book> books, List<BookGenreRow> genres) {
        genres.forEach(row -> {
            books.stream().filter(book -> {
                return book.getId() == row.getBookId();
            }).forEach(book -> {
                List<Genre> gList = book.getGenres() != null ? book.getGenres() : new ArrayList<>();
                gList.add(row.getGenre());
                book.setGenres(gList);
            });
        });
    }

    /**
     * Used FetchMode type Select and batch size=25
     */
    @Override
    public void loadGenresFetchModeSelect(List<Book> books) {
        List<Long> ids = books.stream()
                .mapToLong(Book::getId)
                .boxed()
                .collect(Collectors.toList());
        List<List<Long>> parts = Lists.partition(ids, 25);
        parts.forEach(part -> {
            final HashMap<String, Object> params = new HashMap<>();
            params.put("books", part);
            List<BookGenreRow> genres = jdbc.query("select bg.book_id,\n" +
                    "       g.*\n" +
                    "from book_genre bg\n" +
                    "       inner join genre g on bg.genre_id = g.id\n" +
                    "where bg.book_id in (:books)", params, new BookGenreMapper());

            addGenresToBookList(books, genres);
        });
    }

    /**
     * Used FetchMode type SubSelect
     */
    @Override
    public void loadGenresFetchModeSubSelect(List<Book> books) {
        List<BookGenreRow> genres = jdbc.query("select bg.book_id,\n" +
                "       g.*\n" +
                "from book_genre bg\n" +
                "       inner join genre g on bg.genre_id = g.id\n" +
                "where bg.book_id in (select id from book)", new HashMap<>(), new BookGenreMapper());

        addGenresToBookList(books, genres);
    }

    @Override
    public List<Book> getAllByGenres(long[] genres) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("genres", Arrays.stream(genres)
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.toList()));

        return jdbc.query("select b.* , u.fullname as author_fullname from book b left join user u on b.author_id=u.id inner join book_genre on b.id=book_genre.book_id where book_genre.genre_id in (:genres) group by b.id",
                params, new BookMapper());
    }

    @Override
    public List<Book> getAllByAuthorId(long authorId) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("author_id", authorId);

        return jdbc.query("select b.* , u.fullname as author_fullname from book b left join user u on b.author_id=u.id where b.author_id=:author_id", params, new BookMapper());
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
            String authorFullName = resultSet.getString("author_fullname");
            return new Book(id, title, year, description, new User(authorId, authorFullName));
        }
    }

    private static class BookGenreMapper implements RowMapper<BookGenreRow> {
        @Override
        public BookGenreRow mapRow(ResultSet resultSet, int i) throws SQLException {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int bookId = resultSet.getInt("book_id");
            return new BookGenreRow(new Genre(id, name), bookId);
        }
    }

    private static class BookGenreRow {
        private Genre genre;
        private int bookId;

        BookGenreRow(Genre genre, int bookId) {
            this.genre = genre;
            this.bookId = bookId;
        }

        Genre getGenre() {
            return genre;
        }

        int getBookId() {
            return bookId;
        }
    }
}
