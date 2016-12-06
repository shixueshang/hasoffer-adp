package hasoffer.adp.api.context;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;


public class Context {

    private static final ThreadLocal<Context> threadLocal = new ThreadLocal<Context>();

    private Map<String, Object> map = new HashMap<String, Object>();
    private Map<String, String> headers = new HashMap<String, String>();
    private Map<String, Cookie> cookies = new HashMap<String, Cookie>();


    private Context() {
    }

    public static Context currentContext() {
        if (threadLocal.get() == null) {
            Context context = new Context();
            threadLocal.set(context);
        }

        return threadLocal.get();
    }

    public void init() {
        map.clear();
        headers.clear();
        cookies.clear();
    }

    public void set(String name, Object value) {
        map.put(name, value);
    }

    public Object get(String name) {
        return get(name, null);
    }

    public String getHeader(String name) {
        return getHeader(name, null);
    }

    public String getHeader(String name, String defaultValue) {
        String val = headers.get(name);
        if (val == null) {
            return defaultValue;
        }
        return val;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Object get(String name, Object defaultVal) {
        Object obj = map.get(name);
        return obj == null ? defaultVal : obj;
    }

    public void setCookie(String name, String value) {
        if (value == null) {
            cookies.remove(name);
        } else {
            Cookie cookie = cookies.get(name);
            if (cookie != null) {
                cookie.setValue(value);
            } else {
                cookie = new Cookie(name, value);
                cookies.put(name, cookie);
            }
        }
    }

    public String getCookieAsString(String name, String defaultValue) {
        Cookie cookie = cookies.get(name);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return defaultValue;
        }
    }

    public String getCookieAsString(String name) {
        return this.getCookieAsString(name, "");
    }
}
