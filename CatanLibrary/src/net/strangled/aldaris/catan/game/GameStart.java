package net.strangled.aldaris.catan.game;

import java.util.HashMap;

import net.strangled.aldaris.catan.game.CatanGame.GameState;
import net.strangled.aldaris.catan.game.command.EndTurn;

public class GameStart extends GameState {
	public static final int ID = 1;
	private int[] getNextTurnPlayerIndex; //does not count as state
	private static final GameStart gs = new GameStart();
	private GameStart() {
	}
	public static GameStart getState() {
		return gs;
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
	public int getId() {
		return ID;
	}
	@Override
	public GameState getNextState(CatanGame cg) {
			if (getNextTurnPlayerIndex == null) {
			//array has an index of number of end turns to the player index of the current player
			//example result of for loop for 4 players [0, 1, 2, 3, 4, 5, 6, 7] -> [0, 1, 2, 3, 3, 2, 1, 0]
				int numberOfPlayers = cg.getPlayerData().size();
				getNextTurnPlayerIndex = new int[numberOfPlayers * 2];
					for (int i = 0; i < numberOfPlayers * 2; i++) {
						getNextTurnPlayerIndex[i] = i < numberOfPlayers ? i : numberOfPlayers * 2 - (i + 1);
					}
				}
			int numberOfEndTurns = 0;
			for (Command c : cg.getCommandHistory()) {if (c.getId() == EndTurn.ID) numberOfEndTurns++;}
			
			
			if (numberOfEndTurns == getNextTurnPlayerIndex.length) {
				cg.setCurrentPlayerIndex(0);
				cg.getCatanBoard().startGame();
				return GameStateFactory.get().getGameState(RegularPlayPreRoll.ID);
			}
			else {
				cg.setCurrentPlayerIndex(getNextTurnPlayerIndex[numberOfEndTurns]);
				return this;
			}
		
	}
	

}
