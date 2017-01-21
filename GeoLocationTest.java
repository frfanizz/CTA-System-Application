package finalProject;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeoLocationTest {
    
    protected double latitude;
    protected double longitude;
    protected GeoLocation geoLocation;
    protected void setUp() {
        latitude = 41.8;
        longitude = -88;
        geoLocation = new GeoLocation(latitude, longitude);
    }
    

    @Test
    public void testCalcDistanceMiles() {
        setUp();
        assertTrue(geoLocation.calcDistanceMiles(geoLocation) < 0.00001);
        assertTrue(geoLocation.calcDistanceMiles(latitude, longitude) < 0.00001);
    }
    
    @Test
    public void testEquals() {
        setUp();
        assertTrue(geoLocation.equals(geoLocation));
        assertTrue(geoLocation.equals(new GeoLocation(latitude, longitude)));
    }

}
