package Weather557.METAR;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import Weather557.General.CloudLayer;
import Weather557.General.Location;
import Weather557.General.SupplementaryWinds;
import Weather557.General.Visibility;
import Weather557.General.ExtremeTemperature;
import Weather557.General.Precipitation;
import Weather557.General.WeatherConditions;

@XmlRootElement(name="METAR")
@XmlAccessorType(XmlAccessType.FIELD)
public class Metar {
	public String sourceRef;
	public String cloudCeilingVisibilityOkCode;
	public String cloudCeilingDetermination;
	public String cloudHeightCeiling;
	public String cloudCoverTotal;
	
	@XmlElement(name="location")
	public Location location;
	
	public String latitude;
	public String longitude;
	public String observationRawData;
	public String observationDateTime;
	public String observationRemarks;
	public String platformHeight;
	public String platformIdentifier;
	public String platformMode;
	public String platformNetworkType;
	public String platformReportType;
	
	public String pressure3HourChg;
	public String pressureAltimeterSetting;
	public String pressureMeanSeaLevel;
	public String productBbbStatus;
	public String productReceiptTime;
	public String securityIdentifier;
	public String temperatureAir;
	public String temperatureDewpoint;
	public String windConditionsCode;
	public String windDirection;
	public String windGustSpeed;
	public String windMeasurementMode;
	public String windSpeed;
	public String windVariableDirectionEnd;
	public String windVariableDirectionStart;
	public String validity;
	
	
	@XmlElement(name="CloudLayer")
	public List<CloudLayer> cloudLayer;
	
	@XmlElement(name="ExtremeTemperature")
	public List<ExtremeTemperature> extremeTemperature;
	
	@XmlElement(name="Precipitation")
	public List<Precipitation> precipitation;
	
	@XmlElement(name="SupplementaryWinds")
	public List<SupplementaryWinds> supplementaryWinds;
	
	@XmlElement(name="Visibility")
	public List<Visibility> visibility;
	
	@XmlElement(name="WeatherConditions")
	public List<WeatherConditions> weatherConditions;
}
