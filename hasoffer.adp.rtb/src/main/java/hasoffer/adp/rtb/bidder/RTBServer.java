package hasoffer.adp.rtb.bidder;

import hasoffer.adp.rtb.adx.request.BidRequest;
import hasoffer.adp.rtb.common.Campaign;
import hasoffer.adp.rtb.common.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


public class RTBServer implements Runnable {

    public static final int PERIODIC_UPDATE_TIME = 60000;

    public static final int BID_CODE = 200;
    public static final int NOBID_CODE = 204; // http code no bid

    public static boolean stopped = false;

    public static boolean paused = false;

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
     * The average time
     */
    public static long avgBidTime;

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

    /**
     * Class instantiator of the RTBServer.
     *
     * @param data
     */
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

        if (Configuration.getInstance().deadmanSwitch != null) {
            if (!Configuration.getInstance().deadmanSwitch.canRun()) {
                RTBServer.stopped = true;
            }
        }

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

            time = System.currentTimeMillis() - time;

            response.setHeader("X-TIME", Long.toString(time));
            response.setContentType("application/json;charset=utf-8");
            RTBServer.xtime += time;

            if (code == NOBID_CODE) {
                response.setStatus(NOBID_CODE);
            }

            if (code == BID_CODE) {
                RTBServer.totalBidTime.addAndGet(time);
                RTBServer.bidCountWindow.incrementAndGet();
                response.setStatus(BID_CODE);
            }

            PrintWriter out = response.getWriter();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
