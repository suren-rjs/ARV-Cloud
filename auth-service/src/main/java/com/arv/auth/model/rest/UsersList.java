package com.arv.auth.model.rest;

import com.arv.auth.model.document.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersList {
    List<User> list;
    Integer currentPage;
    Integer totalCount;
    Integer totalPages;
}
