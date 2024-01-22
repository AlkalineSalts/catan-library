package net.strangled.aldaris.catan.game;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.JsonSerializable;

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
	
	@Override
	public JsonObjectBuilder toJson() {
		var builder = Json.createObjectBuilder();
		builder.add("id", getId());
		builder.add("playerTakingAction", getPlayerTakingAction());
		_toJson(builder);
		return builder;
	}
	
}