package net;

import response.ErrorResponse;
import server.SerializerDeserializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class NetUtils {

    public static HttpURLConnection getHttpURLConnection(String baseLink, String urlPath, String requestMethod) throws IOException {
        URL url = new URL(baseLink + urlPath);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod(requestMethod);
        connection.setDoOutput(true);
        connection.connect();
        return connection;
    }

    public static void connectionBodyAdded(String responseBodyJSON, HttpURLConnection givenConnection) throws IOException {
        try(OutputStream requestBody = givenConnection.getOutputStream()) {
            OutputStreamWriter writer = new OutputStreamWriter(requestBody, StandardCharsets.UTF_8);
            writer.write(responseBodyJSON);
        }
    }

    public static <T> T successResponseObject(HttpURLConnection connection, Class<T> objClass) throws IOException {
        Scanner responseBodyReader = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
        String responseBodyJSON = responseBodyReader.useDelimiter("\\A").next();
        responseBodyReader.close();
        return SerializerDeserializer.convertFromJSON(responseBodyJSON, objClass);
    }

    public static ErrorResponse errorResponseObject(HttpURLConnection connection){
        Scanner responseBodyReader = new Scanner(connection.getErrorStream(), StandardCharsets.UTF_8);
        String responseBodyJSON = responseBodyReader.useDelimiter("\\A").next();
        responseBodyReader.close();
        return SerializerDeserializer.convertFromJSON(responseBodyJSON, ErrorResponse.class);
    }


}
