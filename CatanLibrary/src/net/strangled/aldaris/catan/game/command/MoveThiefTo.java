package net.strangled.aldaris.catan.game.command;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.game.CatanGame;
import net.strangled.aldaris.catan.game.Command;
import net.strangled.aldaris.catan.game.ThiefMove;
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
	public void apply(CatanGame cg, ThiefMove r) {
		moveThiefTo(cg, hexagonPoint);
	}

	@Override
	public boolean canApply(CatanGame cg, ThiefMove r) {
		return super.isPlayersTurn(cg) && cg.canMoveThiefHere(hexagonPoint);
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
