package net.strangled.aldaris.catan.game;

import java.util.EnumMap;
import java.util.HashMap;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.Resource;

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
		theResource = Resource.idToResource(jObj.getInt("resource"));
		resourceMap = new EnumMap<>(Resource.class);
		resourceMap.put(theResource, 1);
	}

	@Override
	public int getId() {
		return ID;
	}

	@Override
	public void apply(CatanGame cg) {
		cg.getPlayerData().get(this.getPlayerTakingAction()).removeTheseResources(resourceMap);
	}

	@Override
	public boolean canApply(CatanGame cg) {
		return cg.getPlayerData().get(this.getPlayerTakingAction()).hasTheseResources(resourceMap);
	}

	@Override
	public void _toJson(JsonObjectBuilder builder) {
		builder.add("resource", theResource.getId());
	}

}
