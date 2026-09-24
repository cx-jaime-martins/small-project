package com.example.mcp.server;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

/**
 * Weather tools exposed over MCP, backed by an in-memory mock dataset so the sample
 * has no external network dependency.
 */
@Component
public class WeatherTools {

	private static final Map<String, WeatherInfo> FORECASTS = buildForecasts();

	@McpTool(name = "list_cities", description = "Lists the cities with mock forecast data.")
	public String listCities() {
		return String.join(", ", FORECASTS.keySet());
	}

	@McpTool(name = "get_forecast", description = "Gets the mock weather forecast for a city.")
	public String getForecast(@McpToolParam(
			description = "City name, e.g. 'Seattle'. See list_cities for supported values.",
			required = true) String city) {
		WeatherInfo forecast = FORECASTS.get(normalize(city));
		if (forecast == null) {
			throw new IllegalArgumentException(
					"Unknown city '" + city + "'. Supported cities: " + String.join(", ", FORECASTS.keySet()));
		}
		return String.format(Locale.US, "%s: %s, %d°F, wind %d mph", city, forecast.conditions(),
				forecast.temperatureF(), forecast.windMph());
	}

	private static Map<String, WeatherInfo> buildForecasts() {
		Map<String, WeatherInfo> forecasts = new LinkedHashMap<>();
		forecasts.put("seattle", new WeatherInfo("Rainy", 54, 12));
		forecasts.put("austin", new WeatherInfo("Sunny", 89, 6));
		forecasts.put("denver", new WeatherInfo("Windy", 61, 22));
		forecasts.put("miami", new WeatherInfo("Humid", 85, 9));
		return forecasts;
	}

	private static String normalize(String city) {
		return city == null ? "" : city.trim().toLowerCase(Locale.US);
	}

	private record WeatherInfo(String conditions, int temperatureF, int windMph) {
	}

}
