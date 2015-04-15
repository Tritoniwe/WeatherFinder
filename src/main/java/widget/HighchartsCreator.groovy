package widget

import exceptions.RunException

/**
 * Class that creates simple line chart. With use of Highcharts
 */
class HighchartsCreator {
    private def final COLORS = ["#108ec5", "#CC0033", "#000099", "#339999", "#003300", "#999900"];
    private def s = "\\";
    private String geckoKey;

    HighchartsCreator(String geckoKey) {
        if (geckoKey==null||geckoKey.length() == 0)
            throw new RunException("Error: Can't find GeckoboardAPI key, check Params.json");
        this.geckoKey = geckoKey;
    }

/**
 * Method that creates simple line chart with stated params
 * @param tableName - Name of the Table
 * @param unit - Table units
 * @param list - weather information
 * @param map - map with key parameters that will be used for series (Max 6 params)
 * @return Query String
 */
    String create(String tableName, String unit, def list, def map) {
        def xAxis = [];
        def series = '';
        def count = 0;
        String minMaxYaxis = "";

        if (unit == "Percentage") minMaxYaxis = "min: 0,max:100,";
        try {
            def dayList = list.data.weather;
            dayList.each {
                xAxis.add('\\"' + it.date + '\\"');
            }
            map.each {
                def data = [];
                def nameParam = it.key;
                def nameLong = it.value;
                dayList.each {
                    def requestedParam=it.hourly.(nameParam.toString())[0];
                    if (requestedParam==null) throw new RunException("Error Can't find in WWO data requested parameter");
                    data.add(requestedParam);
                }
                series += """,{color:${s}"${COLORS[count++]}${s}",name:${s}"${nameLong}${s}",data:${data}}""";
            }
        }
        catch (Exception e) {
            if (e.getMessage()=="Error Can't find in WWO data requested parameter") throw e;
            throw new RunException("Error while creating HighChart widgets: Wrong data from WWO");

        }
        def query = """ {"api_key":"${geckoKey}", "data":{"highchart": "{chart:{style: {color: ${s}"#b9bbbb${s}"}\
,renderTo:${s}"container${s}",backgroundColor:${s}"transparent${s}",lineColor:${s}"rgba(35,37,38,100)${s}",plotShadow\
:false,},credits:{enabled:false},title:{style: {color: ${s}"#b9bbbb${s}"},text:${s}"${tableName}${s}"},xAxis:{categories\
:${xAxis}},yAxis:{${minMaxYaxis}title:{style: {color: ${s}"#b9bbbb${s}"}, text:${s}"${unit}${s}"}},legend:{itemStyle: \
{color: ${s}"#b9bbbb${s}"},layout:${s}"vertical${s}",align:${s}"right${s}",verticalAlign:${s}"middle${s}",borderWidth:0},\
series:[${series.substring(1)}]}"}} """;



        return query;
    }

}
