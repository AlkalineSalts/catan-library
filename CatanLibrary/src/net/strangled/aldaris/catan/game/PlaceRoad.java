package net.strangled.aldaris.catan.game;

import java.util.EnumMap;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.Resource;
import net.strangled.aldaris.catan.math.Line;

public class PlaceRoad extends Command {
	

	private static Map<Resource, Integer> woodRecipe = new EnumMap<>(Resource.class);
	static {
		woodRecipe.put(Resource.WOOD, 2);
		woodRecipe.put(Resource.BRICK, 2);
	}
	
	
	public static final int ID = 3;
	private final Line placementLine;
	public PlaceRoad(int playerId, Line placementLine) {
		super(playerId);
		this.placementLine = placementLine;
	}

	public PlaceRoad(JsonObject jObj) {
		super(jObj);
		placementLine = new Line(jObj.getJsonObject("placementLine"));
	}

	@Override
	public int getId() {
		return ID;
	}
	
	@Override
	public void apply(CatanGame cg, RegularPlayPreRoll r) {apply(cg); removeResources(cg);}
	@Override
	public void apply(CatanGame cg, RegularPlayPostRoll r) {apply(cg); removeResources(cg);}
	@Override
	public boolean canApply(CatanGame cg, RegularPlayPreRoll r) {return canApplyWithCost(cg);}
	@Override
	public boolean canApply(CatanGame cg, RegularPlayPostRoll r) {return canApplyWithCost(cg);}
	
	@Override
	public void apply(CatanGame cg, RoadBuilding r) {apply(cg);}
	@Override
	public boolean canApply(CatanGame cg, RoadBuilding r) {return canApplyNoCost(cg);}
	@Override
	public void apply(CatanGame cg, GameStart r) {apply(cg);}
	@Override
	public boolean canApply(CatanGame cg, GameStart r) {return canApplyNoCost(cg);}
	
	
	
	
	private void removeResources(CatanGame cg) {
		cg.getPlayerData().get(this.getPlayerTakingAction()).removeTheseResources(woodRecipe);
	}
	
	private void apply(CatanGame cg) {
		cg.getCatanBoard().placeRoad(placementLine, getPlayerTakingAction());
			
	}
	
	private boolean canApplyWithCost(CatanGame cg) {
		return canApplyNoCost(cg) && cg.getPlayerData().get(this.getPlayerTakingAction()).hasTheseResources(woodRecipe);
	}
	
	private boolean canApplyNoCost(CatanGame cg) {
		 return super.isPlayersTurn(cg) && cg.getCatanBoard().canPlaceRoad(placementLine, getPlayerTakingAction());
	}

	@Override
	public void _toJson(JsonObjectBuilder builder) {
		builder.add("placementLine", placementLine.toJson());
	}

}
