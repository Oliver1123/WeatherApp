## WeatherApp
Weather, everybody wants to know how it is going to be during the week. Will it be rainy, windy,
or sunny? Luckily for us, in the information age, there are open APIs to retrieve information
about it.

For this assignment you will be using the API from: http://openweathermap.org/api. The API key
is provided at the end of the statement, or you can request your own by registering on the
website for free.

Full app assignment can be found [here](https://drive.google.com/open?id=0B7krWRgXRtNvMXFiQ21wcm9zc0E0dHIwRENiSTJxUUlHYTBV)

#### Information:
 * AndroidArchitectureComponents are used for the app as recommended architecture approach
   - Cities and Weather information is saved into local db with Room, so without internet connection user can access cached information
   - ViewModel and LiveData allows to implement orientation change without without having to reload the data
 * Google Places API is used for city selection
   - user can select a place on the map
   - pick one of the places nearby
   - search for the location by name
 * Resources and helpers for weather UI representation are used from Sunshine application
 
#### What can be added/improved
 - Perform weather request right after city is added to db
 - Add pull to refresh for weather screen
 - Confirmation dialog before delete the city
 - Handle loading state and loading errors
     
 