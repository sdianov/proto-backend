package com.example.demo.service

import com.example.demo.entities.ResourceTypeEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class ResourceTypeRepository {

    @PersistenceContext
    EntityManager entityManager;

    List<ResourceTypeEntity> getResourceTypeList() {
        return entityManager
                .createQuery("select e from ResourceTypeEntity e ", ResourceTypeEntity.class)
                .resultList
    }
}
