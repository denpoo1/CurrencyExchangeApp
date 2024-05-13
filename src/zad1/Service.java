package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service class provides methods to fetch weather, currency exchange rates and country information.
 */
public class Service {
    private static final String API_KEY_WEATHER = System.getenv("API_KEY_WEATHER");
    private static final String API_KEY_EXCHANGE = System.getenv("API_KEY_EXCHANGE");
    private String country;

    /**
     * Constructor for the Service class.
     *
     * @param country The country for which the service will fetch information.
     */
    public Service(String country) {
        this.country = country;
    }

    /**
     * Fetches weather information for a given city in the country.
     *
     * @param city The city for which to fetch the weather.
     * @return A string containing the weather information in JSON format.
     */
    public String getWeather(String city) {
        return performRequest("http://api.openweathermap.org/data/2.5/weather?q=" + city + "," + country + "&appid=" + API_KEY_WEATHER);
    }

    /**
     * Fetches the currency code for a given country.
     *
     * @param country The country for which to fetch the currency code.
     * @return The currency code for the country.
     */
    public String getCurrencyCodeByCountry(String country) {
        return extractFromJson(performRequest("https://restcountries.com/v3.1/name/" + country), "\"currencies\":\\s*\\{\\s*\"(\\w+)\"");
    }

    /**
     * Fetches the exchange rate for a given currency code.
     *
     * @param currencyCode The currency code for which to fetch the exchange rate.
     * @return The exchange rate for the currency code.
     */
    public Double getRateFor(String currencyCode) {
        return Double.parseDouble(extractFromJson(performRequest("https://v6.exchangerate-api.com/v6/" + API_KEY_EXCHANGE + "/pair/" + currencyCode + "/" + getCurrencyCodeByCountry(this.country)), "\"" + "conversion_rate" + "\":(\\d+\\.?\\d*)"));
    }

    /**
     * Fetches the NBP rate for the country. If the country is Poland, returns 1.0.
     *
     * @return The NBP rate for the country.
     */
    public Double getNBPRate() {
        if (!this.country.equals("Poland")) {
            return Double.parseDouble(extractFromJson(performRequest("http://api.nbp.pl/api/exchangerates/tables/A/"), "\\{\"currency\":\\s*\"[^\"]*\",\\s*\"code\":\\s*\"" + getCurrencyCodeByCountry(this.country) + "\",\\s*\"mid\":\\s*(\\d+\\.?\\d*)\\}"));
        } else {
            return 1.0;
        }
    }

    /**
     * Fetches country information for a given country.
     *
     * @param country The country for which to fetch the information.
     * @return A string containing the country information in JSON format.
     */
    public String getCountryInfo(String country) {
        return performRequest("https://restcountries.com/v3.1/name/" + country);
    }

    /**
     * Fetches currency information for a given currency code.
     *
     * @param currencyCode The currency code for which to fetch the information.
     * @return A string containing the currency information in JSON format.
     */
    public String getCurrencyInfo(String currencyCode) {
        return performRequest("https://restcountries.com/v3.1/currency/" + currencyCode);
    }

    /**
     * Performs a GET request to a given URL and returns the response.
     *
     * @param urlString The URL to which to send the GET request.
     * @return A string containing the response from the server.
     */
    private String performRequest(String urlString) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = input.readLine()) != null) {
                response.append(line);
            }
            input.close();

        } catch (IOException e) {
            throw new IllegalStateException("Connection error while connecting to: " + urlString);
        }
        return response.toString();
    }

    /**
     * Extracts a value from a JSON string using a regular expression.
     *
     * @param json  The JSON string from which to extract the value.
     * @param regex The regular expression to use for extraction.
     * @return The extracted value.
     */
    private String extractFromJson(String json, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}