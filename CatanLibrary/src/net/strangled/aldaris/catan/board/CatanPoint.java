package net.strangled.aldaris.catan.board;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import net.strangled.aldaris.catan.JsonSerializable;
import net.strangled.aldaris.catan.math.Point;
public class CatanPoint implements JsonSerializable{
	private static final String ownedByStr = "ownedBy";
	private final Point location;
	private Integer ownedBy; //id of the owning player, null if unowned
	private boolean isCity;
	public CatanPoint(Point location) {
		this.location = location;
		ownedBy = null;
	}
	public CatanPoint(JsonObject pointObject) {
		location = new Point(pointObject.getJsonObject("location"));
		if (pointObject.get(ownedByStr).getValueType() == JsonValue.ValueType.NULL) {
			ownedBy = null;
		}
		else {
			ownedBy = pointObject.getInt(ownedByStr);
		}
		isCity = pointObject.getBoolean("isCity");
	}
	void makeCity() {
		if (ownedBy == null) {throw new IllegalStateException("cannot change city state if is a settlement");}
		isCity = true;
	}
	void unmakeCity() {
		if (ownedBy == null) {throw new IllegalStateException("cannot change city state if is not a settlement");}
		isCity = false;
	}
	public boolean isCity() {
		return ownedBy != null && isCity;
	}
	public Point getLocation() {
		return location;
	}
	public boolean hasOwner() {
		return ownedBy != null;
	}
	public Integer getOwner() {
		return ownedBy;
	}
	void setOwner(Integer i) {
		ownedBy = i;
	}
	@Override
	public JsonObjectBuilder toJson() {
		var builder = Json.createObjectBuilder().add("location", location.toJson());
		if (ownedBy == null) {
			builder.addNull(ownedByStr);
		} else {
			builder.add(ownedByStr, ownedBy.intValue());
		}
		builder.add("isCity", isCity);
		return builder;
	}
	@Override
	public int hashCode() {
		return location.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CatanPoint other = (CatanPoint) obj;
		if (isCity != other.isCity)
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (ownedBy == null) {
			if (other.ownedBy != null)
				return false;
		} else if (!ownedBy.equals(other.ownedBy))
			return false;
		return true;
	}
	
	
}
