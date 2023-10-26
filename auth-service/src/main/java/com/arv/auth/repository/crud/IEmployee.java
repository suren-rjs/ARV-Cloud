package com.arv.auth.repository.crud;

import com.arv.auth.model.document.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEmployee extends MongoRepository<Employee, String> {
    Page<Employee> findByNameContainingIgnoreCaseOrPhoneContaining(String keyword, Pageable pageable);
    List<Employee> findByNameContainingIgnoreCaseOrPhoneContaining(String keyword);
}
