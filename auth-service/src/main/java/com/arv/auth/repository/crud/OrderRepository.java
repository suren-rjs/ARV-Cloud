package com.arv.auth.repository.crud;

import com.arv.auth.model.document.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
    Page<Order> findAllByUserId(String id, Pageable pageable);
    Boolean isExistsByIdAndUserId(String id, String userId);
}
