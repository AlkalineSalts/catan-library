package net.strangled.aldaris.catan.game;


import javax.json.JsonObject;

import net.strangled.aldaris.catan.game.command.AcceptTrade;
import net.strangled.aldaris.catan.game.command.BuildDevelopmentCard;
import net.strangled.aldaris.catan.game.command.ChoosePlayer;
import net.strangled.aldaris.catan.game.command.ChooseResource;
import net.strangled.aldaris.catan.game.command.EndTurn;
import net.strangled.aldaris.catan.game.command.MakeCity;
import net.strangled.aldaris.catan.game.command.MoveThiefTo;
import net.strangled.aldaris.catan.game.command.PlaceRoad;
import net.strangled.aldaris.catan.game.command.PlaceSettlement;
import net.strangled.aldaris.catan.game.command.PlayDevelopmentCard;
import net.strangled.aldaris.catan.game.command.ProposeTrade;
import net.strangled.aldaris.catan.game.command.RollDice;

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
		idToMakeFunction.put(MoveThiefTo.ID, MoveThiefTo::new);
		idToMakeFunction.put(PlaceRoad.ID, PlaceRoad::new);
		idToMakeFunction.put(PlayDevelopmentCard.ID, PlayDevelopmentCard::new);
		idToMakeFunction.put(PlaceSettlement.ID, PlaceSettlement::new);
		idToMakeFunction.put(ChooseResource.ID, ChooseResource::new);
		idToMakeFunction.put(ChoosePlayer.ID, ChoosePlayer::new);
		idToMakeFunction.put(MakeCity.ID, MakeCity::new);
		idToMakeFunction.put(RollDice.ID, RollDice::new);
		
		
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
