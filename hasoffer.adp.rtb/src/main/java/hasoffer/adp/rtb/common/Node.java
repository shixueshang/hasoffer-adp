package hasoffer.adp.rtb.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

/**
 * A class that implements a parse-able node in the RTB object, and applies
 * campaign logic. The idea of the node is to define a constraint using the
 * dotted form of the JSON specification of the bid request parameter. By
 * default if you specify a Node and the hierarchy does not exist in the bid
 * request, then this means the campaign does not match. You can override this
 * behavior by setting the object's 'notPresentOk' flag. Then when the hierarchy
 * doesn;t exist, the Node tests true otherwise if it is present, then the Node
 * returns the value of the comparison that was specified.
 * <p>
 * Examples Hierarchies:
 * <p>
 * Retrieve a value: 'user.geo.country' - this means the Node will extract this
 * field from the bid request.
 * <p>
 * Retrieve a value from a list: 'imp.0.id' is equivalent to JS: value =
 * imp[0].id;
 * <p>
 * You also specify what to compare the bid request's values to. In could be
 * that the return values are an array, or maybe just a scalar. The Node can
 * handle both data types.
 * <p>
 * The comparison operators are equal, not equal, lt, le, gt, ge Member of set,
 * not member of set, set intersects, not set intersects, geo in range lat/lon,
 * not in range of lat/lon, in the domain of a range of numbers, and not in the
 * range of numbers.
 * <p>
 *
 */
public class Node {
	
	
	public static Map<String, Map> builtinMap = new HashMap();

	boolean testit = false;

	public static final int QUERY = 0;

	public static final int EQUALS = 1;

	public static final int NOT_EQUALS = 2;

	public static final int MEMBER = 3;

	public static final int NOT_MEMBER = 4;

	public static final int INTERSECTS = 5;

	public static final int NOT_INTERSECTS = 6;

	public static final int INRANGE = 7;

	public static final int NOT_INRANGE = 8;

	public static final int LESS_THAN = 9;

	public static final int LESS_THAN_EQUALS = 10;

	public static final int GREATER_THAN = 11;

	public static final int GREATER_THAN_EQUALS = 12;

	public static final int DOMAIN = 13;

	public static final int NOT_DOMAIN = 14;

	public static final int STRINGIN = 15;

	public static final int NOT_STRINGIN = 16;
	/** Does an attribute exist in the rtb request */
	public static final int EXISTS = 17;
	/** Does an attribute not exist in the rtb reqest */
	public static final int NOT_EXISTS = 18;
	/** Does an attribute not exist in the rtb reqest */
	public static final int OR = 19;

	/**
	 * A convenient map to turn string operator references to their int
	 * conterparts
	 */
	public static Map<String, Integer> OPS = new HashMap();
	static {
		OPS.put("QUERY", QUERY);
		OPS.put("EQUALS", EQUALS);
		OPS.put("NOT_EQUALS", NOT_EQUALS);
		OPS.put("MEMBER", MEMBER);
		OPS.put("NOT_MEMBER", NOT_MEMBER);
		OPS.put("INTERSECTS", INTERSECTS);
		OPS.put("NOT_INTERSECTS", NOT_INTERSECTS);
		OPS.put("INRANGE", INRANGE);
		OPS.put("NOT_INRANGE", NOT_INRANGE);
		OPS.put("LESS_THAN", LESS_THAN);
		OPS.put("LESS_THAN_EQUALS", LESS_THAN_EQUALS);
		OPS.put("GREATER_THAN", GREATER_THAN);
		OPS.put("GREATER_THAN_EQUALS", GREATER_THAN_EQUALS);
		OPS.put("DOMAIN", DOMAIN);
		OPS.put("NOT_DOMAIN", NOT_DOMAIN);
		OPS.put("STRINGIN", STRINGIN);
		OPS.put("NOT_STRINGIN", NOT_STRINGIN);
		OPS.put("EXISTS", EXISTS);
		OPS.put("NOT_EXISTS", NOT_EXISTS);
		OPS.put("OR", OR);
	}

