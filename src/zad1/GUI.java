package zad1;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * GUI class provides a graphical user interface for fetching and displaying weather, currency exchange rates and country information.
 */
public class GUI extends JFrame {
    private Service service;
    private JTextField countryField;
    private JTextField cityField;
    private JTextField countryDestinationCountry;
    private JTextArea resultArea;
    private JButton weatherButton;
    private JButton rateButton;
    private JButton nbpRateButton;

    /**
     * Constructor for the GUI class.
     * Initializes the GUI components and sets up the action listeners for the buttons.
     */
    public GUI() {
        setTitle("Web Service Client");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        countryField = new JTextField(20);
        cityField = new JTextField(20);
        countryDestinationCountry = new JTextField(20);
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);

        weatherButton = new JButton("Get Weather");
        weatherButton.addActionListener(e -> getWeather());

        rateButton = new JButton("Get Rate");
        rateButton.addActionListener(e -> getRate());

        nbpRateButton = new JButton("Get NBP Rate");
        nbpRateButton.addActionListener(e -> getNBPRate());

        panel.add(new JLabel("Enter country name: "));
        panel.add(countryField);
        panel.add(new JLabel("Enter city name: "));
        panel.add(cityField);
        panel.add(new JLabel("Enter change destination country: "));
        panel.add(countryDestinationCountry);
        panel.add(weatherButton);
        panel.add(rateButton);
        panel.add(nbpRateButton);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        panel.add(scrollPane);

        add(panel);
        setVisible(true);
    }

    /**
     * Fetches and displays the weather for the country and city entered in the text fields.
     */
    private void getWeather() {
        if (countryField.getText().isEmpty() || cityField.getText().isEmpty()) {
            showError("Please enter a country name and city name");
            return;
        }

        service = new Service(countryField.getText());
        String weatherJson = service.getWeather(cityField.getText());
        String weatherDescription = extractFromJson(weatherJson, "\"description\":\"([^\"]*)\"");
        resultArea.setText("Weather in " + cityField.getText() + ": " + weatherDescription);
    }

    /**
     * Fetches and displays the exchange rate for the country entered in the text fields.
     */
    private void getRate() {
        if (countryField.getText().isEmpty() || countryDestinationCountry.getText().isEmpty()) {
            showError("Please enter a country name and country destination");
            return;
        }

        service = new Service(countryField.getText());
        String destinationCurrency = service.getCurrencyCodeByCountry(countryDestinationCountry.getText());
        String sourceCurrency = service.getCurrencyCodeByCountry(countryField.getText());
        Double rate = service.getRateFor(destinationCurrency);
        String result = getCountryAndCurrencyInfo(sourceCurrency, destinationCurrency, rate);

        resultArea.setText(result);
    }

    /**
     * Fetches and displays the NBP rate for the country entered in the text field.
     */
    private void getNBPRate() {
        if (countryField.getText().isEmpty()) {
            showError("Please enter a country name");
            return;
        }

        service = new Service(countryField.getText());
        Double rate = service.getNBPRate();
        String sourceCurrencyCode = service.getCurrencyCodeByCountry(countryField.getText());
        String result = getCountryAndCurrencyInfo(sourceCurrencyCode, "PLN", rate);

        resultArea.setText(result);
    }

    /**
     * Fetches and formats the country and currency information for display.
     *
     * @param sourceCurrency      The source currency code.
     * @param destinationCurrency The destination currency code.
     * @param rate                The exchange rate.
     * @return A formatted string containing the country and currency information.
     */
    private String getCountryAndCurrencyInfo(String sourceCurrency, String destinationCurrency, Double rate) {
        String countryInfoJson = service.getCountryInfo(countryField.getText());
        String currencyInfoJson = service.getCurrencyInfo(destinationCurrency);

        String countryName = extractFromJson(countryInfoJson, "\"name\":\"([^\"]*)\"");
        String countryRegion = extractFromJson(countryInfoJson, "\"region\":\"([^\"]*)\"");
        String countrySubregion = extractFromJson(countryInfoJson, "\"subregion\":\"([^\"]*)\"");
        String countryPopulation = extractFromJson(countryInfoJson, "\"population\":([\\d]*)");
        String countryArea = extractFromJson(countryInfoJson, "\"area\":([\\d.]*)");
        String currencyName = extractFromJson(currencyInfoJson, "\"name\":\"([^\"]*)\"");
        String currencySymbol = extractFromJson(currencyInfoJson, "\"symbol\":\"([^\"]*)\"");

        return String.format(
                "Rate from %s to %s: %.2f\n\n" +
                        "Country Info:\n" +
                        "\tName: %s\n" +
                        "\tRegion: %s\n" +
                        "\tSubregion: %s\n" +
                        "\tPopulation: %s\n" +
                        "\tArea: %s\n" +
                        "Currency Info:\n" +
                        "\tName: %s\n" +
                        "\tSymbol: %s\n",
                sourceCurrency, destinationCurrency, rate, countryName, countryRegion, countrySubregion, countryPopulation, countryArea, currencyName, currencySymbol
        );
    }

    /**
     * Displays an error message in a dialog box.
     *
     * @param message The error message to display.
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(GUI.this, message, "Error", JOptionPane.ERROR_MESSAGE);
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