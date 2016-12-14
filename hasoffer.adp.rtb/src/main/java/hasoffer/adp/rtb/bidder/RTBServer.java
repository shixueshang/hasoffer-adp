package hasoffer.adp.rtb.bidder;

import hasoffer.adp.rtb.adx.request.BidRequest;
import hasoffer.adp.rtb.adx.request.ForensiqClient;
import hasoffer.adp.rtb.common.Campaign;
import hasoffer.adp.rtb.common.Configuration;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.GZIPOutputStream;

/**
 * A JAVA based RTB2.2 server.<br>
 * This is the RTB Bidder's main class. It is a Jetty based http server that
 * encapsulates the Jetty server. The class is Runnable, with the Jetty server
 * joining in the run method. This allows other parts of the bidder to interact
 * with the server, mainly to obtain status information from a command sent via
 * REDIS.
 * <p>
 * Prior to calling the RTBServer the configuration file must be Configuration
 * instance needs to be created - which is a singleton. The RTBServer class (as
 * well as many of the other classes also use configuration data in this
 * singleton.
 * <p>
 * A Jetty based Handler class is used to actually process all of the HTTP
 * requests coming into the bidder system.
 * <p>
 * Once the RTBServer.run method is invoked, the Handler is attached to the
 * Jetty server.
 * 
 */
public class RTBServer implements Runnable {

	/** Period for updateing performance stats in redis */
	public static final int PERIODIC_UPDATE_TIME = 60000;

	/** The HTTP code for a bid object */
	public static final int BID_CODE = 200; // http code ok
	/** The HTTP code for a no-bid objeect */
	public static final int NOBID_CODE = 204; // http code no bid

	/** The percentage of bid requests to consider for bidding */
	public static AtomicLong percentage = new AtomicLong(100);

	/** Indicates of the server is not accepting bids */
	public static boolean stopped = false;
	
	public static boolean paused = false;

	/** number of threads in the jetty thread pool */
	public static int threads = 512;

	public static long request = 0;
	/** Counter for number of bids made */
	public static long bid = 0;
	/** Counter for number of nobids made */
	public static long nobid = 0;
	/** Number of errors in accessing the bidder */
	public static long error = 0;
	/** Number of actual requests */
	public static long handled = 0;
	/** Number of unknown accesses */
	public static long unknown = 0;
	/** The configuration of the bidder */
	/** The number of win notifications */
	public static long win = 0;
	/** The number of clicks processed */
	public static long clicks = 0;
	/** The number of pixels fired */
	public static long pixels = 0;
	/** The average time */
	public static long avgBidTime;
	/** Fraud counter */
	public static long fraud = 0;
	/** xtime counter */
	public static long xtime = 0;

	/** double adpsend */
	public static volatile double adspend;
	/** is the server ready to receive data */
	boolean ready;

	static long deltaTime = 0, deltaWin = 0, deltaClick = 0, deltaPixel = 0, deltaNobid = 0, deltaBid = 0;
	static double qps = 0;
	static double avgx = 0;

	static AtomicLong totalBidTime = new AtomicLong(0);
	static AtomicLong bidCountWindow = new AtomicLong(0);

	static Server server;

	/**
	 * The bidder's main thread for handling the bidder's actibities outside of
	 * the JETTY processing
	 */
	Thread me;
	
	/** Trips right before the join with jetty */
	CountDownLatch startedLatch = null;

	/** The campaigns that the bidder is using to make bids with */
	static CampaignSelector campaigns;

	/** Bid target to exchange class map */
	public static Map<String, BidRequest> exchanges = new HashMap<>();

	/**
	 * Class instantiator of the RTBServer.
	 * 
	 * @param data
	 */
	public RTBServer(String data) throws Exception {

		Configuration.reset();
		AddShutdownHook hook = new AddShutdownHook();
		hook.attachShutDownHook();

		Configuration.getInstance(data);
		campaigns = CampaignSelector.getInstance();

		kickStart();
	}

	void kickStart()  {
		startedLatch = new CountDownLatch(1);
		me = new Thread(this);
		me.start();
		try {
			startedLatch.await();
			Thread.sleep(2000);
		} catch (Exception error) {
			try {
			} catch (Exception e) {
				System.out.println("Fatal error: " + error.toString());
			}
			me.interrupt();
		}
	}

	/**
	 * Returns whether the server has started.
	 * 
	 * @return boolean. Returns true if ready to start.
	 */
	public boolean isReady() {
		return ready;
	}

	public static void panicStop() {
		try {
			//BidderEngine.getInstance().sendShutdown();
			Thread.sleep(100);

		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error x) {
			x.printStackTrace();
		}
	}

