//private void fetchWeatherData(double latitude, double longitude, String city) {
//    Log.d("WeatherApi", "Fetching weather data for: " + city + " (" + latitude + ", " + longitude + ")");
//    String units = getTemperatureUnit().equals("F") ? "imperial" : "metric";
//    OpenWeatherMapService apiService = ApiClient.getClient().create(OpenWeatherMapService.class);
//    Call<WeatherResponse> call = apiService.getCurrentWeatherByCoords(latitude, longitude, API_KEY, units);
//    Call<WeatherResponse> hourlyCall = apiService.getFiveDayForecast(latitude, longitude, API_KEY, units);
//
//    call.enqueue(new Callback<WeatherResponse>() {
//        @Override
//        public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
//            if (response.isSuccessful() && response.body() != null) {
//                WeatherResponse weatherData = response.body();
//                updateUI(weatherData);
//                fetchFiveDayForecast(latitude, longitude); // Fetch 5-day forecast
//                fetchAQIData(weatherData.getCoord().getLat(), weatherData.getCoord().getLon());
//                sharedPreferences.edit().putString(KEY_CITY, city).apply();
//            } else {
//                Log.e("WeatherApi", "Unknown location error: " + response.message());
//                fetchWeatherByCity(city); // Fallback to city data
//            }
//        }
//
//        @Override
//        public void onFailure(Call<WeatherResponse> call, Throwable t) {
//            Log.e("WeatherApi", "Error fetching data: " + t.getMessage());
//            fetchWeatherByCity(city);
//        }
//    });
//
//    hourlyCall.enqueue(new Callback<WeatherResponse>() {
//        @Override
//        public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
//            if (response.isSuccessful() && response.body() != null) {
//                WeatherResponse hourlyData = response.body();
//                updateHourlyForecast(hourlyData);
//            } else {
//                Log.e("WeatherApi", "Failed to fetch hourly forecast: " + response.message());
//            }
//        }
//
//        @Override
//        public void onFailure(Call<WeatherResponse> call, Throwable t) {
//            Log.e("WeatherApi", "Error fetching hourly forecast: " + t.getMessage());
//        }
//    });
//}
//
//private void updateFiveDayForecastUI(WeatherResponse forecastData) {
//    if (forecastData == null || forecastData.getList() == null || forecastData.getList().isEmpty()) {
//        Log.e("WeatherApi", "No forecast data available");
//        return;
//    }
//
//    Map<String, List<WeatherResponse.ForecastItem>> dailyForecastMap = new HashMap<>();
//    SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//
//    for (WeatherResponse.ForecastItem item : forecastData.getList()) {
//        String dayKey = sdfDay.format(new Date(item.getDt() * 1000));
//        if (!dailyForecastMap.containsKey(dayKey)) {
//            dailyForecastMap.put(dayKey, new ArrayList<>());
//        }
//        dailyForecastMap.get(dayKey).add(item);
//    }
//
//    // Get today's date
//    Calendar today = Calendar.getInstance();
//    String todayKey = sdfDay.format(today.getTime());
//
//    // Get today's forecast items
//    List<WeatherResponse.ForecastItem> todayItems = dailyForecastMap.get(todayKey);
//
//    if (todayItems != null && !todayItems.isEmpty()) {
//        // Calculate high and low for today
//        double high = -Double.MAX_VALUE;
//        double low = Double.MAX_VALUE;
//
//        for (WeatherResponse.ForecastItem item : todayItems) {
//            double temp = item.getMain().getTemp();
//            if (temp > high) {
//                high = temp;
//            }
//            if (temp < low) {
//                low = temp;
//            }
//        }
//
//        // Update dailyHighTemp and dailyLowTemp
//        dailyHighTemp = high;
//        dailyLowTemp = low;
//
//        // Save the updated high and low temperatures
//        saveHighAndLow();
//
//        // Update the UI
//        updateUIWithHighLow(high, low);
//    }
//}
//
//private void updateUIWithHighLow(double high, double low) {
//    runOnUiThread(() -> {
//        String tempUnit = getTemperatureUnit();
//        if (tempUnit.equals("F")) {
//            high = (high * 9 / 5) + 32;
//            low = (low * 9 / 5) + 32;
//        }
//
//        textViewHighTemp.setText(String.format("H: %.1f%s", high, tempUnit.equals("C") ? "°C" : "°F"));
//        textViewLowTemp.setText(String.format("L: %.1f%s", low, tempUnit.equals("C") ? "°C" : "°F"));
//    });
//}
//
//private void updateUI(WeatherResponse weatherData) {
//    runOnUiThread(() -> {
//        // Get the current temperature
//        double currentTemp = weatherData.getMain().getTemp();
//
//        // Update high and low temperatures
//        if (currentTemp > dailyHighTemp) {
//            dailyHighTemp = currentTemp;
//        }
//        if (currentTemp < dailyLowTemp) {
//            dailyLowTemp = currentTemp;
//        }
//
//        // Save the updated high and low temperatures
//        saveHighAndLow();
//
//        // Update the UI with the new high and low temperatures
//        updateUIWithHighLow(dailyHighTemp, dailyLowTemp);
//
//        // Rest of the UI update logic...
//    });
//}