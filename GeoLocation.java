//Francesco Fanizza
//2016.12.02

/*                                                                              |
 * GeoLocation is an object class that stores a location as a latitude and 
 * longitude. This object class's unique method is the calcDistanceMiles method,
 * which calculates the distance to another geoLocation (or latitude and
 * longitude pair) in miles.
 */

package finalProject;

public class GeoLocation {
    
    //Instance variables
    protected double latitude;
    protected double longitude;
    
    // Constructor Methods
    public GeoLocation() {
        latitude = 0;
        longitude = 0;
    }
    public GeoLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    // Accessors and Mutators for latitude
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    // Accessors and Mutators for longitude
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    //calcDistanceMiles
    public double calcDistanceMiles(GeoLocation geoLocation) {
        return calcDistanceMiles(geoLocation.getLatitude(), geoLocation.getLongitude());
    }
    //calcDistance method to calculate the distance between the current GeoLocation and a set of
    //latitude and longitude values
    public double calcDistanceMiles(double latitude, double longitude) {
        // reference: http://andrew.hedges.name/experiments/haversine/
        double earthRadius = 3961; //miles = 6373 km
        double latDiff = (latitude - this.getLatitude())*Math.PI/180.0;
        double longDiff = (longitude - this.getLongitude())*Math.PI/180.0;
        double a = 
                Math.pow((Math.sin(latDiff/2.0)), 2) +
                Math.cos(this.getLatitude()) * Math.cos(latitude) * Math.pow((Math.sin(longDiff/2.0)), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadius * c;
    }
    
    
    //non-default toString method
    public String toString() {
        return "Latitude = "+latitude+"\nLongitude = "+longitude;
    }
    
    //non-default equals method
    public boolean equals(GeoLocation geoLocation) {
        if (geoLocation == null)
            return false;
        if (Math.abs(this.latitude - geoLocation.latitude) > 0.000001)
            return false;
        if (Math.abs(this.longitude - geoLocation.longitude) > 0.000001)
            return false;
        return true;
    }
    
    
}
