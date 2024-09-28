package ies.lab01.ipma.api.client;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;

public class IpmaApiClient {
    private final Retrofit retrofit;
    private final IpmaService service;
    private static final Random RANDOM = new Random();
    private final Map<String, Integer> CITIES_CODE = new HashMap<>(Map.ofEntries(
        Map.entry("Aveiro", 1010500),
        Map.entry("Beja", 1020500),
        Map.entry("Braga", 1030300),
        Map.entry("Bragança", 1030800),
        Map.entry("Castelo Branco", 1040200),
        Map.entry("Coimbra", 1050200),
        Map.entry("Évora", 1060300),
        Map.entry("Faro", 1070500),
        Map.entry("Guarda", 1080500),
        Map.entry("Leiria", 1090700),
        Map.entry("Lisboa", 1100900),
        Map.entry("Portalegre", 1110600),
        Map.entry("Porto", 1131200),
        Map.entry("Santarém", 1141600),
        Map.entry("Setúbal", 1151200),
        Map.entry("Viana do Castelo", 1160900),
        Map.entry("Vila Real", 1171400),
        Map.entry("Viseu", 1182300)
    ));

    

    public IpmaApiClient() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("http://api.ipma.pt/open-data/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(IpmaService.class);
    }

    public Map.Entry<String, Integer> getRandomCity() {
        int randIdx = RANDOM.nextInt(CITIES_CODE.size());
        String cityName = CITIES_CODE.keySet().toArray(new String[0])[randIdx];
        Integer cityCode = CITIES_CODE.get(cityName);
        return Map.entry(cityName, cityCode);
    }

    public String getCityInfo(int CITY_ID) {
        Call<IpmaCityForecast> callSync = service.getForecastForACity(CITY_ID);

        try {
            Response<IpmaCityForecast> apiResponse = callSync.execute();
            IpmaCityForecast forecast = apiResponse.body();

            if (forecast != null) {
                var firstDay = forecast.getData().listIterator().next();

                Map<String, Object> forecastData = new HashMap<>();
                forecastData.put("city", forecast.getGlobalIdLocal());
                forecastData.put("date", firstDay.getForecastDate());
                forecastData.put("maxTemperature", Double.parseDouble(firstDay.getTMax()));
                forecastData.put("minTemperature", Double.parseDouble(firstDay.getTMin()));
                forecastData.put("precipitationProbability", Double.parseDouble(firstDay.getPrecipitaProb()));
                forecastData.put("windSpeed", firstDay.getClassWindSpeed());
                forecastData.put("weatherDescription", firstDay.getIdWeatherType());

                Gson gson = new Gson();
                return gson.toJson(forecastData);

            } else {
                System.err.println("No results for this request!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Error calling IPMA API");
        }

        return null;
    }
    
}
