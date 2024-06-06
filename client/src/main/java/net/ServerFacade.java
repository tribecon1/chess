package net;


import model.AuthData;
import model.UserData;
import response.ErrorResponse;
import response.ResponseType;
import server.SerializerDeserializer;

import java.io.IOException;

public class ServerFacade {

    public static String register(UserData user) throws IOException {
        String urlExtension = "/user";
        String registerJSON = SerializerDeserializer.convertToJSON(user);
        ResponseType responseObject = ClientCommunicator.createHttpPost(registerJSON, null, urlExtension);
        if (responseObject instanceof AuthData){
            return ((AuthData) responseObject).authToken();
        }
        else if (responseObject instanceof ErrorResponse){
            return ((ErrorResponse) responseObject).message();
        }
        else{
            return "Unknown error. Please contact us.";
        }
    }



}
