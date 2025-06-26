package com.arv.auth.service;

import com.arv.auth.model.document.Employee;
import com.arv.auth.repository.crud.IEmployee;
import com.arv.auth.repository.data.IEmployeeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements IEmployeeDto {
    @Autowired
    protected IEmployee iEmployee;

    @Override
    public Page<Employee> findAll(Pageable pageable) {
        return iEmployee.findAll(pageable);
    }

    @Override
    public Page<Employee> searchByNameContainingIgnoreCasePhoneContaining(String keyword, Pageable pageable) {
        return iEmployee.findByNameContainingIgnoreCaseOrPhoneContaining(keyword, keyword, pageable);
    }
    @Override
    public List<Employee> searchByNameContainingIgnoreCasePhoneContaining(String keyword) {
        return iEmployee.findByNameContainingIgnoreCaseOrPhoneContaining(keyword, keyword);
    }

    @Override
    public List<Employee> findAll() {
        return iEmployee.findAll();
    }

    @Override
    public Optional<Employee> findOneById(String id) {
        return iEmployee.findById(id);
    }

    @Override
    public void deleteOneById(String id) {
        iEmployee.findById(id).orElseThrow(() -> new RuntimeException("Not found!"));
        iEmployee.deleteById(id);
    }

    @Override
    public Employee save(Employee employee) {
        return iEmployee.save(employee);
    }
}
