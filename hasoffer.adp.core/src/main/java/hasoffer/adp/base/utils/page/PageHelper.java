package hasoffer.adp.base.utils.page;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class PageHelper {
    public static PageModel getPageModel(HttpServletRequest request, Page page) {
        PageModel pageModel = new PageModel();
        if (page != null) {
            pageModel.set(page);
        }
        pageModel.setRequestUrl(request.getRequestURI());

        Map<String, String> pageParams = new HashMap<String, String>();
        Map<String, String[]> params = request.getParameterMap();
        for (String key : params.keySet()) {
            if (key.equals("page") || key.equals("size")) {
                continue;
            }
            String[] ss = params.get(key);
            if (ss == null || ss.length != 1) {
                continue;
            }
            pageParams.put(key, ss[0]);
        }
        pageModel.setPageParams(pageParams);
        return pageModel;
    }
}
