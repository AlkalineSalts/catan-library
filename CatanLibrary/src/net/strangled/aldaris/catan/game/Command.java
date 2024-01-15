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
	public abstract void apply(CatanGame cg);
	public boolean canApply(CatanGame cg) {
		return playerId == cg.getCurrentPlayer();
	}
	
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