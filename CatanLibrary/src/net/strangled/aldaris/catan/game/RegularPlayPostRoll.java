package net.strangled.aldaris.catan.game;

import java.util.List;

import net.strangled.aldaris.catan.game.CatanGame.GameState;

public class RegularPlayPostRoll extends RegularPlay{
	public static final int ID = 3;
	private static final RegularPlayPostRoll rppr = new RegularPlayPostRoll();
	public static RegularPlayPostRoll getState() {return rppr;}
	
	@Override
	public int getId() {
		return ID;
	}

	@Override
	public GameState getNextState(CatanGame cg) {
		List<Command> commandHistory = cg.getCommandHistory();
		//TODO : innacurate end condition, fix later
		if (!commandHistory.isEmpty() && commandHistory.get(0).getId() == EndTurn.ID) {
			//end turn go back to start
			cg.setCurrentPlayerIndex(cg.getCurrentPlayerIndex() + 1 % cg.getPlayerOrder().size());
			return GameStateFactory.get().getGameState(RegularPlayPreRoll.ID);
		} 
		else {
			return this;
		}
	}

}