	/**
	 * Set summary stats.
	 */
	public static void setSummaryStats() {
		if (xtime == 0)
			avgx = 0;
		else
			avgx = (nobid + bid) / (double) xtime;

		if (System.currentTimeMillis() - deltaTime < 30000)
			return;
		deltaWin = win - deltaWin;
		deltaClick = clicks - deltaClick;
		deltaPixel = pixels - deltaPixel;
		deltaBid = bid - deltaBid;
		deltaNobid = nobid - deltaNobid;
		qps = (deltaWin + deltaClick + deltaPixel + deltaBid + deltaNobid);
		long secs = (System.currentTimeMillis() - deltaTime) / 1000;
		qps = qps / secs;
		deltaTime = System.currentTimeMillis();
	}

	/**
	 * Establishes the HTTP Handler, creates the Jetty server and attaches the
	 * handler and then joins the server. This method does not return, but it is
	 * interruptable by calling the halt() method.
	 * 
	 */
	@Override
	public void run() {
		
		QueuedThreadPool threadPool = new QueuedThreadPool(threads, 50);

		server = new Server(threadPool);
		ServerConnector connector = null;
			connector = new ServerConnector(server);
			connector.setPort(8080);
			connector.setIdleTimeout(60000);

			server.setConnectors(new Connector[] { connector });

		Handler handler = new Handler();

		try {
			SessionHandler sh = new SessionHandler();

			sh.setHandler(handler);

			server.setHandler(sh); // set session handle
			

			if (Configuration.getInstance().cacheHost != null) {

			/**
			 * Quickie tasks for periodic logging
			 */
			Runnable redisupdater = () -> {
				try {
					while (true) {
					//	BidderEngine.getInstance().setMemberStatus(getStatus());
						Thread.sleep(5000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
			Thread nthread = new Thread(redisupdater);
			nthread.start();
			}

			Runnable task = () -> {
				long count = 0;
				while (true) {
					try {
						
						RTBServer.paused = true; // for a short time, send no-bids, this way any queues needing to drain have a chance to do so
						
						avgBidTime = totalBidTime.get();
						double window = bidCountWindow.get();
						if (window == 0)
							window = 1;
						avgBidTime /= window;

						long a = ForensiqClient.forensiqXtime.get();
						long b = ForensiqClient.forensiqCount.get();

						ForensiqClient.forensiqXtime.set(0);
						ForensiqClient.forensiqCount.set(0);
						totalBidTime.set(0);
						bidCountWindow.set(0);

						server.getThreadPool().isLowOnThreads();
						if (b == 0)
							b = 1;


						CampaignSelector.adjustHighWaterMark();
						
						Thread.sleep(100);
						RTBServer.paused = false;
						Thread.sleep(PERIODIC_UPDATE_TIME);

					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}
			};
			Thread thread = new Thread(task);
			thread.start();

			if (Configuration.getInstance().deadmanSwitch != null) {
				if (Configuration.getInstance().deadmanSwitch.canRun() == false) {
					RTBServer.stopped = true;
				}
			}

			server.start();

			Thread.sleep(500);

			ready = true;
			deltaTime = System.currentTimeMillis(); // qps timer

			startedLatch.countDown();
			server.join();
		} catch (Exception error) {

				error.printStackTrace();
		}
	}

	/**
	 * Returns the sertver's campaign selector used by the bidder. Generally
	 * used by javascript programs.
	 * 
	 * @return CampaignSelector. The campaign selector object used by this
	 *         server.
	 */
	public CampaignSelector getCampaigns() {
		return campaigns;
	}

	/**
	 * Stop the RTBServer, this will cause an interrupted exception in the run()
	 * method.
	 */
	public void halt() {
		try {
			me.interrupt();
		} catch (Exception error) {

		}
		try {
			server.stop();
			while (server.isStopped() == false)
				;
		} catch (Exception error) {
			error.printStackTrace();
		}

	}

	/**
	 * Is the Jetty server running and processing HTTP requests?
	 * 
	 * @return boolean. Returns true if the server is running, otherwise false
	 *         if null or isn't running
	 */
	public boolean isRunning() {
		if (server == null)
			return false;

		return server.isRunning();
	}

}

/**
 * JETTY handler for incoming bid request.
 * 
 * This HTTP handler processes RTB2.2 bid requests, win notifications, click
 * notifications, and simulated exchange transactions.
 * <p>
 * Based on the target URI contents, several actions could be taken. A bid
 * request can be processed, a file resource read and returned, a click or pixel
 * notification could be processed.
 * 
 * @author Ben M. Faul
 * 
 */
@MultipartConfig
class Handler extends AbstractHandler {

	private static final Configuration config = Configuration.getInstance();
	Random rand = new Random();

	/**
	 * Handle the HTTP request. Basically a list of if statements that
	 * encapsulate the various HTTP requests to be handled. The server makes no
	 * distinction between POST and GET and ignores DELETE>
	 * <p>>
	 * 
	 * @throws java.io.IOException
	 *             if there is an error reading a resource.
	 * @throws javax.servlet.ServletException
	 *             if the container encounters a servlet problem.
	 */
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Headers","Content-Type");

		InputStream body = request.getInputStream();
		String type = request.getContentType();
		BidRequest br = null;
		String json = "{}";
		String id = "";
		Campaign campaign = null;
		boolean unknown = true;
		RTBServer.handled++;
		int code = RTBServer.BID_CODE;
		baseRequest.setHandled(true);
		long time = System.currentTimeMillis();

		response.setHeader("X-INSTANCE", config.instanceName);

		try {

				RTBServer.request++;

				BidRequest x = RTBServer.exchanges.get(target);

				if (x == null) {
					json = "Wrong target: " + target + " is not configured.";
					code = RTBServer.NOBID_CODE;

					RTBServer.error++;
					baseRequest.setHandled(true);
					response.setStatus(code);
					response.setHeader("X-REASON", json);
					response.getWriter().println("{}");
					return;
				}

				time = System.currentTimeMillis() - time;

				response.setHeader("X-TIME",Long.toString(time));
				RTBServer.xtime += time;

				response.setContentType("application/json;charset=utf-8"); //   "application/json;charset=utf-8");
				if (code == 204) {
					response.setHeader("X-REASON", json);
					if (Configuration.getInstance().printNoBidReason)
						System.out.println("No bid: " + json);
					//response.setStatus(br.returnNoBidCode());
				}
				
				baseRequest.setHandled(true);
				if (unknown)
					RTBServer.unknown++;

				if (code == 200) {
					RTBServer.totalBidTime.addAndGet(time);
					RTBServer.bidCountWindow.incrementAndGet();
					response.setStatus(code);
					//bresp.writeTo(response);
				} else {
					//br.writeNoBid(response,time);
				}
				return;
			} catch (Exception e1) {
            e1.printStackTrace();
        }

			if (target.contains("/rtb/win")) {
				StringBuffer url = request.getRequestURL();
				String queryString = request.getQueryString();
				response.setStatus(HttpServletResponse.SC_OK);
				json = "";
				if (queryString != null) {
					url.append('?');
					url.append(queryString);
				}
				String requestURL = url.toString();

				try {
					//json = WinObject.getJson(requestURL);
					if (json == null) {
						
					}
					RTBServer.win++;
				} catch (Exception error) {
					response.setHeader("X-ERROR",
                            "Error processing win response");
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					error.printStackTrace();
				}
				response.setContentType("text/html;charset=utf-8");
				baseRequest.setHandled(true);
				response.getWriter().println(json);
				return;
			}



			if (target.contains("/redirect")) {
                try {
                    BidderEngine.getInstance().publishClick(target);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                StringBuffer url = request.getRequestURL();
				String queryString = request.getQueryString();
				String params[] = null;
				if (queryString != null)
					params = queryString.split("url=");

				baseRequest.setHandled(true);
				if (params != null)
					response.sendRedirect(params[1]);
				RTBServer.clicks++;
				return;
			}

			if (target.contains("info")) {
				response.setContentType("text/javascript;charset=utf-8");
				response.setStatus(HttpServletResponse.SC_OK);
				baseRequest.setHandled(true);
			/*	Echo e = RTBServer.getStatus();
				String rs = e.toJson();
				response.getWriter().println(rs);*/
				return;
			}

			RTBServer.error++;

			return;
		}

	public void sendResponse(HttpServletResponse response, String html) throws Exception {

		try {
			byte [] bytes = compressGZip(html);
			response.addHeader("Content-Encoding", "gzip");
			int sz = bytes.length;
			response.setContentLength(sz);
			response.getOutputStream().write(bytes);

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getOutputStream().println("");
		}
	}
	
	 private static byte[] compressGZip(String uncompressed) throws Exception {
          ByteArrayOutputStream baos  = new ByteArrayOutputStream();
          GZIPOutputStream gzos       = new GZIPOutputStream(baos);
   
          byte [] uncompressedBytes   = uncompressed.getBytes();
   
          gzos.write(uncompressedBytes, 0, uncompressedBytes.length);
          gzos.close();
   
          return baos.toByteArray();
  }

}


class AddShutdownHook {
	public void attachShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				RTBServer.panicStop();
			}
		});
		System.out.println("*** Shut Down Hook Attached. ***");
	}
}