package hasoffer.adp.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hasoffer.adp.base.utils.Constants;
import hasoffer.adp.base.utils.FileUtil;
import hasoffer.data.redis.IRedisMapService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lihongde on 2016/12/21 17:59
 */
@RestController
@RequestMapping(value = "/ym")
public class YeahmobiController extends BaseController {

    private static final String CLICK_PREF_URL = "http://adclick.hasoffer.cn/rd?ad=%s&url=%s";

    private static final String AD_CHANNEL = "HASAD_YM";

    /**
     * 命中次数
     */
    public static long successMatchs = 0;

    /**
     * 请求失败次数
     */
    public static long failed = 0;

    @Resource
    IRedisMapService redisMapService;

    ObjectMapper mapper = new ObjectMapper();

    /**
     * 提供获得广告素材接口
     *
     * @param country
     * @param androidid
     * @param gaid      google advertiser id
     * @param width
     * @param height
     * @return
     */
    @RequestMapping(value = "/getAd", method = RequestMethod.GET)
    public Map<String, Object> bidForYeahmobi(@RequestParam(value = "country", defaultValue = "IN") String country,
                                              @RequestParam(value = "aid", required = false) String androidid,
                                              @RequestParam(value = "gaid", required = false) String gaid,
                                              @RequestParam(value = "imgw", defaultValue = "506") int width,
                                              @RequestParam(value = "imgh", defaultValue = "900") int height) {

        /**
         * 根据机器mac地址,给每台机器设置通投或不投开关,如果某台机器设置了通投并且没有匹配的素材则随机选取一个素材投放
         */
        String mac = FileUtil.getMacAddress();
        String dswitch = (String) redisMapService.getValue(Constants.REDIS_MAP_KEY.DELIVERYSWITCH, mac);
        if (dswitch == null) {
            redisMapService.putMap(Constants.REDIS_MAP_KEY.DELIVERYSWITCH, mac, String.valueOf(false));
        }
        boolean flag = Boolean.valueOf(dswitch);

        System.out.println("ad-api request aid : " + androidid);
        String msg = "No matching material found";
        Map<String, Object> result = new ConcurrentHashMap<>();
        if (StringUtils.isEmpty(androidid)) {
            if (flag) {
                Set<String> aids = redisMapService.getKeys(Constants.REDIS_MAP_KEY.AIDTAGMAP);
                androidid = FileUtil.getRandomElement(aids);
            } else {
                result.put("error_msg", msg);
                return result;
            }
        }

        Object eq = redisMapService.getValue(Constants.REDIS_MAP_KEY.AIDTAGMAP, androidid);
        if (eq == null) {
            if (flag) {
                Set<String> tags = redisMapService.getKeys(Constants.REDIS_MAP_KEY.MATTAGMAP);
                eq = FileUtil.getRandomElement(tags);
            } else {
                result.put("error_msg", msg);
                return result;
            }
        }

        String[] tags = eq.toString().split(",");
        Object mids = redisMapService.getValue(Constants.REDIS_MAP_KEY.MATTAGMAP, tags[0]);
        if (mids == null) {
            if (flag) {
                Set<String> matids = redisMapService.getKeys(Constants.REDIS_MAP_KEY.MRESULT);
                mids = FileUtil.getRandomElement(matids);
            } else {
                result.put("error_msg", msg);
                return result;
            }
        }

        String mid = mids.toString().split(",")[0];

        Object m = redisMapService.getValue(Constants.REDIS_MAP_KEY.MRESULT, mid);
        if (m == null) {
            if (flag) {
                Set<String> matids = redisMapService.getKeys(Constants.REDIS_MAP_KEY.MRESULT);
                String matid = FileUtil.getRandomElement(matids);
                m = redisMapService.getValue(Constants.REDIS_MAP_KEY.MRESULT, matid);
            } else {
                result.put("error_msg", msg);
                return result;
            }
        }

        try {
            result = mapper.readValue(m.toString(), Map.class);

            List<String> clk_tks = (List) result.get("clk_tks");
            String[] ctarr = new String[clk_tks.size()];
            for (int i = 0; i < clk_tks.size(); i++) {
                ctarr[i] = clk_tks.get(i) + "?aid=" + androidid + "&ad=" + mid;
            }
            result.put("clk_tks", ctarr);

            List<String> imp_tks = (List) result.get("imp_tks");
            String[] itarr = new String[imp_tks.size()];
            for (int j = 0; j < imp_tks.size(); j++) {
                itarr[j] = imp_tks.get(j) + "?aid=" + androidid + "&ad=" + mid;
            }
            result.put("imp_tks", itarr);

            //String url = FlipkartHelper.getUrlWithAff(result.get("clk_url").toString(), new String[]{AD_CHANNEL, androidid});

            String nurl = String.format(CLICK_PREF_URL, mid, hasoffer.base.utils.StringUtils.urlEncode(result.get("clk_url").toString() + "&affid=189103703"));

            result.put("clk_url", nurl);
            result.put("error_msg", "ok");
            successMatchs++;
            System.out.println("successMatchs : " + successMatchs);
        } catch (Exception e) {
            failed++;
            System.out.println("failed : " + failed + " aid : " + androidid);
            e.printStackTrace();
            result.put("error_msg", msg);
            return result;
        }

        return result;
    }

}
