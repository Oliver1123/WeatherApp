package com.oliver.weatherapp.data.remote;

import android.net.Uri;
import android.util.Log;

import com.oliver.weatherapp.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String units = "metric";

    private static final String PARAM_API_KEY = "appid";
    private static final String PARAM_UNITS = "units";
    private static final String PARAM_DAYS = "cnt";
    private static final String PARAM_LATITUDE = "lat";
    private static final String PARAM_LONGITUDE = "lon";


    //http://api.openweathermap.org/data/2.5/forecast?lat=0&lon=0&appid=c6e381d8c7ff98f0fee43775817cf6ad&units=metric
    public static URL buildUrl(double latitude, double longitude, int days) {
        Uri weatherQueryUri = Uri.parse(BuildConfig.SERVER_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.API_KEY)
                .appendQueryParameter(PARAM_LATITUDE, Double.toString(latitude))
                .appendQueryParameter(PARAM_LONGITUDE, Double.toString(longitude))
                .appendQueryParameter(PARAM_UNITS, units)
                .appendQueryParameter(PARAM_DAYS, Integer.toString(days))
                .build();

        try {
            URL weatherQueryUrl = new URL(weatherQueryUri.toString());
            Log.v(TAG, "URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}