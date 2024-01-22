package net.strangled.aldaris.catan.game;

import net.strangled.aldaris.catan.game.CatanGame.GameState;

public class ThiefSteal extends GameState {
	public static final int ID = -100;
	public int getId()
	{
		return ID;
	}
	@Override
	public GameState getNextState(CatanGame cg) {
		// TODO Auto-generated method stub
		return this;
	}
	@Override
	public boolean canApply(Command command, CatanGame catanGame) {
		return command.canApply(catanGame, this);
	}
	@Override
	public void apply(Command command, CatanGame catanGame) {
		command.canApply(catanGame, this);
	}
	
}
