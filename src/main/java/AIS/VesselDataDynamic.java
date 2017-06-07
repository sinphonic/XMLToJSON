package AIS;

import javax.xml.bind.annotation.XmlType;

@XmlType(name="VesselDataDynamic")
public class VesselDataDynamic {
	public Coordinate Coordinate;
	public String COG;
	public String SOG;
	public String HDT;
	public String ROT;
	public String NavStatus;
	public String PosAcc;
	
}
