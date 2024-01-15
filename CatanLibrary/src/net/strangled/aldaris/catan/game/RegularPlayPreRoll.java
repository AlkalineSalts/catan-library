package net.strangled.aldaris.catan.game;

import java.util.List;

import net.strangled.aldaris.catan.game.CatanGame.GameState;

public class RegularPlayPreRoll extends RegularPlay {
	public static final int ID = 2;
	private static final RegularPlayPreRoll rppr = new RegularPlayPreRoll();
	public static RegularPlayPreRoll getState() {return rppr;}
	
	private RegularPlayPreRoll () {}
	@Override
	public int getId() {
		return ID;
	}

	@Override
	public GameState getNextState(CatanGame cg) {
		var commandHistory = cg.getCommandHistory();
		if (!commandHistory.isEmpty() && commandHistory.get(0).getId() == RollDice.ID) {
			return GameStateFactory.get().getGameState(RegularPlayPostRoll.ID);
		}
		else {
			return this;
		}
	}

}
