package OutputFormats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import AIS.Track;

public class MMSIVessels {
	
	public Map<String, String> VESSELS = null;
	
	public void addObject(Object obj, Class<?> clazz) {
		if (clazz == Track.class) {
			if (VESSELS == null) {				
				VESSELS = new HashMap<String, String>();
			}
			
			Track tObj = (Track)obj;
			String mmsi = extractMMSI(tObj);
			String vesselName = extractVesselName(tObj);
			if (vesselName == null)
				vesselName = "";
			
			if (mmsi != null) {				
				if (VESSELS.containsKey(mmsi)) {
					String storedNames = VESSELS.get(mmsi);
					if (!storedNames.isEmpty()) {
						//System.out.println("--MMSI: " + mmsi + ", NAME: " + vesselName);
						if (!vesselName.isEmpty() && !doesValueExistInStore(vesselName, storedNames)) {
							//System.out.println("New Value Name found for MMSI: " + mmsi + "..." + vesselName);
							//TODO
							//String newValue = vesselName + "|" + 
							
							//OVERWRITE FOR NOW
							VESSELS.put(mmsi, storedNames + "|" + vesselName);
							
						}
					}
					else {
						//System.out.println("MMSI: " + mmsi+ ", NAME: WAS EMPTY" + ", NOW: " + vesselName);
						VESSELS.put(mmsi, vesselName);
					}
				}
				else {
					//System.out.println("FIRST ENTRY FOR MMSI:" + mmsi);
					VESSELS.put(mmsi, vesselName);
				}
			}
		}
	}
	
	private boolean doesValueExistInStore(String value, String store) {
		String[] splitString = store.split("\\|");
		boolean found = false;
		for (int i = 0; i < splitString.length; i++) {
			if (value.equals(splitString[i])) {
				found = true;
			}
		}
		return found;
	}
	
	private String extractMMSI(Track tObj) {
		String mmsi = null;
		if (tObj.TrackInfo != null && tObj.TrackInfo.vessel != null && tObj.TrackInfo.vessel.VesselId != null) {
			mmsi = ((Track)tObj).TrackInfo.vessel.VesselId.MMSI;
		}
		return mmsi;
	}
	
	private String extractVesselName(Track tObj) {
		String name = null;
		if (tObj.TrackInfo != null && tObj.TrackInfo.vessel != null && tObj.TrackInfo.vessel.VesselId != null) {
			name = ((Track)tObj).TrackInfo.vessel.VesselId.Name;
		}
		return name;
	}
	
}
