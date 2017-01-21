//Francesco Fanizza
//2016.12.02

/*                                                                              |
 * CTAStation is an object class that stores information about a CTA Station. It
 * inherits from GeoLocation to store the Station's location, and also has 
 * variables for the station name, vertical location (elevated, ground, etc.), 
 * wheelchair accessibility, and it's position on each CTA Line.
 * 
 * This Object class contains many methods that are used by CTARoute, CTASystem,
 * or directly.
 */

package finalProject;

import java.util.ArrayList;

public class CTAStation extends GeoLocation {
    //instance variables
    private String name;
    private String location;
    private boolean wheelchair;
    private ArrayList<Integer> posOnLine = new ArrayList<Integer>();
    //0 red, 1 green, 2 blue, 3 brown, 4 purple, 5 pink, 6 orange, 7 yellow

    //Constructor methods
    public CTAStation() {
        name = "";
        location = "";
        wheelchair = false;
        for (int i=0; i<posOnLine.size(); i++) {
            posOnLine.set(i, -1);
        }
    }
//    public CTAStation(double latitude, double longitude) {
//        super(latitude, longitude);
//        name = "";
//        location = "";
//        wheelchair = false;
//        for (int i=0; i<posOnLine.size(); i++) {
//            posOnLine.set(i, -1);
//        }
//    }
//    public CTAStation(String name, String location, boolean wheelchair,
//            double latitude, double longitude, int[] ctaLines) {
//        super(latitude,longitude);
//        this.name = name;
//        this.location = location;
//        this.wheelchair = wheelchair;
//        for (int i=0; i<ctaLines.length; i++) {
//            this.posOnLine.set(i, ctaLines[i]);
//        }
//    }
    public CTAStation(String name, String location, boolean wheelchair,
            double latitude, double longitude, ArrayList<Integer> posOnLine) {
        super(latitude,longitude);
        this.name = name;
        this.location = location;
        this.wheelchair = wheelchair;
        this.posOnLine = posOnLine;
    }
    
    //Accessor and mutator methods for all instance variables
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    
    public boolean isWheelchair() {
        return wheelchair;
    }
    public void setWheelchair(boolean wheelchair) {
        this.wheelchair = wheelchair;
    }
    
    public ArrayList<Integer> getPosOnLine() {
        return posOnLine;
    }
    public void setPosOnLine(ArrayList<Integer> posOnLine) {
        this.posOnLine = posOnLine;
    }
    
    //calcDistance method to calculate the distance between the current CTAStation and another CTAStation
//    public double calcDistanceKilometer(CTAStation ctaStation) {
//        double latitudeDifference = ctaStation.getLatitude() - this.getLatitude();
//        double longitudeDifference = ctaStation.getLongitude() - this.getLongitude();
//        return Math.sqrt(Math.pow(latitudeDifference,2) + Math.pow(longitudeDifference,2));
//    }
    
    //calcDistance method to calculate the distance between the current CTAStation and a set of
    //latitude and longitude values
    public double calcDistanceMiles(CTAStation ctaStation) {
        return calcDistanceMiles(ctaStation.getLatitude(), ctaStation.getLongitude());
    }
    
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
    
//    //calcDistance method to calculate the distance between the current CTAStation and another CTAStation
//    public double calcDistanceBasic(CTAStation ctaStation) {
//        return calcDistanceBasic(ctaStation.getLatitude(), ctaStation.getLongitude());
//    }
//    
//    //calcDistance method to calculate the distance between the current CTAStation and a set of
//    //latitude and longitude values
//    public double calcDistanceBasic(double latitude, double longitude) {
//        double latitudeDifference = latitude - this.getLatitude();
//        double longitudeDifference = longitude - this.getLongitude();
//        return Math.sqrt(Math.pow(latitudeDifference,2) + Math.pow(longitudeDifference,2));
//    }
    
//    //method to determine if the current CTAStation and another CTAStation are on the same line
//    public int onSameLine(CTAStation ctaStation) {
//        for (int i = 0; i < this.getPosOnLine().size(); i++) {
//            if (this.getPosOnLine().get(i) != -1 && ctaStation.getPosOnLine().get(i) != -1) {
//                return i;
//            }
//        }
//        return -1;
//    }
    
    //non-default toString method
    public String toString() {
        return this.getName();
    }
    
    //non-default toString method
    public String toStringWithCoordinates() {
        return this.getName()+", located at Latitude: "+
                this.getLatitude()+", Longitude: "+this.getLongitude();
    }
    
    
    //get CTA line from position in csv file
//    public String whichLine(int lineNumber) {
//        // Read from file
//        File inputFile = new File("CTAStops.csv");  //TODO replace hardcoded file name
//        Scanner fromFile;
//        try {
//            fromFile = new Scanner(inputFile);
//            String firstLine = fromFile.nextLine();
//            fromFile.close();
//            String[] firstLineArray = firstLine.split(",");
//            return firstLineArray[5+lineNumber];
//        } catch (FileNotFoundException e) {
//            return "";
//        }
//    }
    
    //non-default toString method
    public String stationInformationToString() {
        String stringReturn = "";
        stringReturn += "Station name: " + this.getName() + "\n";
        stringReturn += "Latitude: " + this.getLatitude() + "\n";
        stringReturn += "Longitude: " + this.getLongitude() + "\n";
        stringReturn += "Station location (relative to ground): " + this.getLocation() + "\n";
        if (this.isWheelchair()) {
            stringReturn += "Wheelchair accessible" + "\n";
        } else {
            stringReturn += "Not wheelchair accessible" + "\n";
        }
        return stringReturn;
    }
    //toCSV
    public String toCSVString() {
        String stringReturn = this.getName() + "," + this.getLatitude() + "," + this.getLongitude() + "," +
                this.getLocation() + "," + Boolean.toString(this.isWheelchair()).toUpperCase() + ",";
        for (int i = 0; i < this.getPosOnLine().size(); i++) {
            stringReturn += this.getPosOnLine().get(i);
            if (i < this.getPosOnLine().size()-1) {
                stringReturn += ",";
            }
        }
        return stringReturn;
    }
    
    //non-default equals method
    public boolean equals(CTAStation ctaStation) {
        if (ctaStation == null)
            return false;
        if (this.getLatitude() != ctaStation.getLatitude())
            return false;
        if (this.getLongitude() != ctaStation.getLongitude())
            return false;
        for (int i=0; i<getPosOnLine().size(); i++) {
            if (this.getPosOnLine().get(i) != ctaStation.getPosOnLine().get(i))
                return false;
        }
        return true;
    }
    
//    //Copy method
//    public CTAStation deepCopy() {
//        CTAStation copiedCTA = new CTAStation();
//        copiedCTA.setLatitude(getLatitude());
//        copiedCTA.setLongitude(getLongitude());
//        copiedCTA.setWheelchair(wheelchair);
//        copiedCTA.setName(String.copyValueOf(name.toCharArray()));
//        copiedCTA.setName(String.copyValueOf(location.toCharArray()));
//        return copiedCTA;
//    }
    
}
