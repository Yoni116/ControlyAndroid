package net.controly.controly;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import net.controly.controly.model.User;
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

    /**
     * The currently authenticated user.
     */
    private User user;

    /**
     * The name of the user shared preference.
     */
    private final String PREF_USER = "user";

    public static ControlyApplication getInstace() {
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

    /**
     * @return Whether there is an authenticated user.
     */
    public boolean isAuthenticated() {
        try {
            return getAuthenticatedUser() != null;
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * The method receives a user and sets it as the authenticated user.
     *
     * @param user The user to set as authenticated/
     */
    public void setAuthenticatedUser(User user) {
        this.user = user;

        //Convert the user entity to JSON.
        Gson gson = new Gson();
        String userJson = gson.toJson(user);

        //Save the JSON user.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getInstace());
        sp.edit().putString(PREF_USER, userJson).apply();
    }

    /**
     * @return The authenticated user.
     */
    public User getAuthenticatedUser() throws RuntimeException {

        //If the user field is not null, return it.
        if (user != null) {
            return user;
        }

        //Try to get the user saved in the phone memory.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getInstace());
        String userJson = sp.getString(PREF_USER, null);

        if (userJson != null) {
            Gson gson = new Gson();
            this.user = gson.fromJson(userJson, User.class);

            return user;
        }

        //If there is no user authenticated, throw a runtime exception.
        throw new RuntimeException("Tried to get the authenticated user, but there is no user in the application memory.");
    }
}
