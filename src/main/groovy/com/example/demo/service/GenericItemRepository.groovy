package com.example.demo.service

import com.example.demo.dto.BatchRequest
import com.example.demo.entities.GenericItemEntity
import groovy.transform.CompileStatic
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Service
@CompileStatic
class GenericItemRepository {

    @PersistenceContext
    EntityManager entityManager;

    List<GenericItemEntity> getItems(Long itemTypeId/* TODO: query */, Long parentId = 0) {

        def query = entityManager.createQuery("select e from GenericItemEntity e" +
                " where e.resourceTypeId = :itemTypeId " +
                " and e.parentId = :parentId ", GenericItemEntity);

        query.setParameter("itemTypeId", itemTypeId);
        query.setParameter("parentId", parentId);
        return query.resultList;
    }

    GenericItemEntity getItem(Long itemTypeId, String itemId, Long parentId = 0) {

        def query = entityManager.createQuery("select e from GenericItemEntity e" +
                " where e.resourceTypeId = :itemTypeId" +
                " and e.itemId = :itemId " +
                " and e.parentId = :parentId ", GenericItemEntity);

        query.setParameter("itemTypeId", itemTypeId);
        query.setParameter("itemId", itemId);
        query.setParameter("parentId", parentId);
        return query.singleResult;
    }

    GenericItemEntity putItem(GenericItemEntity item) {

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
