package net.strangled.aldaris.catan.game;

import net.strangled.aldaris.catan.game.CatanGame.GameState;
import net.strangled.aldaris.catan.game.command.ChooseResource;
import net.strangled.aldaris.catan.game.command.RollDice;

public class Monopoly extends GameState {
	private static final Monopoly monopoly = new Monopoly();
	public static Monopoly getState() {return monopoly;}
	public static final int ID = 366;
	@Override
	public int getId() {
		return ID;
	}

	@Override
	public GameState getNextState(CatanGame cg) {
		var history = cg.getCommandsDoneThisTurn();
		if (!history.isEmpty() && history.get(0).getId() == ChooseResource.ID) {
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
