package net.strangled.aldaris.catan.game;

import java.util.EnumMap;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.DevelopmentCard;
import net.strangled.aldaris.catan.Resource;

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
	
	@Override
	public boolean canApply(CatanGame cg) {
		return super.canApply(cg) && cg.getPlayerData().get(this.getPlayerTakingAction()).hasTheseResources(developmentCardRecipe) && cg.hasMoreDevelopmentCards();
	}
	
	@Override
	public void apply(CatanGame cg) {
		var player = cg.getPlayerData().get(this.getPlayerTakingAction());
		player.removeTheseResources(developmentCardRecipe);
		if (builtDevelopmentCard == null) {
			builtDevelopmentCard = cg.getRandomDevelopmentCard();
		}
		player.giveDevelopmentCard(builtDevelopmentCard);
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
