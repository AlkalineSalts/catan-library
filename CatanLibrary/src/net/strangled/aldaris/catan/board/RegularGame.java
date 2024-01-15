package net.strangled.aldaris.catan.board;

import java.util.HashSet;
import java.util.List;

import net.strangled.aldaris.catan.board.CatanBoard.BoardState;
import net.strangled.aldaris.catan.math.Line;
import net.strangled.aldaris.catan.math.Point;

public class RegularGame extends BoardState {
	private static final int ID = 2;
	private static RegularGame regularGame = new RegularGame();
	public static RegularGame getBoardState() {return regularGame;}
	private RegularGame() {
		super(ID);
	}

	@Override
	public boolean canPlaceSettlement(CatanBoard cb, Point point, int playerId) {
		HashSet<Point> points = new HashSet<>();
		List<Line> connectedLines = cb.getLinesWithEndpoint(point);
		for (Line line : connectedLines) {
			points.add(line.getP1());
			points.add(line.getP2());
		}
		for (Point mp : points) {
			var catanPoint = cb.pointsToDataPoints.get(mp);
			if (catanPoint.hasOwner()) 
				return false;
		}
		
		// make sure a road is connected to potential settlement
		for (Line line : connectedLines) {
			Integer owner = cb.lineToDataLine.get(line).getOwner();
			if (owner != null && owner.intValue() == playerId) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean canPlaceRoad(CatanBoard cb, Line line, int pId) {
		if (!cb.lineToDataLine.containsKey(line)) {return false;}
		if (cb.lineToDataLine.get(line).hasOwner()) {return false;}
		Integer playerId = Integer.valueOf(pId);
		//gets all lines around this line, removes this line
		HashSet<Line> lines = new HashSet<>();
		lines.addAll(cb.getLinesWithEndpoint(line.getP1()));
		lines.addAll(cb.getLinesWithEndpoint(line.getP2()));
		lines.remove(line);
		//checks if any of these lines are owned by the seatching player
		for (Line ml : lines) {
			if (playerId.equals(cb.lineToDataLine.get(ml).getOwner())) {
				return true;
			}
		}
		//if none of these nearby roads are owned by the player, chek to see if either end of the line has a settlement from which to start from
		return playerId.equals(cb.pointsToDataPoints.get(line.getP1()).getOwner()) || playerId.equals(cb.pointsToDataPoints.get(line.getP2()).getOwner());	
	}

	@Override
	public boolean canUpgradeToCity(CatanBoard cb, Point point, int pId) {
		Integer playerId = Integer.valueOf(pId);
		if (!cb.pointsToDataPoints.containsKey(point)) {return false;}
		var catanPoint = cb.pointsToDataPoints.get(point);
		return !catanPoint.isCity() && playerId.equals(catanPoint.getOwner());
		 
	
	}

}
