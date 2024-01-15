package net.strangled.aldaris.catan.math;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.JsonSerializable;

//intended to make immu
public class Point implements JsonSerializable {
	private final int x;
	private final int y;
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Point(JsonObject pointObject) {
		this.x = pointObject.getInt("x");
		this.y = pointObject.getInt("y");
	}
	public int getY() {
		return y;
	}
	public int getX() {
		return x;
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
		Point other = (Point) obj;
		return x == other.x && y == other.y;
	}
	@Override
	public JsonObjectBuilder toJson() {
		return Json.createObjectBuilder().add("x", x).add("y", y);
	}
	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
	
}
