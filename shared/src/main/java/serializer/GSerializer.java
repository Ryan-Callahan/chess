package serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStreamReader;

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

    public static <T> T deserialize(InputStreamReader reader, Class<T> objectType) {
        return GSON_SERIALIZER.fromJson(reader, objectType);
    }
}