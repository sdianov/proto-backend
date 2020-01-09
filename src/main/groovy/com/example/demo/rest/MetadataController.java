package com.example.demo.rest;

import com.example.demo.entities.ResourceTypeEntity;
import com.example.demo.service.ResourceFieldRepository;
import com.example.demo.service.ResourceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/meta")
public class MetadataController {

    private final ResourceTypeRepository resourceTypeRepository;
    private final ResourceFieldRepository resourceFieldRepository;

    @Autowired
    public MetadataController(ResourceTypeRepository resourceTypeRepository,
                              ResourceFieldRepository resourceFieldRepository) {

        this.resourceTypeRepository = resourceTypeRepository;
        this.resourceFieldRepository = resourceFieldRepository;
    }

    @RequestMapping(value = "/types", method = {
            RequestMethod.GET
    }, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ResourceTypeEntity> getResourceTypeEntities() {
        return resourceTypeRepository.getResourceTypeListByParent(0L);
    }

    @RequestMapping(value = "/types", method = {
            RequestMethod.POST
    }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResourceTypeEntity addTypeEntity(@RequestBody ResourceTypeEntity entity) {
        // TODO: check existing
        resourceTypeRepository.addResourceType(entity);
        return entity;
    }

    @RequestMapping(value = "/types/{typeId}", method = {
            RequestMethod.PUT
    }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResourceTypeEntity putTypeEntity(
            @PathVariable("typeId") long typeId,
            @RequestBody ResourceTypeEntity entity) {
        // TODO: check existing
        resourceTypeRepository.putResourceType(typeId, entity);
        return entity;
    }


}
