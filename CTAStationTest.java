package finalProject;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class CTAStationTest {
    
    protected String name;
    protected String location;
    protected boolean wheelchair; 
    protected double latitude;
    protected double longitude;
    protected ArrayList<Integer> posOnLine;
    protected CTAStation ctaStation;
    protected void setUp() {
        name = "StationName";
        location = "LocationTest";
        wheelchair = true;
        latitude = 41.8;
        longitude = -88;
        posOnLine = new ArrayList<Integer>();
        posOnLine.add(-1);
        posOnLine.add(4);
        posOnLine.add(2);
        ctaStation = new CTAStation(name, location, wheelchair, latitude, longitude, posOnLine);
    }

    @Test
    public void calcDistanceMilesTest() {
        setUp();
        assertTrue(Math.abs(ctaStation.calcDistanceMiles(latitude, longitude)) < 0.000001);
        assertTrue(Math.abs(ctaStation.calcDistanceMiles(ctaStation)) < 0.000001);
    }
    
    @Test
    public void calcDistanceInheritedTest() {
        setUp();
        assertTrue(Math.abs(ctaStation.calcDistanceMiles(new GeoLocation(latitude, longitude))) < 0.000001);
    }
    
    @Test
    public void equalsTest() {
        setUp();
        assertTrue(ctaStation.equals(new CTAStation(name, location, wheelchair, latitude, longitude, posOnLine)));
        assertTrue(ctaStation.equals(ctaStation));
    }

}
