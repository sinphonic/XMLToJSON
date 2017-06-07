
package Weather557.General;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="location")
public class Location {
	
	@XmlElement(name = "Point")
	public Point point;
	
	public Point getPoint() {
		return point;
	}
}
