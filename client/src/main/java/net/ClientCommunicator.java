package net;

import model.AuthData;
import response.ErrorResponse;
import response.ResponseType;
import server.SerializerDeserializer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientCommunicator {
    private static String baseLink;

    public ClientCommunicator(String givenLink) {
        baseLink = givenLink;
    }

    public static ResponseType createHttpPost(String requestBodyJSON, String authToken, String urlPath) throws IOException {
        HttpURLConnection connection = getHttpURLConnection(urlPath, "POST");
        if (urlPath.equals("/game")){
            connection.addRequestProperty("authorization", authToken);
        }
        connectionBodyAdded(requestBodyJSON, connection);

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return successResponseObject(connection, AuthData.class);
        }
        else {
            return errorResponseObject(connection);
        }
    }


    public static ResponseType createHttpPut(String requestBodyJSON, String authToken, String urlPath) throws IOException {

        return null;
    }






    private static HttpURLConnection getHttpURLConnection(String urlPath, String requestMethod) throws IOException {
        URL url = new URL(baseLink + urlPath);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod(requestMethod);
        connection.setDoOutput(true);
        connection.connect();
        return connection;
    }

    private static void connectionBodyAdded(String responseBodyJSON, HttpURLConnection givenConnection) throws IOException {
        try(OutputStream requestBody = givenConnection.getOutputStream()) {
            OutputStreamWriter writer = new OutputStreamWriter(requestBody, StandardCharsets.UTF_8);
            writer.write(responseBodyJSON);
        }
    }

    private static <T> T successResponseObject(HttpURLConnection connection, Class<T> objClass) throws IOException {
        Scanner responseBodyReader = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
        String responseBodyJSON = responseBodyReader.useDelimiter("\\A").next();
        responseBodyReader.close();
        return SerializerDeserializer.convertFromJSON(responseBodyJSON, objClass);
    }

    private static ErrorResponse errorResponseObject(HttpURLConnection connection){
        Scanner responseBodyReader = new Scanner(connection.getErrorStream(), StandardCharsets.UTF_8);
        String responseBodyJSON = responseBodyReader.useDelimiter("\\A").next();
        responseBodyReader.close();
        return SerializerDeserializer.convertFromJSON(responseBodyJSON, ErrorResponse.class);
    }


}
