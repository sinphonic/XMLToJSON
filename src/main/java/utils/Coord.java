package utils;

public class Coord {
	private double lat, lon;
	
	public Coord (double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}
	
	public double getLat() {
		return lat;
	}
	
	public double getLon() {
		return lon;
	}
	
	public String toString() {
		return "[" + lat + " " + lon + "]";
	}
}
