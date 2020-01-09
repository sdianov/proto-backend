package com.example.demo.config;

import com.example.demo.rest.MetadataResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {

        register(MetadataResource.class);
    }
}
