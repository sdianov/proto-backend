package com.example.demo.rest;

import com.example.demo.entities.ResourceTypeEntity;
import com.example.demo.service.ResourceFieldRepository;
import com.example.demo.service.ResourceTypeRepository;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/meta")
public class MetadataResource {

    private final ResourceTypeRepository resourceTypeRepository;
    private final ResourceFieldRepository resourceFieldRepository;

    @Inject
    public MetadataResource(ResourceTypeRepository resourceTypeRepository,
                            ResourceFieldRepository resourceFieldRepository) {

        this.resourceTypeRepository = resourceTypeRepository;
        this.resourceFieldRepository = resourceFieldRepository;
    }

    @GET
    @Path("/types")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ResourceTypeEntity> getResourceTypeEntities() {
        List<ResourceTypeEntity> result = resourceTypeRepository.getResourceTypeListByParent(0L);
        return result;
    }

    @POST
    @Path("/types")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResourceTypeEntity addTypeEntity(ResourceTypeEntity entity) {
        // TODO: check existing
        resourceTypeRepository.addResourceType(entity);
        return entity;
    }

    @PUT
    @Path("/types/{typeId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResourceTypeEntity putTypeEntity(
            @PathParam("typeId") @NotNull long typeId,
            ResourceTypeEntity entity) {

        // TODO: check existing
        resourceTypeRepository.putResourceType(typeId, entity);
        return entity;
    }

}
