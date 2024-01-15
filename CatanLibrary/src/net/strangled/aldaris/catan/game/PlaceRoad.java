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
	private boolean hasNoCost(CatanGame cg) {
		return cg.getGameState() == GameStart.ID || cg.getGameState() == RoadBuilding.ID;
	}
	@Override
	public void apply(CatanGame cg) {
		cg.getCatanBoard().placeRoad(placementLine, getPlayerTakingAction());
		if (!this.hasNoCost(cg)) 
			cg.getPlayerData().get(this.getPlayerTakingAction()).removeTheseResources(woodRecipe);
	}

	@Override
	public boolean canApply(CatanGame cg) {
		 return super.canApply(cg) && cg.getCatanBoard().canPlaceRoad(placementLine, getPlayerTakingAction())
		 &&  (this.hasNoCost(cg) || cg.getPlayerData().get(this.getPlayerTakingAction()).hasTheseResources(woodRecipe));
	}

	@Override
	public void _toJson(JsonObjectBuilder builder) {
		builder.add("placementLine", placementLine.toJson());
	}

}
