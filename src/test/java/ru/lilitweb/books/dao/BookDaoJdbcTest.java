package ru.lilitweb.books.dao;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import ru.lilitweb.books.domain.Book;
import ru.lilitweb.books.domain.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@JdbcTest
@Import({BookDaoJdbc.class, GenreDaoJdbc.class, DataSourceAutoConfiguration.class})
public class BookDaoJdbcTest {

    @Autowired
    BookDao bookDao;

    @Autowired
    GenreDao genreDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/bookDaoJdbc/beforeTestingCount.sql")
    public void count() {
        bookDao.count();
        assertEquals(2, bookDao.count());
    }

    @Test
    public void insert() {
        Book book = new Book(
                "Руслан и Людмила",
                2019,
                "Описание",
                new User(1));
        book.setGenres(Collections.singletonList(genreDao.getById(1)));
        bookDao.insert(book);

        int countRecords = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "book", String.format(
                "id=%d and title='%s' and year=%d and description='%s' and author_id='%s'",
                book.getId(),
                book.getTitle(),
                book.getYear(),
                book.getDescription(),
                book.getAuthor().getId()
        ));

        assertTrue(book.getId() > 0);
        assertEquals(1, countRecords);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "book_genre", String.format(
                "book_id=%d",
                book.getId()
        )));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/bookDaoJdbc/beforeTestingUpdate.sql")
    public void update() {
        Book book = bookDao.getById(1);
        book.setTitle("new title");
        book.setDescription("new description");
        book.setYear(book.getYear() + 1);
        book.setGenres(Collections.singletonList(genreDao.getById(2)));
        bookDao.update(book);
        int countRecords = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "book", String.format(
                "id=%d and title='%s' and year=%d and description='%s' and author_id='%s'",
                book.getId(),
                book.getTitle(),
                book.getYear(),
                book.getDescription(),
                book.getAuthor().getId()
        ));

        assertEquals(1, countRecords);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "book_genre", String.format(
                "book_id=%d",
                book.getId()
        )));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "book_genre", String.format(
                "book_id=%d and genre_id=%d",
                book.getId(),
                2
        )));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/bookDaoJdbc/beforeTestingDelete.sql")
    public void delete() {
        Book book = bookDao.getById(1);
        bookDao.delete(1);
        int countRecords = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "book", String.format(
                "id=%d",
                book.getId()
        ));

        assertEquals(0, countRecords);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/bookDaoJdbc/beforeTestingGetById.sql")
    public void getById() {
        Book foundedBook = bookDao.getById(1);
        assertEquals(1, foundedBook.getId());
        assertEquals("test book title", foundedBook.getTitle());
        assertEquals(1994, foundedBook.getYear());
        assertEquals("test book description", foundedBook.getDescription());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/bookDaoJdbc/beforeTestingGetAll.sql")
    @Transactional
    public void getAll() {
        List<Book> books = bookDao.getAll();
        assertEquals(2, books.size());

        assertEquals("test book title(1)", books.get(0).getTitle());
        assertEquals("test book description(2)", books.get(1).getDescription());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/bookDaoJdbc/beforeTestingGetAll.sql")
    @Transactional
    public void getAllWithPayloads() {
        List<Book> books = bookDao.getAllWithGenres();
        assertEquals(2, books.size());

        assertEquals("test book title(1)", books.get(0).getTitle());
        assertEquals("test book description(2)", books.get(1).getDescription());
        assertEquals(2, books.get(1).getGenres().size());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/bookDaoJdbc/beforeTestingGetAllByGenres.sql")
    public void getAllByGenres() {
        long[] genres = {1, 2};
        List<Book> books = bookDao.getAllByGenres(genres);
        assertEquals(2, books.size());

        assertEquals("test book title(1)", books.get(0).getTitle());
        assertEquals("test book title(2)", books.get(1).getTitle());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/bookDaoJdbc/beforeTestingGetAllByAuthorId.sql")
    public void getAllByAuthorId() {
        List<Book> books = bookDao.getAllByAuthorId(1);
        assertEquals(2, books.size());

        assertEquals("test book title(1)", books.get(0).getTitle());
        assertEquals("test book title(2)", books.get(1).getTitle());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/bookDaoJdbc/beforeTestingLoadGenres.sql")
    public void loadGenresFetchModeSelect() {
        List<Book> books = bookDao.getAll();
        bookDao.loadGenresFetchModeSelect(books);
        assertEquals("Поэзия", books.get(0).getGenres().get(0).getName());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/bookDaoJdbc/beforeTestingLoadGenres.sql")
    public void loadGenresFetchModeSubSelect() {
        List<Book> books = bookDao.getAll();
        bookDao.loadGenresFetchModeSubSelect(books);
        assertEquals("Поэзия", books.get(0).getGenres().get(0).getName());
    }
}
