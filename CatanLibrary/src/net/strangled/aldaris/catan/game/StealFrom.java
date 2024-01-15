package net.strangled.aldaris.catan.game;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.Resource;

public class StealFrom extends Command {
	public static final int ID = 4;
	private static final Random random = new Random();
	
	private final int targetPlayerId;
	
	public StealFrom(int playerId, int targetPlayerId) {
		super(playerId);
		this.targetPlayerId = targetPlayerId;
	}

	public StealFrom(JsonObject jObj) {
		super(jObj);
		targetPlayerId = jObj.getInt("targetPlayerId");
	}

	@Override
	public int getId() {
		return ID;
	}

	@Override
	public void apply(CatanGame cg) {
		ArrayList<Resource> opponentResources = new ArrayList<>(10);
		var otherPlayer = cg.getPlayerData().get(targetPlayerId);
		for (Map.Entry<Resource, Integer> entry : otherPlayer.getResources().entrySet()) {
			var resource = entry.getKey();
			for (int i = 0; i < entry.getValue(); i++) {
				opponentResources.add(resource);
			}
		}
		var singleResource = opponentResources.get(random.nextInt(opponentResources.size()));
		var stealingResource = Player.packageResources(singleResource);
		otherPlayer.removeTheseResources(stealingResource);
		cg.getPlayerData().get(this.getPlayerTakingAction()).giveResource(singleResource);		
	}

	@Override
	public boolean canApply(CatanGame cg) {
		return super.canApply(cg) && cg.getPlayerOrder().contains(targetPlayerId) && getPlayerTakingAction() != targetPlayerId && cg.getPlayerData().get(targetPlayerId).getAmountOfResources() > 0;
	}

	@Override
	public void _toJson(JsonObjectBuilder builder) {
		builder.add("targetPlayerId", targetPlayerId);

	}

}
