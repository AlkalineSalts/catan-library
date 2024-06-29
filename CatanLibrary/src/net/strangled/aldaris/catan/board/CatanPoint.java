package net.strangled.aldaris.catan.board;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.AbstractMap.SimpleImmutableEntry;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import net.strangled.aldaris.catan.JsonSerializable;
import net.strangled.aldaris.catan.Resource;
import net.strangled.aldaris.catan.math.Point;
public class CatanPoint implements JsonSerializable{
	private static final String ownedByStr = "ownedBy";
	private static final String tradeRatioStr = "tradeRatio";
	private static final String tradeResourceStr = "tradeResource";
	
	private final Point location;
	private Integer ownedBy; //id of the owning player, null if unowned
	private boolean isCity;
	
	private final SimpleImmutableEntry<Resource, Integer> tradeRatio; //used for foreign trade
	private final Optional<Map.Entry<Resource, Integer>> optional; //transient, no need to serialize
	/*
	 * point - a location on the board, cannot be null
	 * tradeRatio -  map entry containing a resource and the amount needed to trade for one of any other resouce
	 * If null, this tile has no special trade ratio. If the resource is null, then the trade can be with any resource, provided the player
	 * has at least integer copies of it.
	 */
	public CatanPoint(Point location, Map.Entry<Resource, Integer> tradeRatio) {
		this.location = location;
		ownedBy = null;
		if (tradeRatio == null) {
			this.tradeRatio = null;
		}
		else if (tradeRatio.getValue() == null) {
			throw new IllegalArgumentException("if specifying a trade value, must include an amount of the resource necessary to be traded for the other resource");
		}
		else {
			//if trade ratio exists and is valid, make immutable
			this.tradeRatio = new SimpleImmutableEntry<>(tradeRatio.getKey(), tradeRatio.getValue());
		}
		optional = Optional.ofNullable(this.tradeRatio);
	}
	
	public CatanPoint(Point location) {
		this(location, null);
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
		
		//deals with trade ratio
		if (pointObject.isNull(tradeRatioStr)) {
			tradeRatio = null;
		}
		else {
			var tradeObject = pointObject.getJsonObject(tradeRatioStr);
			Resource resource = tradeObject.isNull(tradeResourceStr) ? null : Resource.valueOf(tradeObject.getString(tradeResourceStr));
			tradeRatio = new SimpleImmutableEntry<>(resource, tradeObject.getInt("amount"));
		}
		optional = Optional.ofNullable(tradeRatio);
		
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
	/*
	 * Returns an optional immutable map entry containing a resource and the amount needed to trade for one of any other resouce
	 * If it has no value, this tile has no special trade ratio. If the resource is null, then the trade can be with any resource, provided the player
	 * has at least integer copies of it.
	 */
	
	public Optional<Map.Entry<Resource, Integer>> getTradeRatio() {
		return optional;
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
		
		//deals with the trade ratio
		if (tradeRatio == null) {
			builder.addNull(tradeRatioStr);
		}
		else {
			var mapBuilder = Json.createObjectBuilder();
			if (tradeRatio.getKey() == null) {
				mapBuilder.addNull(tradeResourceStr);
			}
			else {
				mapBuilder.add(tradeResourceStr, tradeRatio.getKey().name());
			}
			mapBuilder.add("amount", tradeRatio.getValue().intValue());
			builder.add(tradeRatioStr, mapBuilder);
		}
		
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
		if (tradeRatio == null && other.tradeRatio != null) 
			return false;
		else if (tradeRatio != null && !tradeRatio.equals(other.tradeRatio))
			return false;
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
