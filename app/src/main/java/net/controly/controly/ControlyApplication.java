package net.controly.controly;

import android.app.Application;

import net.controly.controly.util.FontUtils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This is the main class of the application.
 */
public class ControlyApplication extends Application {

    /**
     * Implementing the singleton design pattern.
     */
    private static ControlyApplication sInstance;

    /**
     * The configuration of the relationship with the API.
     */
    private Retrofit retrofit;

    public static ControlyApplication getsInstance() {
        return sInstance;
    }

    /**
     * On create, configure the {@code Retrofit} connection and the default font.
     */
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        final String baseUrl = "https://api.controly.net/ControlyApi/Receiver.php/";
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final String fontName = "Brandon_reg.ttf";
        FontUtils.setDefaultFont(this, "MONOSPACE", fontName);
    }

    /**
     * @param serviceClass The class of the request service.
     * @param <T>          The type of service.
     * @return An HTTP request service class.
     */
    public <T> T getService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
