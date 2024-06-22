package net.strangled.aldaris.catan.game.command;
import javax.json.JsonObject;

import net.strangled.aldaris.catan.game.CatanGame;
import net.strangled.aldaris.catan.game.CatanTrade;
import net.strangled.aldaris.catan.game.RegularPlayPostRoll;
import net.strangled.aldaris.catan.game.RegularPlayPreRoll;

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
	
	private boolean canApply(CatanGame cg) {return cg.hasBeenProposed(getCatanTrade()) || (isPlayersTurn(cg) && cg.getBestForeignTrade(getCatanTrade()).isPresent());}
	private void apply(CatanGame cg) {
			cg.acceptTrade(getCatanTrade());
		}
	
	@Override
	public boolean canApply(CatanGame cg, RegularPlayPreRoll g) {
		return canApply(cg);
	}
	
	@Override
	public void apply(CatanGame cg, RegularPlayPreRoll g) {
		apply(cg);
	}
	
	@Override
	public boolean canApply(CatanGame cg, RegularPlayPostRoll g) {
		return canApply(cg);
	}
	
	@Override
	public void apply(CatanGame cg, RegularPlayPostRoll g) {
		apply(cg);
	}

}
