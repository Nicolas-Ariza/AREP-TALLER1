package edu.escuelaing.app.taller;

import java.net.*;
import java.io.*;

public class HttpServer {
    private static HTTPResponseHeaders serverResponseHeaders = new HTTPResponseHeaders("html");
    private static HTTPResponseData serverResponseData = new HTTPResponseData();
    private static APIConnection API = new APIConnection();
    
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
            try {
                Socket clientSocket = serverSocket.accept();
                Thread clientThread = new Thread(() -> { // Multiuser feature delegated to threads
                    try {
                        handleClientConnection(clientSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                clientThread.start();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            
        }
        serverSocket.close();
    }

    private static void handleClientConnection(Socket client) throws IOException{
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String inputLine;
        boolean firstLine = true;
        String query = "";

        while ((inputLine = in.readLine()) != null) {
            if(firstLine){
                query = inputLine.split(" ")[1].toLowerCase();
                firstLine = false;
            }
            if (!in.ready()) {
                break;
            }
        }

        if(query.equals("/")){ // Return page index
            HTTPResponse(out);
        }else if ((query.startsWith("/?name=")) && (query.length() > 7)){ // Validates the message and queries in the API
            API.setQuery(query.substring(7));
            API.getRequest(out, serverResponseHeaders, serverResponseData);
        }else{ // Error if everything else fails
            HTTPError(out);
        }
        
        out.close();
        in.close();
        client.close();
    }

    public static void HTTPResponse (PrintWriter outPut) throws FileNotFoundException, IOException{
        // Send headers to the client
        serverResponseHeaders.setContentType("html");
        outPut.println(serverResponseHeaders.OKResponse());
        // Send index.html to the client
        outPut.println(serverResponseData.getIndexPage());
    }

    private static void HTTPError(PrintWriter outPut) {
        // Send headers to the client
        serverResponseHeaders.setContentType("html");
        outPut.println(serverResponseHeaders.BadRequestResponse());
        // Send HTML structure to the client
        outPut.println(serverResponseData.getNotFoundPage());
    }
}


