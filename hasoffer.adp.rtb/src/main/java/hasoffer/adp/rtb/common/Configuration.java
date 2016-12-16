package hasoffer.adp.rtb.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import hasoffer.adp.rtb.adx.request.BidRequest;
import hasoffer.adp.rtb.adx.request.ForensiqClient;
import hasoffer.adp.rtb.bidder.RTBServer;
import hasoffer.adp.rtb.redis.RedisClient;
import hasoffer.adp.rtb.tools.MacroProcessing;

import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The singleton class that makes up the Configuration object. A configuration
 * is a JSON file that describes the campaigns and operational parameters needed
 * by the bidding engine.
 * 
 * All classes needing config data retrieve it here.
 * 
 */

public class Configuration {
	/** The singleton instance */
	static volatile Configuration theInstance;
	
	public static String ipAddress = null;

	/** The url of this bidder */
	public String url;
	/** Set to true to see why the bid response was not bid on */
	public boolean printNoBidReason = false;
	/** The campaign watchdog timer */
	public long timeout = 80;
	/** The standard name of this instance */
	public static String instanceName = "default";
	/** The exchange seat ids used in bid responses */
	public Map<String, String> seats;
	/** the configuration item defining seats and their endpoints */
	public List<Map<String, Object>> seatsList;
	/** The campaigns used to make bids */
	public List<Campaign> campaignsList = new ArrayList<Campaign>();
	/** An empty template for the exchange formatted message */
	public Map template = new HashMap();
	/** Standard pixel tracking URL */
	public String pixelTrackingUrl;
	/** Standard win URL */
	public String winUrl;
	/** The redirect URL */
	public String redirectUrl;

	/** Macros found in the templates */
	public List<String> macros = new ArrayList<>();
	/** The templates by by their exchange name */
	public Map<String, String> masterTemplate = new HashMap<>();

	public String data;

	/** Test bid request for fraud */
	public static ForensiqClient forensiq;


	public RedisClient redisson;


	private Configuration() throws Exception {

	}

    public static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

	public static void reset() {
		theInstance = null;
	}

	/**
	 * Clear the config entries to default state,
	 */
	public void clear() {
		url = null;
		campaignsList.clear();
	}

    /**
     * Return the configuration instance.
     *
     * @return The instance.
     */
    public static Configuration getInstance() {
        if (theInstance == null)
            throw new RuntimeException("Please initialize the Configuration instance first.");
        return theInstance;
    }

    /**
     * Return the instance of Configuration, and if necessary, instantiates it first.
     */
    public static Configuration getInstance(String data) throws Exception {
        if (theInstance == null) {
            synchronized (Configuration.class) {
                if (theInstance == null) {
                    theInstance = new Configuration();
                }
                    theInstance.initialize(data);
            }
        }
        return theInstance;
    }


    public void initialize(String data) throws Exception {

		Map<?, ?> requestData = mapper.readValue(data, Map.class);

		seats = new HashMap<String, String>();
		
		InetAddress localMachine = InetAddress.getLocalHost();
        ipAddress = localMachine.getHostAddress();


		/**
         * 接入的adx是哪一家
		 *  {
         *   "name": "smaato",
         *   "id": "seat1",
         *   "bid": "/rtb/bids/smaato=com.xrtb.exchanges.Smaato"
         *   }
		 */
		seatsList = (List<Map<String, Object>>) requestData.get("seats");
        if(seatsList != null){
            for (Map<String, Object> x : seatsList) {
                String name = (String) x.get("name");
                String id = (String) x.get("id");
                seats.put(name, id);

                String className = (String) x.get("bid");
                String parts[] = className.split("=");
                String uri = parts[0];
                className = parts[1];
                Class<?> c = Class.forName(className);
                BidRequest br = (BidRequest) c.newInstance();
                if (br == null) {
                    throw new Exception("Could not make new instance of: " + className);
                }
                RTBServer.exchanges.put(uri, br);
            }
        }

		encodeTemplates();
		encodeTemplateStubs();

		campaignsList.clear();

	}


	/**
	 * 处理宏
	 */
	public void encodeTemplates() throws Exception {
		Map m = (Map) template.get("exchange");
		if (m == null)
			return;
		Set set = m.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = (String) m.get(key);
			MacroProcessing.findMacros(macros, value);
		}