	public static List<String> OPNAMES = new ArrayList();
	static {
		OPNAMES.add("QUERY");
		OPNAMES.add("EQUALS");
		OPNAMES.add("NOT_EQUALS");
		OPNAMES.add("MEMBER");
		OPNAMES.add("NOT_MEMBER");
		OPNAMES.add("INTERSECTS");
		OPNAMES.add("NOT_INTERSECTS");
		OPNAMES.add("INRANGE");
		OPNAMES.add("NOT_INRANGE");
		OPNAMES.add("LESS_THAN");
		OPNAMES.add("LESS_THAN_EQUALS");
		OPNAMES.add("GREATER_THAN");
		OPNAMES.add("GREATER_THAN_EQUALS");
		OPNAMES.add("DOMAIN");
		OPNAMES.add("NOT_DOMAIN");
		OPNAMES.add("STRINGIN");
		OPNAMES.add("NOT_STRINGIN");
		OPNAMES.add("EXISTS");
		OPNAMES.add("NOT_EXISTS");
		OPNAMES.add("OR");
	}

	/** campaign identifier */
	public String name;
	/** dotted form of the item in the bid to pull (eg user.geo.lat) */
	transient public String hierarchy;
	/** which operator to use */
	transient public int operator = -1;
	/** the sub operator if operator is query */
	transient public int suboperator = -1;
	/** Node's value as an object. */
	public Object value;
	/** Node's value as a map */
	transient Map mvalue;
	/** The retrieved object from the bid, as defined in the hierarchy */
	transient protected Object brValue;

	/** when the value is a number */
	transient Number ival = null;
	/** when the value is a string */
	transient String sval = null;
	/** when the value is a set */
	transient Set qval = null;
	/** when the value is a map */
	transient Map mval = null;
	/** When the value is a list */
	transient List lval = null;

	/** if present will execute this JavaScript code */
	protected String code = null;
	/** text name of the operator */
	public String op;
	/** text name of the query sub op */
	transient String subop = null;

	/** set to false if required field not present */
	public boolean notPresentOk = true;
	/** decomposed hierarchy */
	public List<String> bidRequestValues = new ArrayList();

	/**
	 * Simple constructor useful for testing.
	 */
	public Node() {

	}

	public Node(String name, String hierarchy, String operator, Object value) throws Exception {

		this.name = name;
		this.hierarchy = hierarchy;
		op = operator;
		this.value = value;

		setBRvalues();
		setValues();

	}

	public Node(Map map) throws Exception {
		this.name = (String) map.get("name");
		Object test = map.get("op");
		op = (String) test;
		value = map.get("value");
		List brv = (List) map.get("bidRequestValues");
		hierarchy = "";
		for (int i = 0; i < brv.size() - 1; i++) {
			hierarchy += brv.get(i);
			hierarchy += ".";
		}
		hierarchy += brv.get(brv.size() - 1);

		setBRvalues();
		setValues();

	}

	/**
	 * Sets the values from the this.value object.
	 * 
	 * @throws Exception
	 *             if this.values is not a recognized object.
	 */
	public void setValues() throws Exception {
		if (op != null) {
			Integer x = OPS.get(op);
			if (x == null)
				throw new Exception("Unknown operator: " + op);
			operator = x;
		}

		/**
		 * If its an array, connvert to a list. Passing in arrays screws up
		 * membership and set operations which expect lists
		 */

		if (value instanceof String[] || value instanceof int[] || value instanceof double[]) {
			List list = new ArrayList();
			Object[] x = (Object[]) value;
            Collections.addAll(list, x);
			value = list;
		}

		if (value instanceof Integer || value instanceof Double) {
			ival = (Number) value;
		}
		if (value instanceof TreeSet)
			qval = (TreeSet) value;
		if (value instanceof String)
			sval = (String) value;
		if (value instanceof Map)
			mval = (Map) value;
        if (value instanceof List) {
            if (this.op.equals("OR") || this.operator == OR) {
                List x = (List) value;
                Node y;
                List newList = new ArrayList();
                for (int i = 0; i < x.size(); i++) {

                    Object test = x.get(i);
                    if (test instanceof LinkedHashMap) {
                        y = new Node((Map) test);
                    } else
                        y = (Node) test;
                    y.setValues();
                    newList.add(y);
                }
                value = newList;
                lval = (List) value;
            } else if (this.op.equals("QUERY") || this.operator == QUERY) {
                List x = (List) value;
                String source = (String) x.get(0);
                String name = (String) x.get(1);
                subop = (String) x.get(2);
                Object operand = x.get(3);
                resetFromMap(operand);
                suboperator = OPS.get(subop);
                if (source.equals("builtin")) {
                    value = builtinMap.get(name);
                }
            } else
                lval = (List) value;
        }

		StringBuilder sh = new StringBuilder();
		for (int i = 0; i < bidRequestValues.size(); i++) {
			sh.append(bidRequestValues.get(i));
			if (i + 1 < bidRequestValues.size()) {
				sh.append(".");
			}
		}
		
		
		hierarchy = sh.toString();
	}
	
