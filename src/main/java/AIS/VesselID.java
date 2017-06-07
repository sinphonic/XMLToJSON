package AIS;

import javax.xml.bind.annotation.XmlType;

@XmlType(name="VesselID")
public class VesselID {
	
	public String Name;
	public String MMSI;
	public String IMO;
	public String CallSign;
	
}
