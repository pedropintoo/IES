package ies.lab01.weatherradar;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ies.lab01.weatherradar.ipma_client.IpmaCityForecast; //may need to adapt package name
import ies.lab01.weatherradar.ipma_client.IpmaService;

/**
 * demonstrates the use of the IPMA API for weather forecast
 */
public class WeatherStarter {
    private static int CITY_ID_AVEIRO;

    public static void  main(String[] args) {

        try {
            CITY_ID_AVEIRO = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.out.println("The city ID must be an integer");
            System.exit(1);
        }


        // get a retrofit instance, loaded with the GSon lib to convert JSON into objects
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.ipma.pt/open-data/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // create a typed interface to use the remote API (a client)
        IpmaService service = retrofit.create(IpmaService.class);

        // prepare the call to remote endpoint
        Call<IpmaCityForecast> callSync = service.getForecastForACity(CITY_ID_AVEIRO);

        try {
            Response<IpmaCityForecast> apiResponse = callSync.execute();
            IpmaCityForecast forecast = apiResponse.body();

            if (forecast != null) {
                var firstDay = forecast.getData().listIterator().next();

                System.out.printf(
                        "Weather forecast for %s:%n" +
                        "Date: %s%n" +
                        "Max Temperature: %4.1f °C%n" +
                        "Min Temperature: %4.1f °C%n" +
                        "Precipitation Probability:  %4.1f °C%n" +
                        "Wind Speed: %s km/h%n" +
                        "Weather Description: %s%n",
                        forecast.getGlobalIdLocal(),
                        firstDay.getForecastDate(),
                        Double.parseDouble(firstDay.getTMax()),
                        Double.parseDouble(firstDay.getTMin()),
                        Double.parseDouble(firstDay.getPrecipitaProb()),
                        firstDay.getClassWindSpeed(),
                        firstDay.getIdWeatherType()
                );
            } else {
                System.out.println("No results for this request!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.exit(0); // close all threads

    }
}