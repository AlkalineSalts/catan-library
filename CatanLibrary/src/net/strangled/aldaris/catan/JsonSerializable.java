package net.strangled.aldaris.catan;
import javax.json.JsonObjectBuilder;
public interface JsonSerializable {
	public JsonObjectBuilder toJson();
}
