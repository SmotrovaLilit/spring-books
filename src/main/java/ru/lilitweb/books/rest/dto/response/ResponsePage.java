package ru.lilitweb.books.rest.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResponsePage<T> {
    @ApiModelProperty(value = "Elements")
    private List<T> elements;
    @ApiModelProperty(value = "Count pages", example = "10")
    private Integer totalPages;
    @ApiModelProperty(value = "Count all elements", example = "100")
    private Long totalElements;
    @ApiModelProperty(value = "Count elements on page", example = "10")
    private Integer size;
    @ApiModelProperty(value = "Current page number", example = "1")
    private Integer pageNumber;
}
