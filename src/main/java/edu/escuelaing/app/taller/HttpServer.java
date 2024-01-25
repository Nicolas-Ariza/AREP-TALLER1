package edu.escuelaing.app.taller;

import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class HttpServer {
    
    public static void main(String[] args) throws IOException {
        Map <String, String> headers = null;
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
                System.out.println("Listo para recibir ...");
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
                System.out.println(inputLine);
                if(firstLine){
                    query = inputLine.split(" ")[1];
                    firstLine = false;
                }
                if (!in.ready()) {
                    break;
                }
            }

            if(query.equals("/cliente")){
                sendIndex(clientSocket, out);
            }else{
                sendError(clientSocket, out);
            }
            
            
            

            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    public static void sendIndex (Socket client, PrintWriter outPut) throws FileNotFoundException, IOException{
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("index.html"))) {
            outPut.println("HTTP/1.1 200 OK");
            outPut.println("Content-Type: text/html");
            outPut.println("Content-Length: " + bufferedInputStream.available());
            outPut.println();

            // Enviar el contenido del archivo HTML
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                client.getOutputStream().write(buffer, 0, bytesRead);
            }
        }
    }

    public static void sendError (Socket client, PrintWriter outPut) {
        outPut.println("HTTP/1.1 404 Not found");
        outPut.println("Content-Type: text/html");
        outPut.println("<!DOCTYPE html>\r\n" + //
                            "<html>\r\n" + 
                            "<p>Error, this resource is not available</p>\r\n" +
                            "</html>");
    }

    // private static void requestManager (Socket client, PrintWriter outPut){
        
    // }
}

