package net.strangled.aldaris.catan.game;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

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
	public void apply(CatanGame cg) {
		//no need to implement, state will handle that
	}

	@Override
	public void _toJson(JsonObjectBuilder builder) {
		//no need to implement

	}

}
