package net.strangled.aldaris.catan.game.command;

import java.util.Random;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.board.CatanHexagon;
import net.strangled.aldaris.catan.board.CatanPoint;
import net.strangled.aldaris.catan.game.CatanGame;
import net.strangled.aldaris.catan.game.Command;
import net.strangled.aldaris.catan.game.RegularPlayPreRoll;
import net.strangled.aldaris.catan.math.Point;

public class RollDice extends Command {
	private final int rolledNumber;
	
	public static final int ID = 0;
	private static final String rolledNumberString = "rolledNumber";
	
	
	public RollDice(int playerId, int rolledNumber) {
		super(playerId);
		this.rolledNumber = rolledNumber;
	}
	
	
	
	public RollDice(JsonObject jObj) {
		super(jObj);
		rolledNumber = jObj.getInt(rolledNumberString);
		
	}
	
	public int getRolledNumber() {
		return rolledNumber;
	}
	
	@Override
	public int getId() {
		return ID;
	}
	
	@Override
	public boolean canApply(CatanGame cg, RegularPlayPreRoll r) {
		return super.isPlayersTurn(cg);
	}

	@Override
	public void apply(CatanGame cg, RegularPlayPreRoll r) {
		
		if (rolledNumber == 7) {
			//do nothing, intentionally so. The game state will change in RegularPlayPreRoll to the next phase
			
		}
		else {
			//this one liner gets hexagons with the given number w/o the thief on it
			var catanHexagons = cg.getCatanBoard().getCoordinateToDataHexagon().values().stream().filter(hexagon -> hexagon.getCollectResourceNumber() == rolledNumber).filter(hexagon -> !hexagon.getPoint().equals(cg.getThiefOn())).toList();
			//gives each player their resources, going over all the points
			//players get one per settlement and two per city
			for (CatanHexagon hexagon : catanHexagons) {
				for (Point mp : hexagon.getMathematicalPoints()) {
					CatanPoint catanPoint = cg.getCatanBoard().getPointToDataPoint().get(mp);
					if (catanPoint.hasOwner()) {
						var owner = cg.getPlayerData().get(catanPoint.getOwner());
						hexagon.getResourceType().ifPresent(resource -> {
							giveResource(owner, resource);
							if (catanPoint.isCity()) {
						giveResource(owner, resource);
							}
						});
					}
				}
			}
		}
	}

	

	@Override
	public void _toJson(JsonObjectBuilder builder) {
		builder.add(rolledNumberString, rolledNumber);
	}

}
