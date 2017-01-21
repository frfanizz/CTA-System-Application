# CTA-System-Application

A course project for accelerated introduction to programming course at IIT. An application which imports CTA Station and CTA Line information (saved in CTAStops.csv) and displays information based on user prompts.

Included are source code files, as well as documentation files.

# Class descriptions:

CTAStopApp: CTAStopApp is the main application class which prompts the user for choices and based on the user input, uses functions to accomplish tasks to display data. This class uses all other classes to accomplish this, but most data manipulation is done through the CTASystem class methods.

CTASystem: CTASystem is an object class which stores all the CTA Station of the CTA system as an ArrayList, and all the CTA Lines as another ArrayList. Most of CTASystem's functionality comes from looping through the CTA Lines, and calling CTARoute methods to access or modify data. This avoids the need to do this in the Application class, and also combines the necessary ArrayLists of CTA Stations and CTA Lines into one easy-to-use object. For tasks that require manipulation of CTA Stations and CTA Lines separately, this class is able to accomplish those tasks in a cleaner way. For tasks that only require one or the other, there is almost no extra effort required to do them.

CTARoute: CTARoute is an object class which contains information relevant to a CTA Line. CTARoute contains an ArrayList variable of CTAStations, an integer representation of the line as part of the CTASystem, the name (color) of the line, and a CTAStation variable denoting the last two-way station of the line before it becomes one way at the loop. CTARoute contains many methods which iterate through all CTAStations stored in the ArrayList to access and manipulate the data in useful ways.

CTAStation: CTAStation is an object class that stores information about a CTA Station. It inherits from GeoLocation to store the Station's location, and also has variables for the station name, vertical location (elevated, ground, etc.), wheelchair accessibility, and it's position on each CTA Line. This Object class contains many methods that are used by CTARoute, CTASystem, or directly.

GeoLocation: GeoLocation is an object class that stores a location as a latitude and longitude. This object class's unique method is the calcDistanceMiles method, which calculates the distance to another geoLocation (or latitude and longitude pair) in miles.

Also included are several unit test case classes, which test methods of GeoLocation, CTAStation, and CTARoute.
