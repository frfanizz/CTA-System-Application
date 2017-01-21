//Francesco Fanizza
//2016.12.02

/*                                                                              |
 * CTASystem is an object class which stores all the CTA Station of the CTA
 * system as an ArrayList, and all the CTA Lines as another ArrayList.
 * 
 * Most of CTASystem's functionality comes from looping through the CTA Lines, 
 * and calling CTARoute methods to access or modify data. This avoids the need to
 * do this in the Application class, and also combines the necessary ArrayLists of
 * CTA Stations and CTA Lines into one easy-to-use object. For tasks that require
 * manipulation of CTA Stations and CTA Lines separately, this class is able to
 * accomplish those tasks in a cleaner way. For tasks that only require one or the
 * other, there is almost no extra effort required to do them.
 */

package finalProject;

import java.util.ArrayList;

public class CTASystem {
    
    //ArrayList variables which store all the routes(lines) and stations
    private ArrayList<CTARoute> ctaLines;
    private ArrayList<CTAStation> ctaStops;
    private ArrayList<CTAStation> transferStops;
    
    //Constructors
    public CTASystem() {
        ctaLines = new ArrayList<CTARoute>();
        ctaStops = new ArrayList<CTAStation>();
        transferStops = new ArrayList<CTAStation>();
    }
    public CTASystem(ArrayList<CTARoute> ctaLines, ArrayList<CTAStation> ctaStops) {
        this.ctaLines = ctaLines;
        this.ctaStops = ctaStops;
        this.transferStops = collectTransferStations();
    }

    //getters and setters
    public ArrayList<CTARoute> getCtaLines() {
        return ctaLines;
    }
    public void setCtaLines(ArrayList<CTARoute> ctaLines) {
        this.ctaLines = ctaLines;
    }

    public ArrayList<CTAStation> getCtaStops() {
        return ctaStops;
    }
    public void setCtaStops(ArrayList<CTAStation> ctaStops) {
        this.ctaStops = ctaStops;
    }
    
    public ArrayList<CTAStation> getTransferStops() {
        return transferStops;
    }
    public void setTransferStops(ArrayList<CTAStation> transferStops) {
        this.transferStops = transferStops;
    }
    
    //non-default toString
    public String toString() {
        String stringReturn = "";
        for (CTARoute line : ctaLines) {
            stringReturn += line.toString() + "\n";
        }
        return stringReturn;
    }
    //wheelchair toString
    public String wheelchairToString(boolean isWantsWheelchair) {
        String stringReturn = "";
        for (CTARoute line : ctaLines) {
            stringReturn += line.wheelchairToString(isWantsWheelchair) + "\n";
        }
        return stringReturn;
    }
    //stationInformationToString
    public String stationInformationToString(CTAStation ctaStation) {
        String stringReturn = ctaStation.stationInformationToString();
        if (whichLines(ctaStation) != null) {
            for (String color : whichLines(ctaStation)) {
                stringReturn += "Station is on the "+color+" line.\n";
            }
        }
        return stringReturn;
    }
    //toCSVString
    public String toCSVString() {
        String stringReturn = "Name,Latitude,Longitude,Location,Wheelchair,";
        for (int i = 0; i < ctaLines.size(); i++) {
            stringReturn += ctaLines.get(i).getLineColor().substring(0, 1).toUpperCase() +
                    ctaLines.get(i).getLineColor().substring(1) + ":" + ctaLines.get(i).getCtaRoute().size();
            if (i < ctaLines.size()-1) {
                stringReturn += ",";
            } else {
                stringReturn += "\n";
            }
        }
        stringReturn += "Null,Null,Null,Null,Null,";
        for (int i = 0; i < ctaLines.size(); i++) {
            if (ctaLines.get(i).getLastTwoWay() == null) {
                stringReturn += "Null";
            } else {
                stringReturn += ctaLines.get(i).getLastTwoWay().getName();
            }
            if (i < ctaLines.size()-1) {
                stringReturn += ",";
            } else {
                stringReturn += "\n";
            }
        }
        for (int i = 0; i < ctaStops.size(); i++) {
            stringReturn += ctaStops.get(i).toCSVString();
            if (i < ctaStops.size()-1) {
                stringReturn += "\n";
            }
        }
        return stringReturn;
    }
    
    //non-default equals
    public boolean equals(CTASystem ctaSystem) {
        boolean allEqual = true;
        if (!ctaSystem.getCtaLines().equals(this.getCtaLines())) {
            return false;
        }
        for (int i = 0; i < ctaSystem.getCtaStops().size(); i++) {
            if (!ctaSystem.getCtaStops().get(i).equals(this.getCtaStops().get(i))) {
                return false;
            }
        }
        return allEqual;
    }
    
