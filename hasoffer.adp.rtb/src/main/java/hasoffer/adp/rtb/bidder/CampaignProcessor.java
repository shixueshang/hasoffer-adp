package hasoffer.adp.rtb.bidder;


import hasoffer.adp.rtb.adx.request.BidRequest;
import hasoffer.adp.rtb.common.Campaign;
import hasoffer.adp.rtb.common.Creative;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CampaignProcessor. Given a campaign, process it into a bid. The
 * CampaignSelector creates a CampaignProcessor, which is given a bid request
 * and a campaign to analyze. The CampaignSelector creates one CampaignProcessor
 * for each Campaign in the system. The Selector creates Future tasks and calls
 * the processor. The call() method loops through the Nodes that define the
 * constraints of the campaign.
 * 
 */
public class CampaignProcessor implements Runnable {
	static Random randomGenerator = new Random();

	Campaign camp;

	BidRequest br;

	SelectedCreative selected = null;

	Thread me = null;

	boolean done = false;

	public CampaignProcessor(Campaign camp, BidRequest br) {
		this.camp = camp;
		this.br = br;
	}

	public void start() {
		me = new Thread(this);
		me.start();
	}

	public void run() {

		if (camp == null)
			return;
		
		List<Creative> candidates = new ArrayList();

		Map<String,String> capSpecs = new ConcurrentHashMap();


		if (candidates.size() == 0) {
			done = true;
			return;
		}
		
		int index = 0;
		
		if (candidates.size() > 1)
			index = randomGenerator.nextInt(candidates.size());

		Creative creative = candidates.get(index);
		selected = new SelectedCreative(camp, creative);
		done = true;
	}

}
