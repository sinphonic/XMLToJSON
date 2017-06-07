package Weather557.General;

import javax.xml.bind.annotation.XmlType;

@XmlType(name="Precipitation")
public class Precipitation {
	public String observationPeriod;
	public String precipitationMeasurementType;
	public String precipitationAccumulated;
	public String precipitationCondition;
}
