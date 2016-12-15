package hasoffer.adp.api.controller;

import hasoffer.adp.rtb.adx.response.Bid;
import hasoffer.adp.rtb.adx.response.BidResponse;
import hasoffer.adp.rtb.adx.response.SeatBid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihongde on 2016/12/14 17:51
 */
@RestController
@RequestMapping(value = "/")
public class BidController extends BaseController {

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public Object bid(String testbid) throws UnsupportedEncodingException {

        if("nobid".equals(testbid)){
            response.setStatus(204);
            return "no  bid";
        }

        BufferedReader br = null;
        String body = "";

        try {
            br = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
            StringBuffer sb = new StringBuffer("");
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            br.close();
            body = sb.toString();



        } catch (IOException e) {
            e.printStackTrace();
        }

        BidResponse bidResponse = new BidResponse();
        bidResponse.setBidid("abc1123");
        bidResponse.setId("1DGXhoQYtm");

        List<SeatBid> sbs = new ArrayList<>();
        SeatBid ab = new SeatBid();
        List<Bid> bids = new ArrayList<>();
        Bid bid = new Bid();
        bid.setId("1DGXhoQYtm");
        bid.setImpid("1");
        bid.setPrice(0.98f);
        bid.setAdid("314");
        bid.setNurl("http://reports.ubimo.com/fb?b=JdZQFdbCARgKMURHWGhvUVl0bSMBJeAhAA&c=MTo6&wp=${AUCTION_PRICE}");
        bid.setAdm("<ad xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"smaato_ad_v0.9.xsd\" modelVersion=\"0.9\">\n" +
                "                <imageAd>\n" +
                "                    <clickUrl>http://reports.ubimo.com/fb?b=JdZQFdbCARgKMURHWGhvUVl0bSMBJeAhAA&amp;c=Mzo6&amp;t=https%3A%2F%2Fad.doubleclick.net%2Fddm%2Fclk%2F292804678%3B119963336%3Bw%3Fhttp%3A%2F%2Fwww.academy.com%2Fwebapp%2Fwcs%2Fstores%2Fservlet%2FContainer_10151_10051_-1_%3Fname%3DOfficial_Rules%26uv%3Dvanity%3Aofficialrules\n" +
                "                    </clickUrl>\n" +
                "                    <imgUrl>http://static.ubimo.com/io/603/ecd01dce\n" +
                "                    </imgUrl>\n" +
                "                    <height>20</height>\n" +
                "                    <width>120</width>\n" +
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

        return bidResponse;

    }


}
