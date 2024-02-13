package net.strangled.aldaris.catan.game.command;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.game.CatanGame;
import net.strangled.aldaris.catan.game.Command;
import net.strangled.aldaris.catan.game.RegularPlayPostRoll;

public class EndTurn extends Command {
	public EndTurn(int playerId) {
		super(playerId);
	}
	public EndTurn(JsonObject json) {
		super(json);
	}

	public static final int ID = -2;
	@Override
	public int getId() {
		return ID;
	}

	
	
	@Override
	public boolean canApply(CatanGame cg, RegularPlayPostRoll g) {
		return true;
	}
	
	@Override
	public void apply(CatanGame cg, RegularPlayPostRoll g) {
		//no need to implement, state will change accordingly
	}
	@Override
	public void _toJson(JsonObjectBuilder builder) {
		//no need to implement

	}

}
