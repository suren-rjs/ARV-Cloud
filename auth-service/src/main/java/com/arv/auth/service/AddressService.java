package com.arv.auth.service;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.document.Address;
import com.arv.auth.model.document.User;
import com.arv.auth.model.dto.request.address.AddressDto;
import com.arv.auth.repository.crud.AddressRepository;
import com.arv.auth.repository.data.AddressDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class AddressService implements AddressDtoRepository {

    @Autowired
    private GlobalCommonService globalCommonService;
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public void save(AddressDto addressDto) {
        User user = globalCommonService.getCurrentUser();
        Address address = (Address) globalCommonService.convertObject(addressDto, Address.class);
        address.setUserId(user.getId());
        addressRepository.save(address);
    }

    @Override
    public void update(Address address) throws RuntimeException {
        Optional<Address> existing = findOneById(address.getId());
        if (existing.isPresent()) {
            addressRepository.save(address);
            return;
        }
        throw new RuntimeException("Invalid address id");
    }

    @Override
    public Optional<Address> findOneById(String id) {
        User user = globalCommonService.getCurrentUser();
        return addressRepository.findOneByIdAndUserId(id, user.getId());
    }

    @Override
    public List<Address> findAll() {
        User user = globalCommonService.getCurrentUser();
        return addressRepository.findAllByUserId(user.getId());
    }

    @Override
    public void delete(String id) {
        User user = globalCommonService.getCurrentUser();
        addressRepository.deleteByIdAndUserId(id, user.getId());
    }

    @Override
    public void deleteAll() {
        User user = globalCommonService.getCurrentUser();
        addressRepository.deleteAllByUserId(user.getId());
    }
}
