package org.openiam.webconsole.web.resolver;

import org.openiam.webconsole.web.constant.CommonWebConstant;
import org.openiam.webconsole.web.model.UserSessionModel;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * User: Alexander Duckardt<br/>
 * Date: 09/06/12
 */
public class UserSessionArgumentResolver implements
        HandlerMethodArgumentResolver {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.web.method.support.HandlerMethodArgumentResolver#
     * supportsParameter(org.springframework.core.MethodParameter)
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return UserSessionModel.class.isAssignableFrom(parameter
                .getParameterType());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.web.method.support.HandlerMethodArgumentResolver#
     * resolveArgument(org.springframework.core.MethodParameter,
     * org.springframework.web.method.support.ModelAndViewContainer,
     * org.springframework.web.context.request.NativeWebRequest,
     * org.springframework.web.bind.support.WebDataBinderFactory)
     */
    @Override
    public Object resolveArgument(MethodParameter parameter,
            ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        return webRequest.getAttribute(CommonWebConstant.userSession.name(),
                WebRequest.SCOPE_SESSION);
    }

}
