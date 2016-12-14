package hasoffer.adp.rtb.bidder;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import hasoffer.adp.rtb.adx.request.BidRequest;
import hasoffer.adp.rtb.adx.response.BidResponse;
import hasoffer.adp.rtb.adx.response.NobidResponse;
import hasoffer.adp.rtb.common.Campaign;
import hasoffer.adp.rtb.common.Configuration;
import hasoffer.adp.rtb.common.ForensiqLog;
import hasoffer.adp.rtb.redis.RedisClient;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


/**
 * A class for handling REDIS based commands to the RTB server. The Controller
 * open REDIS channels to the requested channels to handle commands, and logging
 * channels for log messages, win notifications, bid requests and bids. The idea
 * is to transmit all this information through REDIS so that you can\ build your
 * own database, accounting, and analytic processes outside of the bidding
 * engine.
 *
 * Another job of the Controller is to create the REDIS cache. There could be
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

    /** The loop object used for reading commands */
  /*  static CommandLoop loop;*/

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
    public static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS");

    /* The configuration object used bu the controller */
    static Configuration config = Configuration.getInstance();

    /** A factory object for making timnestamps */
    static final JsonNodeFactory factory = JsonNodeFactory.instance;

    /**
     * Private construcotr with specified hosts
     *
     * @throws Exception
     *             on REDIS errors.
     */
    public static BidderEngine getInstance() throws Exception {
        /** the cache of bid adms */

        if (bidCachePool == null) {
            bidCachePool = Configuration.getInstance().redisson;

           /* RTopic t = new RTopic(Configuration.getInstance().commandAddresses);
            t.addListener(new CommandLoop());*/

            // System.out.println("============= COMMAND LOOP ESTABLIISHED =================");

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

    /**
     * Add a campaign from REDIS
     *
     * @param c
     *            BasiCommand. The command to add
     * @throws Exception
     *             on REDIS errors.
     */
    /*public void addCampaign(BasicCommand c) throws Exception {
        System.out.println("ADDING " + c.owner + "/" + c.target);
        Campaign camp = WebCampaign.getInstance().db.getCampaign(c.owner,
                c.target);
        BasicCommand m = new BasicCommand();
        m.owner = c.owner;
        m.to = c.from;
        m.from = Configuration.getInstance().instanceName;
        m.id = c.id;
        m.type = c.type;
        if (camp == null) {
            m.status = "Error";
            m.msg = "Campaign load failed, could not find " + c.owner + "/"
                    + c.target;
            responseQueue.add(m);
        } else {

            System.out.println("------>" + camp.owner + "/" + camp.adId);

            Configuration.getInstance().deleteCampaign(camp.owner, camp.adId);
            Configuration.getInstance().addCampaign(camp);

            // System.out.println(camp.toJson());

            m.msg = "Campaign " + camp.owner + "/" + camp.adId + " loaded ok";
            m.name = "AddCampaign Response";
            sendLog(1, "AddCampaign", m.msg + " by " + c.owner);
            responseQueue.add(m);
        }
        System.out.println(m.msg);
    }
*/
    /**
     * Deletes the user, then tells all the other bidders to stop, then reload all their campaigns.
     * @param owner String. The user or root that is deleting the use.
     * @param name String name. The user to delete
     * @throws Exception on database errors from Redisson
     */
    /*public void deleteUser(String owner, String name) throws Exception {
        Configuration.getInstance().deleteUser(owner, name);
        BidderEngine.getInstance().deleteCampaign(name,"*");		// delete from bidder;

    }*/

    /**
     * Delete a campaign.
     *
     *            String. The Map of this command.
     * @throws Exception
     *             if there is a JSON parse error.
     */
    public void deleteCampaign(String owner, String name) throws Exception {
        Configuration.getInstance().deleteCampaign(owner, name);
    }

   /* public void deleteUser(BasicCommand cmd) throws Exception {
        boolean b = Configuration.getInstance().deleteUser(cmd.owner,
                cmd.target);
        BasicCommand m = new BasicCommand();
        if (!b) {
            m.msg = "error, no such User " + cmd.target;
            m.status = "error";
        } else
            m.msg = "User deleted: " + cmd.target + " by " + cmd.target;
        m.to = cmd.from;
        m.from = Configuration.getInstance().instanceName;
        m.id = cmd.id;
        m.type = cmd.type;
        m.name = "DeleteCommand Response";
        responseQueue.add(m);
        this.sendLog(1, "DeleteUser", cmd.msg + " by " + cmd.owner);
    }*/
    /**
     * From Campaign List in Server delete campaign
     * Note, if this is a cache2k based system (Not Aerospike) it will delete from the cache2k database too.
     *
     * @param cmd
     *            BasicCommand. The delete command
     */
   /* public void deleteCampaign(BasicCommand cmd) throws Exception {
        boolean b = Configuration.getInstance().deleteCampaign(cmd.owner,
                cmd.target);
        BasicCommand m = new BasicCommand();
        if (!b) {
            m.msg = "error, no such campaign " + cmd.owner + "/" + cmd.target;
            m.status = "error";
        } else
            m.msg = "Campaign deleted: " + cmd.owner + "/" + cmd.target;
        m.to = cmd.from;
        m.from = Configuration.getInstance().instanceName;
        m.id = cmd.id;
        m.type = cmd.type;
        m.name = "DeleteCommand Response";
        responseQueue.add(m);

        if (cmd.name == null) {
            Configuration.getInstance().campaignsList.clear();
            this.sendLog(1, "deleteCampaign", "All campaigns cleared by "
                    + cmd.from);
        } else
            this.sendLog(1, "DeleteCampaign", cmd.msg + " by " + cmd.owner);
    }
*/
    /**
     * Stop the bidder from REDIS
     *
     * @param cmd
     *            BasicCommand. The command as a map.
     * @throws Exception
     *             if there is a JSON parsing error.
     */
    public void stopBidder(String cmd) throws Exception {
       /* RTBServer.stopped = true;
        BasicCommand m = new BasicCommand();
        m.msg = "stopped";
        m.to = cmd.from;
        m.from = Configuration.getInstance().instanceName;
        m.id = cmd.id;
        m.type = cmd.type;
        m.name = "StopBidder Response";
        responseQueue.add(m);
        this.sendLog(1, "stopBidder", "Bidder stopped by command from "
                + cmd.from);*/
    }

    /**
     * Start the bidder from REDIS
     *
     * @param cmd
     *            BasicCmd. The command.
     * @throws Exception
     *             if there is a JSON parsing error.
     */
    public void startBidder(String cmd) throws Exception {

      /*  if (Configuration.getInstance().deadmanSwitch != null) {
            if (Configuration.getInstance().deadmanSwitch.canRun() == false) {
                BasicCommand m = new BasicCommand();
                m.msg = "Error, the deadmanswitch is not present";
                m.to = cmd.from;
                m.from = Configuration.getInstance().instanceName;
                m.id = cmd.id;
                m.type = cmd.type;
                m.name = "StartBidder Response";
                responseQueue.add(m);
                this.sendLog(1, "startBidder",
                        "Error: attempted start bidder by command from "
                                + cmd.from + " failed, deadmanswitch is thrown");
                return;
            }
        }

        RTBServer.stopped = false;
        BasicCommand m = new BasicCommand();
        m.msg = "running";
        m.to = cmd.from;
        m.from = Configuration.getInstance().instanceName;
        m.id = cmd.id;
        m.type = cmd.type;
        m.name = "StartBidder Response";
        responseQueue.add(m);
        this.sendLog(1, "startBidder", "Bidder started by command from "
                + cmd.from);*/
    }

    /**
     * Set the throttle percentage from REDIS
     *
     *            . JsoNode - JSON of the command. TODO: this needs
     *            implementation.
     */
    /*public void setPercentage(JsonNode node) throws Exception {
        responseQueue.add(new BasicCommand());
    }*/

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
                System.err.println("---------->" + key + ", " + value);
                values.put(key, 0);
            }
        } else {
            //System.err.println("-----------> Unknown type: " + key + ", "
            //		+ value);
            values.put(key, def);
        }
    }

    /**
     * Retrieve a member RTB status from REDIS
     *
     * @param member
     *            String. The member's instance name.
     * @return Map. A Hash,ap of data.
     */
    public Map getMemberStatus(String member) {
        Map values = new HashMap();
        Map<String, String> m = null;

        try {
            m = null;//bidCachePool.hgetAll(member);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (m != null) {
            load(values,m,"total", new Long(0));
            load(values,m,"request",new Long(0));
            load(values,m,"bid",new Long(0));
            load(values,m,"nobid",new Long(0));
            load(values,m,"win",new Long(0));
            load(values,m,"clicks",new Long(0));
            load(values,m,"pixels",new Long(0));
            load(values,m,"errors", new Long(0));
            load(values,m,"adspend",new Double(0));
            load(values,m,"qps",new Double(0));
            load(values,m,"avgx",new Double(0));
            load(values,m,"fraud",new Long(0));
            load(values,m,"stopped",new Boolean(true));
            load(values,m,"ncampaigns",new Long(0));
            load(values,m,"bid",new Long(0));
            load(values,m,"loglevel",new Long(-3));
            load(values,m,"nobidreason",new Boolean(false));
        }
        return values;
    }

    /**
     * Record the member stats in REDIS
     *
     * @param e
     *            Echo. The status of this campaign.
     */
  /*  public void setMemberStatus(Echo e) throws Exception {
        String member = Configuration.getInstance().instanceName;
        Map m = new HashMap();
        m.put("total", "" + e.handled);
        m.put("request", "" + e.request);
        m.put("bid", "" + e.bid);
        m.put("nobid", "" + e.nobid);
        m.put("win", "" + e.win);
        m.put("clicks", "" + e.clicks);
        m.put("pixels", "" + e.pixel);
        m.put("errors", "" + e.error);
        m.put("adspend", "" + e.adspend);
        m.put("qps", "" + e.qps);
        m.put("avgx", "" + e.avgx);
        m.put("fraud", "" + e.fraud);

        m.put("time", "" + System.currentTimeMillis());

        m.put("cpu", Performance.getCpuPerfAsString());
        m.put("diskpctfree", Performance.getPercFreeDisk());
        m.put("threads", ""+Performance.getThreadCount());
        m.put("cores", ""+Performance.getCores());

        m.put("stopped", "" + RTBServer.stopped);
        m.put("ncampaigns", ""
                + Configuration.getInstance().campaignsList.size());
        m.put("loglevel", ""
                + Configuration.getInstance().logLevel);
        m.put("nobidreason", ""
                + Configuration.getInstance().printNoBidReason);

        bidCachePool.hmset(member,m,RTBServer.PERIODIC_UPDATE_TIME/1000+15);

    }*/

    /**
     * THe echo command and its response.
     *
     * @param cmd
     *            BasicCommand. The command used
     * @throws Exception
     *             if there is a JSON parsing error.
     */
   /* public void echo(BasicCommand cmd) throws Exception {
        Echo m = RTBServer.getStatus();
        m.to = cmd.from;
        m.from = Configuration.getInstance().instanceName;
        m.id = cmd.id;
        m.name = "Echo Response";
        responseQueue.add(m);
    }*/

    /**
     * Send a shutdown notice to all concerned!
     *
     * @throws Exception
     *             on Redisson errors.
     */
    /*public void sendShutdown() throws Exception {
        ShutdownNotice cmd = new ShutdownNotice(
                Configuration.getInstance().instanceName);
        responseQueue.add(cmd);
    }*/

   /* public void setLogLevel(BasicCommand cmd) throws Exception {
        int old = Configuration.getInstance().logLevel;
        Configuration.getInstance().logLevel = Integer.parseInt(cmd.target);
        Echo m = RTBServer.getStatus();
        m.to = cmd.from;
        m.from = Configuration.getInstance().instanceName;
        m.id = cmd.id;
        m.msg = "Log level changed from " + old + " to " + cmd.target;
        m.name = "SetLogLevel Response";
        responseQueue.add(m);
        this.sendLog(1, "setLogLevel", m.msg + ", by " + cmd.from);
    }*/

    /**
     * This will whack a creative out of a campaign. This stops the bidding on
     * it
     *
     * @param cmd
     *            BasicCommand. The command.
     * @throws Exception
     */
    /*public void deleteCreative(DeleteCreative cmd) throws Exception {
        String owner = cmd.owner;
        String campaignid = cmd.name;
        String creativeid = cmd.target;

        Echo m = RTBServer.getStatus();
        m.owner = cmd.owner;
        m.to = cmd.from;
        m.from = Configuration.getInstance().instanceName;
        m.id = cmd.id;
        try {
            Configuration.getInstance().deleteCampaignCreative(owner,
                    campaignid, creativeid);
            m.msg = "Delete campaign creative " + owner + "/" + campaignid
                    + "/" + creativeid + " succeeded";
        } catch (Exception error) {
            m.msg = "Delete campaign creative " + owner + "/" + campaignid
                    + "/" + creativeid + " failed, reason: "
                    + error.getMessage();
        }
        m.name = "DeleteCampaign Response";
        responseQueue.add(m);
        this.sendLog(1, "setLogLevel", m.msg + ", by " + cmd.from);
    }
*/
    public void setNoBidReason(String cmd) throws Exception {
        /*boolean old = Configuration.getInstance().printNoBidReason;
        Configuration.getInstance().printNoBidReason = Boolean
                .parseBoolean(cmd.target);
        Echo m = RTBServer.getStatus();
        m.to = cmd.from;
        m.from = Configuration.getInstance().instanceName;
        m.id = cmd.id;
        m.msg = "Print no bid reason level changed from " + old + " to "
                + cmd.target;
        m.name = "SetNoBidReason Response";
        responseQueue.add(m);
        this.sendLog(1, "setNoBidReason", m.msg + ", by " + cmd.from);*/
    }

    /*
     * The not handled response to the command entity. Used when an unrecognized
     * command is sent.
     *
     * @param cmd. BasicCommand - the error message.
     *
     * @throws Exception if there is a JSON parsing error.
     */
    public void notHandled(String cmd) throws Exception {
       /* Echo m = RTBServer.getStatus();
        m.msg = "error, unhandled event";
        m.status = "error";
        m.to = cmd.from;
        m.from = Configuration.getInstance().instanceName;
        m.id = cmd.id;
        m.name = "Unhandled Response";
        responseQueue.add(m);*/
    }

    /**
     * Sends an RTB request out on the appropriate REDIS queue
     *
     * @param br
     *            BidRequest. The request
     */

    public void sendRequest(BidRequest br) throws Exception {
        if (requestQueue != null) {
            ObjectNode original = null;

            ObjectNode child = factory.objectNode();
            child.put("timestamp", System.currentTimeMillis());
            child.put("exchange", "");

            ObjectNode ext = (ObjectNode) original.get("ext");
            if (ext != null) {
                ext.put("timestamp", System.currentTimeMillis());
                ext.put("exchange", "");
            } else {
                child.put("timestamp", System.currentTimeMillis());
                child.put("exchange", "");
                original.put("ext", child);
            }
            requestQueue.add(original);
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
     * Sends an RTB win out on the appropriate REDIS queue
     *
     * @param hash
     *            String. The bid id.
     * @param cost
     *            String. The cost component of the win.
     * @param lat
     *            String. The latitude component of the win.
     * @param lon
     *            String. The longitude component of the win.
     * @param adId
     *            String. The campaign adid of this win.
     * @param cridId
     *            String. The creative id of this win.
     * @param pubId
     *            String. The publisher id component of this win/
     * @param image
     *            String. The image part of the win.
     * @param forward
     *            String. The forward URL of the win.
     * @param price
     *            String. The bid price of the win.
     * @param adm
     *            String. the adm that was returned on the win notification. If
     *            null, it means nothing was returned.
     */
    public void sendWin(String hash, String cost, String lat, String lon,
                        String adId, String cridId, String pubId, String image,
                        String forward, String price, String adm) {
        /*if (winsQueue != null)
            winsQueue.add(new WinObject(hash, cost, lat, lon, adId, cridId,
                    pubId, image, forward, price, adm));*/
    }

    /**
     * Determine if it is appropriate to log. Use this on debug log messages so you dont create a lot of
     * objects for the log message, and then it just gets tossed because of the log level.
     * @param level int. The level you want to log at.
     * @return boolean. Returns true if it will log at this level, else returns false.
     */
   /* public boolean canLog(int level) {
        int checkLog = config.logLevel;
        if (checkLog < 0)
            checkLog = -checkLog;

        if (level > checkLog)
            return false;

        if (loggerQueue == null)
            return false;

        return true;
    }*/

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
       /* int checkLog = config.logLevel;
        if (checkLog < 0)
            checkLog = -checkLog;

        if (level > checkLog)
            return;

        if (loggerQueue == null)
            return;

        LogMessage ms = new LogMessage(level, config.instanceName, field, msg);
        if (checkLog >= level && config.logLevel < 0) {
            System.out.format("[%s] - %d - %s - %s - %s\n",
                    sdf.format(new Date()), ms.sev, ms.source, ms.field,
                    ms.message);
        }
        loggerQueue.add(ms);*/
    }

    /**
     * Send click info.
     *
     * @param target
     *            String. The URI of this click data
     */
    public void publishClick(String target) {
        /*if (clicksQueue != null) {
            ClickLog log = new ClickLog(target);
            clicksQueue.add(log);
        }*/
    }

    /**
     * Send pixel info. This fires when the ad actually loads into the users web
     * page.
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
     * Send pixel info. This fires when the ad actually loads into the users web
     * page.
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

    /**
     * Record a bid in Aerospike
     *
     * @param br
     *            BidResponse. The bid response that we made earlier.
     * @throws Exception
     *             on redis errors.
     */
    public void recordBid(BidResponse br) throws Exception {

       /* Map map = new HashMap();
        map.put("ADM", br.getAdmAsString());
        map.put("PRICE", Double.toString(br.creat.price));
        if (br.capSpec != null) {
            map.put("SPEC", br.capSpec);
            map.put("EXPIRY", br.creat.capTimeout);
        }
        try {
            bidCachePool.hmset(br.oidStr, map, Configuration.getInstance().ttl);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

    }

    public int getCapValue(String capSpec) {
        /*String str = bidCachePool.get(capSpec);
        if (str == null)
            return -1;
        try {
            int k = Integer.parseInt(str);
            return k;
        } catch (Exception error) {

        }*/
        return -1;
    }

    /**
     * Remove a bid object from the cache.
     *
     * @param hash
     *            String. The bid object id.
     */
    public void deleteBidFromCache(String hash) throws Exception {
       /* Map map = null;
        map = bidCachePool.hgetAll(hash);
        if (map != null) {
            String capSpec = (String)map.get("SPEC");
            if (capSpec != null) {
                String s = (String)map.get("EXPIRY");
                int n = Integer.parseInt(s);
                long r = bidCachePool.incr(capSpec);
                if (r == 1) {
                    bidCachePool.expire(capSpec, n);
                }
            }
            bidCachePool.del(hash);
        }*/

    }

    /**
     * Retrieve previously recorded bid data
     *
     * @param oid
     *            String. The object id of the bid.
     * @return Map. A map of the returned data, will be null if not found.
     */
   /* public Map getBidData(String oid) throws Exception {
        Map m = bidCachePool.hgetAll(oid);
        return m;
    }*/

}

