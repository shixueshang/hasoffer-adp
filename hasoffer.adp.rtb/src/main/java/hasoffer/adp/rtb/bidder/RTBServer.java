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
     * 竞价次数计数器
     */
    public static long bid = 0;
    /**
     * 不参与竞价计数器
     */
    public static long nobid = 0;
    /**
     * 竞价过程出错次数
     */
    public static long error = 0;

    /**
     * 处理请求花费时间
     */
    public static long xtime = 0;

    /**
     * 处理竞价花费时间
     */
    static AtomicLong totalBidTime = new AtomicLong(0);

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

        long time = System.currentTimeMillis();
        Campaign campaign = null;
        boolean unknown = true;
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
                response.setStatus(BID_CODE);
            }

            out.println(JSON.toJSON(brep));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
