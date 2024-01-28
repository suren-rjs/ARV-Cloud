package com.arv.auth.repository.data;

import com.arv.auth.model.document.Config;
import com.arv.auth.model.dto.request.auth.ConfigDTO;

import java.util.List;

public interface IConfigDTO {
    Config findByKeyContainingIgnoreCase(String key);
    Config updateByKey(ConfigDTO configDTO);
    Config save(ConfigDTO configDTO);
    List<Config> findAll();
}