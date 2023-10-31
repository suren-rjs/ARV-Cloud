package com.arv.auth.controller;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.document.Employee;
import com.arv.auth.model.rest.EmployeeList;
import com.arv.auth.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = "/employee", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeController {
    @Value("${product.pageSize}")
    private int PAGE_COUNT;
    @Autowired
    protected EmployeeService employeeService;


    @Autowired
    protected GlobalCommonService globalCommonService;

    @GetMapping("/all")
    private ResponseEntity<?> getAll(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "") String keyword, @RequestParam(required = false, defaultValue = "false") Boolean isSearch) {
        Pageable pageable = PageRequest.of(page, PAGE_COUNT);
        if (isSearch) {
            Page<Employee> employees = employeeService.searchByNameContainingIgnoreCasePhoneContaining(keyword, pageable);
            return globalCommonService.getResponseOfObject(Optional.of(new EmployeeList(employees.getContent(), page, employees.getNumberOfElements(), globalCommonService.getTotalPage(employees.getTotalPages()))), HttpStatus.OK);
        } else {
            return globalCommonService.getResponseOfObject(Optional.of(employeeService.searchByNameContainingIgnoreCasePhoneContaining(keyword)), HttpStatus.OK);
        }
    }

    @PostMapping
    private ResponseEntity<?> add(@RequestBody Employee employee) {
        return globalCommonService.getResponseOfObject(Optional.of(employeeService.save(employee)), HttpStatus.OK);
    }

    @PutMapping
    private ResponseEntity<?> update(@RequestBody Employee employee) {
        return globalCommonService.getResponseOfObject(Optional.of(employeeService.save(employee)), HttpStatus.OK);
    }

    @DeleteMapping
    private ResponseEntity<?> delete(@RequestParam String id) {
        HttpStatus status = HttpStatus.OK;
        String message = "Employee deletion success !";
        try {
            employeeService.deleteOneById(id);
        } catch (Exception e) {
            status = HttpStatus.NOT_ACCEPTABLE;
            message = e.getMessage();
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }
}
