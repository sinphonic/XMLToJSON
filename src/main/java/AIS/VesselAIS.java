package AIS;

import javax.xml.bind.annotation.XmlType;

@XmlType(name="VesselAIS")
public class VesselAIS {

	public VesselDataDynamic vesselDataDynamic;
	public VesselDataStatic vesselDataStatic;
	
}
