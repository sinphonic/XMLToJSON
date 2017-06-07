package utils;

public class BoundingBox {
	private Coord topLeft, bottomRight;
	
	public BoundingBox(Coord topL, Coord bottomR) {
		topLeft = topL;
		bottomRight = bottomR;
	}
	
	public Coord getTopLeft() {
		return topLeft;
	}
	
	public Coord getBottomRight() {
		return bottomRight;
	}
	
	public boolean isCoordInBox(BoundingBox box, Coord p) {
		if ((p.getLat() >= box.getBottomRight().getLat() && p.getLat() <= box.getTopLeft().getLat()) &&
				(p.getLon() >= box.getTopLeft().getLon() && p.getLon() <= box.getBottomRight().getLon())) {
			return true;
		}
		else
			return false;
	}
	
	public String toString() {
		return topLeft.toString() + "-" + bottomRight.toString(); 
	}
	
	//Top-Down 
	public String getLats() {
		return topLeft.getLat() + " " + bottomRight.getLat();
	}
	
	//Right-Left
	public String getLons() {
		return bottomRight.getLon() + " " + topLeft.getLon();
	}
}
