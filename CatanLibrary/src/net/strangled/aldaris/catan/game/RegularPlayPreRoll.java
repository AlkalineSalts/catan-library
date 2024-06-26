package net.strangled.aldaris.catan.game;

import java.util.List;

import net.strangled.aldaris.catan.game.CatanGame.GameState;
import net.strangled.aldaris.catan.game.command.RollDice;

public class RegularPlayPreRoll extends GameState {
	public static final int ID = 2;
	private static final RegularPlayPreRoll rppr = new RegularPlayPreRoll();
	public static RegularPlayPreRoll getState() {return rppr;}
	
	private RegularPlayPreRoll () {}
	@Override
	public int getId() {
		return ID;
	}
	@Override
	public boolean canApply(Command command, CatanGame catanGame) {
		return command.canApply(catanGame, this);
	}
	@Override
	public void apply(Command command, CatanGame catanGame) {
		command.apply(catanGame, this);
	}
	@Override
	public GameState getNextState(CatanGame cg) {
		var commandHistory = cg.getCommandHistory();
		if (!commandHistory.isEmpty() && commandHistory.get(0).getId() == RollDice.ID) {
			RollDice rd = (RollDice)commandHistory.get(0);
			if (rd.getRolledNumber() == 7) {
				return GameStateFactory.get().getGameState(DiscardPhase.ID).getNextState(cg); //will give the discard phase 
			}
			else 
			{return GameStateFactory.get().getGameState(RegularPlayPostRoll.ID);}
		}
		else {
			return this;
		}
	}

}
