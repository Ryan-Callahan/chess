import com.google.gson.Gson;

public class Serializer {
    private static final Gson gsonSerializer = new Gson();
    public static String serialize(Object src) {
        return gsonSerializer.toJson(src);
    }

    public static <T> T deserialize(String json, Class<T> objectType) {
        return gsonSerializer.fromJson(json, objectType);
    }
}
