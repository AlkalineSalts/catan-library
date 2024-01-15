package net.strangled.aldaris.catan.board;

import java.util.HashSet;

import net.strangled.aldaris.catan.board.CatanBoard.BoardState;
import net.strangled.aldaris.catan.math.Line;
import net.strangled.aldaris.catan.math.Point;

public class StartGame extends BoardState {
	private static final int START_GAME_ID = 0;
	private static final StartGame sg = new StartGame();
	private StartGame() {
		super(START_GAME_ID);
	}
	public static StartGame getBoardState() {
		return sg;
	}
	@Override
	public boolean canPlaceSettlement(CatanBoard cb, Point point, int playerId) {
		if (!cb.pointsToDataPoints.containsKey(point)) {return false;}
		
		HashSet<Point> points = new HashSet<>();
		for (Line line : cb.getLinesWithEndpoint(point)) {
			points.add(line.getP1());
			points.add(line.getP2());
		}
		for (Point mp : points) {
			var catanPoint = cb.pointsToDataPoints.get(mp);
			if (catanPoint.hasOwner()) 
				return false;
		}
		return true;
	}
	@Override
	public boolean canPlaceRoad(CatanBoard cb, Line line, int playerId) {
		if (!cb.lineToDataLine.containsKey(line)) {return false;}
		
		//checks to make sure that the line is connected to a settlement that the requesting player knows
		Point ownerPoint = null;
		var dataPointOne = cb.pointsToDataPoints.get(line.getP1());
		var dataPointTwo = cb.pointsToDataPoints.get(line.getP2());
		if (dataPointOne.getOwner().equals(playerId)) {
			ownerPoint = line.getP1();
		}
		else if (dataPointTwo.getOwner().equals(playerId)) {
			ownerPoint = line.getP2();
		}
		else {
			return false;
		}
		
		//if road is already connected to the settlement return false else true
		for (Line connectedLine : cb.getLinesWithEndpoint(ownerPoint)) {
			CatanLine catanLine = cb.lineToDataLine.get(connectedLine);
			if ( catanLine.hasOwner() ) {return false;}
		}
		return true;
	}
	@Override
	public boolean canUpgradeToCity(CatanBoard cb, Point point, int playerId) {
		return false;
	}

}
