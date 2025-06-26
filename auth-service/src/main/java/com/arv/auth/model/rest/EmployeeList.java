package com.arv.auth.model.rest;

import com.arv.auth.model.document.Employee;

import java.util.List;

public class EmployeeList extends CollectionList<Employee> {
    public EmployeeList(List<Employee> content, int page, int numberOfElements, int totalPage) {
        super.list = content;
        super.currentPage = page;
        super.totalCount = numberOfElements;
        super.totalPages = totalPage;
    }
}
