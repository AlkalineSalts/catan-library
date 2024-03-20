package net.strangled.aldaris.catan.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.DevelopmentCard;
import net.strangled.aldaris.catan.JsonSerializable;
import net.strangled.aldaris.catan.Resource;
import net.strangled.aldaris.catan.board.CatanBoard;
import net.strangled.aldaris.catan.board.CatanHexagon;
import net.strangled.aldaris.catan.board.CatanPoint;
import net.strangled.aldaris.catan.game.command.EndTurn;
import net.strangled.aldaris.catan.game.command.RollDice;
import net.strangled.aldaris.catan.math.Line;
import net.strangled.aldaris.catan.math.Point;

public class CatanGame implements JsonSerializable {
	private static final ArrayList<DevelopmentCard> defaultCardList = new ArrayList<>(25);
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
		Collections.shuffle(defaultCardList);
	}
	public static List<DevelopmentCard> getDefaultCards() {return (List<DevelopmentCard>) defaultCardList.clone();}
	private static final Map.Entry<Resource, Integer> defaultForeignTrade = new SimpleImmutableEntry<>(null, 4);
	
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
		gameState = GameStart.getState();
		for (Player player : players) {
			playerOrder.add(player.getId());
			playerData.put(player.getId(), player);
		}		
		//set the thief on the correct game tile
		for (CatanHexagon catanHexagon : board.getCoordinateToDataHexagon().values()) {
			catanHexagon.getResourceType().ifPresentOrElse(resource -> {}, () -> thiefOn = new Point(catanHexagon.getX(), catanHexagon.getY()));
		}
	}
	public Point getThiefOn() {
		return thiefOn;
	}
	public boolean canMoveThiefHere(Point point) {
		return (point != null && !point.equals(thiefOn) && catanBoard.hexagonExists(point));
	}
	
	protected void moveThiefTo(Point point) {
		thiefOn = point;
	}
	
	public CatanBoard getCatanBoard() {
		return catanBoard;
	}
	public boolean hasMoreDevelopmentCards() {
		return currentPlayerIndex < developmentCardDeck.size();
	}
	protected DevelopmentCard getRandomDevelopmentCard() {
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
	
	public  boolean haveDiceRolledSinceLastEndTurn() {
		//expects commands to be delivered as most recent first
		//checks this by seeing if it can encounter a rolldice before an endturn
		for (Command command : history) {
			if (command.getId() == RollDice.ID) {
				return  true;
			}
			else if (command.getId() == EndTurn.ID) {
				return false;
			}
		}
		return false;
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
	
	//cannot submit any foreign trades to this
	public boolean isValidTrade(CatanTrade trade) {
		return trade.issuingPlayer() != trade.recipient() && 
				(getCurrentPlayer().equals(trade.issuingPlayer()) || trade.recipient() == getCurrentPlayer())
				&& playerData.containsKey(trade.issuingPlayer()) && playerData.containsKey(trade.recipient())
				&& playerData.get(trade.issuingPlayer()).hasTheseResources(trade.willGiveThis()) 
				&& playerData.get(trade.recipient()).hasTheseResources(trade.forThis());
		
	}
	public Map.Entry<Resource, Integer> getDefaultForeignTrade() {
		return defaultForeignTrade;
	}
	
	protected void addTrade(CatanTrade trade) {
		proposedTrades.add(trade);
	}

	
	public boolean hasBeenProposed(CatanTrade trade) {
		return proposedTrades.contains(trade);
	}
	
	public Optional<Map.Entry<Resource, Integer>>  getBestForeignTrade(CatanTrade trade) {
		if (!trade.isForeignTrade())
		{
			return Optional.empty();
		}
		//if this is a foreign trade
		
		//can do this, because it is conformed that only one entry exists in both of these
		Map.Entry<Resource, Integer> willGiveThis = trade.willGiveThis().entrySet().iterator().next();
		Map.Entry<Resource, Integer> forThis = trade.forThis().entrySet().iterator().next();
		
		//first line filters for data points which the issuing player owns 
		return Stream.concat(catanBoard.getPointToDataPoint().values().stream().filter(point -> trade.issuingPlayer() == point.getOwner()).flatMap(point -> point.getTradeRatio().stream()), Stream.of(this.getDefaultForeignTrade()))
				//sorts the ratios to find the best one first
				.sorted((entry1, entry2) -> entry1.getValue() - entry2.getValue())
				//filters to only valid ratios
				//first remove all except those where either the resource key matches or the ratio key is null
				.filter(entry -> entry.getKey() == willGiveThis.getKey() || entry.getKey() == null)
				//then remove any where the amount given is not evenly divided by the ratio and where the result of that division is not equal to the amount requested
				.filter(entry -> willGiveThis.getValue() % entry.getValue() == 0 && forThis.getValue() == willGiveThis.getValue() / entry.getValue())
				.findFirst();
		
	}
	
	

	
	//for foreign & domestic trades
	public void acceptTrade(CatanTrade trade) {
		if (trade.isForeignTrade()) {
			//assumes that the trade is a valid trade given by getBestForeignTrade
			Player player = playerData.get(trade.issuingPlayer());
			player.removeTheseResources(trade.willGiveThis());
			player.giveTheseResources(trade.forThis());
		} else {
		
		playerData.get(trade.recipient()).removeTheseResources(trade.forThis());
		playerData.get(trade.issuingPlayer()).removeTheseResources(trade.willGiveThis());
		
		playerData.get(trade.recipient()).giveTheseResources(trade.willGiveThis());
		playerData.get(trade.issuingPlayer()).giveTheseResources(trade.forThis());
		
		proposedTrades.remove(trade);
		
		//checks that all remaining trades are still valid
		proposedTrades = new HashSet<>(proposedTrades.stream().filter(this::isValidTrade).toList());
		}
		
	}
	
	
	@Override
	public JsonObjectBuilder toJson() {
		return null;
	}
	private void executeCommand(Command command) {
		gameState.apply(command, this);
		history.add(0, command);
		gameState = gameState.getNextState(this);
	}
	public boolean canProcessCommand(Command command) {
		return gameState.canApply(command, this);
	}
	
	//returns if the command executed sucessfully or not
	public void processCommand(Command command) {
		boolean canDoCommand = gameState.canApply(command, this);
		//the first gameState checks if the command can be run now (e.g. it is the correct players turn, correct game state)
		//the second command check checks if the command parameters are valid (is this a valid point, far enough away from others)
		if (canDoCommand) {
			executeCommand(command);
		} else {
			throw new IllegalArgumentException("cannot perform this command:" + command);
		}
		
	}
	
	//important: classes must not contain state (variables)
	public static abstract class GameState {
		public abstract int getId();
		public abstract GameState getNextState(CatanGame cg);
		public abstract boolean canApply(Command command, CatanGame catanGame);
		public abstract void apply(Command command, CatanGame catanGame);
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
		//this method is a utility method for getNextState to easily return the state to it's pre or post roll period respectively
		protected final GameState getRegularGameState(CatanGame cg) {
			if (cg.haveDiceRolledSinceLastEndTurn()) {
				return GameStateFactory.get().getGameState(RegularPlayPostRoll.ID);
			}
			else {
				return GameStateFactory.get().getGameState(RegularPlayPreRoll.ID);
			}
		}
	}
	

}
