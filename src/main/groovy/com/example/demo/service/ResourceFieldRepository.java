package com.example.demo.service;

import com.example.demo.entities.ResourceFieldEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class ResourceFieldRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public ResourceFieldEntity addResourceField(ResourceFieldEntity entity) {

        entityManager.persist(entity);
        return entity;
    }

}
