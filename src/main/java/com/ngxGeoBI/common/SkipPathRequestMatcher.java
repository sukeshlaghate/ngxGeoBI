/**
 * Created by : Sukesh Laghate
 * Created on : 09-01-2018
 **/
package com.ngxGeoBI.common;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public class SkipPathRequestMatcher implements RequestMatcher {
    private OrRequestMatcher matchers;
    private RequestMatcher processingMatcher;

    public SkipPathRequestMatcher(List<String> pathsToSkip, String processingPath) {

        if ( !pathsToSkip.isEmpty( ) ) {
            List<RequestMatcher> m = pathsToSkip.stream().map(path -> new AntPathRequestMatcher(path)).collect(Collectors.toList());
            matchers = new OrRequestMatcher(m);
            processingMatcher = new AntPathRequestMatcher(processingPath);
        }
    }

    @Override
    public boolean matches(HttpServletRequest httpServletRequest) {

        // simply return false for the paths that need to be skipped this way JWT TOken will not be required for those paths.
        if (matchers.matches(httpServletRequest)) {
            return false;
        }
        return processingMatcher.matches(httpServletRequest) ? true : false;

    }
}
