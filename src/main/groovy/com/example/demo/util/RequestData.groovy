package com.example.demo.util

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import org.apache.commons.io.IOUtils

import javax.servlet.http.HttpServletRequest


@Canonical
class PathFragment {
    String resourceType; // not null
    String resourceId;
}

@CompileStatic
class RequestData {

    final static int PATH_PREFIX_SIZE = 3;

    String method;
    List<PathFragment> pathFragments;
    String body;
    String query;

    static List<PathFragment> pathToFragments(String[] path) {
        List<PathFragment> result = new ArrayList<>();
        for (int i = 0; i < path.length / 2; i++) {
            result.add(new PathFragment(
                    resourceType: path[i * 2],
                    resourceId: path.length <= i * 2 + 1 ? null : path[i * 2 + 1]
            ));
        }
        return result;
    }

    static RequestData fromHttp(HttpServletRequest req) {
        String[] path = req.getServletPath().split("/");

        if (path.length < PATH_PREFIX_SIZE) {
            throw new IllegalArgumentException("wrong path");
        }
        path = Arrays.copyOfRange(path, PATH_PREFIX_SIZE, path.length);

        String body = IOUtils.toString(req.getReader());

        return new RequestData(method: req.getMethod(),
                pathFragments: pathToFragments(path),
                body: body,
                query: req.getParameter("query"));
    }
}
