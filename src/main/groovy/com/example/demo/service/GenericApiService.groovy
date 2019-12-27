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
class GenericApiService {

    @Autowired
    GenericItemRepository genericItemRepository;

    @Autowired
    ResourceTypeRepository resourceTypeRepository;

    @Autowired
    DbSchemaService dbSchemaService;

    private void checkFields(Long parentTypeId, String typeName, Map object) {

        def keys = new ArrayList<>(object.keySet());
        keys.remove("id");
        def fields = dbSchemaService.getTypeFields(parentTypeId, typeName).collect { it.name };

        if (!keys.every { fields.contains(it) }) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unknown fields")

        }
    }

    Object processRequest(RequestData requestData) {
        long parentId = 0;
        long parentTypeId = 0;
        for (def nextFragment : requestData.pathFragments.init()) {
            def nextType = resourceTypeRepository.getByParentAndName(parentTypeId, nextFragment.resourceType);
            def nextItem = genericItemRepository.getItem(nextType.id, nextFragment.resourceId, parentId);
            parentTypeId = nextType.id;
            parentId = nextItem.id;
        }

        def fragment = requestData.pathFragments.last();
        def fragmentType = resourceTypeRepository.getByParentAndName(parentTypeId, fragment.resourceType);

        if ("GET".equals(requestData.method)) {
            if (fragment.resourceId == null) {

                def expression = new QueryExpression(requestData.query);

                def items = genericItemRepository.getItems(fragmentType.id, parentId);
                def parser = new JsonSlurper();
                return items.collect { it ->
                    parser.parseText(it.jsonData)
                }.findAll { expression.matches(it) };

            } else {
                def item = genericItemRepository.getItem(fragmentType.id, fragment.resourceId, parentId);
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
            checkFields(parentTypeId, fragment.resourceType, json);

            def item = new GenericItemEntity(resourceTypeId: fragmentType.id,
                    itemId: id,
                    jsonData: JsonOutput.toJson(json),
                    parentId: parentId);
            return genericItemRepository.putItem(item);
        }

        if ("DELETE".equals(requestData.method)) {
            if (fragment.resourceId == null) {
                throw new IllegalArgumentException("invalid parameters");
            }

            def item = genericItemRepository.getItem(fragmentType.id, fragment.resourceId);
            genericItemRepository.deleteItem(item);
        }

        if ("PUT".equals(requestData.method)) {
            if (fragment.resourceId == null) {
                throw new IllegalArgumentException("invalid parameters");
            }
            def json = new JsonSlurper().parseText(requestData.body) as Map;
            checkFields(parentTypeId, fragment.resourceType, json);

            def item = genericItemRepository.getItem(fragmentType.id, fragment.resourceId);

            item.setJsonData(JsonOutput.toJson(json));
            genericItemRepository.putItem(item);
            return requestData.body;
        }

    }
}
