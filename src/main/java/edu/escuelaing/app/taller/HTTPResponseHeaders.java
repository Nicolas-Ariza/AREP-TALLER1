package edu.escuelaing.app.taller;

public class HTTPResponseHeaders{
    private String CONTENT_TYPE;

    public HTTPResponseHeaders(String contentType){
        this.CONTENT_TYPE = contentType;
    }

    public String OKResponse(){
        return "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/" + this.CONTENT_TYPE + "\r\n" +
                "";
    }

    public String NotFoundResponse(){
        return "HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: text/" + this.CONTENT_TYPE + "\r\n" +
                "";
    }

    public void setContentType(String newContentType) {
        this.CONTENT_TYPE = newContentType;
    }
}