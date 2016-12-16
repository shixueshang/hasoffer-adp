package hasoffer.adp.rtb.tools;

import hasoffer.adp.rtb.adx.request.BidRequest;
import hasoffer.adp.rtb.common.Configuration;
import hasoffer.adp.rtb.common.Creative;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Class provides macro processing for the RTB4FREE system.
 *
 */
public class MacroProcessing {

	static Random random = new Random();
	/*
	 * {redirect_url}", config.redirectUrl); {pixel_url}",
	 * config.pixelTrackingUrl);
	 *
	 * {creative_forward_url}", creat.forwardurl); {creative_ad_price}",
	 * creat.strPrice); {creative_ad_width}", creat.strW);
	 * {creative_ad_height}", creat.strH); {creative_id}", creat.impid);
	 * {creative_image_url}", creat.imageurl); {site_id}", br.siteId);
	 * {lat}",lat); {lon}", lon); {site_domain}",br.siteDomain); {pub}",
	 * exchange); {bid_id}", oidStr); {ad_id}", adid);" + " replaceAll(sb,
	 * "%7Bpub%7D", exchange); replaceAll(sb, "%7Bbid_id%7D", oidStr);
	 * replaceAll(sb, "%7Bad_id%7D", adid); replaceAll(sb, "%7Bsite_id%7D",
	 * br.siteId); replaceAll(sb, "%7Bcreative_id%7D", creat.impid);
	 *
	 * replaceAll(sb, "%7Blat%7D", lat); replaceAll(sb, "%7Blon%7D", lon);
	 */

	static Set<String> macroList = new HashSet();
	static {
		macroList.add("{cachebuster}");
		macroList.add("%7Bcachebuster%7D");

		macroList.add("{redirect_url}");
		macroList.add("%7Bredirect_url%7D");
		macroList.add("{pixel_url}");
		macroList.add("%7Bpixel_url%7D");

		macroList.add("{creative_forward_url}");
		macroList.add("%7Bcreative_forward_url%7D");

		macroList.add("{creative_ad_price}");
		macroList.add("%7Bcreative_ad_price%7D");

		macroList.add("{creative_ad_width}");
		macroList.add("%7Bcreative_ad_width%7D");

		macroList.add("{creative_ad_height}");
		macroList.add("%7Bcreative_ad_height%7D");

		macroList.add("{creative_id}");
		macroList.add("%7Bcreative_id%7D");
		macroList.add("{imp}");
		macroList.add("%7Bimp%7D");

		macroList.add("{creative_image_url}");
		macroList.add("%7Bcreative_image_url%7D");

		macroList.add("{site_id}");
		macroList.add("%7Bsite_id%7D");

		macroList.add("{app_id}");
		macroList.add("%7Bapp_id%7D");

		macroList.add("{lat}");
		macroList.add("%7Blat%7D");

		macroList.add("{lon}");
		macroList.add("%7Blon%7D");

		macroList.add("{site_domain}");
		macroList.add("%7Bsite_domain%7D");

		macroList.add("{app_domain}");
		macroList.add("%7Bapp_domain%7D");

		macroList.add("{site_name}");
		macroList.add("%7Bsite_name%7D");

		macroList.add("{app_name}");
		macroList.add("%7Bapp_name%7D");

		macroList.add("{pub}");
		macroList.add("%7Bpub%7D");

		macroList.add("{exchange}");
		macroList.add("%7Bexchange%7D");

		macroList.add("{bid_id}");
		macroList.add("%7Bbid_id%7D");

		macroList.add("{bidder_ip}");
		macroList.add("%7Bbidder_ip%7D");

		macroList.add("{ad_id}");
		macroList.add("%7Bad_id%7D");

		macroList.add("{isp}");
		macroList.add("%7Bisp%7D");

		macroList.add("{brand}");
		macroList.add("%7brand%7D");
		macroList.add("{make}");
		macroList.add("%7Bmake%7D");

		macroList.add("{model}");
		macroList.add("%7Bmodel%7D");

		macroList.add("{os}");
		macroList.add("%7Bos%7D");

		macroList.add("{osv}");
		macroList.add("%7Bosv%7D");

		macroList.add("{timestamp}");
		macroList.add("%7Btimestamp%7D");

		macroList.add("{ip}");
		macroList.add("%7Bip%7D");

		macroList.add("{gps}");
		macroList.add("%7Bgps%7D");

		macroList.add("{ua}");
		macroList.add("%7Bua%7D");

		macroList.add("{publisher}");
		macroList.add("%7Bpublisher%7D");

		macroList.add("{adsize}");
		macroList.add("%7Badsize%7D");

		macroList.add("{app_bundle}");
		macroList.add("%7Bapp_bundle%7D");

		macroList.add("{ifa}");
		macroList.add("%7Bifa%7D");

		macroList.add("{dnt}");
		macroList.add("%7Bdnt%7D");

	}

	public static void replace(List<String> list, BidRequest br, Creative creat, String adid, StringBuilder sb)
			throws Exception {
		Configuration config = Configuration.getInstance();
		for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);
			switch (item) {
			case "{cachebuster}":
			case "%7Bcachebuster%7D":
				replaceAll(sb, item, Integer.toString(random.nextInt(Integer.SIZE-1)));
				break;

			case "{redirect_url}":
			case "%7Bredirect_url%7D":
				replaceAll(sb, item, config.redirectUrl);
				break;

			case "{pixel_url}":
			case "%7Bpixel_url%7D":
				replaceAll(sb, item, config.pixelTrackingUrl);
				break;

			case "{win_url}":
			case "%7Bwin_url%7D":
				replaceAll(sb, item, config.winUrl);
				break;

			case "{creative_forward_url}":
			case "%7Bcreative_forward_url%7D":
				replaceAll(sb, item, creat.forwardurl);
				break;

			case "{creative_ad_price}":
			case "%7Bcreative_ad_price%7D":
				replaceAll(sb, item, creat.strPrice);
				break;

			case "{creative_ad_width}":
			case "%7Bcreative_ad_width%7D":
				replaceAll(sb, item, creat.strW);
				break;

			case "{creative_ad_height}":
			case "%7Bcreative_ad_height%7D":
				replaceAll(sb, item, creat.strH);
				break;

			case "{creative_id}":
			case "%7Bcreative_id%7D":
			case "{imp}":
			case "%7Bimp%7D":
				replaceAll(sb, item, creat.impid);
				break;

			case "{creative_image_url}":
			case "%7Bcreative_image_url%7D":
				replaceAll(sb, item, creat.imageurl);
				break;

			case "{ad_id}":
			case "%7Bad_id%7D":
				replaceAll(sb, item, adid);
				break;


			case "{bidder_ip}":
			case "%7Bbidder_ip%7D}":
				replaceAll(sb, item, Configuration.ipAddress);
				break;

			}
		}
	}

	public static void findMacros(List<String> macros, String str) {
		if (str == null)
			return;
        macroList.stream().filter(what -> str.indexOf(what) > -1).filter(what -> macros.contains(what) == false).forEach(macros::add);
	}

	/**
	 * Replace All instances of a string.
	 *
	 * @param x
	 *            StringBuilder. The buffer to do replacements in.
	 * @param what
	 *            String. The string we are looking to replace.
	 * @param sub
	 *            String. The string to use for the replacement.
	 */
	public static void replaceAll(StringBuilder x, String what, String sub) {
		if (what == null || sub == null)
			return;
		int start = x.indexOf(what);
		if (start != -1) {
			x.replace(start, start + what.length(), sub);
			replaceAll(x, what, sub);
		}
	}
}
