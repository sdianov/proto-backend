package com.example.demo.rest;

import com.example.demo.dto.BatchResponse;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api/batch")
public class BatchApiResource {

    @Path(value = "**")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public BatchResponse handleBatchApi(HttpServletRequest req) {
        return new BatchResponse();
    }

}
