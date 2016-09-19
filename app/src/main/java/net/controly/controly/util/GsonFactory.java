package net.controly.controly.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class is used to create a gson instance.
 */
public final class GsonFactory {
    private static Gson sGson;

    /**
     * @return Return the gson instance.
     */
    public static Gson getGson() {
        if (sGson == null) {
            sGson = new GsonBuilder().
                    setDateFormat(DateUtils.DEFAULT_DATE_FORMAT)
                    .create();
        }

        return sGson;
    }

    /**
     * @return A Gson converter factory for retrofit.
     */
    public static GsonConverterFactory getGsonConverterFactory() {
        return GsonConverterFactory.create(getGson());
    }
}
