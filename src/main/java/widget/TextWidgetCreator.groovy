package widget

import exceptions.RunException
import groovy.json.JsonBuilder

/**
 * Class for creating simple Text widgets
 */
class TextWidgetCreator {
    private String geckoKey;

    TextWidgetCreator(String geckoKey) {
        if (geckoKey==null||geckoKey.length() == 0)
            throw new RunException("Error: Can't find GeckoboardAPI key, check Params.json");
        this.geckoKey = geckoKey;
    }

    /**
     * Method for creating simple text widget with current weather conditions
     * @param doc - weather data
     * @param cityName
     * @return query for pushing to Geckoboard
     *
     */
    String createCurrentCondition(def doc, String cityName) {
        String text1;
        String text2;
        String text3;

        def updateTime = java.util.Calendar.instance.format("MM/dd/yyyy hh:mm:ss");
        try {
            def list = doc.data.current_condition;
            //Creating first frame
            text1 = """
            |<html><body><p> <font size=4><h3> In ${cityName} now ${list.weatherDesc.value[0].get(0).toLowerCase()} </h3><br>
            |Temperature:&emsp;&ensp; ${list.temp_C[0]} &deg;C<br>
            |Temperature:&emsp;&ensp; ${list.temp_F[0]} &deg;F<br>
            |Feels like:&emsp; &emsp;&emsp;${list.FeelsLikeC[0]} &deg;C<br>
            |Feels like:&emsp; &emsp;&emsp;${list.FeelsLikeF[0]} &deg;F<br>
            |Humidity: &emsp; &emsp;&emsp;${list.humidity[0]} %<br>
            |<img  src= ${list.weatherIconUrl.value[0].get(0)}> <br>
            |Update time<br> ${updateTime}<br>
            |</font></body></html>""".stripMargin();

            //Creating second frame
            text2 = """
            |<html><body><p> <font size=4><h3> ${cityName}</h3><br>
            |Wind direction:&emsp; ${list.winddir16Point[0]}<br>
            |Wind speed:&emsp;${list.windspeedKmph[0]} Kmph<br>
            |Wind speed:&emsp;${list.windspeedMiles[0]} miles<br>
            |Pressure:&emsp;${list.pressure[0]} mmHG<br><br><br><br><br>
            |Update time<br> ${updateTime}<br>
            |</font></body></html>""".stripMargin();

            list = doc.data.weather[0];
            //Creating third frame
            text3 = """
            |<html><body><p> <font size=4><h3>${cityName}</h3><br>Astronomy:<br>
            |Moonrise:&emsp;&ensp; ${list.astronomy.moonrise[0]}<br>
            |Moonset: &emsp;&ensp; ${list.astronomy.moonset[0]}<br>
            |Sunrise: &emsp;&emsp; ${list.astronomy.sunrise[0]}<br>
            |Sunset: &emsp; &emsp; ${list.astronomy.sunset[0]} <br><br><br><br>
            |Update time<br> ${updateTime}<br>
            |</font></body></html>""".stripMargin();

        }
        catch (Exception e) {
            throw new RunException("Error while creating text widget:\n Wrong data from WWO");
        }
        def json = new JsonBuilder();
        //Compiling data in JSON format
        json {
            api_key geckoKey;
            data {
                item([text: text1, type: '0'], [text: text2, type: '0'], [text: text3, type: '0']);
            }
        }
        return json.toString();
    }

/**
 * Method for creating textwidget with weather daily activities
 * @param doc - weather data
 * @param cityName
 * @return Query that contains widget in JSON format
 */
    String createDaymaxmin(def doc, String cityName) {
        String text1;

        def updateTime = java.util.Calendar.instance.format("MM/dd/yyyy hh:mm:ss");
        try {
            def list = doc.data.weather[0];
            text1 = """
            |<html><body><p> <font size=3><h3> ${cityName}</h3><br>
            |Daily acivities:<br>
            |Max temperaure:&ensp; ${list.maxtempC}&deg;C<br>
            |Min temperaure:&emsp;${list.mintempC} &deg;C<br>
            |Max temperaure:&ensp; ${list.maxtempF} &deg;F<br>
            |Min temperaure:&emsp;${list.mintempF} &deg;F<br><br><br>
            |Update time<br> ${updateTime}<br>
            |</font><font size=2>
            |Weather current condition and forecast powered by <a href="httpBuilder://www.worldweatheronline.com/"
            |title="Free Weather API" target="_blank">World Weather Online</a>
            |</font></body></html>""".stripMargin();
        }
        catch (Exception e) {
            throw new RunException("Error while creating text widget:\n Wrong data from WWO");
        }

        def json = new JsonBuilder();
        //Compiling data in JSON format
        json {
            api_key geckoKey;
            data {
                item([text: text1, type: '0']);
            }
        }
        return json.toString();
    }
}