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
	public boolean canApply(CatanGame cg, RegularPlayPreRoll r) {return canApply(cg);}
	@Override
	public boolean canApply(CatanGame cg, RegularPlayPostRoll r) {return canApply(cg);}
	@Override
	public void apply(CatanGame cg, RegularPlayPreRoll r) {apply(cg);}
	@Override
	public void apply(CatanGame cg, RegularPlayPostRoll r) {apply(cg);}
	
	
	private void apply(CatanGame cg) {
		cg.addTrade(getCatanTrade());
	}

	
	private boolean canApply(CatanGame cg) {
		var trade = super.getCatanTrade();
		return  (trade.issuingPlayer() == cg.getCurrentPlayer().intValue() || trade.recipient() == cg.getCurrentPlayer().intValue())
			&& cg.isValidTrade(getCatanTrade()); 
	}
}
