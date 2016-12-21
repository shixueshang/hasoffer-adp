package hasoffer.adp.api.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.BeanUtil;
import hasoffer.adp.api.configuration.RootConfiguration;
import hasoffer.adp.base.utils.AjaxJson;
import hasoffer.adp.base.utils.Constants;
import hasoffer.adp.core.models.po.Equipment;
import hasoffer.adp.core.models.po.Material;
import hasoffer.adp.core.models.po.MaterialCreative;
import hasoffer.adp.core.service.EquipmentService;
import hasoffer.adp.core.service.MaterialService;
import hasoffer.adp.rtb.adx.request.Banner;
import hasoffer.adp.rtb.adx.request.Impression;
import hasoffer.adp.rtb.adx.request.Video;
import hasoffer.adp.rtb.adx.response.Bid;
import hasoffer.adp.rtb.adx.response.BidResponse;
import hasoffer.adp.rtb.adx.response.SeatBid;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by lihongde on 2016/12/14 17:51
 */
@RestController
public class BidController extends BaseController {

    /**请求次数*/
    public static long requests = 0;

    /**竞价次数*/
    public static long bid = 0;

    /**不参与竞价次数*/
    public static long nobid = 0;

    /**总竞价出价*/
    public static float totalBidPrice = 0;

    /**总赢得竞价出价*/
    public static float totalWonPrice = 0;

    @Resource
    private MaterialService materialService;

    @Resource
    private EquipmentService equipmentService;

    @Resource
    RootConfiguration configuration;

    private  ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public void bid(String testbid) throws IOException {

        requests ++;

        if("nobid".equals(testbid)){
            nobid ++;
            response.setStatus(204);
            response.getWriter().println("{}");
            return;
        }

        bid ++;
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
            StringBuffer sb = new StringBuffer("");
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            br.close();
            String body = sb.toString();

            Map<String, Object> bom = mapper.readValue(body, Map.class);

            List<Map<String ,Object>> impList = (List<Map<String, Object>>)bom.get("imp");
            List<Impression> imps = new ArrayList<>();
            for(Map<String, Object> m : impList){
                Impression im = new Impression();
                im.setId(m.get("id").toString());
                im.setInstl((int) m.get("instl"));
                Map<String, Object> bmap = (Map)m.get("banner");
                Banner banner = new Banner();
                banner.setH((int)bmap.get("h"));
                banner.setW((int) bmap.get("w"));
                im.setBanner(banner);
                imps.add(im);
            }

            //媒体提供的广告位，需要根据这些广告位的width和height去素材库匹配
            List<Banner> banners = imps.stream().map(Impression::getBanner).collect(Collectors.toList());

            List<Material> materials = new ArrayList<>();
            for(Banner banner : banners){
                List<Material> ms = materialService.findMaterials(banner.getW(), banner.getH());
                materials.addAll(ms);
            }
            if(materials.size() == 0){
                response.setStatus(204);
                response.getWriter().println("{}");
                return;
            }

            //获得第一个返回
            Material m = materials.get(0);


            totalBidPrice += m.getPrice();

        System.out.println(requests);
        System.out.println(bid);
        System.out.println(nobid);
        System.out.println(totalBidPrice);


        BidResponse bidResponse = new BidResponse();
        bidResponse.setBidid("abc1123");
        bidResponse.setId("1DGXhoQYtm");

        List<SeatBid> sbs = new ArrayList<>();
        SeatBid ab = new SeatBid();
        List<Bid> bids = new ArrayList<>();
        Bid bid = new Bid();
        bid.setId("1DGXhoQYtm");
        bid.setImpid("1");
        bid.setPrice(m.getPrice());
        bid.setAdid("314");
        bid.setNurl("http://reports.ubimo.com/fb?b=JdZQFdbCARgKMURHWGhvUVl0bSMBJeAhAA&c=MTo6&wp=${AUCTION_PRICE}");
        bid.setAdm("<ad xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"smaato_ad_v0.9.xsd\" modelVersion=\"0.9\">\n" +
                "                <imageAd>\n" +
                "                    <clickUrl>http://reports.ubimo.com/fb?b=JdZQFdbCARgKMURHWGhvUVl0bSMBJeAhAA&amp;c=Mzo6&amp;t=https%3A%2F%2Fad.doubleclick.net%2Fddm%2Fclk%2F292804678%3B119963336%3Bw%3Fhttp%3A%2F%2Fwww.academy.com%2Fwebapp%2Fwcs%2Fstores%2Fservlet%2FContainer_10151_10051_-1_%3Fname%3DOfficial_Rules%26uv%3Dvanity%3Aofficialrules\n" +
                "                    </clickUrl>\n" +
                "                    <imgUrl>"+m.getCreatives().get(0).getUrl()+"\n" +
                "                    </imgUrl>\n" +
                "                    <height>"+m.getCreatives().get(0).getHeight()+"</height>\n" +
                "                    <width>"+m.getCreatives().get(0).getWidth()+"</width>\n" +
                "                    <beacons>\n" +
                "                        <beacon>http://reports.ubimo.com/fb?b=JdZQFdbCARgKMURHWGhvUVl0bSMBJeAhAA&amp;c=Mjo6</beacon>\n" +
                "                        <beacon>https://ad.doubleclick.net/ddm/ad/N5865.276855.MOBILEFUSE/B8852634.119963336;sz=1x1;ord=1436319256367</beacon>\n" +
                "                    </beacons>\n" +
                "                </imageAd>\n" +
                "            </ad>");
        bid.setAdomain(new String[]{"academy.com"});
        bid.setCid("5163");
        bid.setCrid("12459");
        bids.add(bid);
        ab.setBid(bids.toArray(new Bid[bids.size()]));
        sbs.add(ab);
        bidResponse.setSeatbid(sbs.toArray(new SeatBid[sbs.size()]));

        response.getWriter().println(JSON.toJSON(bidResponse));

    }

