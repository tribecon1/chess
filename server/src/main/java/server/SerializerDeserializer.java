package server;

import com.google.gson.Gson;

public class SerializerDeserializer {

    private static final Gson serializer = new Gson();

    public static String ConvertToJSON(Object obj){
        return serializer.toJson(obj);
    }

    public static <T> T ConvertFromJSON(String json, Class<T> objClass){
        return serializer.fromJson(json, objClass);
    }

}
