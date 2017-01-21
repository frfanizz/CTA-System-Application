//Francesco Fanizza
//2016.12.02

/*                                                                              |
 * CTARoute is an object class which contains information relevant to a CTA Line.
 * CTARoute contains an ArrayList variable of CTAStations, an integer
 * representation of the line as part of the CTASystem, the name (color) of the
 * line, and a CTAStation variable denoting the last two-way station of the line
 * before it becomes one way at the loop.
 * 
 * CTARoute contains many methods which iterate through all CTAStations stored in
 * the ArrayList to access and manipulate the data in useful ways.
 */

package finalProject;

import java.util.ArrayList;

public class CTARoute {
    // ArrayList variable which contains all the stations in each line
    private int lineColorIndex;
    private String lineColor;
    private ArrayList<CTAStation> ctaRoute;
    private CTAStation lastTwoWay;
    
    //Constructor methods
    public CTARoute() {
        this.lineColorIndex = 0;
        this.lineColor = "";
        this.ctaRoute = new ArrayList<CTAStation>();
        this.lastTwoWay = null;
    }
    public CTARoute(int lineColorIndex, String lineColor, ArrayList<CTAStation> ctaRoute, CTAStation lastTwoWay) {
        this.lineColorIndex = lineColorIndex;
        this.lineColor = lineColor;
        this.ctaRoute = ctaRoute;
        this.lastTwoWay = lastTwoWay;
    }
    
    //getters and setters
    public int getLineColorIndex() {
        return lineColorIndex;
    }
    public void setLineColorIndex(int lineColorIndex) {
        this.lineColorIndex = lineColorIndex;
    }
    
