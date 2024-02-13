package net.strangled.aldaris.catan.game;

import net.strangled.aldaris.catan.game.CatanGame.GameState;
import net.strangled.aldaris.catan.game.command.ChooseResource;
import net.strangled.aldaris.catan.game.command.RollDice;

public class YearOfPlenty extends GameState {
	public static final int ID = 365;
	private static final YearOfPlenty ypp = new YearOfPlenty();
	public static YearOfPlenty getState() {return ypp;}
	
	
	@Override
	public int getId() {
		return ID;
	}

	@Override
	public GameState getNextState(CatanGame cg) {
		var history = cg.getCommandsDoneThisTurn();
		if (history.size() >= 2 && history.get(0).getId() == ChooseResource.ID && history.get(1).getId() == ChooseResource.ID) {
			if (history.stream().anyMatch(command -> command.getId() == RollDice.ID)) {
				return RegularPlayPostRoll.getState();
			} else {
				return RegularPlayPreRoll.getState();
			}
		}
		return this;
	}

	@Override
	public boolean canApply(Command command, CatanGame catanGame) {
		return command.canApply(catanGame, this);
	}

	@Override
	public void apply(Command command, CatanGame catanGame) {
		command.apply(catanGame, this);
	}

}
