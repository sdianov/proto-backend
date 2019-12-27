package com.example.demo.service

import com.example.demo.entities.ResourceTypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class DbSchemaService {

    @Autowired
    ResourceTypeRepository resourceTypeRepository;

    boolean typeRegistered(String typeName) {

        return resourceTypeRepository.getResourceTypeListByParent(0L).any { it.name == typeName };
    }

    List<ResourceTypeEntity> getResourceTypeEntities() {
        return resourceTypeRepository.getResourceTypeListByParent(0L);
    }
}
