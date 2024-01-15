package net.strangled.aldaris.catan;

import java.util.HashMap;

public enum Resource{
	
	BRICK(0), ORE(1), SHEEP(2), WHEAT(3), WOOD(4);
	private int id;
	private static HashMap<Integer, Resource> idToResources = new HashMap<>();
	static {
		for (Resource r : Resource.values()) {
			idToResources.put(r.getId(), r);
		}
	}
	private Resource(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public static Resource idToResource(int potentialId) {
		Resource r = idToResources.get(potentialId);
		if (r == null) {
			throw new IllegalArgumentException(potentialId + " is not a valid resource.");
		}
		return r;
	}
}
