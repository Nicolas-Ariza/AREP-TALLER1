package edu.escuelaing.app.taller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

public class APIConnection {
    private String userQuery;
    private static final String API_KEY = "8339ce9b";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "https://www.omdbapi.com/?apikey=" + API_KEY;
    private static ConcurrentHashMap<String, StringBuffer> cache = new ConcurrentHashMap<String, StringBuffer>();

    public void setQuery(String newQuery){
        this.userQuery = newQuery;
    }

    public void getRequest(PrintWriter outPut, HTTPResponseHeaders sResponseHeaders, HTTPResponseData sResponseData) throws IOException{
        URL obj = new URL(GET_URL + "&t=" + this.userQuery);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        StringBuffer response = new StringBuffer();
        
        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            sResponseHeaders.setContentType("json");
            outPut.println(sResponseHeaders.OKResponse());

            if(response.toString().equals("{\"Response\":\"False\",\"Error\":\"Movie not found!\"}")){
                outPut.println(sResponseData.getJSONErrorMessage());
            }else{
                if(cache.get(this.userQuery) == null){ // Check if the movie name is in the cache
                    // Store in cache
                    cache.put(this.userQuery, response);
                    // Content response
                    outPut.println(response.toString());
                }else{
                    // Content response
                    outPut.println(cache.get(this.userQuery).toString());
                }
            }

        } else {
            System.out.println("GET request not worked");
        }
    }
}
