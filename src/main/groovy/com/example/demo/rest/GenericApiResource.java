package com.example.demo.rest;

import com.example.demo.service.GenericApiService;
import com.example.demo.util.RequestData;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/api/rest")
public class GenericApiResource {

    private final GenericApiService genericApiService;

    @Inject
    public GenericApiResource(GenericApiService genericApiService) {
        this.genericApiService = genericApiService;
    }

    @GET
    @Path("{var:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getResources(@Context UriInfo uriInfo, @QueryParam("query") String query) {

        RequestData req = RequestData.fromParams("GET", uriInfo, query, null);
        return genericApiService.processRequest(req);
    }

    @POST
    @Path("{var:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Object postResource(@Context UriInfo uriInfo, Object body) {

        RequestData req = RequestData.fromParams("POST", uriInfo, null, body);
        return genericApiService.processRequest(req);
    }

    @PUT
    @Path("{var:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Object putResource(@Context UriInfo uriInfo, Object body) {

        RequestData req = RequestData.fromParams("PUT", uriInfo, null, body);
        return genericApiService.processRequest(req);
    }

    @DELETE
    @Path("{var:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object deleteResource(@Context UriInfo uriInfo) {

        RequestData req = RequestData.fromParams("DELETE", uriInfo, null, null);
        return genericApiService.processRequest(req);
    }


}
