package ru.lilitweb.books.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.lilitweb.books.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Repository
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcTemplate jdbc;

    @Autowired
    public GenreDaoJdbc(NamedParameterJdbcTemplate jdbcTemplate) {
        jdbc = jdbcTemplate;
    }

    @Override
    public int count() {
        return jdbc.queryForObject("select count(*) from genre", new HashMap<>(), Integer.class);
    }

    @Override
    public void insert(Genre genre) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("id", genre.getId());
        params.put("name", genre.getName());
        jdbc.update("insert into genre (id, name) values (:id, :name)",
                params);
    }

    @Override
    public void update(Genre genre) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("id", genre.getId());
        params.put("name", genre.getName());
        jdbc.update("update genre set name=:name where id=:id", params);
    }

    @Override
    public Genre getById(long id) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        return jdbc.queryForObject("select * from genre where id=:id", params, new GenreDaoJdbc.GenreMapper());
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query("select * from genre", new HashMap<String, Genre>(), new GenreDaoJdbc.GenreMapper());
    }

    @Override
    public List<Genre> getByIds(List<Long> ids) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        return jdbc.query("select * from genre where id in (:ids)", params, new GenreDaoJdbc.GenreMapper());
    }

    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            return new Genre(id, name);
        }
    }

    @Override
    public void delete(long id) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        jdbc.update("delete from genre where id=:id", params);
    }
}
