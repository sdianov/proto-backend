package com.example.demo.service

import com.example.demo.dto.BatchRequest
import com.example.demo.entities.GenericItemEntity
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Service
@Transactional
@CompileStatic
class GenericItemRepository {

    @PersistenceContext
    EntityManager entityManager;

    List<GenericItemEntity> getItems(String itemType/* TODO: query */) {

        def query = entityManager.createQuery("select e from GenericItemEntity e" +
                " where e.itemType = :itemType ", GenericItemEntity);

        query.setParameter("itemType", itemType);
        return query.resultList;
    }

    GenericItemEntity getItem(String itemType, String itemId) {

        def query = entityManager.createQuery("select e from GenericItemEntity e" +
                " where e.itemType = :itemType" +
                " and e.itemId = :itemId ", GenericItemEntity);

        query.setParameter("itemType", itemType);
        query.setParameter("itemId", itemId);
        return query.singleResult;
    }

    GenericItemEntity putItem(GenericItemEntity item) {

        if (item.itemId == null) {
            throw new IllegalArgumentException("no 'itemId' field");
        }
        if (item.itemType == null) {
            throw new IllegalArgumentException("no 'itemType' field");
        }
        entityManager.persist(item);

        return item;
    }

    void deleteItem(GenericItemEntity itemEntity) {
        entityManager.remove(itemEntity);
    }

    void batchUpdate(BatchRequest batchRequest) {

        for (def item : batchRequest.batchItems) {
            item.operation;
        }
    }
}
