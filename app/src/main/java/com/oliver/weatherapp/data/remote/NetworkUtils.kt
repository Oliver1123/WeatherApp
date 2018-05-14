package com.oliver.weatherapp.data.remote

import android.net.Uri
import com.oliver.weatherapp.BuildConfig
import timber.log.Timber
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

internal object NetworkUtils {

    private const val units = "metric"

    private const val PARAM_API_KEY = "appid"
    private const val PARAM_UNITS = "units"
    private const val PARAM_LATITUDE = "lat"
    private const val PARAM_LONGITUDE = "lon"


    //http://api.openweathermap.org/data/2.5/forecast?lat=0&lon=0&appid=c6e381d8c7ff98f0fee43775817cf6ad&units=metric
    fun buildUrl(latitude: Double, longitude: Double): URL? {
        val weatherQueryUri = Uri.parse(BuildConfig.SERVER_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.API_KEY)
                .appendQueryParameter(PARAM_LATITUDE, java.lang.Double.toString(latitude))
                .appendQueryParameter(PARAM_LONGITUDE, java.lang.Double.toString(longitude))
                .appendQueryParameter(PARAM_UNITS, units)
                .build()

        return try {
            val weatherQueryUrl = URL(weatherQueryUri.toString())
            Timber.v( "URL: $weatherQueryUrl")
            weatherQueryUrl
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            null
        }

    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    @Throws(IOException::class)
    fun getResponseFromHttpUrl(url: URL): String? {
        val urlConnection = url.openConnection() as HttpURLConnection
        try {

            val scanner = Scanner(urlConnection.inputStream)
            scanner.useDelimiter("\\A")

            val hasInput = scanner.hasNext()
            var response: String? = null
            if (hasInput) {
                response = scanner.next()
            }
            scanner.close()
            return response
        } finally {
            urlConnection.disconnect()
        }
    }
}