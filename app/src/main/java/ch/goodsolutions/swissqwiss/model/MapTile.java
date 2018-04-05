package ch.goodsolutions.swissqwiss.model;

public class MapTile {

	private String resourceId;
	private int imagePixelWidth;
	private int imagePixelHeight;
	private int mapOffsetPixelTop;
	private int mapOffsetPixelBottom;
	private int mapOffsetPixelLeft;
	private int mapOffsetPixelRight;
	private double mapCoordinateNorth;
	private double mapCoordinateSouth;
	private double mapCoordinateWest;
	private double mapCoordinateEast;
	/* calculated values */
	private double coordinateToPixelFactorLongitude;
	private double coordinateToPixelFactorLatitude;
	private int markerOffsetPixelTop = 0;
	private int markerOffsetPixelLeft = 0;
	private int renderedWidth = 0;
	private int renderedHeight = 0;
	private double scaleX = 1;
	private double scaleY = 1;

	public MapTile(String resourceId) {
		this.resourceId = resourceId;
	}

	public void compile() throws Exception {
		int mapPixelHeight = imagePixelHeight - mapOffsetPixelTop - mapOffsetPixelBottom;
		int mapPixelWidth = imagePixelWidth - mapOffsetPixelLeft - mapOffsetPixelRight;
		if (mapPixelHeight == 0 || mapPixelWidth == 0) {
			throw new Exception("Invalid map parameters");
		}
		double coordinateHeight = mapCoordinateNorth - mapCoordinateSouth;
		double coordinateWidth = mapCoordinateEast - mapCoordinateWest;
		coordinateToPixelFactorLongitude = mapPixelWidth / coordinateWidth;
		coordinateToPixelFactorLatitude = mapPixelHeight / coordinateHeight;
	}

	public boolean updateRenderedSize(int renderedWidth, int renderedHeight) {
		if (renderedWidth == 0 || renderedHeight == 0)
			return false;
		if (this.renderedWidth == renderedWidth && this.renderedHeight == renderedHeight)
			return false;
		this.renderedWidth = renderedWidth;
		this.renderedHeight = renderedHeight;
		scaleX = (double) renderedWidth / (double) imagePixelWidth;
		scaleY = (double) renderedHeight / (double) imagePixelHeight;
		return true;
	}

	public void calculateMarkerOffsets(Location location, Marker marker) {
		// top offset
		markerOffsetPixelTop = (int) (Math
				.round((((mapCoordinateNorth - location.getLatitude()) * coordinateToPixelFactorLatitude) + mapOffsetPixelTop - marker
						.getOffsetPixelTop()) * scaleY));
		// left offset
		markerOffsetPixelLeft = (int) (Math
				.round((((location.getLongitude() - mapCoordinateWest) * coordinateToPixelFactorLongitude) + mapOffsetPixelLeft - marker
						.getOffsetPixelLeft()) * scaleX));
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public int getImagePixelWidth() {
		return imagePixelWidth;
	}

	public void setImagePixelWidth(int imagePixelWidth) {
		this.imagePixelWidth = imagePixelWidth;
	}

	public int getImagePixelHeight() {
		return imagePixelHeight;
	}

	public void setImagePixelHeight(int imagePixelHeight) {
		this.imagePixelHeight = imagePixelHeight;
	}

	public int getMapOffsetPixelTop() {
		return mapOffsetPixelTop;
	}

	public void setMapOffsetPixelTop(int mapOffsetPixelTop) {
		this.mapOffsetPixelTop = mapOffsetPixelTop;
	}

	public int getMapOffsetPixelBottom() {
		return mapOffsetPixelBottom;
	}

	public void setMapOffsetPixelBottom(int mapOffsetPixelBottom) {
		this.mapOffsetPixelBottom = mapOffsetPixelBottom;
	}

	public int getMapOffsetPixelLeft() {
		return mapOffsetPixelLeft;
	}

	public void setMapOffsetPixelLeft(int mapOffsetPixelLeft) {
		this.mapOffsetPixelLeft = mapOffsetPixelLeft;
	}

	public int getMapOffsetPixelRight() {
		return mapOffsetPixelRight;
	}

	public void setMapOffsetPixelRight(int mapOffsetPixelRight) {
		this.mapOffsetPixelRight = mapOffsetPixelRight;
	}

	public double getMapCoordinateNorth() {
		return mapCoordinateNorth;
	}

	public void setMapCoordinateNorth(double mapCoordinateNorth) {
		this.mapCoordinateNorth = mapCoordinateNorth;
	}

	public double getMapCoordinateSouth() {
		return mapCoordinateSouth;
	}

	public void setMapCoordinateSouth(double mapCoordinateSouth) {
		this.mapCoordinateSouth = mapCoordinateSouth;
	}

	public double getMapCoordinateWest() {
		return mapCoordinateWest;
	}

	public void setMapCoordinateWest(double mapCoordinateWest) {
		this.mapCoordinateWest = mapCoordinateWest;
	}

	public double getMapCoordinateEast() {
		return mapCoordinateEast;
	}

	public void setMapCoordinateEast(double mapCoordinateEast) {
		this.mapCoordinateEast = mapCoordinateEast;
	}

	public int getMarkerOffsetPixelTop() {
		return markerOffsetPixelTop;
	}

	public int getMarkerOffsetPixelLeft() {
		return markerOffsetPixelLeft;
	}

	@Override
	public String toString() {
		return "Map [resourceId=" + resourceId + ", imagePixelWidth=" + imagePixelWidth + ", imagePixelHeight="
				+ imagePixelHeight + ", mapOffsetPixelTop=" + mapOffsetPixelTop + ", mapOffsetPixelBottom="
				+ mapOffsetPixelBottom + ", mapOffsetPixelLeft=" + mapOffsetPixelLeft + ", mapOffsetPixelRight="
				+ mapOffsetPixelRight + ", mapCoordinateNorth=" + mapCoordinateNorth + ", mapCoordinateSouth="
				+ mapCoordinateSouth + ", mapCoordinateWest=" + mapCoordinateWest + ", mapCoordinateEast=" + mapCoordinateEast
				+ ", coordinateToPixelRatioLongitude=" + coordinateToPixelFactorLongitude + ", coordinateToPixelRatioLatitude="
				+ coordinateToPixelFactorLatitude + ", markerOffsetPixelTop=" + markerOffsetPixelTop
				+ ", markerOffsetPixelLeft=" + markerOffsetPixelLeft + "]";
	}

}
