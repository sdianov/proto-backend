package com.example.demo.service

import com.example.demo.entities.GenericItemEntity
import com.example.demo.util.QueryExpression
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@CompileStatic
class MyApiService {

    @Autowired
    GenericItemRepository genericItemRepository;

    @Autowired
    DbSchemaService dbSchemaService;

    private void checkTypeRegistered(String typeName) {
        if (!dbSchemaService.typeRegistered(typeName)) {
            throw new IllegalArgumentException("Resource not registered: $typeName");
        }
    }

    Object processRequest(String method, String[] path, String body, String query) {

        if ("GET".equals(method)) {
            if (path.length > 2) {
                throw new IllegalArgumentException("invalid parameters");
            }
            if (path.length == 1) {

                def expression = new QueryExpression(query);

                checkTypeRegistered(path[0]);

                def items = genericItemRepository.getItems(path[0]);
                def parser = new JsonSlurper();
                return items.collect { it ->
                    parser.parseText(it.jsonData)
                }.findAll { expression.matches(it) };

            } else {
                checkTypeRegistered(path[0]);

                def item = genericItemRepository.getItem(path[0], path[1]);
                return item.jsonData;
            }
        }

        if ("POST".equals(method)) {
            if (path.length > 1) {
                throw new IllegalArgumentException("invalid parameters");
            }
            def json = new JsonSlurper().parseText(body) as Map;
            def id = json.get('id') as String;
            if (id == null) {
                throw new IllegalArgumentException("no Id");
            }

            checkTypeRegistered(path[0]);

            def item = new GenericItemEntity(itemType: path[0], itemId: id, jsonData: JsonOutput.toJson(json));
            return genericItemRepository.putItem(item);
        }

        if ("DELETE".equals(method)) {
            if (path.length > 2) {
                throw new IllegalArgumentException("invalid parameters");
            }
            checkTypeRegistered(path[0]);

            def item = genericItemRepository.getItem(path[0], path[1]);
            genericItemRepository.deleteItem(item);
        }

        if ("PUT".equals(method)) {
            if (path.length > 2) {
                throw new IllegalArgumentException("invalid parameters");
            }
            checkTypeRegistered(path[0]);

            def item = genericItemRepository.getItem(path[0], path[1]);

            item.setJsonData(body);
            genericItemRepository.putItem(item);
            return body;
        }

    }
}
