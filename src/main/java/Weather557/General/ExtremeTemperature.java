package Weather557.General;

import javax.xml.bind.annotation.XmlType;

@XmlType(name="ExtremeTemperature")
public class ExtremeTemperature {
	public String observationPeriod;
	public String temperatureMaximum;
	public String temperatureMinimum;
}
