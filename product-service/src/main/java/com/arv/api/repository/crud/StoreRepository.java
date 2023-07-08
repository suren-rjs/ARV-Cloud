package com.arv.api.repository.crud;

import com.arv.api.model.document.StoreInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StoreRepository extends MongoRepository<StoreInfo, String> {

    <S extends StoreInfo> Optional<S> findOneById(String id);

    Optional<StoreInfo> findOneByName(String name);

    Page<StoreInfo> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
