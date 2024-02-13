package net.strangled.aldaris.catan.game.command;

import java.util.EnumMap;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.Resource;
import net.strangled.aldaris.catan.game.*;
import net.strangled.aldaris.catan.game.Command;

public class DiscardCard extends Command {
	public static final int ID = -100;
	
	private final Resource theResource;
	private final EnumMap<Resource, Integer> resourceMap; // just used to represent the data for the player class
	
	
	public DiscardCard(int playerId, Resource resource) {
		super(playerId);
		theResource = resource;
		resourceMap = new EnumMap<>(Resource.class);
		resourceMap.put(resource, 1);
	}

	public DiscardCard(JsonObject jObj) {
		super(jObj);
		theResource = Resource.valueOf(jObj.getString("resource"));
		resourceMap = new EnumMap<>(Resource.class);
		resourceMap.put(theResource, 1);
	}

	@Override
	public int getId() {
		return ID;
	}

	
	public void apply(CatanGame cg, ThiefSteal r) {
		removeTheseResources(cg.getPlayerData().get(this.getPlayerTakingAction()), resourceMap);
	}

	
	public boolean canApply(CatanGame cg, ThiefSteal r) {
		return cg.getPlayerData().get(this.getPlayerTakingAction()).hasTheseResources(resourceMap);
		//TODO: Make sure to check that they can only discard up to half of their hand
	}
	
	

	@Override
	public void _toJson(JsonObjectBuilder builder) {
		builder.add("resource", theResource.name());
	}

}
