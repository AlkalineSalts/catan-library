package net.strangled.aldaris.catan.game;

import javax.json.JsonObject;

public class AcceptTrade extends Trade {
	public static final int ID = 7;
	public AcceptTrade(CatanTrade trade) {
		super(trade);
	}

	public AcceptTrade(JsonObject jObj) {
		super(jObj);
	}

	@Override
	public int getId() {
		return ID;
	}
	@Override
	public boolean canApply(CatanGame cg) {
		return super.canApply(cg) && cg.hasBeenProposed(getCatanTrade());
	}
	
	@Override
	public void apply(CatanGame cg) {
		cg.acceptTrade(getCatanTrade());
	}

}
