package net.strangled.aldaris.catan.board;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import net.strangled.aldaris.catan.JsonSerializable;
import net.strangled.aldaris.catan.math.Line;

public class CatanLine implements JsonSerializable {
	private static final String ownerIdStr = "ownerID";
	private final Line line;
	private Integer ownerId;
	public CatanLine(Line line) {
		this.line = line;
		ownerId = null;
	}
	public CatanLine(JsonObject lineObject) {
		line = new Line(lineObject.getJsonObject("line"));
		if (lineObject.get(ownerIdStr).getValueType() == JsonValue.ValueType.NULL) {
			ownerId = null;
		} else {
			ownerId = lineObject.getInt(ownerIdStr);
		}
				
	}
	@Override
	public int hashCode() {
		return line.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CatanLine other = (CatanLine) obj;
		if (line == null) {
			if (other.line != null)
				return false;
		} else if (!line.equals(other.line))
			return false;
		return true;
	}
	public Line getLine() {
		return line;
	}
	public boolean hasOwner() {
		return ownerId != null;
	}
	public Integer getOwner() {
		return ownerId;
	}
	void setOwner(Integer ownerId) {
		this.ownerId = ownerId;
	}
	@Override
	public JsonObjectBuilder toJson() {
		var builder = Json.createObjectBuilder().add("line", line.toJson());
		if (ownerId == null) 
			builder.addNull(ownerIdStr);
		else
			builder.add(ownerIdStr, ownerId.intValue());
		return builder;
	}
	
	
}
