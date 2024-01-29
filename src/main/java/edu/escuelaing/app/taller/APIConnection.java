package edu.escuelaing.app.taller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The `APIConnection` class is responsible for establishing a connection to the OMDB API
 * and retrieving information about a specified movie title. It includes methods to set
 * the user query, make a GET request to the API, and handle the response.
 * 
 * The class utilizes a ConcurrentHashMap for caching responses to avoid redundant API calls
 * for the same movie title.
 * 
 * @Author Nicolas Ariza Barbosa
 */
public class APIConnection {
    private String userQuery;
    private static final String API_KEY = "8339ce9b";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "https://www.omdbapi.com/?apikey=" + API_KEY;
    private static ConcurrentHashMap<String, StringBuffer> cache = new ConcurrentHashMap<>();

    /**
     * Sets the user query to the specified movie title.
     * 
     * @param newQuery The movie title to set as the user query.
     */
    public void setUserQuery(String newQuery){
        this.userQuery = newQuery;
    }

    /**
     * Retrieves the current user query.
     * 
     * @return The current user query.
     */
    public String getUserQuery(){
        return this.userQuery;
    }

    /**
     * Stores the movie data in the cache.
     * 
     * @param movieName  The name of the movie to use as the cache key.
     * @param movieData  The movie data to store in the cache.
     */
    public void storeMovieData(String movieName, StringBuffer movieData){
        cache.put(movieName, movieData);
    }

    /**
     * Retrieves movie data from the cache.
     * 
     * @param movieName The name of the movie to retrieve from the cache.
     * @return The movie data stored in the cache.
     */
    public StringBuffer getMovieData(String movieName){
        return cache.get(movieName);
    }

    /**
     * Makes a GET request to the OMDB API using the specified user query and handles the response.
     * 
     * @param outPut           PrintWriter object to send the API response to the client.
     * @param sResponseHeaders Object to manage HTTP response headers.
     * @param sResponseData    Object to manage HTTP response data.
     * @throws IOException If an I/O error occurs while making the API request.
     */
    public void getRequest(PrintWriter outPut, HTTPResponseHeaders sResponseHeaders, HTTPResponseData sResponseData) throws IOException{
        
        // Sets the content type as JSON and sends the OK response headers to the client
        sResponseHeaders.setContentType("json");
        outPut.println(sResponseHeaders.OKResponse());

        if(cache.get(getUserQuery()) == null){ // Check if the movie name is already in cache
            // Establishes a connection to the OMDB API
            URL obj = new URL(GET_URL + "&t=" + getUserQuery());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            StringBuffer response = new StringBuffer();
            
            // Retrieves the response code from the API request
            int responseCode = con.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                // Reads the API response and appends it to the StringBuffer
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Checks if the movie is not found in the API response
                if(response.toString().equals("{\"Response\":\"False\",\"Error\":\"Movie not found!\"}")){
                    outPut.println(sResponseData.getJSONErrorMessage());
                }else{
                    // Stores the API response in the cache
                    storeMovieData(this.userQuery, response);
                    // Sends the API response to the client
                    outPut.println(response.toString());
                }

            } else {
                System.out.println("GET request not worked");
            }
        }else{
            // Sends the cached API response to the client
            outPut.println(getMovieData(this.userQuery).toString());
        }
    }
}
