package ch.goodsolutions.swissqwiss.model;

public class Location {

	private int id;
	private String displayName;
	private double longitude;
	private double latitude;

	public Location(int id, String displayName, double latitude, double longitude) {
		this.id = id;
		this.displayName = displayName;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return "Location [id=" + id + ", displayName=" + displayName + ", longitude=" + longitude + ", latitude="
				+ latitude + "]";
	}
}
