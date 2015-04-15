package services.api

import exceptions.RunException
import groovyx.net.http.*

import javax.net.ssl.SSLPeerUnverifiedException

/**
 * Class for pushing data to Geckoboard
 */
class GeckoAPI {
    private final def httpBuilder = new HTTPBuilder("https://push.geckoboard.com/v1/send/");

/**
 * Method for publising data to Geckovoard
 * @param query - String that contains JSON data for Geckoboard widget
 * @param url - widget url for push
 *
 * */
    void publish(def query, String widget) {
        httpBuilder.handler.failure = { resp ->
            throw new RunException("Problem with Geckoboard\n Unexpected failure while pushing to Geckoboard: ${resp.statusLine}");
        };
        try {
        httpBuilder.post(path: widget, body: query) { resp -> };}
        catch (SSLPeerUnverifiedException e) {
            e.printStackTrace()
        }
    }


}
