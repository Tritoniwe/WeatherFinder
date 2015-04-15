package widget

import exceptions.RunException
import groovy.json.JsonSlurper
import services.api.GeckoAPI
import utils.TwoTuple

/**
 * Class for creating and publishing widgets for Geckoboard
 */
class WidgetHandler {
    private def slurper = new JsonSlurper();
    private def geckoAPI = new GeckoAPI();
    private def hichartCreator;
    private def textWidgetCreator;
    private def geckoKey;
    private def widgetMap;

    /**
     *
     * @param map - map of widgets parameters
     * @param geckoKey - key for services.api.GeckoAPI
     */
    WidgetHandler(def map, def geckoKey) {
        widgetMap = map;
        if (map==null||map.size() == 0) throw new RunException("Error: can't find any widget in Params.json");
        this.geckoKey = geckoKey;
        hichartCreator = new HighchartsCreator(geckoKey);
        textWidgetCreator = new TextWidgetCreator(geckoKey);
    }

/**
 * Method which prepare widgets and save it to queries
 * @param is - InputStream from WWO
 * @param cityName
 */
    void publish(def is, String cityName) {
        println("Getting weather forecast");
        def data
        try {
            data = slurper.parse(is);
        }
        catch (Exception) {
            throw new RunException("Error: Wrong weather data from WWO");
        }
        println("Creating and publishing widgets");
        widgetMap.each {
            TwoTuple widget = (prepeareWidget(it.value, data, cityName));
            geckoAPI.publish(widget.first, widget.second);
        }

    }

/**
 * Method which creates widgets from
 * @param widget - map of widget parameters
 * @param data - weather data from WWO in JSON format
 * @param cityName
 * @return Tuple ( first - Query string with widget , second - windget number in Gecko )
 */
    def prepeareWidget(def widget, def data, String cityName) {
        if (widget.number == null || widget.number.length() == 0)
            throw new RunException("Error:Wrong widget properties, there are no geckoboard widgetnumber. Check Params.json")
        if (widget.type == "CurrentCondition")
            return new TwoTuple(textWidgetCreator.createCurrentCondition(data, cityName), widget.number);
        else if (widget.type == "Highcharts")
            return new TwoTuple(hichartCreator.create(widget.tablename, widget.unit, data, widget.map), widget.number);
        else if (widget.type == "DailyActivities")
            return new TwoTuple(textWidgetCreator.createDaymaxmin(data, cityName), widget.number);
        else throw new RunException("Error:Wrong widget properties. Check Params.json");
    }
}
