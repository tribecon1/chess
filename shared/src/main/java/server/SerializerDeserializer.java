package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SerializerDeserializer {


    //prep the gson to serialize/deserialize UserCommand and ServerMessage, THEN set the var SERIALIZER equal to it

    private static final Gson SERIALIZER = new Gson();

    public static String convertToJSON(Object obj){
        if (obj instanceof Exception){
            JsonObject json = new JsonObject();
            json.addProperty("message", ((Exception) obj).getMessage());
            return json.toString();
        }
        return SERIALIZER.toJson(obj);
    }

    public static <T> T convertFromJSON(String json, Class<T> objClass){
        return SERIALIZER.fromJson(json, objClass);
    }

}
