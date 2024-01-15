package net.strangled.aldaris.catan;

import java.util.Map;
import java.util.EnumMap;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class Util {
	private static final String keyString = "key";
	private static final String valueString = "value";
	private Util() {
		//these are static utility functions
	}
	
	public static JsonArrayBuilder toJson(Map<Resource, Integer> map) {
		var resourceMap = Json.createArrayBuilder();
		for (Map.Entry<Resource, Integer> entry : map.entrySet()) {
			var entryObject = Json.createObjectBuilder();
			entryObject.add(keyString, entry.getKey().getId());
			entryObject.add(valueString, entry.getValue());
			resourceMap.add(entryObject);
		}
		return resourceMap;
		
	}
	public static Map<Resource, Integer> fromJson(JsonArray array) {
		EnumMap<Resource, Integer> map = new EnumMap<>(Resource.class);
		for (JsonValue resource : array) {
			var resourceObject = (JsonObject)resource;
			map.put(Resource.idToResource(resourceObject.getInt(keyString)), resourceObject.getInt(valueString));
		}
		return map;
	}

}