/**
 * A class to retrieve RTBServer commands from REDIS.
 *
 *
 * @author Ben M. Faul
 *
 */
/*
class CommandLoop implements hasoffer.rtb.jmq.MessageListener<BasicCommand> {
    */
/** The thread this command loop uses to process REDIS subscription messages *//*

    */
/** The configuration object *//*

    Configuration config = Configuration.getInstance();

    */
/**
     * On a message from REDIS, handle the command.
     *
     * @param arg0
     *            . String - the channel of this message.
     * @param arg1
     *            . String - the JSON encoded message.
     *//*

    @Override
    public void onMessage(String arg0, BasicCommand item) {

        try {
            if (item.to != null && (item.to.equals("*") == false)) {
                boolean mine = Configuration.getInstance().instanceName
                        .matches(item.to);
                if (item.to.equals("") == false && !mine) {
                    BidderEngine.getInstance().sendLog(5,
                            "Controller:onMessage:" + item,
                            "Message was not for me: " + item);
                    return;
                }
            }
        } catch (Exception error) {
            try {
               */
/* Echo m = new Echo();
                m.from = Configuration.getInstance().instanceName;
                m.to = item.from;
                m.id = item.id;
                m.status = "error";
                m.msg = error.toString();
                BidderEngine.getInstance().responseQueue.add(m);
                BidderEngine.getInstance().sendLog(1,
                        "Controller:onMessage:" + item,
                        "Error: " + error.toString());*//*

                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

        }

        try {
            Runnable task = null;
            Thread thread;
            switch (item.cmd) {
                case BidderEngine.ADD_CAMPAIGN:

                    task = () -> {
                        try {
                            BidderEngine.getInstance().addCampaign(item);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    };
                    thread = new Thread(task);
                    thread.start();

                    break;
                case BidderEngine.DEL_CAMPAIGN:
                    task = () -> {
                        try {
                            BidderEngine.getInstance().deleteCampaign(item);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    };
                    thread = new Thread(task);
                    thread.start();

                    break;
                case BidderEngine.DELETE_USER:
                    task = () -> {
                        try {
                            BidderEngine.getInstance().deleteUser(item);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    };
                    thread = new Thread(task);
                    thread.start();
                    break;
                case BidderEngine.STOP_BIDDER:
                    BidderEngine.getInstance().stopBidder(item);
                    break;
                case BidderEngine.START_BIDDER:
                    BidderEngine.getInstance().startBidder(item);
                    break;
                case BidderEngine.ECHO:
                    BidderEngine.getInstance().echo(item);
                    break;
                case BidderEngine.SETLOGLEVEL:
                    BidderEngine.getInstance().setLogLevel(item);
                    break;
                case BidderEngine.DELETE_CREATIVE:
                    BidderEngine.getInstance().deleteCreative((DeleteCreative) item);
                    break;

                default:
                    BidderEngine.getInstance().notHandled(item);
            }

        } catch (Exception error) {
            try {
                item.msg = error.toString();
                item.to = item.from;
                item.from = Configuration.getInstance().instanceName;
                item.status = "error";
                BidderEngine.getInstance().responseQueue.add(item);
                error.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            error.printStackTrace();
        }

    }

}
*/
