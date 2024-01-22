package net.strangled.aldaris.catan.game;

import java.util.EnumMap;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.Resource;

public class ChooseResource extends Command {
	public static final int ID = 365;
	private final Resource resource;
	protected ChooseResource(int playerId, Resource resource) {
		super(playerId);
		if (resource == null) {throw new IllegalArgumentException("resource cannot be null");}
		this.resource = resource;
	}

	public ChooseResource(JsonObject jObj) {
		super(jObj);
		resource = Resource.valueOf(jObj.getString("resource"));
	}
	public Resource getResource() {
		return resource;
	}
	public int getId() {return ID;}
	
	@Override
	public boolean canApply(CatanGame cg, YearOfPlenty r) {
		return this.isPlayersTurn(cg);
	}
	@Override
	public void apply(CatanGame cg, YearOfPlenty r) {
		cg.getPlayerData().get(getPlayerTakingAction()).giveResource(getResource());
	}
	
	@Override
	public boolean canApply(CatanGame cg, Monopoly r) {
		return this.isPlayersTurn(cg);
	}
	@Override
	public void apply(CatanGame cg, Monopoly r) {
		int numberOfResource = 0;
		for (Map.Entry<Integer, Player> entry : cg.getPlayerData().entrySet()) {
			if (entry.getKey().equals(this.getPlayerTakingAction())) continue;
			//adds to the number of all the other player's resource
			int theyHaveThisMany =  entry.getValue().getResources().get(getResource());
			numberOfResource += theyHaveThisMany;
			 //removes all of this resource from them
			EnumMap<Resource, Integer> map = new EnumMap<>(Resource.class);
			map.put(getResource(), theyHaveThisMany);
			entry.getValue().removeTheseResources(map);
		}
		
		//gives the monopoly user these resources
		EnumMap<Resource, Integer> map = new EnumMap<>(Resource.class);
		map.put(getResource(), numberOfResource);
		cg.getPlayerData().get(this.getPlayerTakingAction()).giveTheseResource(map);

	}


	@Override
	public void _toJson(JsonObjectBuilder builder) {
		builder.add("resource", resource.name());

	}

}
