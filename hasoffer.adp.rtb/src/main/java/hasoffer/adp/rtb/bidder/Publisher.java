package hasoffer.adp.rtb.bidder;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A publisher for Aerospike based messages, sharable by multiple threads.
 * 
 */
public class Publisher implements Runnable {
	/** The objects thread */
	protected Thread me;
	/** The JEDIS connection used */
	String channel;
	/** The topic of messages */
	protected ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();

	/** Filename, if not using redis */
	protected String fileName;
	protected StringBuilder sb;
	protected ObjectMapper mapper;
	protected boolean errored = false;

	public Publisher() {
		
	}
	
	public Publisher(String address, String topic) throws Exception {

		me = new Thread(this);
		me.start();
	}
	
	public Publisher(String address) throws Exception {
		if (address.startsWith("file://")) {
			int i = address.indexOf("file://");
			if (i > -1) {
				address = address.substring(7);
			}
			this.fileName = address;
			mapper = new ObjectMapper();
			sb = new StringBuilder();
		} else {
			String [] parts = address.split("&");
		}
		me = new Thread(this);
		me.start();
	}

	public void runFileLogger() {
		Object obj = null;

		while (true) {
			try {
				Thread.sleep(60000);

				synchronized (sb) {
					if (sb.length() != 0) {
                        BufferedWriter bw = null;
                        bw = new BufferedWriter(new FileWriter(fileName, true));
                        bw.write(sb.toString());
                        bw.flush();
                        bw.close();
						sb.setLength(0);
					}
				}
			} catch (Exception error) {
				errored = true;
				try {
					BidderEngine.getInstance().sendLog(1, "Publisher:"+fileName,"Publisher log error on " + fileName + ", error = " + error.toString());
				} catch (Exception e) {
				}
				error.printStackTrace();
				sb.setLength(0);
			}
		}
	}

	public void run() {
			runFileLogger();
	}



	/**
	 * Add a message to the messages queue.
	 * 
	 * @param s String. JSON formatted message.
	 */
	public void add(Object s) {
		if (fileName != null) {
			if (errored)
				return;
			
			synchronized (sb) {
				sb.append(s);
				sb.append("\n");
			}
		} else
			queue.add(s);
	}
}