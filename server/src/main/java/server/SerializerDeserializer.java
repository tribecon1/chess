package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SerializerDeserializer {

    private static final Gson serializer = new Gson();

    public static String ConvertToJSON(Object obj){
        if (obj instanceof Exception){
            JsonObject json = new JsonObject();
            json.addProperty("message", ((Exception) obj).getMessage());
            return json.toString();
        }
        return serializer.toJson(obj);
    }

    public static <T> T ConvertFromJSON(String json, Class<T> objClass){
        return serializer.fromJson(json, objClass);
    }

}
