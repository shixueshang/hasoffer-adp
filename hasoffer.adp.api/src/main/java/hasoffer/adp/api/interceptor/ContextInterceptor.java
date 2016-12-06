package hasoffer.adp.api.interceptor;

import hasoffer.adp.api.context.Context;
import hasoffer.adp.api.context.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class ContextInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //以下init顺序不能变
        Context.currentContext().init();

        this.initHeaders(httpServletRequest);
        this.initCookie(httpServletRequest, httpServletResponse);
        this.initSession(httpServletRequest);
        this.initToken(httpServletRequest);

        return true;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {

        }
    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception ex)
            throws Exception {
        if (ex != null) {

        }
    }

    private void initToken(HttpServletRequest httpRequest) {

    }

    private void initHeaders(HttpServletRequest httpServletRequest) {
        Map<String, String> headerMap = new HashMap<String, String>();
        Enumeration enumeration = httpServletRequest.getHeaderNames();
        Object e = enumeration.nextElement();
        while (e != null) {
            String hs = httpServletRequest.getHeader(e.toString());
            headerMap.put(e.toString(), hs);
            e = enumeration.nextElement();
        }

        Context.currentContext().setHeaders(headerMap);
    }

    private void initCookie(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse) {
        Cookie[] cookies = httpRequest.getCookies();
        if (cookies == null) {
            return;
        }
        for (Cookie cookie : cookies) {
            Context.currentContext().setCookie(cookie.getName(), cookie.getValue());
        }
    }

    private void initSession(HttpServletRequest httpRequest) {
        final HttpSession httpSession = httpRequest.getSession();
        Session session = new Session(httpSession.getId());

        Enumeration<String> attributeNames = httpSession.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            session.setAttribute(attributeName, httpSession.getAttribute(attributeName));
        }
    }
}