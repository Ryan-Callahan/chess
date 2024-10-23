package server.serializer;

import com.google.gson.Gson;

public class GSerializer {
    private static final Gson GSON_SERIALIZER = new Gson();

    public static String serialize(Object src) {
        return GSON_SERIALIZER.toJson(src);
    }

    public static <T> T deserialize(String json, Class<T> objectType) {
        return GSON_SERIALIZER.fromJson(json, objectType);
    }
}
