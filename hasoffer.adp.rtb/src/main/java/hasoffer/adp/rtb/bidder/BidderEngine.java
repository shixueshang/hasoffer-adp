package hasoffer.adp.rtb.bidder;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import hasoffer.adp.rtb.adx.response.BidResponse;
import hasoffer.adp.rtb.adx.response.NobidResponse;
import hasoffer.adp.rtb.common.Campaign;
import hasoffer.adp.rtb.common.Configuration;
import hasoffer.adp.rtb.common.ForensiqLog;
import hasoffer.adp.rtb.redis.RedisClient;

import java.text.SimpleDateFormat;
import java.util.Map;


/**
 * A class for handling REDIS  to the RTB server.
 * Another job is to create the REDIS cache. There could be
 * multiple bidders running in the infrastructure, but handling a win
 * notification requires that you have information about the original bid. This
 * means the system receiving the notification may not be the same system that
 * made the bid. The bid is stored in the cache as a map so the win handling
 * system can handle the win, even though it did not actually make the bid.
 *
 */
public enum BidderEngine {

    INSTANCE;

    /** The JEDIS object for creating bid hash objects */
    static RedisClient bidCachePool;

    /** The queue for posting responses on */
    static Publisher responseQueue;
    /** Queue used to send wins */
    static Publisher winsQueue;
    /** Queue used to send bids */
    static Publisher bidQueue;
    /** Queue used to send nobid responses */
    static Publisher nobidQueue;
    /** Queue used for requests */
    static Publisher requestQueue;
    /** Queue for sending log messages */
    static Publisher loggerQueue;
    /** Queue for sending clicks */
    static Publisher clicksQueue;
    /** Formatter for printing Xforensiqs messages */
    static Publisher forensiqsQueue;
    /** Formatter for printing log messages */
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


    static Configuration config = Configuration.getInstance();

    /** A factory object for making timnestamps */
    static final JsonNodeFactory factory = JsonNodeFactory.instance;

    public static BidderEngine getInstance() throws Exception {
        if (bidCachePool == null) {
            bidCachePool = Configuration.getInstance().redisson;
            responseQueue = new Publisher(config.RESPONSES);

            if (config.REQUEST_CHANNEL != null) {
                if (config.REQUEST_CHANNEL.startsWith("file://"))
                    requestQueue = new Publisher(config.REQUEST_CHANNEL);
                else
                    requestQueue = new Publisher(config.REQUEST_CHANNEL);
            }
            if (config.WINS_CHANNEL != null) {
                winsQueue = new Publisher(config.WINS_CHANNEL);
            }
            if (config.BIDS_CHANNEL != null) {
                bidQueue = new Publisher(config.BIDS_CHANNEL);
            }
            if (config.NOBIDS_CHANNEL != null) {
                nobidQueue = new Publisher(config.NOBIDS_CHANNEL);
            }
            if (config.LOG_CHANNEL != null) {
                loggerQueue = new Publisher(config.LOG_CHANNEL);
            }
            if (config.CLICKS_CHANNEL != null) {
                clicksQueue = new Publisher(config.CLICKS_CHANNEL);
            }
            if (config.FORENSIQ_CHANNEL != null) {
                forensiqsQueue = new Publisher(config.FORENSIQ_CHANNEL);
            }
        }

        return INSTANCE;
    }

    /**
     * Simplest form of the add campaign
     *
     * @param c
     *            Campaign. The campaign to add.
     * @throws Exception
     *             on redis errors.
     */
    public void addCampaign(Campaign c) throws Exception {
        Configuration.getInstance().deleteCampaign(c.owner, c.adId);
        Configuration.getInstance().addCampaign(c);
    }

    public void stopBidder(String cmd) throws Exception {

    }

    public void startBidder(String cmd) throws Exception {


    }

    private void load(Map values, Map<String, String> m, String key, Object def) {
        String value = null;
        if (m.get(key) != null) {
            try {
                value = m.get(key);
                if (def instanceof String) {
                    values.put(key, value);
                } else if (def instanceof Long) {
                    values.put(key, Long.parseLong(value));
                } else if (def instanceof Boolean) {
                    values.put(key, Boolean.parseBoolean(value));
                } else if (def instanceof Integer) {
                    values.put(key, Integer.parseInt(value));
                }
                if (def instanceof Double) {
                    values.put(key, Double.parseDouble(value));
                }
            } catch (Exception error) {
                values.put(key, 0);
            }
        } else {
            values.put(key, def);
        }
    }

    /**
     * Sends an RTB bid out on the appropriate ZeroMQ queue
     *
     * @param bid
     *            BidResponse. The bid
     */
    public void sendBid(BidResponse bid) throws Exception {
        if (bidQueue != null)
            bidQueue.add(bid);
    }

    /**
     * Channel to send no bid information
     *
     * @param nobid
     *            NobidResponse. Info about the no bid
     */
    public void sendNobid(NobidResponse nobid) {
        if (nobidQueue != null)
            nobidQueue.add(nobid);
    }




    /**
     * Sends a log message on the appropriate REDIS queue
     *
     * @param level
     *            int. The log level of this message.
     * @param field
     *            String. An identification field for this message.
     * @param msg
     *            String. The JSON of the message
     */
    public void sendLog(int level, String field, String msg) {

    }

    /**
     * Send click info.
     *
     * @param target String. The URI of this click data
     */
    public void publishClick(String target) {
        /*if (clicksQueue != null) {
            ClickLog log = new ClickLog(target);
            clicksQueue.add(log);
        }*/
    }

    /**
     * Send pixel info. This fires when the ad actually loads into the users web page.
     *
     * @param target
     *            String. The URI of this pixel data
     */
    public void publishPixel(String target) {
       /* if (clicksQueue != null) {
            PixelLog log = new PixelLog(target);
            clicksQueue.add(log);
        }*/
    }

    public void publishFraud(ForensiqLog m) {
        if (forensiqsQueue != null) {
            forensiqsQueue.add(m);
        }
    }

    /**
     * Send pixel info. This fires when the ad actually loads into the users web page.
     *
     * @param target
     *            String. The URI of this pixel data
     */
    public void publishConvert(String target) {
        /*if (clicksQueue != null) {
            ConvertLog log = new ConvertLog(target);
            clicksQueue.add(log);
        }*/
    }
}

