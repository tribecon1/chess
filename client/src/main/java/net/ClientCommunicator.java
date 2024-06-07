package net;

import model.AuthData;
import response.CreateGameResponse;
import response.ListGamesResponse;
import response.ResponseType;
import java.io.*;
import java.net.HttpURLConnection;

import static net.NetUtils.*;

public class ClientCommunicator {
    private static String baseLink = "http://localhost:";//DO I NEED A CONSTRUCTOR FOR THIS? Better to create in ServerFacade?

    public ClientCommunicator(int givenPort) {
        baseLink = baseLink + givenPort + "/";
    }


    public static ResponseType createHttpPost(String requestBodyJSON, String authToken, String urlPath) throws IOException {
        HttpURLConnection connection = getHttpURLConnection(baseLink, urlPath, "POST");
        if (urlPath.equals("game")){
            connection.addRequestProperty("authorization", authToken);
        }
        connectionBodyAdded(requestBodyJSON, connection);

        connection.connect();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            if (urlPath.equals("game")){
                return successResponseObject(connection, CreateGameResponse.class);
            }
            else{
                return successResponseObject(connection, AuthData.class);
            }
        }
        else {
            return errorResponseObject(connection);
        }
    }


    public static ResponseType createHttpPut(String requestBodyJSON, String authToken, String urlPath) throws IOException {
        HttpURLConnection connection = getHttpURLConnection(baseLink, urlPath, "PUT");
        connection.addRequestProperty("authorization", authToken);
        connectionBodyAdded(requestBodyJSON, connection);

        connection.connect();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return null;
        }
        else {
            return errorResponseObject(connection);
        }
    }

    public static ResponseType createHttpDelete(String authToken, String urlPath) throws IOException {
        HttpURLConnection connection = getHttpURLConnection(baseLink, urlPath, "DELETE");
        connection.addRequestProperty("authorization", authToken);

        connection.connect();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return null;
        }
        else {
            return errorResponseObject(connection);
        }
    }


    public static ResponseType createHttpGet(String authToken, String urlPath) throws IOException {
        HttpURLConnection connection = getHttpURLConnection(baseLink, urlPath, "GET");
        connection.addRequestProperty("authorization", authToken);

        connection.connect();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return successResponseObject(connection, ListGamesResponse.class);
        }
        else {
            return errorResponseObject(connection);
        }
    }





}
