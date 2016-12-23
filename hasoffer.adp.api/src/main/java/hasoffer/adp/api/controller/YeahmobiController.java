package hasoffer.adp.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hasoffer.data.redis.IRedisMapService;
import hasoffer.data.redis.IRedisService;
import hasoffer.site.helper.FlipkartHelper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lihongde on 2016/12/21 17:59
 */
@RestController
@RequestMapping(value = "/ym")
public class YeahmobiController extends BaseController {

    @Resource
    IRedisService redisService;

    @Resource
    IRedisMapService redisMapService;

    ObjectMapper mapper = new ObjectMapper();

    /**
     * 提供获得广告素材接口
     * @param country
     * @param androidid
     * @param gaid google advertiser id
     * @param width
     * @param height
     * @return
     */
    @RequestMapping(value = "/getAd", method = RequestMethod.GET)
    public Map<String, Object> bidForYeahmobi(@RequestParam(value = "country", defaultValue = "IN") String country,
                                              @RequestParam(value = "aid", required = false) String androidid,
                                              @RequestParam(value = "gaid", required = false) String gaid,
                                              @RequestParam(value = "imgw") int width,
                                              @RequestParam(value = "imgh") int height){

        String msg = "No matching material found";
        Map<String, Object> result = new ConcurrentHashMap<>();
        if(StringUtils.isEmpty(androidid)){
            result.put("error_msg" ,msg);
            return result;
        }

        Object eq = redisMapService.getValue("AIDTAGMAP", androidid);
        if(eq == null){
            result.put("error_msg" ,msg);
            return result;
        }

        String[] tags = eq.toString().split(",");
        Object mids = redisMapService.getValue("MATTAGMAP", tags[0]);
        if (mids == null) {
            result.put("error_msg" ,msg);
            return result;
        }

        String mid = mids.toString().split(",")[0];

        Object m = redisMapService.getValue("MRESULT", mid);
        if (m == null) {
            result.put("error_msg", msg);
            return result;
        }

        try {
            result = mapper.readValue(m.toString(), Map.class);

            List<String> clk_tks = (List) result.get("clk_tks");
            String[] ctarr = new String[clk_tks.size()];
            for (int i = 0; i < clk_tks.size(); i++) {
                ctarr[i] = clk_tks.get(i) + "click?aid=" + androidid + "&ad=" + mid;
            }
            result.put("clk_tks", ctarr);

            List<String> imp_tks = (List) result.get("imp_tks");
            String[] itarr = new String[imp_tks.size()];
            for (int j = 0; j < imp_tks.size(); j++) {
                itarr[j] = imp_tks.get(j) + "?aid=" + androidid + "&ad=" + mid;
            }
            result.put("imp_tks", itarr);

            String url = FlipkartHelper.getUrlWithAff(result.get("clk_url").toString(), new String[]{"HASAD_YM", androidid});
            result.put("clk_url", url);
            result.put("error_msg", "ok");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
