package AIS;

import javax.xml.bind.annotation.XmlType;

@XmlType(name="VesselDataVoyage")
public class VesselDataVoyage {
	public String ETA;
	public String Draught;
	public String Destination;
}
