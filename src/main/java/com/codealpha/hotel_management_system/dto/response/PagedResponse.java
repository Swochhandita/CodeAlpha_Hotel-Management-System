package com.codealpha.hotel_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import java.util.function.Function;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse<T>{
    private List<T> content;     // the actual data for this page
    private int pageNumber;      // 0-indexed current page
    private int pageSize;

    public static <T, S> PagedResponse<T> fromPage(Page<S> page, Function<S, T> mapper) {
        return PagedResponse.<T>builder()
                .content(page.getContent().stream().map(mapper).toList())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .build();
    }
}
