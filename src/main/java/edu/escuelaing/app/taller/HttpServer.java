package edu.escuelaing.app.taller;

import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class HttpServer {
    private static final String API_KEY = "8339ce9b";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "https://www.omdbapi.com/?apikey=" + API_KEY;
    private static Map<String, StringBuffer> cache = new HashMap<String, StringBuffer>();
    
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        boolean running = true;

        while(running){
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            boolean firstLine = true;
            String query = "";

            while ((inputLine = in.readLine()) != null) {
                if(firstLine){
                    query = inputLine.split(" ")[1];
                    firstLine = false;
                }
                if (!in.ready()) {
                    break;
                }
                    
            }

            if(query.equals("/")){ // Get page index
                HTTPResponse(out);
            }else if ((query.startsWith("/?name=")) && (query.length() > 7)){ // Validates the message and queries in the API
                APIConnection(out, query);
            }else{ // Error if everything else fails
                HTTPError(out);
            }
            
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    public static void HTTPResponse (PrintWriter outPut) throws FileNotFoundException, IOException{
            // Send headers to the client
            outPut.println("HTTP/1.1 200 OK");
            outPut.println("Content-Type: text/html");
            outPut.println();
            // Send HTML structure to the client
            outPut.println("<!DOCTYPE html>\r\n" + //
                    "<html>\r\n" + //
                    "    <head>\r\n" + //
                    "        <title>Movie DB</title>\r\n" + //
                    "        <meta charset=\"UTF-8\">\r\n" + //
                    "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + //
                    "    </head>\r\n" + //
                    "    <body>\r\n" + //
                    "        <h1>Query Your Favorite Movies</h1>\r\n" + //
                    "        <form action=\"/\">\r\n" + //
                    "            <label for=\"movieName\">Name:</label><br>\r\n" + //
                    "            <input type=\"text\" id=\"movieName\" name=\"name\"><br><br>\r\n" + //
                    "            <input type=\"button\" value=\"Query\" onclick=\"loadGetMovie(movieName)\">\r\n" + //
                    "        </form> \r\n" + //
                    "        <div id=\"getresp\"></div>\r\n" + //
                    "\r\n" + //
                    "        <script>\r\n" + //
                    "            function loadGetMovie(name){\r\n" + //
                    "                let url = \"/?name=\" + name.value;\r\n" + //
                    "                fetch(url, { method: 'GET' })\r\n" + //
                    "                    .then(x => x.json())\r\n" + //
                    "                    .then(y => document.getElementById(\"getresp\").innerHTML = JSON.stringify(y, null, 2));\r\n" + //
                    "            }\r\n" + //
                    "        </script>\r\n" + //
                    "    </body>\r\n" + //
                    "</html>");
    }

    public static void HTTPError(PrintWriter outPut) {
        outPut.println("HTTP/1.1 400 Not Found");
        outPut.println("Content-Type: text/html");
        outPut.println("");
        outPut.println("<!DOCTYPE html>\r\n" +
                        "<html>\r\n" +
                        "<h1>Error, bad request in your query</h1>\r\n" +
                        "</html>");
    }

    private static StringBuffer APIConnection(PrintWriter outPut, String query) throws IOException{
        String movieName = query.substring(7);
        URL obj = new URL(GET_URL + "&t=" + movieName);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        StringBuffer response = new StringBuffer();
        
        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        // System.out.println("GET Response Code :: " + responseCode);
        
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Headers response
            outPut.println("HTTP/1.1 200 OK");
            outPut.println("Content-Type: text/json");
            outPut.println();

            if(cache.get(movieName) == null){ // Check if the movie name is in the cache
                System.out.println("Not in Cache");
                cache.put(movieName, response);
                // Content response
                outPut.println(response.toString());
            }else{
                System.out.println("In Cache");
                // Content response
                outPut.println(cache.get(movieName).toString());
            }

        } else {
            System.out.println("GET request not worked");
        }
        System.out.println("GET DONE");
        return response;
    }
}


