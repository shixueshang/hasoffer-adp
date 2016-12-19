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
 * 对于每一个竞价请求列出所有投放，选出匹配的创意，如果有多个则随机选择一个，如果没有则返回null
 * 
 */
public class CampaignSelector {

	static Random randomGenerator = new Random();

	Configuration config;

	static CampaignSelector theInstance;


	private CampaignSelector() {

	}

	/**
	 * 返回投放选择器的单实例
	 *
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

	public BidResponse getHeuristic(BidRequest br) throws Exception {
		long time = System.currentTimeMillis();

		Iterator<Campaign> it = config.campaignsList.iterator();
        List<SelectedCreative> candidates = new ArrayList();
        List<CampaignProcessor> tasks = new ArrayList();

		 CountDownLatch latch = new CountDownLatch(config.campaignsList.size());

		List<Campaign> list = new ArrayList<>();


		for (Campaign c : list) {
			CampaignProcessor p = new CampaignProcessor(c, br);
			tasks.add(p);
		}

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

	public void clear() {
		config.campaignsList.clear();
	}

	public int size() {
		return config.campaignsList.size();
	}

	public List<Campaign> getCampaigns() {
		return config.campaignsList;
	}
}
