package com.example.demo.rest;


import com.example.demo.dto.BatchResponse;
import com.example.demo.service.MyApiService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping(path = "/api")
public class MyApiController {

    private final MyApiService myApiService;

    @Autowired
    public MyApiController(MyApiService myApiService) {
        this.myApiService = myApiService;
    }

    @RequestMapping(value = "/rest/**", method = {
            RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE
    }, produces = MediaType.APPLICATION_JSON_VALUE)
    Object handleApi(HttpServletRequest req) throws IOException {
        String[] path = req.getServletPath().split("/");

        if (path.length < 3 || !"rest".equals(path[2])) {
            throw new IllegalArgumentException("wrong path");
        }

        String body = IOUtils.toString(req.getReader());

        Object data = myApiService.processRequest(req.getMethod(),
                Arrays.copyOfRange(path, 3, path.length),
                body,
                req.getParameter("query"));

        return data;
    }

    @RequestMapping(value = "/batch/**", method = {RequestMethod.POST},
            produces = MediaType.APPLICATION_JSON_VALUE)
    BatchResponse handleBatchApi(HttpServletRequest req) throws IOException {
        return new BatchResponse();
    }

}
