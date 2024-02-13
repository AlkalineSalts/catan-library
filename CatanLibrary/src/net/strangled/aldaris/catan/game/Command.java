package net.strangled.aldaris.catan.game;

import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.DevelopmentCard;
import net.strangled.aldaris.catan.JsonSerializable;
import net.strangled.aldaris.catan.Resource;
import net.strangled.aldaris.catan.math.Point;

public abstract class Command implements JsonSerializable {
	private final int playerId;
	protected Command(int playerId) {
		this.playerId = playerId;
	}
	protected Command(JsonObject jObj) {
		playerId = jObj.getInt("playerTakingAction");
	}
	public abstract int getId();
	
	protected final boolean isPlayersTurn(CatanGame g) {
		return playerId == g.getCurrentPlayer().intValue();
	}
	
	
	public boolean canApply(CatanGame cg, GameStart g) {return false;}
	
	public boolean canApply(CatanGame cg, GameEnded g) {return false;}
	
	public boolean canApply(CatanGame cg, RegularPlayPreRoll g) {return false;}
	
	public boolean canApply(CatanGame cg, RegularPlayPostRoll g) {return false;}
	
	public boolean canApply(CatanGame cg, ThiefSteal g) {return false;}
	
	public boolean canApply(CatanGame cg, RoadBuilding r) {return false;}
	
	public boolean canApply(CatanGame cg, Monopoly r) {return false;}
	
	public boolean canApply(CatanGame cg, YearOfPlenty r) {return false;}
	
	public boolean canApply(CatanGame cg, ThiefMove r) {return false;}
	
	
	public void apply(CatanGame cg, GameStart g) {}
	
	public void apply(CatanGame cg, GameEnded g) {}
	
	public void apply(CatanGame cg, RegularPlayPreRoll g) {}
	
	public void apply(CatanGame cg, RegularPlayPostRoll g) {}
	
	public void apply(CatanGame cg, ThiefSteal g) {}
	
	public void apply(CatanGame cg, RoadBuilding g) {}
	
	public void apply(CatanGame cg, Monopoly r) {}
	
	public void apply(CatanGame cg, YearOfPlenty r) {}
	
	public void apply(CatanGame cg, ThiefMove r) {}
	
	//use the template to complete json object for subclasses
	public abstract void _toJson(JsonObjectBuilder builder); //template design pattern
	
	public int getPlayerTakingAction() {
		return playerId;
	}
	
	//use these methods to mutate the state of the catan game, the purpose of having these here are to protect anything outside
	//of a command from mutatuing the game state. These are proxy methods for the mutators on the catan game
	protected final void moveThiefTo(CatanGame cg, Point point) {
		cg.moveThiefTo(point);
	}
	protected final void addTrade(CatanGame cg, CatanTrade trade) {
		cg.addTrade(trade);
	}
	protected final DevelopmentCard getRandomDevelopmentCard(CatanGame cg) {
		return cg.getRandomDevelopmentCard();
	}
	
	//same idea for players, players sshould not be modifiable outside of commands
	
	protected final void giveDevelopmentCard(Player player, DevelopmentCard d) {
		player.giveDevelopmentCard(d);
	}
	protected final void playDevelopmentCard(Player player, DevelopmentCard d) {
		player.playDevelopmentCard(d);
	}
	
	protected final void giveResource(Player player, Resource r) {
		player.giveResource(r);
	}
	
	protected final void removeTheseResources(Player player, Map<Resource, Integer> resourcesAmount) {
		player.removeTheseResources(resourcesAmount);
	}
	
	protected final void giveTheseResource(Player player, Map<Resource, Integer> resourcesAmount) {
		player.giveTheseResource(resourcesAmount);
	}

	
	
	@Override
	public JsonObjectBuilder toJson() {
		var builder = Json.createObjectBuilder();
		builder.add("id", getId());
		builder.add("playerTakingAction", getPlayerTakingAction());
		_toJson(builder);
		return builder;
	}
	
}