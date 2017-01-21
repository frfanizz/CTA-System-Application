package finalProject;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class CTARouteTest {

    protected CTARoute route;
    protected CTAStation stationOne;
    protected CTAStation stationTwo;
    protected void setUp() {
        ArrayList<CTAStation> stations = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String name = "Station Name " + i + 1;
            String location = "Location Test " + i + 1;
            boolean wheelchair = (i%2 == 0) ? true : false;
            double latitude = 41.8 + 0.001*i;
            double longitude = -88 - 0.001*i;
            ArrayList<Integer> posOnLine = new ArrayList<Integer>();
            posOnLine.add(i);
            posOnLine.add(-1);
            posOnLine.add(-1 + 2*i);
            CTAStation currStation = new CTAStation(name, location, wheelchair, latitude, longitude, posOnLine);
            if (i < 5) {
                stations.add(currStation);
            } else if (i == 5) {
                stationOne = currStation;
            } else if (i == 6) {
                for (int j = 0; j < posOnLine.size(); j++) {
                    posOnLine.set(j, -1);
                }
                currStation.setPosOnLine(posOnLine);
                stationTwo = currStation;
            }
        }
        route = new CTARoute(0, "Color", stations, null);
    }
    
    @Test
    public void addStationTest() {
        setUp();
        assertTrue(route.addStation(stationOne));
    }
    
    @Test
    public void insertStationTest() {
        //for inserting (not at ends)
        setUp();
        assertTrue(route.insertStation(stationOne, route.getCtaRoute().get(1), route.getCtaRoute().get(2)));
        setUp();
        assertFalse(route.insertStation(stationOne, route.getCtaRoute().get(1), route.getCtaRoute().get(3)));
        //for inserting (at ends)
        setUp();
        assertTrue(route.insertStation(stationOne, route.getCtaRoute().get(0)));
        setUp();
        assertTrue(route.insertStation(stationOne, route.getCtaRoute().get(route.getCtaRoute().size()-1)));
        setUp();
        assertFalse(route.insertStation(stationOne, route.getCtaRoute().get(1)));
    }
    
    @Test
    public void removeStationTest() {
        setUp();
        route.addStation(stationOne);
        assertTrue(route.removeStation(stationOne));
        assertFalse(route.removeStation(stationOne));
    }
    
    @Test
    public void lookupStationTest() {
        setUp();
        assertEquals(route.getCtaRoute().get(1), route.lookupStation(route.getCtaRoute().get(1).getName()));
    }
    
    @Test
    public void findByNumberTest() {
        setUp();
        assertEquals(route.getCtaRoute().get(1), route.findByNumber(1));
    }
    
    @Test
    public void isOnRouteTest() {
        setUp();
        assertFalse(route.isOnRoute(stationTwo));
        setUp();
        assertTrue(route.isOnRoute(route.getCtaRoute().get(1)));
    }
    
    @Test
    public void isAtEndTest() {
        setUp();
        assertTrue(route.isAtEnd(route.getCtaRoute().get(0)));
        assertTrue(route.isAtEnd(route.getCtaRoute().get(route.getCtaRoute().size()-1)));
        assertFalse(route.isAtEnd(route.getCtaRoute().get(1))); //TODO check failed assertion
    }
    
    @Test
    public void nearestStationTest() {
        setUp();
        assertEquals(route.getCtaRoute().get(0), route.nearestStation(route.getCtaRoute().get(0)));
    }
    
//    @Test
//    public void updateRouteNumbersTest() {
//        setUp();
//        route.addStation(stationTwo);
//        route.updateRouteNumbers();
//        assertTrue(route.isOnRoute(stationTwo)); //check if posOnLine numbers change
//    }
    
//    @Test
//    public void sortStationsTest() {
//        setUp();
//        CTARoute originalRoute = new CTARoute();
//        for (int i = 0; i < route.getCtaRoute().size(); i++) {
//            originalRoute.addStation(route.getCtaRoute().get(i));
//        }
//        CTAStation temp = route.getCtaRoute().get(1);
//        route.getCtaRoute().set(1, route.getCtaRoute().get(0));
//        route.getCtaRoute().set(0, temp);
//        route.sortStations();
//        assertTrue(route.equals(originalRoute)); //check if positions change
//    }
    
    @Test
    public void equalsTest() {
        setUp();
        assertTrue(route.equals(route));
    }
    

}
