package net.strangled.aldaris.catan.board;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import net.strangled.aldaris.catan.JsonSerializable;
import net.strangled.aldaris.catan.board.CatanHexagon.CatanHexagonBuilder;
import net.strangled.aldaris.catan.math.Line;
import net.strangled.aldaris.catan.math.Point;

public class CatanBoard implements JsonSerializable {
	protected HashMap<Point, CatanHexagon> coordsToDataHexagons;
	protected HashMap<Point, CatanPoint> pointsToDataPoints;
	protected HashMap<Line, CatanLine> lineToDataLine;
	private RoadFinder roadFinder; //transient, do not store this data in json or compare in equals
	private BoardState boardState;
	private final int boardTypeId;
	
	///Generates a blank catan board in the begin game state
	protected CatanBoard(int boardTypeId) {
		this.boardTypeId = boardTypeId;
		coordsToDataHexagons = new HashMap<>();
		pointsToDataPoints = new HashMap<>();
		lineToDataLine = new HashMap<>();
		
		boardState = StartGame.getBoardState();
		memoizedPointesToLines = new HashMap<>();
		
	}
	
	public CatanBoard(JsonObject boardObject) {
		this(boardObject.getInt("boardTypeId"));
		for (JsonValue value : boardObject.getJsonArray("catanHexagons")) {
			var catanHexagon = new CatanHexagon((JsonObject)value);
			coordsToDataHexagons.put(new Point(catanHexagon.getX(), catanHexagon.getY()), catanHexagon);
		}
		for (JsonValue value : boardObject.getJsonArray("catanPoints")) {
			var catanPoint = new CatanPoint((JsonObject)value);
			pointsToDataPoints.put(catanPoint.getLocation(), catanPoint);
		}
		for (JsonValue value : boardObject.getJsonArray("catanLines")) {
			var catanLine = new CatanLine((JsonObject)value);
			lineToDataLine.put(catanLine.getLine(), catanLine);
		}
		boardState = BoardStateFactory.get().getState(boardObject.getInt("boardState"));
	}
	public boolean hexagonExists(Point point) {
		return coordsToDataHexagons.containsKey(point);
	}
	public boolean canPlaceSettlement(Point point, Integer playerId) {
		return boardState.canPlaceSettlement(this, point, playerId);
	}
	public boolean canPlaceRoad(Line line, Integer playerId) {
		return boardState.canPlaceRoad(this, line, playerId);
	}
	public boolean canUpgradeToCity(Point point, int id) {
		return boardState.canUpgradeToCity(this, point, id);
	}
	
	
	//returns an unmodifiable view of the points and their data
	public Map<Point, CatanPoint> getPointToDataPoint() {
		return Collections.unmodifiableMap(pointsToDataPoints);
	}
	//returns an unmodifiable view of the lines and their data
	public Map<Line, CatanLine> getLineToDataLine() {
		return Collections.unmodifiableMap(this.lineToDataLine);
	}
	
	//returns an unmodifiable view of the hexagons and their data
	public Map<Point, CatanHexagon> getCoordinateToDataHexagon() {
		return Collections.unmodifiableMap(this.coordsToDataHexagons);
	}
	
	public JsonObjectBuilder toJson() {
		var builder = Json.createObjectBuilder();
		var hexagons = Json.createArrayBuilder();
		for (CatanHexagon hexagon: coordsToDataHexagons.values()) {
			hexagons.add(hexagon.toJson());
		}
		builder.add("catanHexagons", hexagons);
		
		var points = Json.createArrayBuilder();
		for (CatanPoint point: pointsToDataPoints.values()) {
			points.add(point.toJson());
		}
		builder.add("catanPoints", points);
		
		var lines = Json.createArrayBuilder();
		for (CatanLine line: lineToDataLine.values()) {
			lines.add(line.toJson());
		}
		builder.add("catanLines", lines);
		builder.add("boardState", boardState.getId());
		builder.add("boardTypeId", boardTypeId);
		return builder;
	}
	
	
	
	public int getBoardType() {
		return boardTypeId;
	}
	
	
	//only for finding the people with the largest roads, discard after use
	private class RoadFinder {
		