		MacroProcessing.findMacros(macros, "{creative_ad_width} {creative_ad_height}");
	}

	/**
	 * For each of the seats, find out which template to use
	 */
	void encodeTemplateStubs() {
		Map m = (Map) template.get("exchange");
		String defaultStr = (String) template.get("default");

		Iterator<String> sr = seats.keySet().iterator();
		while (sr.hasNext()) {
			String key = sr.next();
			String value = (String) m.get(key);
			if (value == null)
				masterTemplate.put(key, defaultStr);
			else
				masterTemplate.put(key, value);
		}

	}


	/**
	 * Is the configuration object initialized.
	 * 
	 * @return boolean. Returns true of initialized, else returns false.
	 */
	public static boolean isInitialized() {
		if (theInstance == null)
			return false;
		return true;

	}

	/**
	 * Returns an input stream from the file of the given name.
	 * 
	 * @param fname
	 *            String. The fully qualified file name.
	 * @return InputStream. The stream to read from.
	 * @throws Exception
	 *             on file errors.
	 */
	public static InputStream getInputStream(String fname) throws Exception {
		File f = new File(fname);
		return  new FileInputStream(f);
	}



	public boolean deleteCampaign(String owner, String name) throws Exception {
		boolean delta = false;
		if ((owner == null || owner.length() == 0)) {
			campaignsList.clear();
			return true;
		}

		List<Campaign> deletions = new ArrayList();
		Iterator<Campaign> it = campaignsList.iterator();
		while (it.hasNext()) {
			Campaign c = it.next();
			if (owner.equals("root") || c.owner.equals(owner)) {
				if (name.equals("*") || c.adId.equals(name)) {
					deletions.add(c);
					delta = true;
					if (!name.equals("*"))
						break;
				}
			}
		}

        deletions.forEach(campaignsList::remove);
		return delta;
	}


	public void deleteCampaignCreative(String owner, String name, String crid) throws Exception {

		Iterator<Campaign> it = campaignsList.iterator();
		while (it.hasNext()) {
			Campaign c = it.next();
			if (c.owner.equals(owner) && c.adId.equals(name)) {
				for (Creative cr : c.creatives) {
					if (cr.impid.equals(crid)) {
						c.creatives.remove(cr);
						return;
					}
				}
				throw new Exception("No such creative found");
			}
		}
		throw new Exception("No such campaign found");
	}



	public void addCampaign(Campaign c) throws Exception {
		if (c == null)
			return;

		for (int i = 0; i < campaignsList.size(); i++) {
			Campaign test = campaignsList.get(i);
			if (test.adId.equals(c.adId) && test.owner.equals(c.owner)) {
				campaignsList.remove(i);
				break;
			}
		}

		c.encodeCreatives();
		c.encodeAttributes();
		campaignsList.add(c);

	}

	/**
	 * Is the identified campaign running?
	 *
	 * @param owner
	 *            String. The campaign owner
	 * @param name
	 *            String. The campaign adid.
	 * @return boolean. Rewturns true if it is loaded, else false.
	 */
	public boolean isRunning(String owner, String name) {
		for (Campaign c : campaignsList) {
			if (c.owner.equals(owner) && c.adId.equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a list of all the campaigns that are running
	 *
	 * @return List. The list of campaigns, byadIds, that are running.
	 */
	public List<String> getLoadedCampaignNames() {
        return  campaignsList.stream().map(c -> c.adId).collect(Collectors.toList());
	}

	/**
	 * Add a campaign to the campaigns list using the shared map database of
	 * campaigns
	 */
	public void addCampaign(String owner, String name) throws Exception {

	}

	/**
	 * Return your IP address by posting to api.externalip.net
	 * @return String. The IP address of this instance.
	 */
	public static String getIpAddress() {
		URL myIP;
		try {
			myIP = new URL("http://api.externalip.net/ip/");

			BufferedReader in = new BufferedReader(new InputStreamReader(myIP.openStream()));
			return in.readLine();
		} catch (Exception e) {
			try {
				myIP = new URL("http://myip.dnsomatic.com/");

				BufferedReader in = new BufferedReader(new InputStreamReader(myIP.openStream()));
				return in.readLine();
			} catch (Exception e1) {
				try {
					myIP = new URL("http://icanhazip.com/");

					BufferedReader in = new BufferedReader(new InputStreamReader(myIP.openStream()));
					return in.readLine();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}

		return null;
	}
}
