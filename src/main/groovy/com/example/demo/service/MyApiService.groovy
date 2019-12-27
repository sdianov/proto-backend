package com.example.demo.service

import com.example.demo.entities.GenericItemEntity
import com.example.demo.util.QueryExpression
import com.example.demo.util.RequestData
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
@CompileStatic
class MyApiService {

    @Autowired
    GenericItemRepository genericItemRepository;

    @Autowired
    DbSchemaService dbSchemaService;

    private void checkTypeRegistered(List<String> typePath) {
        if (!dbSchemaService.typeRegistered(typePath)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not registered: $typePath");
        }
    }

    Object processRequest(RequestData requestData) {
        long parentId = 0;
        for (def nextFragment : requestData.pathFragments.init()) {
            def nextItem = genericItemRepository.getItem(nextFragment.resourceType, nextFragment.resourceId, parentId);
            parentId = nextItem.id;
        }

        def fragment = requestData.pathFragments.last();
        def typePath = requestData.pathFragments.collect { it.resourceType };
        checkTypeRegistered(typePath);

        if ("GET".equals(requestData.method)) {
            if (fragment.resourceId == null) {

                def expression = new QueryExpression(requestData.query);

                def items = genericItemRepository.getItems(fragment.resourceType, parentId);
                def parser = new JsonSlurper();
                return items.collect { it ->
                    parser.parseText(it.jsonData)
                }.findAll { expression.matches(it) };

            } else {
                def item = genericItemRepository.getItem(fragment.resourceType, fragment.resourceId, parentId);
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

            def item = new GenericItemEntity(itemType: fragment.resourceType,
                    itemId: id,
                    jsonData: JsonOutput.toJson(json),
                    parentId: parentId);
            return genericItemRepository.putItem(item);
        }

        if ("DELETE".equals(requestData.method)) {
            if (fragment.resourceId == null) {
                throw new IllegalArgumentException("invalid parameters");
            }

            def item = genericItemRepository.getItem(fragment.resourceType, fragment.resourceId);
            genericItemRepository.deleteItem(item);
        }

        if ("PUT".equals(requestData.method)) {
            if (fragment.resourceId == null) {
                throw new IllegalArgumentException("invalid parameters");
            }

            def item = genericItemRepository.getItem(fragment.resourceType, fragment.resourceId);

            item.setJsonData(requestData.body);
            genericItemRepository.putItem(item);
            return requestData.body;
        }

    }
}
