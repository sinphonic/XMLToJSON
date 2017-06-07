package AIS;

import javax.xml.bind.annotation.XmlType;

@XmlType(name="VesselDataStatic")
public class VesselDataStatic {
	public String Type;
	public String CargoClass;
	public String Size_A;
	public String Size_B;
	public String Size_C;
	public String Size_D;
	public String FixDev;
}
