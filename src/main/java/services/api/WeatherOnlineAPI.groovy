package services.api

import exceptions.RunException

/**
 * Class for interaction with WorldWeatherOnline API
 */
class WeatherOnlineAPI {

/**
 * Method that connects to WWO, checking response code and giving back InputStreamReader
 * @param url - query to WWO
 * @return InputStreamReader for JSON parsers
 */
    def getInputStream(URL url) {

        try {
            def urlConnect = url.openConnection();
            def response = urlConnect.getResponseCode();
            if (response != 200) {
                throw new Exception("Wrong responce code:  ${response}");
            }

            return new InputStreamReader(urlConnect.getInputStream()); ;
        }
        catch (Exception e) {
            throw new RunException("Error while connecting to WWO:\n ${e.getMessage()}")
        }

    }
}
