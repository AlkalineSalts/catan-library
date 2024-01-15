package net.strangled.aldaris.catan.game;
import net.strangled.aldaris.catan.game.CatanGame.GameState;

import java.util.HashMap;


public class GameStateFactory {
	private static final GameStateFactory gsFactory = new GameStateFactory();
	private HashMap<Integer, GameState> idToGameState;
	private GameStateFactory() {
		idToGameState = new HashMap<>();
		idToGameState.put(GameEnded.ID, GameEnded.getState());
		idToGameState.put(GameStart.ID, GameStart.getState());
		idToGameState.put(RegularPlayPreRoll.ID, RegularPlayPreRoll.getState());
		idToGameState.put(RegularPlayPostRoll.ID, RegularPlayPostRoll.getState());
	}
	public GameState getGameState(int id) {
		var state = idToGameState.get(id);
		if (state == null) {
			throw new IllegalArgumentException(id + " is not a valid number");
		}
		return state;
	}
	public static GameStateFactory get() {
		return gsFactory;
	}
}
