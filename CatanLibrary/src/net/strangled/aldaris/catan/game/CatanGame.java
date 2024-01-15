package net.strangled.aldaris.catan.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.DevelopmentCard;
import net.strangled.aldaris.catan.JsonSerializable;
import net.strangled.aldaris.catan.board.CatanBoard;
import net.strangled.aldaris.catan.math.Line;
import net.strangled.aldaris.catan.math.Point;

public class CatanGame implements JsonSerializable {
	private static final LinkedList<DevelopmentCard> defaultCardList = new LinkedList<>();
	static {
		for (int i = 0; i < 14; i++) {
			defaultCardList.add(DevelopmentCard.KNIGHT);
			if (i < 2) {
				defaultCardList.add(DevelopmentCard.ROAD_BUILDING);
				defaultCardList.add(DevelopmentCard.YEAR_OF_PLENTY);
				defaultCardList.add(DevelopmentCard.MONOPOLY);
			}
			if (i < 5) {defaultCardList.add(DevelopmentCard.VICTORY_POINT);}
		}
	}
	public static List<DevelopmentCard> getDefaultCards() {return (List<DevelopmentCard>) defaultCardList.clone();}
	
	private CatanBoard catanBoard;
	
	private List<DevelopmentCard> developmentCardDeck;
	private int developmentCardPosition;
	
	private final List<Integer> playerOrder; //is an unmodifiable list
	private Map<Integer, Player> playerData;
	private int currentPlayerIndex;
	private List<Command> history; //history is displayed back to front, the oldest command is the farthest from the start
	private HashSet<CatanTrade> proposedTrades;
	private GameState gameState;
	private Point thiefOn;
	
	public CatanGame(CatanBoard board, List<DevelopmentCard> developmentCardDeck, Player... players) {//assumes this is a new game if using this constructor
		catanBoard = board; 
		this.developmentCardDeck = developmentCardDeck;
		developmentCardPosition = 0;
		currentPlayerIndex = 0;
		history = new LinkedList<>();
		proposedTrades = new HashSet<>();
		playerOrder = new ArrayList<>();
		playerData = new HashMap<>();
		for (Player player : players) {
			playerOrder.add(player.getId());
			playerData.put(player.getId(), player);
		}		
	}
	
	public boolean canMoveThiefHere(Point point) {
		return (point != null && !point.equals(thiefOn) && catanBoard.hexagonExists(point));
	}
	
	void moveThiefTo(Point point) {
		thiefOn = point;
	}
	
	public CatanBoard getCatanBoard() {
		return catanBoard;
	}
	public boolean hasMoreDevelopmentCards() {
		return currentPlayerIndex < developmentCardDeck.size();
	}
	DevelopmentCard getRandomDevelopmentCard() {
		if (developmentCardPosition == developmentCardDeck.size()) {
			throw new IllegalStateException("cannot draw from an empty development card deck");
		}
		return this.developmentCardDeck.get(developmentCardPosition++);
	}
	public Map<Integer, Player> getPlayerData() {
		return playerData;
	}
	
	int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}
	
	void setCurrentPlayerIndex(int index) {
		currentPlayerIndex = index;
	}
	
	
	public Integer getCurrentPlayer() {
		return playerOrder.get(currentPlayerIndex);
	}
	
	//remember, order is displayed most recent command to least recent command
	public List<Command> getCommandHistory() {
		return Collections.unmodifiableList(history);
	}
	
	//convenience method, also goes from most to least recent
	public List<Command> getCommandsDoneThisTurn() {
		if (history.isEmpty()) {return new LinkedList<>();} 
		int i = 0;
		while (i < history.size()) {
			if (history.get(i).getId() == EndTurn.ID) {
				break;
			}
			++i;
		}
		return history.subList(0, i);
	}
	
	public List<Integer> getPlayerOrder() {
		return playerOrder;
	}
	
	public boolean isValidTrade(CatanTrade trade) {
		return trade.issuingPlayer() != trade.recipient() && 
				(getCurrentPlayer().equals(trade.issuingPlayer()) || trade.recipient() == getCurrentPlayer())
				&& playerData.containsKey(trade.issuingPlayer()) && playerData.containsKey(trade.recipient())
				&& playerData.get(trade.issuingPlayer()).hasTheseResources(trade.willGiveThis()) 
				&& playerData.get(trade.recipient()).hasTheseResources(trade.forThis());
	}
	
	protected void addTrade(CatanTrade trade) {
		proposedTrades.add(trade);
	}

	
	public boolean hasBeenProposed(CatanTrade trade) {
		return proposedTrades.contains(trade);
	}
	
	public void acceptTrade(CatanTrade trade) {
		playerData.get(trade.recipient()).removeTheseResources(trade.forThis());
		playerData.get(trade.issuingPlayer()).removeTheseResources(trade.willGiveThis());
		
		playerData.get(trade.recipient()).giveTheseResource(trade.willGiveThis());
		playerData.get(trade.issuingPlayer()).giveTheseResource(trade.forThis());
		
		proposedTrades.remove(trade);
		
		//checks that all remaining trades are still valid
		proposedTrades = new HashSet<>(proposedTrades.stream().filter(this::isValidTrade).toList());
		
	}
	public int getGameState() {
		return gameState.getId();
	}
	
	@Override
	public JsonObjectBuilder toJson() {
		return null;
	}
	private void executeCommand(Command command) {
		command.apply(this);
		history.add(0, command);
		gameState = gameState.getNextState(this);
	}
	//returns if the command executed sucessfully or not
	public boolean processCommand(Command command) {
		boolean canDoCommand = gameState.canDoCommand(this, command) && command.canApply(this);
		//the first gameState checks if the command can be run now (e.g. it is the correct players turn, correct game state)
		//the second command check checks if the command parameters are valid (is this a valid point, far enough away from others)
		if (canDoCommand) {
			executeCommand(command);
		}
		return canDoCommand;
		
	}
	
	//important: classes must not contain state (variables)
	public abstract static class GameState {
		public abstract int getId();
		public abstract boolean canDoCommand(CatanGame cg, Command command);
		public abstract GameState getNextState(CatanGame cg);
		public int hashCode() {return getId();}
		public boolean equals(Object other) {
			if (!(other instanceof GameState)) {
				return false;
			}
			else {
				var otherBoardState = (GameState)other;
				return getId() == otherBoardState.getId();
			}
		}
		
	}
	

}
