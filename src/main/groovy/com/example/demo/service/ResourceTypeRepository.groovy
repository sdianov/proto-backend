package com.example.demo.service

import com.example.demo.entities.ResourceTypeEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.web.server.ResponseStatusException

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@Repository
@Transactional
class ResourceTypeRepository {

    public static final ResourceTypeEntity ROOT_ENTITY = new ResourceTypeEntity(0L);

    @PersistenceContext
    EntityManager entityManager;

    List<ResourceTypeEntity> getResourceTypeListByParent(Long parentId) {
        return entityManager
                .createQuery("select e from ResourceTypeEntity e " +
                        " where e.parent.id = :parentId and e.id <> 0 ",
                        ResourceTypeEntity.class)
                .setParameter("parentId", parentId)
                .resultList
    }

    ResourceTypeEntity getByParentAndName(Long parentId, String name) {
        return entityManager
                .createQuery("select e from ResourceTypeEntity e " +
                        " where e.parent.id = :parentId and e.name = :name ",
                        ResourceTypeEntity.class)
                .setParameter("parentId", parentId)
                .setParameter("name", name)
                .singleResult;
    }

    ResourceTypeEntity addResourceType(ResourceTypeEntity entity) {
        if (entity.parent == null) {
            entity.parent = ROOT_ENTITY;
        }
        entityManager.persist(entity);
        return entity;
    }

    ResourceTypeEntity putResourceType(long typeId, ResourceTypeEntity entity) {

        def existing = entityManager.find(ResourceTypeEntity, typeId);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no type");
        }



        entityManager.persist(entity);
        return entity;
    }
}
