package hasoffer.adp.admin.web.context;

import java.util.*;

/**
 * Created by glx on 2015/3/23.
 */
public class Session {

    private String id;
    private Map<String, Object> map = new HashMap<String, Object>();

    public Session(String id) {
        this.id = id;
    }

    public <T> T getAttribute(String key) {
        return (T) map.get(key);
    }

    public <T> T getAttribute(String key, T defaultValue) {
        T value = (T) map.get(key);
        if (value == null) {
            return defaultValue;
        } else {
            return value;
        }
    }

    public <T> void setAttribute(String key, T value) {
        String attributeName = key;
        T oldAttribute = (T) map.get(key);
        T newAttribute = value;
        map.put(key, value);
    }

    public List<String> getAttributeNames() {
        Set<String> keySet = map.keySet();
        List<String> names = new ArrayList<String>();
        names.addAll(keySet);
        return names;
    }

    public String getId() {
        return id;
    }
}
