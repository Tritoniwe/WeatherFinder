package utils

import exceptions.RunException
import groovy.json.JsonSlurper
import sun.util.logging.PlatformLogger

import java.util.logging.Level
import java.util.logging.Logger

/**
 * Class that loading main parameters such as:WWOAPI key,GeckoboardAPI key and list of url for pushing on Geckoboard
 */
class ParameterLoader {

    private def slurper = new JsonSlurper();
    private def doc;

    ParameterLoader(String fileName) {
        try {
            doc = slurper.parse(new File(fileName));
        }
        catch (Exception e) {
            println(e.getMessage());
            throw new RunException("Error while loading param file: \n${e.message}");

        }
    }

/**
 * Getting necessary parameter
 * @param paramName - name of parameter
 * @return parameter
 */
    def loadParams(String paramName) {
        try {
            return doc."${paramName}";
        }
        catch (Exception e) {

            throw new RunException("Param file damaged or have wrong data");

        }


    }

}
