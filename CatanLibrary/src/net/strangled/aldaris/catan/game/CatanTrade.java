package net.strangled.aldaris.catan.game;

import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.JsonSerializable;
import net.strangled.aldaris.catan.Resource;
import net.strangled.aldaris.catan.Util;

public record CatanTrade(int issuingPlayer, Map<Resource, Integer> forThis, int recipient, Map<Resource, Integer> willGiveThis) implements JsonSerializable{

	
	@Override
	public int hashCode() {
		return issuingPlayer * 10 + recipient;
	}




	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CatanTrade other = (CatanTrade) obj;
		if (forThis == null) {
			if (other.forThis != null)
				return false;
		} else if (!forThis.equals(other.forThis))
			return false;
		if (issuingPlayer != other.issuingPlayer)
			return false;
		if (recipient != other.recipient)
			return false;
		if (willGiveThis == null) {
			if (other.willGiveThis != null)
				return false;
		} else if (!willGiveThis.equals(other.willGiveThis))
			return false;
		return true;
}



	@Override
	public JsonObjectBuilder toJson() {
		var builder = Json.createObjectBuilder();
		builder.add("issuingPlayer", issuingPlayer);
		builder.add("recipient", recipient);
		builder.add("willGiveThis", Util.toJson(willGiveThis));
		builder.add("forThis", Util.toJson(forThis));
		return builder;
	}
	
	public static CatanTrade fromJson(JsonObject tradeObject) {
		int issuingPlayer = tradeObject.getInt("issuingPlayer");
		int recipient = tradeObject.getInt("recipient");
		var willGiveThis = Util.fromJson(tradeObject.getJsonArray("willGiveThis"));
		var forThis = Util.fromJson(tradeObject.getJsonArray("forThis"));
		return new CatanTrade(issuingPlayer, forThis, recipient, willGiveThis);
	}
	
}
