package ru.lilitweb.books.web.helper;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PageHelper<T> {
    public List<Integer> getPageNumbers(Page<T> page) {
        if (page.getTotalPages() > 0) {
            return IntStream.rangeClosed(1, page.getTotalPages())
                    .boxed()
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
