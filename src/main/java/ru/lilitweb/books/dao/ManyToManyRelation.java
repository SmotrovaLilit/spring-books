package ru.lilitweb.books.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

@Builder
public class ManyToManyRelation<E extends Entity, R extends Entity> {
    private final RelationSetter<E, R> relationSetter;
    private final String table;
    private final String foreignKey;
    private final String otherKey;

    public void load(List<E> entities, NamedParameterJdbcTemplate jdbc, RelatedEntitiesLoader<R> relatedEntitiesLoader) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("ids", entities.stream().
                mapToLong(Entity::getId).
                distinct().
                boxed().
                collect(Collectors.toList()));

        List<Row> rows = jdbc.query(
                String.format("select * from %s where %s in (:ids)", table, foreignKey),
                params,
                new Mapper(foreignKey, otherKey));

        List<Long> relatedIds = rows.stream().map(row -> row.otherKey).collect(Collectors.toList());
        List<R> relatedEntities = new ArrayList<>();
        if (relatedIds.size() > 0) {
            relatedEntities = relatedEntitiesLoader.getByIds(relatedIds);
        }

        HashMap<Long, Set<Long>> foreignToOtherMap = new HashMap<>();
        rows.forEach(row -> {
            if (foreignToOtherMap.containsKey(row.foreignKey)) {
                foreignToOtherMap.get(row.foreignKey).add(row.otherKey);
                return;
            }

            HashSet<Long> s = new HashSet<>();
            s.add(row.otherKey);
            foreignToOtherMap.put(row.foreignKey, s);
        });

        List<R> finalRelatedEntities = relatedEntities;

        entities.forEach(e -> relationSetter.apply(e,
                finalRelatedEntities.stream().
                        filter(r -> foreignToOtherMap.containsKey(e.getId())).
                        filter(r -> foreignToOtherMap.get(e.getId()).contains(r.getId())).
                        collect(Collectors.toList())));
    }

    @AllArgsConstructor
    @Data
    private static class Row {
        Long foreignKey;
        Long otherKey;
    }

    @AllArgsConstructor
    private static class Mapper implements RowMapper<Row> {
        private String foreignKey;
        private String otherKey;

        @Override
        public Row mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Row(
                    resultSet.getLong(this.foreignKey),
                    resultSet.getLong(this.otherKey)
            );
        }
    }

    @FunctionalInterface
    public interface RelationSetter<E, R> {
        void apply(E e, List<R> r);
    }

}
