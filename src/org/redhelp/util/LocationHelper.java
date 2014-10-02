package org.redhelp.util;

import org.redhelp.common.types.Location;

public class LocationHelper {
    //DistanceFactor:
    //0.22 for 25 km
    //0.44 for 50 km
    public static Location addDistanceToLocation(Location currentLocation, Double distanceFactor) {
	Location location = new Location();
	location.latitude = currentLocation.latitude + 0.22;
	location.longitude = currentLocation.longitude + 
		    (0.22 / Math.cos(Math.toRadians(location.latitude)));
	return location;
    }

    public static Location subtractDistanceToLocation(Location currentLocation, Double distanceFactor) {
   	Location location = new Location();
   	location.latitude = currentLocation.latitude - 0.22;
   	location.longitude = currentLocation.longitude -
   		    (0.22 / Math.cos(Math.toRadians(location.latitude)));
   	return location;
       }
}
