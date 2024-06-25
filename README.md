![Logo](https://i.pinimg.com/originals/5a/a8/69/5aa869da340cbf31e3570f19ea3452a6.gif)

# Weather, Currency Exchange, and Country Information Project

This Java application provides a graphical user interface (GUI) for fetching and displaying weather forecasts, currency exchange rates, and country information using web services.

## Features
- **Weather Information**: Fetches current weather conditions for a specified city.
- **Currency Exchange Rates**: Retrieves exchange rates between different currencies.
- **Country Information**: Displays detailed information about a specified country, including its population, area, and currency details.

## Technologies Used
- **Java**: Programming language used for the application.
- **Swing**: GUI toolkit for building the graphical interface.
- **RESTful Web Services**: Utilized to fetch weather, currency, and country information.
- **Regular Expressions**: Used for parsing JSON responses from web services.

## How to Run the Application

### Prerequisites
- Java Development Kit (JDK) installed on your system.
- API keys for OpenWeatherMap and ExchangeRate-API.

### Steps to Run

1. **Clone the Repository**:

   ```bash
   git clone <repository_url>
   cd zad1


2. **Set API Keys**:

- Ensure environment variables API_KEY_WEATHER and API_KEY_EXCHANGE are set with your API keys for OpenWeatherMap and ExchangeRate-API respectively.


3. **Compile the Java Code**:

   ```bash
   javac GUI.java
   ```

4. **Using the Application**

- Enter the country name and city name to fetch weather information for that city.
- Enter the country name and the destination country to get the exchange rate.
- Enter the country name to retrieve the exchange rate against the Polish Zloty (PLN).
- Click on the respective buttons ("Get Weather", "Get Rate", "Get NBP Rate") to fetch and display the information.

