package hasoffer.adp.rtb.bidder;

import hasoffer.adp.rtb.redis.RedisClient;

/**
 * A class used to stop runaway bidders. If deadmanswitch is set in the startup json, it must be present in the
 * REDIs store before the bidder will bid. The switch is set by the accounting program.
 *
 */
public class DeadmanSwitch implements Runnable {
	
	RedisClient redisson;
	Thread me;
	String key;
	boolean sentStop = false;
	public static boolean testmode = false;
	
	
	public DeadmanSwitch(RedisClient redisson, String key) {
		this.redisson = redisson;
		this.key = key;
		me = new Thread(this);
		me.start();
	}

	
	public DeadmanSwitch() {
		
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				if (canRun() == false) {
					if (sentStop == false) {
						try {
							if (!testmode) {
								//TODO STOP BIDDER
							} else {
								System.out.println("Deadman Switch is thrown");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					sentStop = true;
				} else {
					sentStop = false;
				}
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public String getKey() {
		return key;
	}
	
	public boolean canRun() {
		String value = redisson.get(key, 0);
		if (value == null) {
			return false;
		}
		sentStop = false;
		return true;
	}
}
