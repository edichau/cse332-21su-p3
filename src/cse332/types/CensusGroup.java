package cse332.types;


public class CensusGroup {
	public int   population;
	public double realLatitude;
	public double latitude;
	public double longitude;
	public CensusGroup(int pop, double lat, double lon) {
		population = pop;
		realLatitude = lat;
		latitude   = mercatorConversion(lat);
		longitude  = lon;
	}
	
	private double mercatorConversion(double lat){
		double latpi = lat * Math.PI / 180;
		double x = Math.log(Math.tan(latpi) + 1 / Math.cos(latpi));
		//System.out.println(lat + " -> " + x);
		return x;
	}
}
