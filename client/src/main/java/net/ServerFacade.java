package net;


import model.AuthData;
import model.UserData;
import response.ResponseType;
import server.SerializerDeserializer;

import java.io.IOException;

public class ServerFacade {

    public static String register(UserData user) throws IOException {
        String urlExtension = "/user";
        String registerJSON = SerializerDeserializer.convertToJSON(user);
        ResponseType newAuthData = ClientCommunicator.createPost(registerJSON, urlExtension);
        //convert newAuthData into object using GSON?
        return ((AuthData) newAuthData).authToken();
    }



}
