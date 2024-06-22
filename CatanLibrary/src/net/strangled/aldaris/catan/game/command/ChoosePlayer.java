package net.strangled.aldaris.catan.game.command;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.Resource;
import net.strangled.aldaris.catan.game.CatanGame;
import net.strangled.aldaris.catan.game.Command;
import net.strangled.aldaris.catan.game.Player;
import net.strangled.aldaris.catan.game.ThiefSteal;
import net.strangled.aldaris.catan.math.Point;

public class ChoosePlayer extends Command {
	public static final int ID = 4;
	private static final Random random = new Random(-1002);
	
	private final int targetPlayerId;
	private Integer removeCardNo; //used to store randomly stolen card information
	public ChoosePlayer(int playerId, int targetPlayerId) {
		super(playerId);
		this.targetPlayerId = targetPlayerId;
		removeCardNo = null;
	}

	public ChoosePlayer(JsonObject jObj) {
		super(jObj);
		targetPlayerId = jObj.getInt("targetPlayerId");
		if (jObj.isNull("removeCardNo")) {
			removeCardNo = null;
		}
		else {
			removeCardNo = Integer.valueOf(jObj.getInt("removeCardNo"));
		}
	}

	@Override
	public int getId() {
		return ID;
	}
	
	

	@Override
	public void apply(CatanGame cg, ThiefSteal ts) {
		ArrayList<Resource> opponentResources = new ArrayList<>(10);
		var otherPlayer = cg.getPlayerData().get(targetPlayerId);
		for (Map.Entry<Resource, Integer> entry : otherPlayer.getResources().entrySet()) {
			var resource = entry.getKey();
			for (int i = 0; i < entry.getValue(); i++) {
				opponentResources.add(resource);
			}
		}
		if (removeCardNo == null) removeCardNo = random.nextInt(opponentResources.size());
		
		var singleResource = opponentResources.get(removeCardNo);
		var stealingResource = Player.packageResources(singleResource);
		removeTheseResources(otherPlayer, stealingResource);
		giveResource(cg.getPlayerData().get(this.getPlayerTakingAction()), singleResource);		
	}
	
	public static Set<Integer> getValidStealTargets(CatanGame cg) {
		Set<Integer> validPlayers = cg.getPlayerIdsOnHex(cg.getThiefOn());
		validPlayers.remove(cg.getCurrentPlayer()); //removes the player making this action, can't steal from self
		var listSet = validPlayers.stream().filter(playerId -> cg.getPlayerData().get(playerId).getAmountOfResources() > 0).toList();
		HashSet<Integer> validPlayerSet = new HashSet<Integer>(); 
		for (Integer i : listSet) {validPlayerSet.add(i);}
		return validPlayerSet;
	}
	
	@Override
	public boolean canApply(CatanGame cg, ThiefSteal ts) {
		
		return super.isPlayersTurn(cg) && cg.getPlayerOrder().contains(targetPlayerId) && cg.getPlayerData().get(targetPlayerId).getAmountOfResources() > 0;
	}

	@Override
	public void _toJson(JsonObjectBuilder builder) {
		builder.add("targetPlayerId", targetPlayerId);
		if (removeCardNo == null) {
			builder.addNull("removeCardNo");
		} else {
			builder.add("removeCardNo", removeCardNo.intValue());
		}

	}

}
