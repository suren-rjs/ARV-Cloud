package com.arv.auth.service;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.document.Config;
import com.arv.auth.model.dto.request.auth.ConfigDTO;
import com.arv.auth.repository.crud.IConfig;
import com.arv.auth.repository.data.IConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigService implements IConfigDTO {

    @Autowired protected IConfig configRepository;

    @Autowired
    protected GlobalCommonService globalCommonService;
    @Override
    public Config findByKeyContainingIgnoreCase(String key) {
        Optional<Config> config = configRepository.findByKeyContainingIgnoreCase(key);
        return config.orElseThrow(() -> new NotFoundException("Key not found"));
    }

    @Override
    public Config updateByKey(ConfigDTO configDTO) {
        Config config = findByKeyContainingIgnoreCase(configDTO.getKey());
        config.setValue(configDTO.getValue());
        return configRepository.save(config);
    }

    @Override
    public Config save(ConfigDTO configDTO) {
        Config config = (Config) globalCommonService.convertObject(configDTO, Config.class);
        return configRepository.save(config);
    }

    @Override
    public List<Config> findAll() {
        return configRepository.findAll();
    }
}
