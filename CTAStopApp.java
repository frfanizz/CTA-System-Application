//Francesco Fanizza
//2016.12.02

/*                                                                              |
 * CTAStopApp is the main application class which prompts the user for choices
 * and based on the user input, uses functions to accomplish tasks to display
 * data. This class uses all other classes to accomplish this, but most data
 * manipulation is done through the CTASystem class methods.
 */

package finalProject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CTAStopApp {

    public static void main(String[] args) throws FileNotFoundException {
        
        Scanner input = new Scanner(System.in);
//        String inputFileName = getFileName(input); //TODO: uncomment this line
        String inputFileName = "CTAStops.csv"; //TODO: delete this line
        
        // retrieve information from csv, and store values in an array
        ArrayList<CTAStation> allStops = retrieveStations(inputFileName);
        ArrayList<CTARoute> allLines = retrieveRoutes(allStops, inputFileName);
        CTASystem ctaSystem = new CTASystem(allLines, allStops);
        
        // menu loop
        boolean isExit = false;
        
        do {
            System.out.println("\n"+"Please choose a menu option: ");
            System.out.println("(1) Display all station names");
            System.out.println("(2) Display all stations with or without wheelchair access");
            System.out.println("(3) Find the nearest station to a location");
            System.out.println("(4) Display station information (search)");
            System.out.println("(5) Generate directions between stations");
            System.out.println("(6) Add a new station");
            System.out.println("(7) Delete an existing station");
            
            System.out.println("(0) Exit");
            char userChoice;
            try {
                userChoice = input.nextLine().charAt(0);
            } catch (Exception e) {
                userChoice = ' ';
            }
            switch (userChoice) {
            case '1':
                displayAllNames(ctaSystem);
                break;
            case '2':
                displayWheelchair(ctaSystem, input);
                break;
            case '3':
                findNearest(ctaSystem, input);
                break;
            case '4':
                findInformation(ctaSystem, input);
                break;
            case '5':
                generateRoute(ctaSystem, input);
                break;    
            case '6':
                addNewStation(ctaSystem, input);
                break;
            case '7':
                findDeleteStation(ctaSystem, input);
                break;
                
            case '0':
                saveToFile(ctaSystem, input);
                System.out.println("Goodbye!");
                isExit = true;
                break;
            default:
                System.out.println("I'm sorry, that is not a valid input, please try again.");
            }
            
        } while (!isExit);
        input.close();
    }
    
    // ----------------------------------------------------------------------------
    
    //function to prompt user for a valid file name
    public static String getFileName(Scanner input) {
        boolean validFileName = false;
        String inputFileName;
        do {
            System.out.println("Enter the name of the file you would like to open (don't forget the file extension):");
            inputFileName = getUserString(input);
            //test inputFileName
            try {
                File inputFile = new File(inputFileName);
                Scanner fromFile = new Scanner(inputFile);
                if (fromFile.hasNextLine()) {
                    validFileName = true;
                }
                fromFile.close();
            } catch (Exception e) {
                System.out.println("Invalid file. Please try again.");
            }
        } while (!validFileName);
        return inputFileName;
    }
    
    // ----------------------------------------------------------------------------
    
    //Function to create routes from a list of stops
    public static ArrayList<CTARoute> retrieveRoutes(ArrayList<CTAStation> allStations, String inputFileName) {
        try {
            // Read from file
            File inputFile = new File(inputFileName);
            Scanner fromFile = new Scanner(inputFile);
            ArrayList<CTARoute> allRoutes = new ArrayList<CTARoute>();
            int lineCounter = 0;
            while (fromFile.hasNextLine()) {
                String[] currLineArray = fromFile.nextLine().split(","); //CSV -> split at commas
                //Create array of CTAStation
                if (lineCounter == 0) { 
                    //use the first row to collect lines
                    for (int i = 0; i < currLineArray.length - 5; i++) {
                        allRoutes.add(new CTARoute());
                        allRoutes.get(i).setLineColorIndex(i);
                        allRoutes.get(i).setLineColor(currLineArray[5+i].split(":")[0]);
                    }
                    //add stations to the routes
                    for (CTARoute currRoute : allRoutes) {
                        for (CTAStation currStation : allStations) {
                            if (currStation.getPosOnLine().get(currRoute.getLineColorIndex()) != -1) {
                                currRoute.addStation(currStation);
                            }
                        }
                        currRoute.sortStations();
                    }
                    
                }
                if (lineCounter == 1) {
                    //use the second row to collect stations before one way loop routes
                    for (int i = 0; i < currLineArray.length - 5; i++) {
                        if (!currLineArray[5+i].equals("Null")) {
                            allRoutes.get(i).setLastTwoWay(allRoutes.get(i).lookupStation(currLineArray[5+i]));
                        }
                    }
                }
                lineCounter++;
            }
            fromFile.close();
            return allRoutes;
        } catch (FileNotFoundException e) {
            System.out.println("The file was not found. Check to make sure the file name is correct.");
            return new ArrayList<CTARoute>();
        }
    }
    
    //Function to retrieve CTAStations from a file (saves them to an array of CTAStation and returns that array)
    public static ArrayList<CTAStation> retrieveStations(String inputFileName) {
        // Read from file
        boolean isFound = false;
        do {
//            System.out.println(arg0);
            try {
                File inputFile = new File(inputFileName);
                Scanner fromFile = new Scanner(inputFile);
                
                //Array variables
                ArrayList<CTAStation> ctaStations = new ArrayList<CTAStation>();
                
                //Loop over all lines in csv file
                while (fromFile.hasNextLine()) {
                    String currentLine = fromFile.nextLine();
                    String[] currentLineArray = currentLine.split(","); //CSV -> split at commas
                    //Create array of CTAStation
                    if (!(currentLineArray[0].equalsIgnoreCase("Name") ||
                            currentLineArray[0].equalsIgnoreCase("Null"))) { //ignore first two lines (assume no station's name is "name"
                        ArrayList<Integer> lines = new ArrayList<Integer>();
                        for (int i = 0; i < currentLineArray.length-5; i++) {
                            String lineValue = currentLineArray[5+i];
                            lines.add(Integer.parseInt(lineValue));
                        }
                        CTAStation currCTAStation = new CTAStation(
                                currentLineArray[0],
                                currentLineArray[3],
                                Boolean.parseBoolean(currentLineArray[4]),
                                Double.parseDouble(currentLineArray[1]),
                                Double.parseDouble(currentLineArray[2]),
                                lines
                                );
                        ctaStations.add(currCTAStation);
                    }
                }
                
                fromFile.close();
                return ctaStations;
            } catch (FileNotFoundException e) {
                System.out.println("The file was not found. Check to make sure the file name is correct.");
            }
        } while (!isFound);
        return new ArrayList<CTAStation>();
    }
    
    //saveToFile (prompts user for option to save, if yes, prompts for file name and saves to that file)
    public static void saveToFile(CTASystem ctaSystem, Scanner input) {
        System.out.println("Would you like to save any changes you've made to the CTA System to a file?");
        if (getUserBoolean(input)) {
            //save and return
            System.out.println("What file name would you like to save these changes to?");
            System.out.println("Remember to include a file extension!");
            String fileName = getUserString(input);
            //TODO write to file
            BufferedWriter outFile;
            try {
                outFile = new BufferedWriter(new FileWriter(fileName));
                outFile.write(ctaSystem.toCSVString());
                outFile.close();
            } catch (IOException e) {
                System.out.println("Something went wrong. Your data could not be saved.");
            }
            System.out.println("Changes have been saved as: "+fileName);
        } else {
            //just return
            System.out.println("Changes have been discarded.");
        }
    }
    
    // ----------------------------------------------------------------------------

    //get user string function
    public static String getUserString(Scanner input) {
        boolean isValid = false;
        do {
            try {
                String newString = input.nextLine();
                return newString;
            } catch (Exception e) {
                System.out.println("I'm sorry, that is not a valid input. Please try again.");
            }
        } while (!isValid);
        return null;
    }
    
    //get user boolean function
    public static boolean getUserBoolean(Scanner input) {
        boolean isValid = false;
        do {
            try {
                char newBoolean = input.nextLine().charAt(0);
                switch (newBoolean) {
                case 'Y':
                case 'y':
                    return true;
                case 'N':
                case 'n':
                    return false;
                default:
                    System.out.println("I'm sorry, that is not a valid input. Please try again.");
                    break;
                }
                
            } catch (Exception e) {
                System.out.println("I'm sorry, that is not a valid input. Please try again.");
            }
        } while (!isValid);
        return false;
    }
    
    //get user double function
    public static double getUserDouble(Scanner input) {
        boolean isDone = false;
        do {
            try {
                double newDouble = input.nextDouble();
                return newDouble;
            } catch (Exception e) {
                input.nextLine();
                System.out.println("I'm sorry, that is not a valid number. Please try again.");
            }
        } while (!isDone);
        return 0;
    }
    
    
    // ----------------------------------------------------------------------------
    
    //(1) Display all station names
    //Displays all stations in array (uses numbering if boolean is true)
    public static void displayAllNames(CTASystem ctaSystem) {
        System.out.println(ctaSystem.toString());
    }

    //(2) Display all station names with or w/o wheelchair accessibility (user prompted choice)
    public static void displayWheelchair(CTASystem ctaSystem, Scanner input) {
        System.out.println("Enter \"y\" to view wheelchair accessable stations, " +
                "and \"n\" to view stations without wheelchair accessability: ");
        boolean wantsWheelchair = getUserBoolean(input);
        System.out.println(ctaSystem.wheelchairToString(wantsWheelchair));
    }
    
    //(3) Find nearest station to location (latitude, longitude)
    public static void findNearest(CTASystem ctaSystem, Scanner input) {
        
        //Prompt user for a latitude and longitude
        System.out.println("Enter a latitude: ");
        double userLatitude = getUserDouble(input);
        System.out.println("Enter a longitude: ");
        double userLongitude = getUserDouble(input);
        
        //Store location as geoLocation:
        GeoLocation userGeoLocation = new GeoLocation(userLatitude, userLongitude);
        
        //Set initial closest station
        CTAStation nearestStation = ctaSystem.nearestStation(userGeoLocation);

        //Print results
        System.out.println("The nearest station to you is "+nearestStation.getName()+
                ". It is "+nearestStation.calcDistanceMiles(userGeoLocation)+" miles away.");
        input.nextLine();
    }
    
    //(4) Search for a station and Display station information
    public static void findInformation(CTASystem ctaSystem, Scanner input) {
        CTAStation stationMatch = findStation(ctaSystem, input);
        System.out.println(ctaSystem.stationInformationToString(stationMatch));
    }
    
    //Find station with user prompt
    public static CTAStation findStation(CTASystem ctaSystem, Scanner input) {
        boolean isFound = false;
        do {
            System.out.println("Enter the name of a station: ");
            String searchString = getUserString(input);
            ArrayList<CTAStation> stationMatch = ctaSystem.lookupStation(searchString);
            if (stationMatch == null) {
                System.out.println("I'm sorry. I could not find a station with that name. Please try again.");
            } else {
                if (stationMatch.size() < 2) {
                    return stationMatch.get(0);
                } else {
                    System.out.println("Multiple stations with the name "+searchString+" have been found.");
                    boolean hasPicked = false;
                    do {
                        for (int i = 0; i < stationMatch.size(); i++) {
                            System.out.println("("+i+") " + stationMatch.get(i).toStringWithCoordinates()
                            + " on the " + ctaSystem.whichLines(stationMatch.get(i)) + " line");
                        }
                        System.out.println("Which station (number) do you want?");
                        int userChoice = Integer.parseInt(getUserString(input));
                        try {
                            stationMatch.get(userChoice);
                            return stationMatch.get(userChoice);
                        } catch (Exception e) {
                            System.out.println("I'm sorry, that is not a valid choice. Please try again.");
                        }
                    } while (!hasPicked);
                }
            }
        } while (isFound != true);
        return null;
    }
    
    //(5) Generate directions between two stations
    public static void generateRoute(CTASystem ctaSystem, Scanner input) {
        System.out.println("What station are you starting from?");
        CTAStation start = findStation(ctaSystem, input);
        System.out.println("What station do you want to go to?");
        CTAStation end = findStation(ctaSystem, input);
        System.out.println(ctaSystem.generateRoute(start, end));
    }
    
    
    //(6) Add a new station
    public static void addNewStation(CTASystem ctaSystem, Scanner input) {
        System.out.println("Enter the name of a station to add:");
        String newStationName = getUserString(input);
        System.out.println("Enter "+newStationName+"'s latitude:");
        double  newLatitude = getUserDouble(input);
        System.out.println("Enter "+newStationName+"'s longitude:");
        double  newLongitude = getUserDouble(input);
        input.nextLine();
        System.out.println("What is "+newStationName+"'s location relative to the ground?");
        System.out.println("Examples: elevated, subway, surface, embankment, etc.");
        String  newLocation = getUserString(input);
        System.out.println("Does this station have wheelchair accessibility (Y/N)?");
        boolean newIsWheelchair = getUserBoolean(input);
        //array variable for route position of new station
        ArrayList<Integer> newPosOnLine = new ArrayList<Integer>();
        for (int i = 0; i < ctaSystem.getCtaLines().size(); i++) {
            newPosOnLine.add(-1);
        }
        
        CTAStation newCTAStation = new CTAStation(newStationName,
                newLocation,
                newIsWheelchair,
                newLatitude,
                newLongitude,
                newPosOnLine); //CTALines value will be updated with CTARoute.updateRouteNumbers();
        //add station to station list, then modify route position values
        ctaSystem.addStationToStations(newCTAStation);
        System.out.println(newCTAStation.getName()+" has been created.");
        for (int i = 0; i < ctaSystem.getCtaLines().size(); i++) {
            System.out.println("Is "+newCTAStation.getName()+" on the "+
                    ctaSystem.getCtaLines().get(i).getLineColor()+" line?");
            boolean isOnLine = getUserBoolean(input);
            if (isOnLine) {
                CTAStation stationBefore = new CTAStation();
                boolean onRightLine = false;
                do {
                    System.out.println("What station comes before "+newCTAStation.getName()+"?");
                    stationBefore = findStation(ctaSystem, input);
                    if (ctaSystem.getCtaLines().get(i).isOnRoute(stationBefore)) {
                        onRightLine = true;
                    } else {
                        System.out.println("I'm sorry, "+stationBefore.getName()+
                                " is not on the "+ctaSystem.getCtaLines().get(i).getLineColor()+" line. Try again.");
                    }
                } while (!onRightLine);
                
                if (ctaSystem.getCtaLines().get(i).isAtEnd(stationBefore)) {
                    CTAStation stationChoiceOne = null;
                    if (stationBefore.getPosOnLine().get(i) == 0) {
                        stationChoiceOne = ctaSystem.getCtaLines().get(i)
                            .findByNumber(stationBefore.getPosOnLine().get(i)+1);
                    } else {
                        stationChoiceOne = ctaSystem.getCtaLines().get(i)
                                .findByNumber(stationBefore.getPosOnLine().get(i)-1);
                    }
                    System.out.println("Does "+newCTAStation.getName()+
                            " come after \n(1) "+stationChoiceOne+" or: \n(2) is it at the end of the line?");
                    boolean oneOrTwo = false;
                    do {
                        String userChoice = getUserString(input);
                        if (userChoice.charAt(0) == '1') {
                            ctaSystem.getCtaLines().get(i).insertStation(newCTAStation, stationBefore, stationChoiceOne);
                            oneOrTwo = true;
                        } else if (userChoice.charAt(0) == '2') {
                            ctaSystem.getCtaLines().get(i).insertStation(newCTAStation, stationBefore);
                            oneOrTwo = true;
                        } else {
                            System.out.println("I'm sorry, that is not a valid choice. Try again.");
                        }
                    } while (!oneOrTwo);
                } else {
                    CTAStation stationChoiceOne = ctaSystem.getCtaLines().get(i)
                            .findByNumber(stationBefore.getPosOnLine().get(i)+1);
                    CTAStation stationChoiceTwo = ctaSystem.getCtaLines().get(i)
                            .findByNumber(stationBefore.getPosOnLine().get(i)-1);
                    System.out.println("Which station comes after "+newCTAStation.getName()+
                            ", "+stationChoiceOne+" or "+stationChoiceTwo+"?");
                    boolean isAdjacent = false;
                    do {
                        String userChoice = getUserString(input);
                        if (userChoice.equalsIgnoreCase(stationChoiceOne.getName())) {
                            ctaSystem.getCtaLines().get(i).insertStation(newCTAStation, stationBefore, stationChoiceOne);
                            isAdjacent = true;
                        } else if (userChoice.equalsIgnoreCase(stationChoiceTwo.getName())) {
                            ctaSystem.getCtaLines().get(i).insertStation(newCTAStation, stationBefore, stationChoiceTwo);
                            isAdjacent = true;
                        } else {
                            System.out.println("I'm sorry, that is not a valid choice, try again.");
                        }
                    } while (!isAdjacent);
                }
                
                System.out.println(newCTAStation.getName()+" has been added to the "
                        +ctaSystem.getCtaLines().get(i).getLineColor()+" line:");
                ctaSystem.updateRouteNumbers();
            }
            
        }
        
    }
    
    
    //(7) search and delete function
    public static void findDeleteStation(CTASystem ctaSystem, Scanner input) {
        //search for a station to delete
        CTAStation stationToDelete = findStation(ctaSystem, input);
        String tempName = stationToDelete.getName();
        System.out.println("The following station will be deleted: ");
        System.out.println(ctaSystem.stationInformationToString(stationToDelete));
        System.out.println("Are you sure you want to continue (Y/N)?");
        boolean userResponse = getUserBoolean(input);
        if (userResponse) {
            if (deleteStation(stationToDelete, ctaSystem)) {
                System.out.println(tempName+" station deleted");
            }
        } else {
            System.out.println(stationToDelete.getName()+" station has not been deleted.");
        }
    }
    
    //delete function from ctaSystem
    public static boolean deleteStation(CTAStation stationToDelete, CTASystem ctaSystem) {
        boolean somethingDeleted = ctaSystem.removeStation(stationToDelete);
        if (!somethingDeleted) {
            System.out.println("That station was not found in the CTA system.");
        }
        return somethingDeleted;
    }

}
