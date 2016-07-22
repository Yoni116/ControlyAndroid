package net.controly.controly;

import android.app.Application;

import retrofit2.Retrofit;

/**
 * This is the main class of the application.
 */
public class ControlyApplication extends Application {

    /**
     * Implementing the singleton design pattern.
     */
    private static ControlyApplication sInstance;

    /**
     * The base url of the API.
     */
    private final String BASE_URL = "https://api.controly.net/ControlyApi/Receiver.php/";

    /**
     * The configuration of the relationship with the API.
     */
    private Retrofit retrofit;

    public static ControlyApplication getsInstance() {
        return sInstance;
    }

    /**
     * On create, configure the {@code Retrofit} connection.
     */
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();
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
