package net.strangled.aldaris.catan.game.command;


import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.game.CatanTrade;
import net.strangled.aldaris.catan.game.Command;

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
