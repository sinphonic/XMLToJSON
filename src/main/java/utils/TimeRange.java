package utils;

public class TimeRange {
	private String hour;
	private String minute;
	private String timeZoneCode;
	
	//T14:00:10Z, T14:00
	public TimeRange(String timeString) {
		String[] timeArr;
		if (timeString.startsWith("T"))
			timeArr = (timeString.substring(1)).split(":");
		else
			timeArr = timeString.split(":");
		
		hour = timeArr[0];
		minute = timeArr[1];
		if (timeArr[2] != null)
			timeZoneCode = timeArr[2].replaceAll("[^A-Za-z]+", "");
		else
			timeZoneCode = "";
	}
	
	//Formatting for filenames
	public String toFilenameTimeRange() {
		return "T" + hour + "00-" + hour + "59";
	}
	
	//Formatting for JSON File
	public String toJSONTimeRange() {
		return hour + ":00-" + hour + ":59"; 
	}
	
	public String toFormattedString() {
		return hour + ":" + minute;
	}
}
