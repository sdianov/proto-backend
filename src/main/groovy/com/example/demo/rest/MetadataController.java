package com.example.demo.rest;

import com.example.demo.entities.ResourceTypeEntity;
import com.example.demo.service.ResourceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/meta")
public class MetadataController {

    private final ResourceTypeRepository resourceTypeRepository;

    @Autowired
    public MetadataController(ResourceTypeRepository resourceTypeRepository) {
        this.resourceTypeRepository = resourceTypeRepository;
    }

    @RequestMapping(value = "/types", method = {
            RequestMethod.GET
    }, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ResourceTypeEntity> getResourceTypeEntities() {
       return resourceTypeRepository.getResourceTypeListByParent(0L);
    }


}
