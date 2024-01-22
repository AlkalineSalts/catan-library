package net.strangled.aldaris.catan.board;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.JsonSerializable;
import net.strangled.aldaris.catan.Resource;
import net.strangled.aldaris.catan.math.Line;
import net.strangled.aldaris.catan.math.Point;

public class CatanHexagon implements JsonSerializable{
	public static final int THIEF_NUMBER = 7;
	private static final String resourceTypeString = "resourceType";
	private static final String collectResourceNumberString = "collectResourceNumber";
	private Integer collectResourceNumber;
	private Resource resourceType;
	private int x;
	private int y;
	private CatanHexagon(int x, int y, Integer collectNumber, Resource rType) {
		this.x = x;
		this.y = y;
		this.collectResourceNumber = collectNumber;
		this.resourceType = rType;
	}
	public CatanHexagon(JsonObject hexagonObject) {
		x = hexagonObject.getInt("x");
		y = hexagonObject.getInt("y");
		resourceType = hexagonObject.isNull(resourceTypeString) ? null : Resource.valueOf(hexagonObject.getString(resourceTypeString));
		collectResourceNumber =  hexagonObject.isNull(collectResourceNumberString) ? null :  hexagonObject.getInt(collectResourceNumberString);
	}
	//get mathematical points. These points are on a plane unrelated to the hexagons
	//returns an array of length 6 of awt points
	//returns points in a clockwise fashion from the top
	public Point[] getMathematicalPoints() {
		Point[] points = new Point[6];
		points[0] = new Point(x, y);
		points[1] = new Point(-x, -y + 1);
		points[2] = new Point(x + 1, y + 1);
		points[3] = new Point(-x , -y);
		points[4] = new Point(x, y + 1);
		points[5] = new Point(-x + 1, -y + 1);
		return  points;
	}
	
	public int getCollectResourceNumber() {
		return collectResourceNumber;
	}
	public Resource getResourceType() {
		return resourceType;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	@Override
	public JsonObjectBuilder toJson() {
		var objectBuilder  = Json.createObjectBuilder().add("x", x).add("y", y);
		if (resourceType == null) {
			objectBuilder.addNull(resourceTypeString);
		}
		else {
			objectBuilder.add(resourceTypeString, resourceType.name());
		}
		if (collectResourceNumber == null) {
			objectBuilder.addNull(collectResourceNumberString);
		}
		else {
			objectBuilder.add(collectResourceNumberString, collectResourceNumber);
		}
		return objectBuilder;
	}
	
	//generate lines going clockwise from the top
	public Line[] getMathematicalLines() {
		Point[] points = getMathematicalPoints();
		Line[] lines = new Line[6];
		for (int i = 0; i < 6; i++) {
			lines[i] = new Line(points[i], points[(i+1) % 6]);
		}
		return lines;
	}
	static class CatanHexagonBuilder {
		private static final int UNINITIALIZED = Integer.MIN_VALUE;
		private Integer collectResourceNumber = null;
		private Resource resourceType = null;
		private int x = UNINITIALIZED;
		private int y = UNINITIALIZED;
		public CatanHexagonBuilder() {
			//no need to set anything
			}
		public void setX(int x) {
			this.x = x;
		}
		public void setY(int y) {
			this.y = y;
		}
		public void setResourceNumber(Integer num) {
			collectResourceNumber = num;
		}
		public void setResourceType(Resource r) {
			resourceType = r;
		}
		public CatanHexagon build() {
			if (x == UNINITIALIZED) {
				throw new IllegalStateException("x is uninitialized");
			}
			if (y == UNINITIALIZED) {
				throw new IllegalStateException("y is uninitialized");
			}
			if (collectResourceNumber == null && resourceType != null) {
				throw new IllegalStateException("collectResourceNumber is uninitialized when resourceType is");
			}
			else if (collectResourceNumber != null && resourceType == null) {
				throw new IllegalStateException("collectResourceNumber is initialized when resourceType is not");
			}
			else if (collectResourceNumber != null && (collectResourceNumber > 12 || collectResourceNumber < 2 || collectResourceNumber == THIEF_NUMBER)) {
				throw new IllegalStateException("collectResourceNumber " +  collectResourceNumber + " is invalid (must be between 2-12 and not 7)");
			}
			
			return new CatanHexagon(x, y, collectResourceNumber, resourceType);
		}
		
	}
	@Override
	public int hashCode() {
		return x * y;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CatanHexagon other = (CatanHexagon) obj;
		if (collectResourceNumber == null) {
			if (other.collectResourceNumber != null)
				return false;
		} else if (!collectResourceNumber.equals(other.collectResourceNumber))
			return false;
		if (resourceType != other.resourceType)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
}
