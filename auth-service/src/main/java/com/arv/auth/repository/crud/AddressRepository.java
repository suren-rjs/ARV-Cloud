package com.arv.auth.repository.crud;

import com.arv.auth.model.document.Address;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends MongoRepository<Address, String> {
    void deleteByIdAndUserId(String id, String userId);

    void deleteAllByUserId(String id);

    List<Address> findAllByUserId(String id);

    <S extends Address> Optional<S> findOneByIdAndUserId(String id, String userId);
}
