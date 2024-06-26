package net.strangled.aldaris.catan.game.command;
import java.util.EnumMap;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.DevelopmentCard;
import net.strangled.aldaris.catan.Resource;
import net.strangled.aldaris.catan.game.*;
import net.strangled.aldaris.catan.game.Command;

public class BuildDevelopmentCard extends Command {
	public static final int ID = 10;
	private static Map<Resource, Integer> developmentCardRecipe = new EnumMap<>(Resource.class);
	private static String keyString = "builtDevelopmentCard";
	private DevelopmentCard builtDevelopmentCard;
	static {
		developmentCardRecipe.put(Resource.ORE, 1);
		developmentCardRecipe.put(Resource.SHEEP, 1);
		developmentCardRecipe.put(Resource.WHEAT, 1);
	}
	
	public BuildDevelopmentCard(int playerId) {
		super(playerId);
	}
	
	public DevelopmentCard getBuiltDevelopmentCard() {
		return this.builtDevelopmentCard;
	}

	public BuildDevelopmentCard(JsonObject jObj) {
		super(jObj);
		if (jObj.isNull(keyString)) {builtDevelopmentCard = null;}
		else {
			builtDevelopmentCard = DevelopmentCard.idToDevelopmentCard(jObj.getInt(keyString));
		}
		
	}
	
	@Override
	public int getId() {
		return ID;
	}
	
	
	private boolean canApply(CatanGame cg) {
		return super.isPlayersTurn(cg) && cg.getPlayerData().get(this.getPlayerTakingAction()).hasTheseResources(developmentCardRecipe) && cg.hasMoreDevelopmentCards();
	}
	
	
	private void apply(CatanGame cg) {
		var player = cg.getPlayerData().get(this.getPlayerTakingAction());
		removeTheseResources(player, developmentCardRecipe);
		if (builtDevelopmentCard == null) {
			builtDevelopmentCard = getRandomDevelopmentCard(cg);
		}
		else {
			throw new IllegalStateException();
		}
		giveDevelopmentCard(player, builtDevelopmentCard);
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
		if (builtDevelopmentCard == null) {
			builder.addNull(keyString);
		}
		else {
			builder.add(keyString, builtDevelopmentCard.getId());
		}
	}
}
