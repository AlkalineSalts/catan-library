package net.strangled.aldaris.catan.game;

import net.strangled.aldaris.catan.game.CatanGame.GameState;
import net.strangled.aldaris.catan.game.command.MoveThiefTo;
import net.strangled.aldaris.catan.game.command.RollDice;

public class ThiefMove extends GameState {
	private static ThiefMove tm = new ThiefMove();
	public static final ThiefMove getState() {return tm;}
	public static final int ID = 6;
	@Override
	public int getId() {
		return ID;
	}

	@Override
	public GameState getNextState(CatanGame cg) {
		var history = cg.getCommandsDoneThisTurn();
		if (history.size() > 1 && history.get(0).getId() == MoveThiefTo.ID) {
			return ThiefSteal.getState();
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
