package hasoffer.adp.rtb.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that implements a campaign. Provide the campaign with evaluation
 * Nodes (a stack) and a bid request, and this campaign will determine if the
 * bid request in question matches this campaign.
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Campaign implements Comparable {
	
	/** points back to the name of the owner of the campaign */
	public String owner = null;
	/** The id of the campaign */
	public String adId = "default-campaign";
	/** The campaign name */
	public String name;
	/** The default ad domain */
	public String adomain = "default-domain";
	/** The list of creatives for this campaign */
	public List<Creative> creatives = new ArrayList<>();
	/** Start and end date for this campaign */
	public List<Integer> date = new ArrayList<>();
	/** IAB Categories */
	public List<String> category;
	/** encoded IAB category */
	public transient StringBuilder encodedIab;	
	/** Should you do forensiq fingerprinting for this campaign? */
	public boolean forensiq = false;
	
	/**
	 * Empty constructor, simply takes all defaults, useful for testing.
	 */
	public Campaign() {

	}

    public static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
	
	public Campaign(String data) throws Exception {

		Campaign camp = mapper.readValue(data, Campaign.class);
		this.adomain = camp.adomain;
		this.creatives = camp.creatives;
		this.date = camp.date;
		this.adId = camp.adId;
		this.name = camp.name;
		this.owner = camp.owner;
		this.forensiq = camp.forensiq;
		if (camp.category != null)
			this.category = camp.category;
		
		encodeCreatives();
		encodeAttributes();	
	}


    /**
	 * Get a creative of this campaign.
	 * @param crid: String. The creative id.
	 * @return Creative. The creative or null;
	 */
	public Creative getCreative(String crid) {
		for (Creative c : creatives) {
			if (c.impid.equals(crid)) {
				return c;
			}
		}
		return null;
	}
	
	/**
	 * Creates a copy of this campaign
	 * @return Campaign. A campaign that is an exact clone of this one
	 * @throws Exception on JSON parse errors.
	 */
	public Campaign copy() throws Exception {

		String str =  mapper.writer().writeValueAsString(this);
		Campaign x = mapper.readValue(str, Campaign.class);
		x.encodeAttributes();
		return x;
	}

	
	/**
	 * Enclose the URL fields. GSON doesn't pick the 2 encoded fields up, so you have to make sure you encode them.
	 * This is an important step, the WIN processing will get mangled if this is not called before the campaign is used.
	 * Configuration.getInstance().addCampaign() will call this for you.
	 */
	public void encodeCreatives() throws Exception {

        creatives.forEach(hasoffer.adp.rtb.common.Creative::encodeUrl);
	}
	
	/**
	 * Encode the values of all the attributes, instantiating from JSON does not do this, it's an incomplete serialization
	 * Always call this if you add a campaign without using Configuration.getInstance().addCampaign();
	 * @throws Exception if the attributes of the node could not be encoded.
	 */
	public void encodeAttributes() throws Exception {

		
		if (category == null) {
			category = new ArrayList<>();
		}
		
		if (category.size()>0) {
			String str = "\"cat\":" + mapper.writer().withDefaultPrettyPrinter().writeValueAsString(category);
			encodedIab = new StringBuilder(str);
		}
	}

	/**
	 * The compareTo method to ensure that multiple campaigns
	 * don't exist with the same id.
	 * @param o Object. The object to compare with.
	 * @return int. Returns 1 if the ids match, otherwise 0.
	 */
	@Override
	public int compareTo(Object o) {
		Campaign other = (Campaign)o;
		if (this.adId.equals(other.adId))
			return 1;
		
		return 0;
	}
	
	/**
	 * Returns this object as a JSON string
	 * @return String. The JSON representation of this object.
	 */
	public String toJson() {
		try {
			return mapper.writer().withDefaultPrettyPrinter().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
