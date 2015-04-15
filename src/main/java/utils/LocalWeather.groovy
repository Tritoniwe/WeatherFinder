package utils

import exceptions.RunException
import groovyx.net.http.URIBuilder
import services.api.WeatherOnlineAPI

/**
 * Class for creating weather query to WWO
 */
class LocalWeather {
    private final URIBuilder uRIBuilder = new URIBuilder("http://api.worldweatheronline.com/free/v2/weather.ashx");
    private final WeatherOnlineAPI weatherOnlineAPI = new WeatherOnlineAPI();
    private String wwoKey;

    LocalWeather(String wwoKey) {
        if (wwoKey==null||wwoKey.length() == 0) throw new RunException("Error: Can't find WorldWeatherOnlineAPI key. Check Params.json");
        this.wwoKey = wwoKey;
    }

/**
 * Method which gets InputStream for requested query
 * @param cityName
 * @return Inputstream from WWO
 */
    def getInputStr(String cityName) {
        def params = [format: 'json', num_of_days: '24', q: (String) cityName, showlocaltime: 'yes', key: wwoKey];
        uRIBuilder.addQueryParams(params);
        return weatherOnlineAPI.getInputStream(uRIBuilder.toURL());
    }
}
