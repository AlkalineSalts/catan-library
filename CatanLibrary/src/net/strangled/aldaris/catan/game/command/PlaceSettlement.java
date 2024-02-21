package net.strangled.aldaris.catan.game.command;

import java.util.EnumMap;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.Resource;
import net.strangled.aldaris.catan.game.*;
import net.strangled.aldaris.catan.game.Command;
import net.strangled.aldaris.catan.math.Point;

public class PlaceSettlement extends Command {
	public static final int ID = 2;
	private static Map<Resource, Integer> settlementRecipe = new EnumMap<>(Resource.class);
	static {
		settlementRecipe.put(Resource.WOOD, 1);
		settlementRecipe.put(Resource.BRICK, 1);
		settlementRecipe.put(Resource.SHEEP, 1);
		settlementRecipe.put(Resource.WHEAT, 1);
	}
	private final Point placementPoint;
	public PlaceSettlement(int playerId, Point placementPoint) {
		super(playerId);
		this.placementPoint = placementPoint;
	}

	public PlaceSettlement(JsonObject jObj) {
		super(jObj);
		placementPoint = new Point(jObj.getJsonObject("placementPoint"));
	}

	@Override
	public int getId() {
		return ID;
	}

	
	private void apply(CatanGame cg) {
		cg.getCatanBoard().placeSettlement(placementPoint, getPlayerTakingAction());
	}
	private void removeResources(CatanGame cg) {
		super.removeTheseResources(cg.getPlayerData().get(getPlayerTakingAction()), settlementRecipe);
	}
	
	private boolean hasPlacedSettlementThisTurn(CatanGame cg) {
		var commands = cg.getCommandsDoneThisTurn();
		for (Command c : commands) {
			if (c.getId() == PlaceSettlement.ID && c.getPlayerTakingAction() == this.getPlayerTakingAction()) {return true;}
		}
		return false;
	}
	
	@Override
	public void apply(CatanGame cg, GameStart r) {apply(cg);}
	@Override
	public void apply(CatanGame cg, RegularPlayPreRoll r) {apply(cg); removeResources(cg);}
	@Override
	public void apply(CatanGame cg, RegularPlayPostRoll r) {apply(cg); removeResources(cg);}
	
	@Override
	public boolean canApply(CatanGame cg, GameStart r) {
		return canPlaceNoCost(cg) && !hasPlacedSettlementThisTurn(cg);
	}
	
	@Override 
	public boolean canApply(CatanGame cg, RegularPlayPreRoll r) {
		return canPlaceWithCost(cg);
	}
	
	@Override 
	public boolean canApply(CatanGame cg, RegularPlayPostRoll r) {
		return canPlaceWithCost(cg);
	}
	
	
	
	
	private boolean canPlaceNoCost(CatanGame cg) {
		return this.isPlayersTurn(cg) && cg.getCatanBoard().canPlaceSettlement(placementPoint, this.getPlayerTakingAction());
	}
	
	private boolean canPlaceWithCost(CatanGame cg) {
		return canPlaceNoCost(cg) && cg.getPlayerData().get(this.getPlayerTakingAction()).hasTheseResources(settlementRecipe);
	}
	

	@Override
	public void _toJson(JsonObjectBuilder builder) {
		builder.add("placementPoint", placementPoint.toJson());
	}

}
