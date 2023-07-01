package com.arv.auth.repository.data;

import com.arv.auth.model.document.Address;
import com.arv.auth.model.dto.request.address.AddressDto;

import java.util.List;
import java.util.Optional;

public interface AddressDtoRepository {
    void save(AddressDto addressDto);

    void update(Address address) throws RuntimeException;

    Optional<Address> findOneById(String id);

    List<Address> findAll();

    void delete(String id);

    void deleteAll();
}
