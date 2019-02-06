package ru.lilitweb.books.dao;

import java.util.List;

public interface RelatedEntitiesLoader<R> {
    List<R> getByIds(List<Long> ids);
}
