package server;

import com.google.gson.Gson;

public class SerializerDeserializer {



    public static String ConvertToJSON(Object obj){
        var serializer = new Gson();
        return serializer.toJson(obj);
    }

    public static <T> T ConvertFromJSON(String json, Class<T> objClass){
        var deserializer = new Gson();
        return deserializer.fromJson(json, objClass);
    }

}