    /**
     *
     * @param country
     * @param androidid
     * @param gaid google advertiser id
     * @param width
     * @param height
     * @return
     */
    @RequestMapping(value = "/ym/getAd", method = RequestMethod.GET)
    public Map<String, Object> bidForYeahmobi(@RequestParam(value = "country", defaultValue = "IN") String country,
                                    @RequestParam(value = "aid", required = false) String androidid,
                                    @RequestParam(value = "gaid", required = false) String gaid,
                                    @RequestParam(value = "imgw") int width,
                                    @RequestParam(value = "imgh") int height){

        String msg = "没有找到匹配的素材";
        Map<String, Object> result = new ConcurrentHashMap<>();
        if(StringUtils.isEmpty(androidid)){
            result.put("error_msg" ,msg);
            return result;
        }

        Equipment eq = equipmentService.findByAndroidid(androidid);
        if(eq == null){
            result.put("error_msg" ,msg);
            return result;
        }

        String[] tags = eq.getTags().split(",");
        List<Material> ms = materialService.findLikeByTag(tags[0]);
        if(ms.size() == 0){
            result.put("error_msg" ,msg);
            return result;
        }

        Material m = ms.get(0);
        result.put("error_msg","ok");
        result.put("titel", m.getTitle());
        result.put("desc", m.getDescription());
        result.put("img", configuration.getDomainUrl() + m.getCreatives().get(0).getUrl());
        result.put("imgw", m.getCreatives().get(0).getWidth());
        result.put("imgh", m.getCreatives().get(0).getHeight());
        result.put("icon", configuration.getDomainUrl() +  m.getIcon());
        result.put("clk_url", m.getUrl());
        result.put("btn_text", m.getBtnText());
        result.put("imp_tks", new String[]{m.getPvRequestUrl()});
        result.put("clk_tks", new String[]{"http://adclick.hasoffer.cn"});
        return result;
    }


}