	void resetFromMap(Object value) {
		if (value instanceof Integer || value instanceof Double) {
			ival = (Number) value;
		}
		if (value instanceof TreeSet)
			qval = (TreeSet) value;
		if (value instanceof String)
			sval = (String) value;
		if (value instanceof Map)
			mval = (Map) value;
		if (value instanceof List) { // convert ints to doubles
			lval = (List)value;
		}
	}

	/**
	 * Does this atrribute have this hierarchy
	 * 
	 * @param str
	 *            String. The string to test.
	 * @return true if the hierarchy matches the string
	 */
	public boolean equals(String str) {
		if (hierarchy == null) {
			hierarchy = "";
			for (int i = 0; i < bidRequestValues.size(); i++) {
				hierarchy += bidRequestValues.get(i);
				if (i + 1 != bidRequestValues.size()) {
					hierarchy += ".";
				}
			}
		}
		return str.equals(hierarchy);
	}

	/**
	 * Constructor for campaign node without attached JavaScript code
	 * 
	 * @param name
	 *            String. The name of the node.
	 * @param heirarchy
	 *            The dotted notation hierarchy associated with this node.
	 * @param operator
	 *            int. The operation to apply to the node.
	 * @param value
	 *            Object. The value that the bid request specified by hierarchy
	 *            will be tested against.
	 * @throws Exception
	 *             if the value object is not recognized.
	 */
	public Node(String name, String heirarchy, int operator, Object value) throws Exception {

		this(name, heirarchy, OPNAMES.get(operator), value); // fake this out so
																// we don't call
																// recursive
		this.operator = operator;
		setValues();
		this.op = OPNAMES.get(operator);
	}

	public Node(String name, String heirarchy, String operator, Object value, String code) throws Exception {
		this(name, heirarchy, operator, value);
		this.code = code;
	}

	/**
	 * Set the bidRequest values array from the hierarchy
	 */
	void setBRvalues() {
		if (hierarchy == null) // OR doesn't have a hierarchy
			return;

		String[] splitted = hierarchy.split("\\.");
        Collections.addAll(bidRequestValues, splitted);
	}

	/**
	 * Returns the value of the interrogate of the bid request.
	 * 
	 * @return Object. The value of the bid request derived from the query of
	 *         the hierarchy.
	 */
	public Object getBRvalue() {
		return brValue;
	}

	/**
	 * Return the integer value, if it is a number
	 * 
	 * @return Integer. The integer value, or null if not a number
	 */
	public Integer intValue() {
		if (ival == null)
			return null;
		return ival.intValue();
	}

	/**
	 * Return the double value, if it is a number
	 * 
	 * @return Double. The doublr value, or null if not a number
	 */
	public Double doubleValue() {
		if (ival == null)
			return null;
		return ival.doubleValue();
	}
	
