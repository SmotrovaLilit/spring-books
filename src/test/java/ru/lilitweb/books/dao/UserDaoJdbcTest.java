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
import ru.lilitweb.books.domain.User;

import java.util.List;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@JdbcTest
@Import({UserDaoJdbc.class, DataSourceAutoConfiguration.class})
public class UserDaoJdbcTest {

    @Autowired
    UserDao userDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/userDaoJdbc/resetTeble.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/userDaoJdbc/beforeTestingCount.sql")
    })
    public void count() {
        userDao.count();
        assertEquals(2, userDao.count());
    }

    @Test
    public void insert() {
        User user = new User("Иванов Иван Иванович");
        userDao.insert(user);

        int countRecords = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user", format(
                "id=%d and fullname='%s'",
                user.getId(),
                user.getFullName()
        ));

        assertEquals(1, countRecords);
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/userDaoJdbc/resetTeble.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/userDaoJdbc/beforeTestingUpdate.sql")
    })
    public void update() {
        User user = userDao.getById(1);
        userDao.update(user);
        int countRecords = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user", format(
                "id=%d and fullname='%s'",
                user.getId(),
                user.getFullName()
        ));

        assertEquals(1, countRecords);
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/userDaoJdbc/resetTeble.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/userDaoJdbc/beforeTestingGetById.sql")
    })
    public void getById() {
        List<User> users = userDao.getAll();
        assertEquals(1, users.size());

        User foundedUser = userDao.getById(users.get(0).getId());
        assertEquals(1, foundedUser.getId());
        assertEquals("test user fullname", foundedUser.getFullName());
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/userDaoJdbc/beforeTestingDelete.sql")
    public void delete() {
        User user = userDao.getById(1);
        userDao.delete(1);
        int countRecords = JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user", String.format(
                "id=%d",
                user.getId()
        ));

        assertEquals(0, countRecords);
    }

    @Test
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/userDaoJdbc/resetTeble.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/data/userDaoJdbc/beforeTestingGetAll.sql")
    })
    public void getAll() {
        List<User> users = userDao.getAll();
        assertEquals(2, users.size());

        assertEquals("test user fullname(1)", users.get(0).getFullName());
        assertEquals("test user fullname(2)", users.get(1).getFullName());
    }
}
