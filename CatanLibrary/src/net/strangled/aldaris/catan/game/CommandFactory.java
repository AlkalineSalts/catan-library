package net.strangled.aldaris.catan.game;


import javax.json.JsonObject;
import java.util.HashMap;
import java.util.function.Function;

public class CommandFactory {
	private static final CommandFactory factory = new CommandFactory();
	private HashMap<Integer, Function<JsonObject, Command>> idToMakeFunction;
	
	private CommandFactory() {
		idToMakeFunction = new HashMap<>();
		idToMakeFunction.put(AcceptTrade.ID, AcceptTrade::new);
		idToMakeFunction.put(ProposeTrade.ID, ProposeTrade::new);
		idToMakeFunction.put(BuildDevelopmentCard.ID, BuildDevelopmentCard::new);
		idToMakeFunction.put(EndTurn.ID, EndTurn::new);
		idToMakeFunction.put(DiscardCard.ID, DiscardCard::new);
		idToMakeFunction.put(MoveThiefTo.ID, MoveThiefTo::new);
		idToMakeFunction.put(PlaceRoad.ID, PlaceRoad::new);
		idToMakeFunction.put(PlayDevelopmentCard.ID, PlayDevelopmentCard::new);
		idToMakeFunction.put(PlaceSettlement.ID, PlaceSettlement::new);
		idToMakeFunction.put(ResourcePickMonopoly.ID, ResourcePickMonopoly::new);
		idToMakeFunction.put(ResourcePickYearOfPlenty.ID, ResourcePickYearOfPlenty::new);
		idToMakeFunction.put(StealFrom.ID, StealFrom::new);
		idToMakeFunction.put(MakeCity.ID, MakeCity::new);
		
		
	}
	public Command getCommand(JsonObject object) {
		var function = idToMakeFunction.get(object.getInt("id"));
		if (function == null) {
			throw new IllegalArgumentException(object.getInt("id") + "is not a valid id");
		}
		return function.apply(object);
		
	}
	
	public static CommandFactory get() {
		return factory;
	}
}
