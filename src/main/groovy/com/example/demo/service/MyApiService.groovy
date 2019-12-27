package com.example.demo.service

import com.example.demo.entities.GenericItemEntity
import com.example.demo.util.QueryExpression
import com.example.demo.util.RequestData
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

    Object processRequest(RequestData requestData) {
        def fragment = requestData.pathFragments.get(0);

        if ("GET".equals(requestData.method)) {
            if (fragment.resourceId == null) {

                def expression = new QueryExpression(requestData.query);

                checkTypeRegistered(fragment.resourceType);

                def items = genericItemRepository.getItems(fragment.resourceType);
                def parser = new JsonSlurper();
                return items.collect { it ->
                    parser.parseText(it.jsonData)
                }.findAll { expression.matches(it) };

            } else {
                checkTypeRegistered(fragment.resourceType);

                def item = genericItemRepository.getItem(fragment.resourceType, fragment.resourceId);
                return item.jsonData;
            }
        }

        if ("POST".equals(requestData.method)) {
            if (fragment.resourceId != null) {
                throw new IllegalArgumentException("invalid parameters");
            }
            def json = new JsonSlurper().parseText(requestData.body) as Map;
            def id = json.get('id') as String;
            if (id == null) {
                throw new IllegalArgumentException("no Id");
            }

            checkTypeRegistered(fragment.resourceType);

            def item = new GenericItemEntity(itemType: fragment.resourceType, itemId: id, jsonData: JsonOutput.toJson(json));
            return genericItemRepository.putItem(item);
        }

        if ("DELETE".equals(requestData.method)) {
            if (fragment.resourceId == null) {
                throw new IllegalArgumentException("invalid parameters");
            }
            checkTypeRegistered(fragment.resourceType);

            def item = genericItemRepository.getItem(fragment.resourceType, fragment.resourceId);
            genericItemRepository.deleteItem(item);
        }

        if ("PUT".equals(requestData.method)) {
            if (fragment.resourceId == null) {
                throw new IllegalArgumentException("invalid parameters");
            }
            checkTypeRegistered(fragment.resourceType);

            def item = genericItemRepository.getItem(fragment.resourceType, fragment.resourceId);

            item.setJsonData(requestData.body);
            genericItemRepository.putItem(item);
            return requestData.body;
        }

    }
}
