package net.strangled.aldaris.catan.game;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.math.Point;

public class MoveThiefTo extends Command {
	
	public static final int ID = 1;
	private Point hexagonPoint;
	
	
	public MoveThiefTo(int playerId, Point hexagonPoint) {
		super(playerId);
		if (hexagonPoint == null) {throw new IllegalArgumentException("hexagonPoint cannot be null");}
		this.hexagonPoint = hexagonPoint;
	}
	
	public MoveThiefTo(JsonObject commandObject) {
		super(commandObject);
		hexagonPoint = new Point(commandObject.getJsonObject("hexagonPoint"));
	}

	
	
	
	
	@Override
	public void apply(CatanGame cg) {
		cg.moveThiefTo(hexagonPoint);
	}

	@Override
	public boolean canApply(CatanGame cg) {
		return super.canApply(cg) && cg.canMoveThiefHere(hexagonPoint);
	}

	@Override
	public void _toJson(JsonObjectBuilder builder) {
		builder.add("hexagonPoint", hexagonPoint.toJson());

	}

	@Override
	public int getId() {
		return ID;
	}

}
