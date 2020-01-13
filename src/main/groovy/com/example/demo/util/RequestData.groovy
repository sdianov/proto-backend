package com.example.demo.util

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import org.apache.commons.io.IOUtils

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.core.PathSegment
import javax.ws.rs.core.UriInfo
import java.util.stream.Collectors

@Canonical
class PathFragment {
    String resourceType; // not null
    String resourceId;

    @Override
    String toString() {
        return "$resourceType:$resourceId";
    }
}

@CompileStatic
class RequestData {

    final static int PATH_PREFIX_SIZE = 3;

    String method;
    List<PathFragment> pathFragments;
    Object body;
    String query;

    int nestedLevels;

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

    private static List<PathFragment> segmentsToFragments(List<PathSegment> segments) {
        List<PathFragment> result = new ArrayList<>();

        for (int i = 0; i < segments.size() / 2; i++) {
            result.add(new PathFragment(
                    resourceType: segments.get(i * 2).path,
                    resourceId: segments.size() <= i * 2 + 1 ? null : segments.get(i * 2 + 1).path
            ));
        }
        return result;
    }

    static RequestData fromParams(String method, UriInfo uriInfo, String query, Object body) {
        List<PathSegment> segments = uriInfo.getPathSegments().stream().skip(2).collect(Collectors.toList());
        List<PathFragment> fragments = segmentsToFragments(segments);
        RequestData req = new RequestData();
        req.setBody(body);
        req.setQuery(query);
        req.setMethod(method);
        req.setPathFragments(fragments);

        return req;
    }

}
