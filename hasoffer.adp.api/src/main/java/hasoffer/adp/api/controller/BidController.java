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

       /* if("nobid".equals(testbid)){
            response.setStatus(204);
            return "no  bid";
        }*/

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
        bidResponse.setId("1234567890");
        bidResponse.setCur("USD");

        List<SeatBid> sbs = new ArrayList<>();
        SeatBid ab = new SeatBid();
        ab.setSeat("512");
        List<Bid> bids = new ArrayList<>();
        Bid bid = new Bid();
        bid.setId("1");
        bid.setImpid("102");
        bid.setPrice(0.21f);
        bid.setAdid("314");
        bid.setNurl("http://adserver.com/winnotice?impid=102");
        bid.setAdm("%3C!DOCTYPE%20html%20PUBLIC%20%5C%22-\u00AD‐ %2F%2FW3C%2F%2FDTD%20XHTML%201.0%20Transitional%2F%2FEN%5C%22%20%5C%22htt p%3A%2F%2Fwww.w3.org%2FTR%2Fxhtml1%2FDTD%2Fxhtml1-\u00AD‐ transitional.dtd%5C%22%3E%3Chtml%20xmlns%3D%5C%22http%3A%2F%2Fwww.w3.org%2F1 999%2Fxhtml%5C%22%20xml%3Alang%3D%5C%22en%5C%22%20lang%3D%5C%22en%5C%22 %3E...%3C%2Fhtml%3E");
        bid.setAdomain(new String[]{"advertiserdomain.com"});
        bid.setIurl("http://adserver.com/pathtosampleimage");
        bid.setCid("campaign111");
        bid.setCrid("creative112");
        bid.setAttr(new Integer[]{1, 2, 3, 4, 5, 6, 7, 12});
        bids.add(bid);
        ab.setBid(bids.toArray(new Bid[bids.size()]));
        sbs.add(ab);
        bidResponse.setSeatbid(sbs.toArray(new SeatBid[sbs.size()]));

        return bidResponse;

    }


}
