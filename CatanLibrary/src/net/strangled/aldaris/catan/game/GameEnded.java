package net.strangled.aldaris.catan.game;

import net.strangled.aldaris.catan.game.CatanGame.GameState;

public class GameEnded extends GameState {
	public static final int ID = -1;
	private static final GameEnded ge = new GameEnded();
	private GameEnded() {}
	public static GameEnded getState() {
		return ge;
	}
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return ID;
	}

	@Override
	public boolean canDoCommand(CatanGame cg, Command command) {
		// No commands can be entered, game is over
		return false;
	}

	@Override
	public GameState getNextState(CatanGame cg) {
		// TODO Auto-generated method stub
		return null;
	}

}
