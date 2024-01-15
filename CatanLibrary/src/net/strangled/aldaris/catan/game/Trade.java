package net.strangled.aldaris.catan.game;

import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.Resource;
import net.strangled.aldaris.catan.Util;

public abstract class Trade extends Command {
	
	private final CatanTrade trade;
	
	protected Trade(CatanTrade trade) {
		super(trade.issuingPlayer());
		this.trade = trade;
	}

	protected Trade(JsonObject jObj) {
		super(jObj);
		trade = CatanTrade.fromJson(jObj.getJsonObject("trade"));
	}
	
	protected CatanTrade getCatanTrade() {
		return trade;
	}

	@Override
	public void _toJson(JsonObjectBuilder builder) {
		builder.add("trade", trade.toJson());
	}
	
	
	
	
	
}