	@JsonIgnore
	public String getLucene() {
		String stuff = "";
		
		if (value.toString().startsWith("@"))
			return null;
		
		String hr = this.hierarchy.replace("imp.0.", "imp.");
		hr = hr.replaceAll("exchange", "ext.exchange");
		
		if (this.notPresentOk) {
			stuff = "((-_exists_: " + hr + ") OR ";
		}
		
		
		String strValue = value.toString();
		strValue = strValue.replaceAll("/","\\\\/");
		
		switch (operator) {

		case QUERY:
			return null;

		case EQUALS:
			stuff += hr + ": " + strValue; 
			if (notPresentOk) stuff += ")";
			return stuff;
					
		case NOT_EQUALS:
			stuff += "-" + hr + ": " + strValue;
			if (notPresentOk) stuff += ")";
			return stuff;

		case STRINGIN:
			stuff +=  hr + ": \"" + strValue + "\"";
			if (notPresentOk) stuff += ")";
			return stuff;


		case NOT_STRINGIN:
			stuff += "-" + hr + ": \"" + strValue + "\"";
			if (notPresentOk) stuff += ")";
			return stuff;
			
		case NOT_INTERSECTS:
			if (value instanceof List) {
				String str = "(";
				List list = (List)value;
				for (int i=0; i<list.size();i++) {
					str += "-" + hr + ": *" + list.get(i) + "*";
					
					str = str.replaceAll("/","\\\\/");
					
					if (i + 1 < list.size()) {
						str += " OR ";
					}
				}
				str += ")";
				stuff += str;
				if (notPresentOk) stuff += ")";
				return stuff;
			}
			stuff += "-" + hr + ": " + strValue;
			if (notPresentOk) stuff += ")";
			return stuff;
				
		case INTERSECTS:
			if (value instanceof List) {
				String str = "(";
				List list = (List)value;
				for (int i=0; i<list.size();i++) {
					str +=  hr + ": *" + list.get(i) + "*";
					
					str = str.replaceAll("/","\\\\/");
					
					if (i + 1 < list.size()) {
						str += " OR ";
					}
				}
				str += ")";
				stuff += str;
				if (notPresentOk) stuff += ")";
				return stuff;
			}
			stuff += hr + ": *" + strValue + "*";
			if (notPresentOk) stuff += ")";
			return stuff;
			
		case MEMBER:
			if (value instanceof List) {
				String str = "(";
				List list = (List)value;
				for (int i=0; i<list.size();i++) {
					str +=  hr + ": *" + list.get(i) + "*";
					
					str = str.replaceAll("/","\\\\/");
					
					
					if (i + 1 < list.size()) {
						str += " OR ";
					}
				}
				str += ")";
				stuff += str;
				if (notPresentOk) stuff += ")";
				return stuff;
			}
			stuff +=  hr + ": *" + strValue + "*";
			if (notPresentOk) stuff += ")";
			return stuff;
			
		case NOT_MEMBER:
			if (value instanceof List) {
				String str = "(";
				List list = (List)value;
				for (int i=0; i<list.size();i++) {
					str += "-" + hr + ": *" + list.get(i) + "*";
					
					str = str.replaceAll("/","\\\\/");
					
					
					if (i + 1 < list.size()) {
						str += " OR ";
					}
				}
				str += ")";
				stuff += str;
				if (notPresentOk) stuff += ")";
				return stuff;
			} 
			
			stuff += "-" + hr + ": *" +  strValue + "*";
			if (notPresentOk) stuff += ")";
			return stuff;

		case INRANGE:
			List o = (List)value;
			stuff += hr + ": [" + o.get(0)  + " TO " + o.get(1) + "]";
			if (notPresentOk) stuff += ")";
			return stuff;
			
		case NOT_INRANGE:
			List list = (List)value;
			stuff +=  "-" + hr + ": [" + list.get(0)  + " TO " + list.get(1) + "]";
			if (notPresentOk) stuff += ")";
			return stuff;

		case DOMAIN:
			list = (List)value;	
			stuff += "(" + hr + "< " + list.get(1) + " AND " + hr + "> " + list.get(0) + ")";
			if (notPresentOk) stuff += ")";
			return stuff;
			
			
		case NOT_DOMAIN:
			list = (List)value;	
			stuff += "(" + hr + "> " + list.get(1) + " OR " + hr + "< " + list.get(0) + ")";
			if (notPresentOk) stuff += ")";
			return stuff;

		case LESS_THAN:
			stuff += hr + "< " + strValue;
			if (notPresentOk) stuff += ")";
			return stuff;
			
		case LESS_THAN_EQUALS:
			stuff +=  hr + "<= " + strValue;
			if (notPresentOk) stuff += ")";
			return stuff;

		case GREATER_THAN:
			stuff =  hr + "< " + strValue;
			if (notPresentOk) stuff += ")";
			return stuff;
			
		case GREATER_THAN_EQUALS:
			stuff += hr + ">= " + strValue;
			if (notPresentOk) stuff += ")";
			return stuff;

		case EXISTS:
			return "_exists_: " + hr;
			
		case NOT_EXISTS:
			return "_missing_: " + hr;
		}
		
		return "";

	}
}
