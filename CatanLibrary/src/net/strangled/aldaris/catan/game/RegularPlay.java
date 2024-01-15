package net.strangled.aldaris.catan.game;
import java.util.ArrayList;
import java.util.List;

import net.strangled.aldaris.catan.game.CatanGame.GameState;

public abstract class RegularPlay extends GameState {

	

	@Override
	public boolean canDoCommand(CatanGame cg, Command command) {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean haveDiceRolledSinceLastEndTurn(List<Command> commands) {
		//expects commands to be delivered as most recent first
		//checks this by seeing if it can encounter a rolldice before an endturn
		for (Command command : commands) {
			if (command.getId() == RollDice.ID) {
				return  true;
			}
			else if (command.getId() == EndTurn.ID) {
				return false;
			}
		}
		return false;
	}
	

}
