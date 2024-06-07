package net;

import dataaccess.DataAccessException;
import response.ErrorResponse;
import server.SerializerDeserializer;

import java.io.IOException;
import java.io.OutputStream;
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
        return connection;
    }

    public static void connectionBodyAdded(String responseBodyJSON, HttpURLConnection givenConnection) throws IOException {
        try(OutputStream requestBody = givenConnection.getOutputStream()) {
            requestBody.write(responseBodyJSON.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static <T> T successResponseObject(HttpURLConnection connection, Class<T> objClass) throws IOException {
        Scanner responseBodyReader = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
        String responseBodyJSON = responseBodyReader.useDelimiter("\\A").next();
        responseBodyReader.close();
        return SerializerDeserializer.convertFromJSON(responseBodyJSON, objClass);
    }

    public static ErrorResponse errorResponseObject(HttpURLConnection connection) throws IOException {
        Scanner responseBodyReader = new Scanner(connection.getErrorStream(), StandardCharsets.UTF_8);
        String responseBodyJSON = responseBodyReader.useDelimiter("\\A").next();
        responseBodyReader.close();
        return new ErrorResponse(connection.getResponseCode(), SerializerDeserializer.convertFromJSON(responseBodyJSON, ErrorResponse.class).message());
    }


}
