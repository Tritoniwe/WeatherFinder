package services.api

import exceptions.RunException
import groovy.json.JsonSlurper
import groovyx.net.http.URIBuilder

/**
 * Class that finding city on WWO
 */
class SearcherAPI {
    private URIBuilder uriBuilder = new URIBuilder("http://api.worldweatheronline.com/free/v2/search.ashx");
    private def slurper = new JsonSlurper();
    private String wwoKey;
    private WeatherOnlineAPI weatherAPI = new WeatherOnlineAPI();

    SearcherAPI(String wwoKey) {
        if (wwoKey==null||wwoKey.length() == 0) throw new RunException("Error: Can't find WorldWeatherOnlineAPI key. Check Params.json");
        this.wwoKey = wwoKey;
    }

    /**
     * Method that request information from WWO about locations with same name or coordinates
     * @param name - City name or coordinates
     * @return - List of cities in next format ["full name","latitude,longitude"]
     * (ex: ["London  City of London, Greater London  United Kingdom","42.983,-81.250"])
     */
    def search(String name) {
        def resultMap = [:];
        def doc;
        //Adding params q - location, fromat - format of return data, key - API key to WWO
        uriBuilder.addQueryParams([q: name, format: "json", key: wwoKey]);
        try {
            doc = slurper.parse(weatherAPI.getInputStream(uriBuilder.toURL()));
        }
        catch (Exception e) {
            throw new RunException("Error while reading data from WWO: ${e.getMessage()}}")
        }

        // if WWO can't find location it return Error
        if (doc.data != null) throw new RunException("Error while searching City: ${doc.data.error.msg[0]}");
        try {
            doc.search_api.result.each {
                // adding to map Full name of city with Region and Country and coordinates
                resultMap.put("${it.areaName[0].value}  Country: ${it.country[0].value} Region: ${it.region[0].value}"
                        , it.weatherUrl[0].value.substring(it.weatherUrl[0].value.lastIndexOf('=') + 1));
            }
        }
        catch (Exception e) {
            throw new RunException("Error while reading data from WWO: Wrong data format}")
        }

        return resultMap;

    }
}
