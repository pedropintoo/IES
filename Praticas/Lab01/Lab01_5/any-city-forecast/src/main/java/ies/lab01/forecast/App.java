package ies.lab01.forecast;

import ies.lab01.forecast.App.RemindTask;
import ies.lab01.ipma.api.client.IpmaApiClient;

import java.util.TimerTask;
import java.util.Timer;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * demonstrates the use of the IPMA API Client
 */
public class App {
    private static Logger logger = LogManager.getLogger(App.class);
    private static IpmaApiClient ipmaApiClient = new IpmaApiClient();
    private Timer timer;

    public App() {
        this.timer = new Timer();
        timer.schedule(new RemindTask(), 0, 1000);
    }

    class RemindTask extends TimerTask {
        public void run() {
            Map.Entry<String, Integer> city = ipmaApiClient.getRandomCity();
            logger.info("City: " + city.getKey() + " ID: " + city.getValue());
            String info = ipmaApiClient.getCityInfo(city.getValue());

            if (info != null) {
                logger.info(info);
            } else {
                logger.error("No results for this request!");
            }
        }
    }

    public static void  main(String[] args) {
        new App();
    }
}