		private List<CatanLine> unvisitedRoads;
		private List<List<CatanLine>> roadPaths;
		private final int longestRoadLength;
		public RoadFinder() {
			//get all roads (lines that have an owner) in a mutable list
			unvisitedRoads = new ArrayList<>(lineToDataLine.values().stream().filter(cLine -> cLine.hasOwner()).toList());
			//this a list of all road paths (an unbroken path of roads that has no repeat elements and all have the same owner)
			roadPaths = new LinkedList<>();
			while (!unvisitedRoads.isEmpty()) {
				recursiveHelper(unvisitedRoads.get(unvisitedRoads.size() - 1), new ArrayList<>());
			}
			//comparator sorts the lines from greatest to least number of elements
			roadPaths.sort((c1, c2) -> c2.size() - c1.size());
			
			if (roadPaths.isEmpty()) {
				longestRoadLength = 0;
			} else {
				longestRoadLength = roadPaths.get(0).size();
			}
			

		}
		public int getLongestRoadLength() {
			return  longestRoadLength;
		}
		public List<Integer> getIdsOfLongestRoadOwners() {
			if (roadPaths.isEmpty()) {return new ArrayList<>();}
			
			var ownerIds = new HashSet<Integer>();
			for (List<CatanLine> roadPath : roadPaths) {
				if (roadPath.size() == longestRoadLength) {
					ownerIds.add(roadPath.get(0).getOwner());
				}
				else {
					break;
				}
			}
			return new ArrayList<>(ownerIds);
			
		}
		private void recursiveHelper(CatanLine currentRoad, List<CatanLine> roadList) {
			Integer owner = currentRoad.getOwner();
			roadList.add(currentRoad);
			ArrayList<Line> potentialRoads = new ArrayList<Line>();
			potentialRoads.addAll(getLinesWithEndpoint(currentRoad.getLine().getP1()));
			potentialRoads.addAll(getLinesWithEndpoint(currentRoad.getLine().getP2()));
			//maps lines to datalines, checks to make sure the owner is correct & the road is not alredy included in the building roadline
			List<CatanLine> definiteAdditions = potentialRoads.stream().map(line -> lineToDataLine.get(line)).filter(CatanLine::hasOwner).filter(cLine -> cLine.getOwner().equals(owner)).filter(cLine -> !roadList.contains(cLine)).toList();
			//base case
			if (definiteAdditions.isEmpty()) {
				unvisitedRoads.removeAll(roadList);
				roadPaths.add(roadList);
			}
			else  {//recursive case
				for (CatanLine toAdd : definiteAdditions) {
					ArrayList<CatanLine> copy = new ArrayList<>(roadList);
					recursiveHelper(toAdd, copy);
				}
			}
		}
	}
	
	public void placeSettlement(Point point, int playerId) {
		if (canPlaceSettlement(point, playerId))
			pointsToDataPoints.get(point).setOwner(playerId);
	}
	
	public void placeRoad(Line line, Integer playerId) {
		if (canPlaceRoad(line, playerId)) {
			lineToDataLine.get(line).setOwner(playerId);
			roadFinder = null; //lazy update in case longest road changed
		}
	}
	
	public void upgradeToCity(Point point, int id) {
		if (canUpgradeToCity(point, id)) {
			this.pointsToDataPoints.get(point).makeCity();
		}
	}
	
	public List<Integer> getIdsOfLongestRoadOwners() {
		if (roadFinder == null) {roadFinder = new RoadFinder();}
		return roadFinder.getIdsOfLongestRoadOwners();
	}
	public int getLongestRoadLength() {
		if (roadFinder == null) {roadFinder = new RoadFinder();}
		return roadFinder.getLongestRoadLength();
	}
	
	//called to set the game state of the game to regular play mode
	public  void startGame() {
		boardState = RegularGame.getBoardState();
	}
	//since we know that the data of which lines are connected to which points won't change,  use memoization to speed up checks
	private HashMap<Point, List<Line>> memoizedPointesToLines;
	protected List<Line> getLinesWithEndpoint(Point p) {
		return memoizedPointesToLines.computeIfAbsent(p, point -> lineToDataLine.keySet().stream().filter(line -> line.getP1().equals(point) || line.getP2().equals(point)).toList());
	}
	 abstract static class BoardState {
		private int id;
		protected BoardState(int id) {
			this.id = id;
		}
		public int getId() {
			return id;
		}
		public abstract boolean canPlaceSettlement(CatanBoard cb, Point point, int playerid);
		public abstract boolean canPlaceRoad(CatanBoard cb,  Line line, int playerId);
		public abstract boolean canUpgradeToCity(CatanBoard cb, Point point, int playerId);
		public int hashCode() {return id;}
		public boolean equals(Object other) {
			if (!(other instanceof BoardState)) {
				return false;
			}
			else {
				var otherBoardState = (BoardState)other;
				return id == otherBoardState.id;
			}
		}
		protected void setState(CatanBoard cb, BoardState bs) {
			cb.boardState = bs;
		}
	}

	@Override
	public int hashCode() {
		return this.boardTypeId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof CatanBoard))
			return false;
		CatanBoard other = (CatanBoard) obj;
		if (boardState == null) {
			if (other.boardState != null)
				return false;
		} else if (!boardState.equals(other.boardState))
			return false;
		if (boardTypeId != other.boardTypeId)
			return false;
		if (coordsToDataHexagons == null) {
			if (other.coordsToDataHexagons != null)
				return false;
		} else if (!coordsToDataHexagons.equals(other.coordsToDataHexagons))
			return false;
		if (lineToDataLine == null) {
			if (other.lineToDataLine != null)
				return false;
		} else if (!lineToDataLine.equals(other.lineToDataLine))
			return false;
		if (pointsToDataPoints == null) {
			if (other.pointsToDataPoints != null)
				return false;
		} else if (!pointsToDataPoints.equals(other.pointsToDataPoints))
			return false;
		return true;
	}
	 

}
