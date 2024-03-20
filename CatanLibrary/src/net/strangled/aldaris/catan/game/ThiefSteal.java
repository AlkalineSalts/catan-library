package net.strangled.aldaris.catan.game;

import net.strangled.aldaris.catan.game.CatanGame.GameState;
import net.strangled.aldaris.catan.game.command.ChoosePlayer;
import net.strangled.aldaris.catan.game.command.RollDice;

public class ThiefSteal extends GameState {
	public static final int ID = -100;
	private ThiefSteal() {}
	private static ThiefSteal ts = new ThiefSteal();
	public static ThiefSteal getState() {return ts;}
	
	public int getId()
	{
		return ID;
	}
	@Override
	public GameState getNextState(CatanGame cg) {
		if (cg.getCommandHistory().size() > 1 && cg.getCommandHistory().get(0).getId() == ChoosePlayer.ID)
		{
			if (cg.getCommandHistory().stream().anyMatch(command -> command.getId() == RollDice.ID)) {
				return RegularPlayPostRoll.getState();
			} else {
				return RegularPlayPreRoll.getState();
			}
		} else {
			return this;
		}
		
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
