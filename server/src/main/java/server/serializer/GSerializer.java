package server.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GSerializer {
    private static final Gson GSON_SERIALIZER = new GsonBuilder()
            .serializeNulls()
            .create();

    public static String serialize(Object src) {
        return GSON_SERIALIZER.toJson(src);
    }

    public static <T> T deserialize(String json, Class<T> objectType) {
        return GSON_SERIALIZER.fromJson(json, objectType);
    }
}