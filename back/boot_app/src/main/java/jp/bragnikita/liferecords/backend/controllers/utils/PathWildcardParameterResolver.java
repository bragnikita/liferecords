package jp.bragnikita.liferecords.backend.controllers.utils;

import org.apache.logging.log4j.util.Strings;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class PathWildcardParameterResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(PathWildcard.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.state(request != null, "No HttpServletRequest");
        String requestUri = request.getRequestURI();
        String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        return resolveDoubleAsteriskPattern(requestUri, pattern);
    }

    static String resolveDoubleAsteriskPattern(String requestUri, String pattern) {
        String [] patternSegments = StringUtils.tokenizeToStringArray(pattern, "/");

        int prefixLength = -1;
        int suffixLength = 0;
        for (int i = 0; i < patternSegments.length; i++) {
            if (patternSegments[i].equals("**")) {
                prefixLength = i;
                suffixLength = patternSegments.length - (prefixLength + 1);
                break;
            }
        }
        if (prefixLength == -1) {
            return "";
        }

        String[] uriSegments = StringUtils.tokenizeToStringArray(requestUri, "/");
        if (uriSegments.length < prefixLength + suffixLength) {
            return "";
        }
        if (uriSegments.length == prefixLength + suffixLength) {
            return requestUri;
        }
        return Strings.join(Arrays.asList(uriSegments)
                .subList(prefixLength, uriSegments.length - suffixLength),
                '/');
    }
}


