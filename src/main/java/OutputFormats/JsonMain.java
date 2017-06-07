package OutputFormats;
import java.util.ArrayList;
import java.util.List;

import Weather557.BUOY.*;
import Weather557.MESOS.*;
import Weather557.METAR.*;
import AIS.*;

public class JsonMain {
	public String Lat;
	public String Lon;
	public String Time;
	
	public List<Buoy> BUOY = null;
	public List<Mesos> MESOS = null;
	public List<Metar> METAR = null;
	public List<Track> TRACK = null;
	
	public void addObject(Object obj, Class<?> clazz) {
		if (clazz == Buoy.class) {
			if (BUOY == null)
				BUOY = new ArrayList<Buoy>();
			BUOY.add((Buoy)obj);
		}
		if (clazz == Mesos.class) {
			if (MESOS == null)
				MESOS = new ArrayList<Mesos>();
			MESOS.add((Mesos)obj);
		}
		if (clazz == Metar.class) {
			if (METAR == null)
				METAR = new ArrayList<Metar>();
			METAR.add((Metar)obj);
		}
		if (clazz == Track.class) {
			if (TRACK == null)
				TRACK = new ArrayList<Track>();
			TRACK.add((Track)obj);
		}
	}
}
