package AIS;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="TrackInfo")
public class TrackInfo {
	
	@XmlElement(name="TrackInfoVessel")
	public Vessel vessel;
}
