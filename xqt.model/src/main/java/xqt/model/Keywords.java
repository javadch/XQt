package xqt.model;

import java.util.HashMap;

public class Keywords {
	public static HashMap<String, String> attributeNames = new  HashMap<>();
	
	static {
		attributeNames.put("class", "qs_class");
		//attributeNames.put("type", "qs_type");
		//attributeNames.put("order", "qs_order");
		//attributeNames.put("field", "qs_field");
		attributeNames.put("int", "qs_int");
		attributeNames.put("long", "qs_long");
		attributeNames.put("float", "qs_float");
		attributeNames.put("string", "qs_string");
		attributeNames.put("public", "qs_public");
	}
}
