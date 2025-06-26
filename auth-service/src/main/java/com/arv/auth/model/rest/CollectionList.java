package com.arv.auth.model.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionList<T> {
    List<T> list;
    Integer currentPage;
    Integer totalCount;
    Integer totalPages;
}
