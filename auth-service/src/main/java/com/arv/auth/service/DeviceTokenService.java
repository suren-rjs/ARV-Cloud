package com.arv.auth.service;

import com.arv.auth.model.document.DeviceToken;
import com.arv.auth.repository.crud.DeviceTokenRepository;
import com.arv.auth.repository.data.DeviceTokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceTokenService implements DeviceTokenDto {

    @Autowired
    protected DeviceTokenRepository deviceTokenRepository;

    @Override
    public void save(DeviceToken deviceToken) {
        Optional<DeviceToken> token = deviceTokenRepository.findOneByUsername(deviceToken.getUsername());
        if (token.isPresent()) {
            token.get().setDeviceToken(deviceToken.getDeviceToken());
            deviceTokenRepository.save(token.get());
        } else
            deviceTokenRepository.save(deviceToken);
    }

    @Override
    public void delete(String id) {
        deviceTokenRepository.deleteById(id);
    }

    @Override
    public void update(DeviceToken deviceToken) {
        deviceTokenRepository.save(deviceToken);
    }

    @Override
    public Optional<DeviceToken> findOne(String id) {
        return deviceTokenRepository.findById(id);
    }

    @Override
    public List<DeviceToken> findAll() {
        return deviceTokenRepository.findAll();
    }
}
