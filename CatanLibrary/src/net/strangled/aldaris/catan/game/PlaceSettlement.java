package net.strangled.aldaris.catan.game;

import java.util.EnumMap;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.Resource;
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

	@Override
	public void apply(CatanGame cg) {
		cg.getCatanBoard().placeSettlement(placementPoint, getPlayerTakingAction());
	}

	@Override
	public boolean canApply(CatanGame cg) {
		return super.canApply(cg) && cg.getPlayerData().get(this.getPlayerTakingAction()).hasTheseResources(settlementRecipe) && cg.getCatanBoard().canPlaceSettlement(placementPoint, this.getPlayerTakingAction());
	}

	@Override
	public void _toJson(JsonObjectBuilder builder) {
		builder.add("placementPoint", placementPoint.toJson());
	}

}
