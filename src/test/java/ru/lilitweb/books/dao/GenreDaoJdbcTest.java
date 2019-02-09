package ru.lilitweb.books.dao;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.lilitweb.books.domain.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@JdbcTest
@Import({GenreDaoJdbc.class, DataSourceAutoConfiguration.class})
public class GenreDaoJdbcTest {

    @Autowired
    GenreDao genreDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/genreDaoJdbc/resetTeble.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/genreDaoJdbc/beforeTestingCount.sql")
    })
    public void count() {
        genreDao.count();
        assertEquals(2, genreDao.count());
    }

    @Test
    public void insert() {
        Genre genre = new Genre("Приключение");
        genreDao.insert(genre);

        int countRecords = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "genre", String.format(
                "id=%d and name='%s'",
                genre.getId(),
                genre.getName()
        ));

        assertEquals(1, countRecords);
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/genreDaoJdbc/resetTeble.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/genreDaoJdbc/beforeTestingUpdate.sql")
    })
    public void update() {
        Genre genre = genreDao.getById(1);
        genreDao.update(genre);
        int countRecords = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "genre", String.format(
                "id=%d and name='%s'",
                genre.getId(),
                genre.getName()
        ));

        assertEquals(1, countRecords);
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/genreDaoJdbc/resetTeble.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/genreDaoJdbc/beforeTestingGetById.sql")
    })
    public void getById() {
        List<Genre> genres = genreDao.getAll();
        assertEquals(1, genres.size());

        Genre foundedGenre = genreDao.getById(genres.get(0).getId());
        assertEquals(1, foundedGenre.getId());
        assertEquals("test genre name", foundedGenre.getName());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/genreDaoJdbc/beforeTestingDelete.sql")
    public void delete() {
        Genre genre = genreDao.getById(1);
        genreDao.delete(1);
        int countRecords = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "genre", String.format(
                "id=%d",
                genre.getId()
        ));

        assertEquals(0, countRecords);
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/genreDaoJdbc/resetTeble.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/genreDaoJdbc/beforeTestingGetAll.sql")
    })
    public void getAll() {
        List<Genre> genres = genreDao.getAll();
        assertEquals(2, genres.size());

        assertEquals("test genre name(1)", genres.get(0).getName());
        assertEquals("test genre name(2)", genres.get(1).getName());
    }
}
