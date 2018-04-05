package ch.goodsolutions.swissqwiss.model;

public class Marker {

	private final String resourceName;
	private final int pixelWidth;
	private final int pixelHeight;
	private final int offsetPixelLeft;
	private final int offsetPixelTop;

	public Marker(String resourceName, int pixelWidth, int pixelHeight, int offsetPixelLeft, int offsetPixelTop) {
		super();
		this.resourceName = resourceName;
		this.pixelWidth = pixelWidth;
		this.pixelHeight = pixelHeight;
		this.offsetPixelLeft = offsetPixelLeft;
		this.offsetPixelTop = offsetPixelTop;
	}

	public String getResourceName() {
		return resourceName;
	}

	public int getPixelWidth() {
		return pixelWidth;
	}

	public int getPixelHeight() {
		return pixelHeight;
	}

	public int getOffsetPixelLeft() {
		return offsetPixelLeft;
	}

	public int getOffsetPixelTop() {
		return offsetPixelTop;
	}

	@Override
	public String toString() {
		return "Marker [resourceName=" + resourceName + ", pixelWidth=" + pixelWidth + ", pixelHeight=" + pixelHeight
				+ ", offsetPixelLeft=" + offsetPixelLeft + ", offsetPixelTop=" + offsetPixelTop + "]";
	}

}
