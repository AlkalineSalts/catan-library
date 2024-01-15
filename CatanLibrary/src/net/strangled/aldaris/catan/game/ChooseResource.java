package net.strangled.aldaris.catan.game;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.Resource;

public abstract class ChooseResource extends Command {
	private final Resource resource;
	protected ChooseResource(int playerId, Resource resource) {
		super(playerId);
		if (resource == null) {throw new IllegalArgumentException("resource cannot be null");}
		this.resource = resource;
	}

	protected ChooseResource(JsonObject jObj) {
		super(jObj);
		resource = Resource.idToResource(jObj.getInt("resource"));
	}
	protected Resource getResource() {
		return resource;
	}
	public abstract int getId();


	@Override
	public void _toJson(JsonObjectBuilder builder) {
		builder.add("resource", resource.getId());

	}

}
