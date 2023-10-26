package com.arv.auth.model.rest;

import com.arv.auth.model.document.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

public class EmployeeList extends CollectionList<Employee> {
    public EmployeeList(List<Employee> content, int page, int numberOfElements, int totalPage) {
        super();
    }
}
