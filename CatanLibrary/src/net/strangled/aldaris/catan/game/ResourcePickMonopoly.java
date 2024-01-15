package net.strangled.aldaris.catan.game;

import java.util.EnumMap;
import java.util.Map;

import javax.json.JsonObject;

import net.strangled.aldaris.catan.Resource;

public class ResourcePickMonopoly extends ChooseResource {
	public static final int ID = 366;
	public ResourcePickMonopoly(int playerId, Resource resource) {
		super(playerId, resource);
	}
	
	public ResourcePickMonopoly(JsonObject jObj) {
		super(jObj);
	}
	

	@Override
	public int getId() {
		return ID;
	}

	@Override
	public void apply(CatanGame cg) {
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

}
