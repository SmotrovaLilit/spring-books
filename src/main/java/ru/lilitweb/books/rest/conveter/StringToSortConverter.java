package ru.lilitweb.books.rest.conveter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort;

public class StringToSortConverter implements Converter<String, Sort> {

    @Override
    public Sort convert(String source) {
        if (source == null || source.equals("")) {
            return Sort.unsorted();
        }

        String[] data = source.split(",");
        if (data.length != 2) {
            throw new IllegalArgumentException("invalid sort value " + source);
        }

        return Sort.by(data[0].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, data[1]);
    }
}
