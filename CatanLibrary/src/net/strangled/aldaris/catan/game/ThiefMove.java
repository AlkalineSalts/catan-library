package net.strangled.aldaris.catan.game;

import net.strangled.aldaris.catan.game.CatanGame.GameState;

public class ThiefMove extends GameState {
	public static final int ID = 6;
	@Override
	public int getId() {
		return ID;
	}

	@Override
	public GameState getNextState(CatanGame cg) {
		return null;
	}

	@Override
	public boolean canApply(Command command, CatanGame catanGame) {
		command.apply(catanGame, this);
		return true;
	}

	@Override
	public void apply(Command command, CatanGame catanGame) {
		command.apply(catanGame, this);

	}

}
