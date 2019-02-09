package ru.lilitweb.books.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.lilitweb.books.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Repository
public class UserDaoJdbc implements UserDao {

    private final NamedParameterJdbcTemplate jdbc;

    @Autowired
    public UserDaoJdbc(NamedParameterJdbcTemplate jdbcTemplate) {
        jdbc = jdbcTemplate;
    }

    @Override
    public int count() {
        return jdbc.queryForObject("select count(*) from user", new HashMap<>(), Integer.class);
    }

    @Override
    public void insert(User user) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("id", user.getId());
        params.put("fullname", user.getFullName());
        jdbc.update("insert into user (id, fullname) values (:id, :fullname)",
                params);
    }

    @Override
    public void update(User user) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("id", user.getId());
        params.put("fullname", user.getFullName());
        jdbc.update("update user set fullname=:fullname where id=:id", params);
    }

    @Override
    public User getById(long id) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        return jdbc.queryForObject("select * from user where id=:id", params, new UserDaoJdbc.UserMapper());
    }

    @Override
    public List<User> getAll() {
        return jdbc.query("select * from user", new HashMap<String, User>(), new UserDaoJdbc.UserMapper());
    }

    private static class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            int id = resultSet.getInt("id");
            String fullname = resultSet.getString("fullname");
            return new User(id, fullname);
        }
    }

    @Override
    public void delete(long id) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        jdbc.update("delete from user where id=:id", params);
    }
}
