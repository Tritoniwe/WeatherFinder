package utils

import exceptions.RunException

/**
 * Helper class for verifying coordinates
 */
class CoordinateChecker {
/**
 * Checking coordinates.
 * @param a - coordinates
 * @return true if coordinates right ( -90<Lattitude<90, -180<Longitude<180 )
 */
    boolean check(def a) {
        try {
            if (a[0] == null || a[1] == null) throw new Exception();
            def latitude = a[0].trim().split('\\.');
            def longitude = a[1].trim().split('\\.');

            def numberFirst = latitude[0].replace("--", "-");
            def latitudeNum = (Integer.parseInt(numberFirst));
            if (latitudeNum == 90 || latitudeNum == -90) {
                if (latitude.size() > 1 && latitude[1].length() != 0 & Integer.parseInt(latitude[1]) != 0) throw new Exception();
            } else {
                if (latitude.size() > 1) Integer.parseInt(latitude[1].length() < 7 ? latitude[1] : null);
                if (latitudeNum > 89 || latitudeNum < -89) {
                    throw new Exception();
                }
            }



            numberFirst = longitude[0].replace("--", "-");
            def longitudeNum = Integer.parseInt(numberFirst);
            if (longitudeNum == 180 || longitudeNum == -180) {
                if (longitude.size() > 1 && longitude[1].length() != 0 & Integer.parseInt(longitude[1]) != 0) throw new Exception();
            } else {
                if (longitude.size() > 1) Integer.parseInt(longitude[1].length() < 7 ? longitude[1] : null);
                if (longitudeNum > 179 || longitudeNum < -179)
                    throw new Exception()
            };
            return true;
        }
        catch (Exception e) {
            throw new RunException("Wrong format of coordinates. Please check your input. Example -c --38.453,46.455");
        }

    }
}
