package com.arv.auth.repository.data;

import com.arv.auth.model.document.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IEmployeeDto {
    Page<Employee> findAll(Pageable pageable);

    Page<Employee> searchByNameContainingIgnoreCasePhoneContaining(String name, Pageable pageable);

    List<Employee> searchByNameContainingIgnoreCasePhoneContaining(String name);

    List<Employee> findAll();

    Optional<Employee> findOneById(String id);

    void deleteOneById(String id);

    Employee save(Employee employee);
}
