package AIS;

import javax.xml.bind.annotation.XmlType;

@XmlType(name="TrackInfoVessel")
public class Vessel {
	public VesselID VesselId;
	public VesselAIS VesselAIS;
	
	
}
