package AIS;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Track")
@XmlAccessorType(XmlAccessType.FIELD)
public class Track {
	public String DateTime;
	public String CountryMID;
	public String RegionID;
	
	public TrackInfo TrackInfo;
}
