package hasoffer.adp.rtb.bidder;


import hasoffer.adp.rtb.adx.request.BidRequest;
import hasoffer.adp.rtb.adx.response.BidResponse;
import hasoffer.adp.rtb.common.Campaign;
import hasoffer.adp.rtb.common.Configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * A singleton object that is used to select campaigns based on a given bid
 * request. The selector, through the get() request will determine which
 * campaigns/creatives match a bid request. If there is more than one creative
 * found, then one is selected at random, and then the BidRequest object is
 * returned. If no campaign matched, then null is returned.
 * 
 * @author Ben M. Faul
 * 
 */
public class CampaignSelector {

	static Random randomGenerator = new Random();
	/** The configuration object used in this selector */
	Configuration config;

	/** The instance of the singleton */
	static CampaignSelector theInstance;

	public static volatile int highWaterMark = 100;

	/**
	 * Empty private constructor.
	 */
	private CampaignSelector() {

	}

	/**
	 * Returns the singleton instance of the campaign selector.
	 * 
	 * @return CampaignSelector. The object that selects campaigns
	 * @throws Exception
	 *             if there was an error loading the configuration file.
	 */
	public static CampaignSelector getInstance() throws Exception {
		if (theInstance == null) {
			synchronized (CampaignSelector.class) {
				if (theInstance == null) {
					theInstance = new CampaignSelector();
					theInstance.config = Configuration.getInstance();
				}
			}
		}
		return theInstance;
	}

	/**
	 * Given a bid request, select a campaign to use in bidding. This method
	 * will create a list of Future tasks, each given a campaign and the bid
	 * request, which will then determine of the campaign is applicable to the
	 * request. If more than one campaign matches, then more than one Future
	 * task will return a non-null object 'SelectedCreative' which can be used
	 * to make a bid, in the multiples case one of the SelectedCreatives is
	 * chosen at random, then the bid response is created and returned.
	 * 
	 * @param br
	 *            BidRequest. The bid request object of an RTB bid request.
	 * @return Campaign. The campaign to use to construct the response.
	 */
	public BidResponse getHeuristic(BidRequest br) throws Exception {
		long time = System.currentTimeMillis();
		int logLevel = 5;

		Iterator<Campaign> it = config.campaignsList.iterator();
        List<SelectedCreative> candidates = new ArrayList();
        List<CampaignProcessor> tasks = new ArrayList();

		 CountDownLatch latch = new CountDownLatch(config.campaignsList.size());

		List<Campaign> list = new ArrayList<>();


		for (Campaign c : list) {
			CampaignProcessor p = new CampaignProcessor(c, br, null, null);
			tasks.add(p);
		}

		// 13%
		long start = System.currentTimeMillis();

		try {
			latch.await();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

        candidates.addAll(tasks.stream().filter(proc -> proc.selected != null).map(proc -> proc.selected).collect(Collectors.toList()));

		if (candidates.size() == 0)
			return null;

		int index = randomGenerator.nextInt(candidates.size());


		return null;
	}



	/**
	 * Hueristic adjustment
	 */
	public static void adjustHighWaterMark() {
		if (RTBServer.avgBidTime > 30) {
			if (highWaterMark > Configuration.getInstance().campaignsList.size())
				highWaterMark = Configuration.getInstance().campaignsList.size();
			highWaterMark -= 5;
		} else {
			if (highWaterMark < Configuration.getInstance().campaignsList.size())
				highWaterMark += 1;
			else
				highWaterMark = Configuration.getInstance().campaignsList.size();
		}

	}


	/**
	 * Clear all the campaigns of the selector.
	 */
	public void clear() {
		config.campaignsList.clear();
	}

	/**
	 * Returns the number of campaigns in the selector.
	 * 
	 * @return int. The number of campaigns in use by the selector.
	 */
	public int size() {
		return config.campaignsList.size();
	}

	/**
	 * Returns the set of campaigns in this selector object.
	 * 
	 * @return List. The campaigns set.
	 */
	public List<Campaign> getCampaigns() {
		return config.campaignsList;
	}
}