    public String getLineColor() {
        return lineColor;
    }
    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }
    
    public ArrayList<CTAStation> getCtaRoute() {
        return ctaRoute;
    }
    public void setCtaRoute(ArrayList<CTAStation> ctaRoute) {
        this.ctaRoute = ctaRoute;
    }
    public CTAStation getLastTwoWay() {
        return lastTwoWay;
    }
    public void setLastTwoWay(CTAStation lastTwoWay) {
        this.lastTwoWay = lastTwoWay;
    }
    
    
    //non-default toString
    public String toString() {
        boolean isStationFound = false;
        String allStationsString = "Stations on the "+lineColor+" Line:\n";
        for (CTAStation currStation : ctaRoute) {
//            allStationsString += currStation.getCTALines()[this.getLineColorIndex()]+" "+currStation.toString()+"\n";
            allStationsString += currStation.toString()+"\n";
            isStationFound = true;
        }
        if (lastTwoWay != null) {
            allStationsString += "The last two way station before the loop is "+lastTwoWay.toString()+"\n";
        }
        if (isStationFound) {
            return allStationsString;
        } else {
            return null;
        }
    }
    
    public String wheelchairToString(boolean isWantsWheelchair) {
        boolean isStationFound = false;
        String wheelchairString;
        if (isWantsWheelchair) {
            wheelchairString = "The following stations on the "+lineColor+" Line have wheelchair access:\n";
        } else {
            wheelchairString = "The following stations on the "+lineColor+" Line do not have wheelchair access:\n";
        }
        for (CTAStation currStation : ctaRoute) {
            if (currStation.isWheelchair() == isWantsWheelchair) {
                wheelchairString += currStation.toString()+"\n";
                isStationFound = true;
            }
        }
        if (isStationFound) {
            return wheelchairString;
        } else {
            if (isWantsWheelchair) {
                return "No stations on the "+lineColor+" Line have wheelchair access.\n";
            } else {
                return "No stations on the "+lineColor+" Line do not have wheelchair access.\n";
            }
        }
    }
    
    //non-default equals
    public boolean equals(CTARoute ctaRoute) {
        boolean allEqual = true;
        if (ctaRoute.getLineColorIndex() != this.getLineColorIndex()) {
            return false;
        }
        if (!ctaRoute.getLineColor().equals(this.getLineColor())) {
            return false;
        }
        for (int i = 0; i < this.ctaRoute.size(); i ++) {
            if (!ctaRoute.getCtaRoute().get(i).equals(this.getCtaRoute().get(i))) {
                return false;
            }
        }
        return allEqual;
    }

    //Add Station method
    //appends CTAStation to route: does not modify values, or place station in variable-dependent position 
    public boolean addStation(CTAStation stationToAdd) {
        try {
            ctaRoute.add(stationToAdd);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    //Insert method
    //inserts CTAStation in route between two CTAStations
    public boolean insertStation(CTAStation stationToAdd, CTAStation previousStation, CTAStation nextStation) {
        for (int i = 0; i < ctaRoute.size();i++) {
            if (ctaRoute.get(i).equals(previousStation)) {
                if (i != 0 && ctaRoute.get(i-1).equals(nextStation)) {
                    ctaRoute.add(i,stationToAdd);
                    return true;
                } else if (i != ctaRoute.size() && ctaRoute.get(i+1).equals(nextStation)) {
                    ctaRoute.add(i+1,stationToAdd);
                    return true;
                }
            }
        }
        return false;
    }
    
    //Insert method
    //inserts CTAStation in route between two CTAStations (or at end)
    public boolean insertStation(CTAStation stationToAdd, CTAStation adjacentStation) {
        for (int i = 0; i < ctaRoute.size();i++) {
            if (ctaRoute.get(i).equals(adjacentStation)) {
                if (i == 0 || i == ctaRoute.size() - 1) {
                    ctaRoute.add(i,stationToAdd);
                    return true;
                }
            }
        }
        return false;
    }
    
    //Delete method
    public boolean removeStation(CTAStation stationToRemove) {
        for (int i = 0; i < ctaRoute.size();i++) {
            if (ctaRoute.get(i).equals(stationToRemove)) {
                ctaRoute.remove(i);
                return true;
            }
        }
        return false;
    }
    
    //lookup station function
    public CTAStation lookupStation(String searchName) {
        for (CTAStation currStation : this.getCtaRoute()) {
            if (searchName.equalsIgnoreCase(currStation.getName())) {
                return currStation;
            }
        }
        return null;
    }
    
    //search by route position
    public CTAStation findByNumber(int routePos) {
        for (CTAStation currStation : this.getCtaRoute()) {
            if (currStation.getPosOnLine().get(this.getLineColorIndex()) == routePos) {
                return currStation;
            }
        }
        return null;
    }
    
    //is CTAStation on line (true or false)
    public boolean isOnRoute(CTAStation stationToCheck) {
        for (CTAStation currStation : ctaRoute) {
            if (currStation.equals(stationToCheck)) {
                return true;
            }
        }
        return false;
    }
    
    //is CTAStation at end of line (true or false)
    public boolean isAtEnd(CTAStation stationToCheck) {
        int routePosition = stationToCheck.getPosOnLine().get(this.lineColorIndex);
        if (routePosition == 0 || routePosition == ctaRoute.size()-1) {
            return true;
        }
        return false;
    }
    
    //Find the nearest CTAStation on this route
    public CTAStation nearestStation(GeoLocation userGeoLocation) {
        CTAStation nearestStation = ctaRoute.get(0);
        double minDistance = nearestStation.calcDistanceMiles(userGeoLocation);
        //loop through all CTAStations (skip the 1st since nearest starts with that)
        for (int i = 1; i < ctaRoute.size(); i++) { 
            if (ctaRoute.get(i) != null) {
                //compare with nearest
                if (userGeoLocation.calcDistanceMiles(ctaRoute.get(i)) <= minDistance) {
                    nearestStation = ctaRoute.get(i);
                    minDistance = userGeoLocation.calcDistanceMiles(ctaRoute.get(i));
                }
            }
        }
        return nearestStation;
    }
    
    //Update station route numbers based on ArrayList position
    public boolean updateRouteNumbers() {
        for (int i = 0; i < ctaRoute.size(); i++) {
            ArrayList<Integer> updatedLines = ctaRoute.get(i).getPosOnLine();
            updatedLines.set(lineColorIndex, i);
            ctaRoute.get(i).setPosOnLine(updatedLines);
        }
        return true;
    }
    
    //sort (Selection sort) stations based on position value (posOnLine)
    public void sortStations() {
        for (int i = 0; i < ctaRoute.size()-1; i++) {
            int min = i;
            for (int j = i+1; j < ctaRoute.size(); j++) {
                if (ctaRoute.get(j).getPosOnLine().get(lineColorIndex) < 
                        ctaRoute.get(min).getPosOnLine().get(lineColorIndex)) {
                    min = j;
                }
            }
            if (min != i) {
                ctaRoute.add(i,ctaRoute.get(min));
                ctaRoute.remove(min+1);
            }
        }
    }

//    //Create route for two stations on same line
//    //Handles loops for lines with one end
//    public String generateRoute(CTAStation start, CTAStation end) {
//        String routeString = "";
//        
//        return routeString;
//    }
    
}

