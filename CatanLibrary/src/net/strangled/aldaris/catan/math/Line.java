package net.strangled.aldaris.catan.math;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.strangled.aldaris.catan.JsonSerializable;

//designed to be immutable
public class Line implements JsonSerializable {
	private final Point p1;
	private final Point p2;
	public Line(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
	public Line(JsonObject lineObject) {
		p1 = new Point(lineObject.getJsonObject("p1"));
		p2 = new Point(lineObject.getJsonObject("p2"));
	}
	public Point getP1() {
		return p1;
	}
	public Point getP2() {
		return p2;
	}
	@Override
	public int hashCode() {
		return p1.getX() + p2.getX() + p1.getY() + p2.getY();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Line other = (Line) obj;
		return p1.equals(other.p1) && p2.equals(other.p2) || p1.equals(other.p2) && p2.equals(other.p1);
	}
	@Override
	public JsonObjectBuilder toJson() {
		return Json.createObjectBuilder().add("p1", p1.toJson()).add("p2", p2.toJson());
	}
	@Override
	public String toString() {
		return "[" + p1.toString() + ", " + p2.toString() + "]";
	}
}
