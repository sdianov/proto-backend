package com.example.demo.util

import groovy.transform.CompileStatic;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@CompileStatic
class RequestDataTest {

    @Test
    void shouldReturnFragments() {

        String[] path = "group/123/item/456".split("/");

        def result = RequestData.pathToFragments(path);

        PathFragment[] expected = [new PathFragment(resourceType: "group", resourceId: "123"),
                                   new PathFragment(resourceType: "item", resourceId: "456")];

        assertThat(result).containsExactlyInAnyOrder(expected);
    }

    @Test
    void shouldReturnFragmentsPartial() {

        String[] path = "group/123/item".split("/");

        def result = RequestData.pathToFragments(path);

        PathFragment[] expected = [new PathFragment(resourceType: "group", resourceId: "123"),
                                   new PathFragment(resourceType: "item")];

        assertThat(result).containsExactlyInAnyOrder(expected);
    }
}
