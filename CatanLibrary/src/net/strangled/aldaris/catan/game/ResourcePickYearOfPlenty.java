package net.strangled.aldaris.catan.game;

import javax.json.JsonObject;

import net.strangled.aldaris.catan.Resource;

public class ResourcePickYearOfPlenty extends ChooseResource {
	public ResourcePickYearOfPlenty(int playerId, Resource resource) {
		super(playerId, resource);
	}
	public ResourcePickYearOfPlenty(JsonObject j) {
		super(j);
	}
	
	public static final int ID = 365;
	@Override
	public int getId() {
		return ID;
	}
	@Override
	public void apply(CatanGame cg) {
		cg.getPlayerData().get(getPlayerTakingAction()).giveResource(getResource());
	}
}
