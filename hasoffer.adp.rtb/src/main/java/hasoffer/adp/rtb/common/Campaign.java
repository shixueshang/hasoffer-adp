package hasoffer.adp.rtb.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;


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


	public Creative getCreative(String crid) {
		for (Creative c : creatives) {
			if (c.impid.equals(crid)) {
				return c;
			}
		}
		return null;
	}

	

	public void encodeCreatives() throws Exception {

        creatives.forEach(hasoffer.adp.rtb.common.Creative::encodeUrl);
	}
	

	public void encodeAttributes() throws Exception {

		
		if (category == null) {
			category = new ArrayList<>();
		}
		
		if (category.size()>0) {
			String str = "\"cat\":" + mapper.writer().withDefaultPrettyPrinter().writeValueAsString(category);
			encodedIab = new StringBuilder(str);
		}
	}


	@Override
	public int compareTo(Object o) {
		Campaign other = (Campaign)o;
		if (this.adId.equals(other.adId))
			return 1;
		
		return 0;
	}

	public String toJson() {
		try {
			return mapper.writer().withDefaultPrettyPrinter().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
