package Weather557.MESOS;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import Weather557.General.*;

@XmlRootElement(name="MESOS")
@XmlAccessorType(XmlAccessType.FIELD)
public class Mesos {
	public String sourceRef;
	
	@XmlElement(name="location")
	public Location location;
	
	public String latitude;
	public String longitude;
	public String observationDateTime;
	public String observationRawData;
	public String platformHeight;
	public String platformIdentifier;
	public String platformMode;
	public String platformNetworkType;
	public String platformReportType;
	public String productReceiptTime;
	public String securityIdentifier;
	public String validity;
	public String humidityRelative;
	public String temperatureAir;
	public String temperatureDewpoint;
	public String windConditionsCode;
	public String windDirection;
	public String windSpeed;
	public String windGustSpeed;
	public String pressureAltimeterSetting;
	public String pressureStation;
	
	@XmlElement(name="SupplementaryWinds")
	public SupplementaryWinds supplementaryWinds;
	
	@XmlElement(name="Precipitation")
	public Precipitation precipitation;
	
	@XmlElement(name="Visibility")
	public Visibility visibility;
}
