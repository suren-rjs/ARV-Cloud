package com.arv.auth.repository.data;

import com.arv.auth.model.document.DeviceToken;

import java.util.List;
import java.util.Optional;

public interface DeviceTokenDto {
    void save(DeviceToken deviceToken);

    void delete(String id);

    void update(DeviceToken deviceToken);

    Optional<DeviceToken> findOne(String id);

    List<DeviceToken> findAll();
}
