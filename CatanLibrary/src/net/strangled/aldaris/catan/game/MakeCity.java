package net.strangled.aldaris.catan.game;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.Resource;
import net.strangled.aldaris.catan.math.Point;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class MakeCity extends Command {
	public static final int ID = 8;
	private static Map<Resource, Integer> cityRecipe = new EnumMap<>(Resource.class);
	static {
		cityRecipe.put(Resource.ORE, 3);
		cityRecipe.put(Resource.WHEAT, 2);
	}
	
	private final Point cityPoint;
	public MakeCity(int playerId, Point point) {
		super(playerId);
		cityPoint = point;
	}

	public MakeCity(JsonObject jObj) {
		super(jObj);
		cityPoint = new Point(jObj.getJsonObject("cityPoint"));
	}

	@Override
	public int getId() {
		return ID;
	}

	
	private void apply(CatanGame cg) {
		cg.getCatanBoard().canUpgradeToCity(cityPoint, this.getPlayerTakingAction());
	}
	
	
	private boolean canApply(CatanGame cg) {
		return super.isPlayersTurn(cg) && cg.getCatanBoard().canUpgradeToCity(cityPoint, getPlayerTakingAction()) && cg.getPlayerData().get(this.getPlayerTakingAction()).hasTheseResources(cityRecipe);
	}
	
	@Override
	public boolean canApply(CatanGame cg, RegularPlayPreRoll g) {
		return canApply(cg);
	}
	
	@Override
	public void apply(CatanGame cg, RegularPlayPreRoll g) {
		apply(cg);
	}
	
	@Override
	public boolean canApply(CatanGame cg, RegularPlayPostRoll g) {
		return canApply(cg);
	}
	
	@Override
	public void apply(CatanGame cg, RegularPlayPostRoll g) {
		apply(cg);
	}

	@Override
	public void _toJson(JsonObjectBuilder builder) {
		builder.add("cityPoint", cityPoint.toJson());
	}

}
