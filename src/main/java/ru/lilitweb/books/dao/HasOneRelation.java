package ru.lilitweb.books.dao;

import lombok.Builder;

import java.util.List;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

@Builder
public class HasOneRelation<E, R extends Entity> {
    private ToLongFunction<E> foreignKeyGetter;
    private RelationSetter<E, R> relationSetter;

    public void load(List<E> entities, RelatedEntitiesLoader<R> relatedEntitiesLoader) {
        List<Long> relationsIds = entities.stream().
                mapToLong(foreignKeyGetter).
                distinct().
                boxed().
                collect(Collectors.toList());

        List<R> relations = relatedEntitiesLoader.getByIds(relationsIds);

        entities.forEach(e -> relations.stream().
                filter(r -> foreignKeyGetter.applyAsLong(e) == r.getId()).
                findFirst().ifPresent(r -> relationSetter.apply(e, r)));
    }

    @FunctionalInterface
    public interface RelationSetter<E, R> {
        void apply(E e, R r);
    }
}
