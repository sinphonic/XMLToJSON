package Weather557.General;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="Point")
public class Point {
	public String pos;
	
	@XmlAttribute(name="srsName")
	public String srsName;
	
	public String getPos() {
		return pos;
	}
	
	public String getSrsName() {
		return srsName;
	}
}
