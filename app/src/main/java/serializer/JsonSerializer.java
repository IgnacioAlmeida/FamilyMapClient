package serializer;

import com.google.gson.Gson;

import java.io.IOException;

public class JsonSerializer {
    public static <T> T deserialize (String value, Class<T> returnType){
        return (new Gson()).fromJson(value, returnType);
    }

    public static String serialize(Object object) throws IOException {
        return (new Gson()).toJson(object);
    }
}
