package net.strangled.aldaris.catan.board;

import java.util.HashMap;

import net.strangled.aldaris.catan.board.CatanBoard.BoardState;

public class BoardStateFactory {
	private static BoardStateFactory factory = new BoardStateFactory();
	private HashMap<Integer, BoardState> intToBoardState;
	private BoardStateFactory() {
		intToBoardState = new HashMap<>();
		addBoardState(StartGame.getBoardState());
		addBoardState(RegularGame.getBoardState());
	}
	private void addBoardState(BoardState bs) {
		if (intToBoardState.containsKey(bs.getId())) {
			throw new IllegalStateException(String.format("attempted to add %s with id %d but %s already has that id", bs, bs.getId(), intToBoardState.get(bs.getId())));
		}
		intToBoardState.put(bs.getId(), bs);
	}
	public BoardState getState(Integer stateId) {
		var boardState = intToBoardState.get(stateId);
		if (boardState == null)
				throw new IllegalArgumentException(stateId.intValue() + "does not refer to a valid state");
		return boardState;
		
	}
	public static BoardStateFactory get() {
		return factory;
	}
}
