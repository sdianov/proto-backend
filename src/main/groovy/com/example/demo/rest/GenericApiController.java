package com.example.demo.rest;


import com.example.demo.dto.BatchResponse;
import com.example.demo.service.GenericApiService;
import com.example.demo.util.RequestData;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api")
public class GenericApiController {

    private final GenericApiService genericApiService;

    @Inject
    public GenericApiController(GenericApiService genericApiService) {
        this.genericApiService = genericApiService;
    }

    @RequestMapping(value = "/rest/**", method = {
            RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE
    }, produces = MediaType.APPLICATION_JSON_VALUE)
    Object handleApi(HttpServletRequest req) {

        return genericApiService.processRequest(RequestData.fromHttp(req));
    }

    @RequestMapping(value = "/batch/**", method = {RequestMethod.POST},
            produces = MediaType.APPLICATION_JSON_VALUE)
    BatchResponse handleBatchApi(HttpServletRequest req) {
        return new BatchResponse();
    }

}
