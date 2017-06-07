package Weather557.BUOY;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import Weather557.General.Location;

@XmlRootElement(name="BUOY")
@XmlAccessorType(XmlAccessType.FIELD)
public class Buoy {
	public String sourceRef;
	public String cloudCeilingVisibilityOkCode;
	
	@XmlElement(name="location")
	public Location location;
	
	public String latitude;
	public String longitude;
	public String productReceiptTime;
	public String observationRawData;
	public String securityIdentifier;
	public String observationDateTime;
	public String platformHeight;
	public String platformMode;
	public String platformIdentifier;
	public String platformNetworkType;
	public String platformReportType;
	public String pressureMeanSeaLevel;
	public String temperatureSeaSurface;
	public String windConditionsCode;
	public String windMeasurementMode;
	public String validity;
	
	@XmlElement(name="OceanProfileElement")
	public OceanProfileElement oceanProfileElement;
}
