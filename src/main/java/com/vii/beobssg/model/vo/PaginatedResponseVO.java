package com.vii.beobssg.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponseVO<T> {
    private int page;
    private List<T> data;
    private int size;
    private long totalElements;
    private int totalPages;
}