    //add station method (adds a station to ctaStops)
    public boolean addStationToStations(CTAStation ctaStation) {
        ctaStops.add(ctaStation);
        return true;
    }
    
    //removeStation (removes station from ctaStops and ctaLines)
    public boolean removeStation(CTAStation stationToRemove) {
        boolean somethingRemoved = false;
        for (CTARoute line : ctaLines) {
            if (line.removeStation(stationToRemove)) {
                somethingRemoved = true;
            }
        }
        for (int i = 0; i < ctaStops.size(); i++) {
            if (ctaStops.get(i).equals(stationToRemove)) {
                ctaStops.remove(i);
                somethingRemoved = true;
            }
        }
        updateRouteNumbers();
        return somethingRemoved;
    }
    
    //lookupStation (look up station by name)
    public ArrayList<CTAStation> lookupStation(String searchName) {
        ArrayList<CTAStation> stationsFound = new ArrayList<CTAStation>();
        boolean isFound = false;
        for (CTAStation stop : ctaStops) {
            if (searchName.equalsIgnoreCase(stop.getName())) {
                stationsFound.add(stop);
                isFound = true;
            }
        }
        if (isFound) {
            return stationsFound;
        } else {
            return null;
        }
    }
    
    //generate route (recursive)
    public String generateRoute(CTAStation start, CTAStation end) {//, int skipStationsBefore) {
        int sharedLineIndex = shareWhichRoute(start, end); //check if on same line
        if (sharedLineIndex != -1) {
            return "\nTake the " + ctaLines.get(sharedLineIndex).getLineColor() + 
                    " line from " + start.toString() + " to " + end.toString();
        } else { 
            //if there is a single transfer station connecting the two stations' lines
            for (CTAStation transfer : transferStops) {
                int sharedTransferLineIndex = shareWhichRoute(start, transfer);
                if (shareWhichRoute(start, transfer) != -1 &&
                        !transfer.equals(start) &&
                        shareWhichRoute(end, transfer) != -1) {
                    return "\nTake the " + ctaLines.get(sharedTransferLineIndex).getLineColor() + 
                            " line from " + start.toString() + " to " + transfer.toString() +
                            generateRoute(transfer, end);
                }
            }
            //if there is not a single transfer station, picks a transfer station that connects to start and
            //recursively calls generateRoute(transfer, end) 
            for (CTAStation transfer : transferStops) {
                int sharedTransferLineIndex = shareWhichRoute(start, transfer);
                if (sharedTransferLineIndex != -1 && !transfer.equals(start) && !transfer.equals(end)) {
                    return "\nTake the " + ctaLines.get(sharedTransferLineIndex).getLineColor() + 
                            " line from " + start.toString() + " to " + transfer.toString() +
                            generateRoute(transfer, end);
                }
            }
        }
        return "\nSorry, there was an error. The route could not be generated.";
    }
    
    public ArrayList<CTAStation> collectTransferStations() {
        ArrayList<CTAStation> transferStations = new ArrayList<>();
        for (CTAStation station : ctaStops) {
            int onStations = 0;
            for (int j = 0; j < ctaLines.size(); j++) {
                if (station.getPosOnLine().get(j) != -1) {
                    onStations++;
                }
            }
            if (onStations > 1) {
                transferStations.add(station);
            }
        }
        return transferStations;
    }
    
    //if the two Stations are on the same line, returns that line index, else returns -1
    public int shareWhichRoute(CTAStation stationOne, CTAStation stationTwo) {
        for (int i = 1; i < ctaLines.size(); i++) {
            if (stationOne.getPosOnLine().get(i) != -1 && stationTwo.getPosOnLine().get(i) != -1) {
                return i;
            }
        }
        return -1;
    }
    
    //whichLines (returns an ArrayList of all linecolors that a station is on) 
    public ArrayList<String> whichLines(CTAStation ctaStation) {
        boolean isOnLine = false;
        ArrayList<String> lineColors = new ArrayList<String>();
        for (CTARoute line : ctaLines) {
            if (ctaStation.getPosOnLine().get(line.getLineColorIndex()) != -1) {
                isOnLine = true;
                lineColors.add(line.getLineColor());
            }
        }
        if (isOnLine) {
            return lineColors;
        } else {
            return null;
        }
    }
    
    //find nearest station
    public CTAStation nearestStation(GeoLocation userGeoLocation) {
        CTARoute nearestStations = new CTARoute();
        for (CTARoute line : ctaLines) {
            nearestStations.addStation(line.nearestStation(userGeoLocation));
        }
        return nearestStations.nearestStation(userGeoLocation);
    }
    
    //update route numbers (updates linePosition variables for all stations on all lines)
    public boolean updateRouteNumbers() {
        for (CTARoute line : ctaLines) {
            line.updateRouteNumbers();
        }
        return true;
    }
    
}
