package net.strangled.aldaris.catan.game;

import javax.json.JsonObject;

public class ProposeTrade extends Trade {
	protected ProposeTrade(CatanTrade trade) {
		super(trade);
	}
	protected ProposeTrade(JsonObject o) {
		super(o);
	}

	public static final int ID = 6;
	@Override
	public int getId() {
		return ID;
	}
	
	@Override
	public void apply(CatanGame cg) {
		cg.addTrade(getCatanTrade());
	}

	@Override
	public boolean canApply(CatanGame cg) {
		return super.canApply(cg) && cg.isValidTrade(getCatanTrade()); 
	}
}
