package hasoffer.adp.rtb.bidder;

import com.alibaba.fastjson.JSON;
import hasoffer.adp.rtb.adx.request.BidRequest;
import hasoffer.adp.rtb.adx.response.BidResponse;
import hasoffer.adp.rtb.common.Campaign;
import hasoffer.adp.rtb.common.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


public class RTBServer implements Runnable {

    public static final int BID_CODE = 200;
    public static final int NOBID_CODE = 204; // http code no bid

    public static long requests = 0;
    /**
     * Counter for number of bids made
     */
    public static long bid = 0;
    /**
     * Counter for number of nobids made
     */
    public static long nobid = 0;
    /**
     * Number of errors in accessing the bidder
     */
    public static long error = 0;
    /**
     * Number of actual requests
     */
    public static long handled = 0;

    /**
     * xtime counter
     */
    public static long xtime = 0;

    static long deltaTime = 0;

    static AtomicLong totalBidTime = new AtomicLong(0);
    static AtomicLong bidCountWindow = new AtomicLong(0);


    private HttpServletRequest request;

    private HttpServletResponse response;

    Thread me;

    static CampaignSelector campaigns;

    public static Map<String, BidRequest> exchanges = new HashMap<>();


    public RTBServer(String data, HttpServletRequest request, HttpServletResponse response) throws Exception {

        this.request = request;
        this.response = response;

        Configuration.reset();

        Configuration.getInstance(data);
        campaigns = CampaignSelector.getInstance();

        me = new Thread(this);
        me.start();
    }


    @Override
    public void run() {


        deltaTime = System.currentTimeMillis();

        long time = System.currentTimeMillis();
        Campaign campaign = null;
        boolean unknown = true;
        RTBServer.handled++;
        int code = RTBServer.BID_CODE;
        RTBServer.requests++;
        try {
            BidRequest br = null;

            if (br == null) {
                code = RTBServer.NOBID_CODE;
                RTBServer.error++;
                response.setStatus(code);
                response.getWriter().println("{}");
                return;
            }

            BidResponse brep = CampaignSelector.getInstance().getHeuristic(br);
            PrintWriter out = response.getWriter();
            time = System.currentTimeMillis() - time;

            response.setHeader("X-TIME", Long.toString(time));
            response.setContentType("application/json;charset=utf-8");
            RTBServer.xtime += time;

            if (code == NOBID_CODE) {
                response.setStatus(NOBID_CODE);
                out.println("{}");
                return;
            }

            if (code == BID_CODE) {
                RTBServer.totalBidTime.addAndGet(time);
                RTBServer.bidCountWindow.incrementAndGet();
                response.setStatus(BID_CODE);
            }

            out.println(JSON.toJSON(brep));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
