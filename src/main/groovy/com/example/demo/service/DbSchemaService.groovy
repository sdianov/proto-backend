package com.example.demo.service

import com.example.demo.entities.ResourceFieldEntity
import com.example.demo.entities.ResourceTypeEntity
import groovy.transform.CompileStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@CompileStatic
class DbSchemaService {

    @Autowired
    ResourceTypeRepository resourceTypeRepository;

    boolean typeRegistered(List<String> typePath) {

        def roots = resourceTypeRepository.getResourceTypeListByParent(0L);
        for (String typeName : typePath) {
            def next = roots.find() { it.name == typeName };
            if (next == null) {
                return false;
            }
            roots = next.children;
        }
        return true;
    }

    List<ResourceTypeEntity> getRootResourceTypeEntities() {
        return resourceTypeRepository.getResourceTypeListByParent(0L);
    }

    def List<ResourceFieldEntity> getTypeFields(Long parentId, String typeName) {
        return resourceTypeRepository.getByParentAndName(parentId, typeName).fieldList;
    }
}
