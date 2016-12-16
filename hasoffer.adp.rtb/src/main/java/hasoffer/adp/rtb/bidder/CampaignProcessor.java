package hasoffer.adp.rtb.bidder;


import hasoffer.adp.rtb.adx.request.BidRequest;
import hasoffer.adp.rtb.common.Campaign;
import hasoffer.adp.rtb.common.Configuration;
import hasoffer.adp.rtb.common.Creative;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * CampaignProcessor. Given a campaign, process it into a bid. The
 * CampaignSelector creates a CampaignProcessor, which is given a bid request
 * and a campaign to analyze. The CampaignSelector creates one CampaignProcessor
 * for each Campaign in the system. The Selector creates Future tasks and calls
 * the processor. The call() method loops through the Nodes that define the
 * constraints of the campaign. If all of the Nodes test() method returns true,
 * then the call() returns a SelectedCreative object that identifies the
 * campaign and the creative within that campaign, that the caller will use to
 * create a bid response. However, if any Node test returns false the call()
 * function will return null - meaning the campaign is not applicable to the
 * bid.
 * 
 */
public class CampaignProcessor implements Runnable {
	static Random randomGenerator = new Random();

	Campaign camp;

	BidRequest br;

	SelectedCreative selected = null;

	Thread me = null;

	boolean done = false;

	AbortableCountDownLatch latch;

	CountDownLatch flag;

	/**
	 * Constructor.
	 * 
	 * @param camp
	 *            Campaign. The campaign to process
	 * @param br
	 *            . BidRequest. The bid request to apply to this campaign.
	 */
	public CampaignProcessor(Campaign camp, BidRequest br, CountDownLatch flag,
                             AbortableCountDownLatch latch) {
		this.camp = camp;
		this.br = br;
		this.latch = latch;
		this.flag = flag;
		
		if (latch != null)
			start();
	}

	public void start() {
		me = new Thread(this);
		me.start();
	}

	public void run() {
		boolean printNoBidReason = Configuration.getInstance().printNoBidReason;
		int logLevel = 5;
		if (printNoBidReason ) {
			printNoBidReason = true;
		}

		if (flag != null) {
			try {
				flag.await();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				if (latch != null)
					latch.countNull();
				return;
			}
		}

		if (camp == null) {
			if (latch != null)
				latch.countNull();
			return;
		}
		
		List<Creative> candidates = new ArrayList();

		Map<String,String> capSpecs = new ConcurrentHashMap();


		if (candidates.size() == 0) {
			done = true;
			if (latch != null)
				latch.countNull();
			return;
		}
		
		int index = 0;
		
		if (candidates.size() > 1)
			index = randomGenerator.nextInt(candidates.size());
		
		if (printNoBidReason) {
			String str = "";
			for (Creative c : candidates) {
				str += c.impid + " ";
			}

		}
		
		
		Creative creative = candidates.get(index);
		selected = new SelectedCreative(camp, creative);
		selected.capSpec = capSpecs.get(creative.impid);
		done = true;

		if (latch != null)
			latch.countDown(selected); 
	}

	/**
	 * Is the campaign processing done?
	 * 
	 * @return boolean. Returns true when the processing is complete.
	 */
	public boolean isDone() {
		return done;
	}

	/**
	 * Terminate the thread processing if c == true.
	 * 
	 * @param c
	 *            boolean. Set to true to cancel
	 */
	public void cancel(boolean c) {
		if (c)
			me.interrupt();
	}


}
