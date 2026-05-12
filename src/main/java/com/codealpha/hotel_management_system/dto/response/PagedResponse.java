package com.codealpha.hotel_management_system.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PagedResponse<T>{
    private List<T> content;     // the actual data for this page
    private int pageNumber;      // 0-indexed current page
    private int pageSize;        // how many items per page
}
