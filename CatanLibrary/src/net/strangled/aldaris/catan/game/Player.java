package net.strangled.aldaris.catan.game;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import net.strangled.aldaris.catan.Util;
import net.strangled.aldaris.catan.DevelopmentCard;
import net.strangled.aldaris.catan.JsonSerializable;
import net.strangled.aldaris.catan.Resource;

public class Player implements JsonSerializable {
	private static final String keyString = "key";
	private static final String valueString = "value";
	
	private int idNumber;
	private Map<Resource, Integer> resources;
	private Map<DevelopmentCard, Integer> developmentCards;
	private Map<DevelopmentCard, Integer> playedDevelopmentCards;
	
	public Player(int id) {
		this.idNumber = id;
		resources = new EnumMap<>(Resource.class);
		developmentCards = new EnumMap<>(DevelopmentCard.class);
		playedDevelopmentCards = new EnumMap<>(DevelopmentCard.class);
		for (DevelopmentCard d : DevelopmentCard.values()) {
			developmentCards.put(d, 0);
			playedDevelopmentCards.put(d, 0);
		}
		for (Resource r : Resource.values()) {
			resources.put(r, 0);
		}
		
	}
	
	public int getAmountOfResources() {
		int amountOfResources = 0;
		for (Resource r : Resource.values()) {
			amountOfResources += resources.get(r);
		}
		return amountOfResources;
	}
	
	//returns a copy of the resources this player has
	public Map<Resource, Integer> getResources() {
		return Collections.unmodifiableMap(resources);
	}
	
	public boolean hasTheseResources(Map<Resource, Integer> resourcesAmount) {
		for (Map.Entry<Resource, Integer> entry : resourcesAmount.entrySet()) {
			if (entry.getValue() > resources.get(entry.getKey())) return  false;
		}
		return true;
	}
	
	public int getNumberOfPlayedCards(DevelopmentCard d) {
		return playedDevelopmentCards.get(d);
	}
	
	public Map<DevelopmentCard, Integer> getDevelopmentCards() {
		return Collections.unmodifiableMap(developmentCards);
	}
	
	protected void giveDevelopmentCard(DevelopmentCard d) {
		developmentCards.compute(d, (key, value) -> value + 1);
	}
	
	public boolean hasDevelopmentCard(DevelopmentCard d) {
		return developmentCards.get(d) > 1;
	}
	
	protected void playDevelopmentCard(DevelopmentCard d) {
		if (hasDevelopmentCard(d)) {
			throw new IllegalStateException("cannot play a card you don't have");
		}
		developmentCards.compute(d, (key, value) -> value - 1);
		playedDevelopmentCards.compute(d, (key, value) -> value + 1);
	}
	
	protected void giveResource(Resource r) {
		resources.compute(r, (resource, amount) -> amount + 1);
	}
	
	protected void removeResource(Resource r) {
		HashMap<Resource, Integer> m = new HashMap<>();
		m.put(r, 1);
		removeTheseResources(m);
	}
	
	protected void removeTheseResources(Map<Resource, Integer> resourcesAmount) {
		if (!hasTheseResources(resourcesAmount)) {
			throw new IllegalStateException("cannot withdraw these resouces without having at least one resource less than 0");
		}
		for (Map.Entry<Resource, Integer> entry : resourcesAmount.entrySet()) {
			resources.compute(entry.getKey(), (key, value) -> value - entry.getValue());
		}
	}
	
	protected void giveTheseResources(Map<Resource, Integer> resourcesAmount) {
		for (Map.Entry<Resource, Integer> entry : resourcesAmount.entrySet()) {
			resources.compute(entry.getKey(), (key, value) -> value + entry.getValue());
		}
	}
	
	
	public Player(JsonObject playerObject) {
		idNumber = playerObject.getInt("idNumber");
		resources = new EnumMap<>(Resource.class);
		developmentCards = new EnumMap<>(DevelopmentCard.class);
		playedDevelopmentCards = new EnumMap<>(DevelopmentCard.class);
		
		resources = Util.fromJson(playerObject.getJsonArray("resourcesMap"));
		for (JsonValue resource : playerObject.getJsonArray("developmentCardsMap")) {
			var developmentCardObject = (JsonObject)resource;
			developmentCards.put(DevelopmentCard.idToDevelopmentCard(developmentCardObject.getInt(keyString)), developmentCardObject.getInt(valueString));
		}
		for (JsonValue resource : playerObject.getJsonArray("playedDevelopmentCardsMap")) {
			var developmentCardObject = (JsonObject)resource;
			playedDevelopmentCards.put(DevelopmentCard.idToDevelopmentCard(developmentCardObject.getInt(keyString)), developmentCardObject.getInt(valueString));
		}
	}
	
	public int getId() {
		return idNumber;
	}

	@Override
	public int hashCode() {
		return idNumber;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		return idNumber == other.idNumber;
	}
	
	public static Map<Resource, Integer> packageResources(Resource... resourcesArray) {
		EnumMap<Resource, Integer> creatingMap = new EnumMap<Resource, Integer>(Resource.class);
		for (Resource r: resourcesArray) {
			creatingMap.compute(r, (resource, amount) -> amount == null ? 0 : amount + 1);
		}
		return creatingMap;
	}
	
	@Override
	public JsonObjectBuilder toJson() {
		var builder = Json.createObjectBuilder();
		builder.add("idNumber", idNumber);
		
		
		builder.add("resourcesMap", Util.toJson(resources));
		
		var developmentCardsMap = Json.createArrayBuilder();
		var playedDevelopmentCardsMap = Json.createArrayBuilder();
		
		for (DevelopmentCard d: DevelopmentCard.values()) {
			var entryObject1 = Json.createObjectBuilder();
			var entryObject2 = Json.createObjectBuilder();
			
			entryObject1.add(keyString, d.getId());
			entryObject1.add(valueString, developmentCards.get(d));
			
			entryObject2.add(keyString, d.getId());
			entryObject2.add(valueString, playedDevelopmentCards.get(d));
			
			developmentCardsMap.add(entryObject1);
			playedDevelopmentCardsMap.add(entryObject2);
		}
		
		builder.add("developmentCardsMap", developmentCardsMap);
		builder.add("playedDevelopmentCardsMap", playedDevelopmentCardsMap);
		
		return builder;
	}
	
	

}
