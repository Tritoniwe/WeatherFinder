import exceptions.RunException
import services.api.SearcherAPI
import utils.CoordinateChecker
import utils.LocalWeather
import utils.ParameterLoader
import utils.TwoTuple
import widget.WidgetHandler

/**
 * Main Class which work with user requests and passing tasks to another classes and holds main parameters of request
 */
class MainWorker {
    private final String FILENAME = "Params.json";
    private def coordinatChecker = new CoordinateChecker();
    private def widgetHandler;
    private def citySearcher;
    private def localWeather;



    MainWorker() {

        ParameterLoader pLoader = new ParameterLoader(FILENAME);

        localWeather = new LocalWeather(pLoader.loadParams("wwokey"));
        citySearcher = new SearcherAPI(pLoader.loadParams("wwokey"));
        widgetHandler = new WidgetHandler(pLoader.loadParams("widgetmap"), pLoader.loadParams("geckoboardkey"));
    }
/**
 * Method which verifying provided arguments
 * @param params - user request
 * @return name or coordinates
 */
    String checkInput(def params) {
        def options
        def cli = new CliBuilder(usage: 'main.groovy -name London',
                header: 'Options:');
        cli.name(args: 1, argName: 'City name', 'Provide city name for searching');
        cli.c(args: 2, valueSeparator: ',', argName: 'Latitude,Longitude',
                'Searching location with given coordinates. Negative numbers looks like --34.586 ');
        cli.help('Show this message');
        options = cli.parse(params);
        if (options == null) {
            throw new RunException("");
        };
        if (options.help) {
            cli.usage();
            throw new RunException("");
        }
        if (!options.name && !options.cs) {
            cli.usage();
            throw new RunException("Wrong arguments! ${params}");
        }
        if (options.name != false) {
            if (!(options.name.length() > 0)) {
                throw new RunException("Wrong city name. Please check your input. Example -London")
            };
            return options.name;
        };
        else if (coordinatChecker.check(options.cs)) return "${options.cs[0].trim()},${options.cs[1].trim()}";
    }
/**
 * Method which gets list of Towns and giving user possibility to choose one from them.
 * If there are only one city in list it will take it by default.
 * @param params - name or coordinates of city
 * @return coordinates of choosen city and name of city
 */
    def defineCity(String params) {
        int b;
        def coordinates = '';
        def cityname = '';

        println("Searching city by your reqest: ${params}")
        def list = getCityList(params);
        if (list.size() == 0) throw new RunException("Error: Can't find any city. Please check your request")
        if (list.size() == 1) {
            coordinates = list.get(list.keySet()[0]);
            cityname = list.keySet()[0].split(" ")[0];
        } else {
            System.out.println("Choose your city:");
            int counter = 1;
            def listtmp = [];
            list.each {
                System.out.println("${counter}) ${it.key}");
                counter++;
                listtmp.add(it.key);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            for (;;) {
                try {
                    b = Integer.parseInt(br.readLine());
                    if (b>1||b<list.size()) break;
                    else throw new NumberFormatException();
                }
                catch (NumberFormatException nfe) {
                    b = -1;
                    System.out.println("Your choose wrong. Please choose from 1 to ${list.size()}");
                }
            }
            br.close();
            coordinates = list.get(listtmp[b - 1]);
            cityname = listtmp[b - 1].split(" ")[0];

        }
        return new TwoTuple(coordinates, cityname);
    }
/**
 * Searching towns
 * @param cityName - coordinates or cityname
 * @return list of cities
 */
    def getCityList(String cityName) {
        return citySearcher.search(cityName);
    }
/**
 * Creates and publish widgets
 * @param coordinates - coordinates of city
 * @param cityName - name of city
 */
    void publishWidgets(String coordinates, String cityName) {
        widgetHandler.publish(localWeather.getInputStr(coordinates), cityName);
    }

}
