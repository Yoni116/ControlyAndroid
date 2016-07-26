package net.controly.controly;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import net.controly.controly.activity.LoginActivity;
import net.controly.controly.http.HeaderInterceptor;
import net.controly.controly.model.User;
import net.controly.controly.util.UIUtils;
import net.controly.controly.util.FontUtils;
import net.controly.controly.util.GsonFactory;

import okhttp3.OkHttpClient;
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
     * The base url of the server.
     */
    private final String BASE_URL = "https://api.controly.net/ControlyApi/";

    /**
     * The configuration of the relationship with the API.
     */
    private Retrofit retrofit;

    /**
     * Image loader for retrieving images from web.
     */
    private ImageLoader mImageLoader;

    /**
     * The currently authenticated user.
     */
    private User user;

    /**
     * The authenticated user's token.
     */
    private String jwt;

    /**
     * The name of the user shared preference.
     */
    private final String PREF_USER = "user";

    /**
     * The name of the jwt shared preference.
     */
    private final String PREF_JWT = "jwt";

    public static ControlyApplication getInstace() {
        return sInstance;
    }

    /**
     * On create, configure the {@code Retrofit} connection and the default font.
     */
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        //Initialize app font
        final String fontName = "Brandon_reg.ttf";
        FontUtils.setDefaultFont(this, fontName);

        //Initialize retrofit
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL + "Receiver.php/")
                .addConverterFactory(GsonFactory.getGsonConverterFactory())
                .client(client)
                .build();

        //Initialize web images
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        mImageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {

            private final int maxCacheSize = 10000;
            private final LruCache<String, Bitmap> mCache = new LruCache<>(maxCacheSize);

            @Override
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
        });
    }

    /**
     * @return The image loader object in order to retrieve images from web.
     */
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    /**
     * @return The base url for retrieving images from the server.
     */
    public String getBaseUrl() {
        return BASE_URL;
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
            return getAuthenticatedUser() != null && getJwt() != null;
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * The method receives a user and sets it as the authenticated user.
     *
     * @param user The user to set as authenticated.
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

    /**
     * Save the given JWT in the shared preferences.
     *
     * @param jwt The JWT token to save.
     */
    public void setJwt(String jwt) {
        this.jwt = jwt;

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getInstace());
        sp.edit().putString(PREF_JWT, jwt).apply();
    }

    /**
     * @return The current jwt.
     */
    public String getJwt() {
        if (jwt != null) {
            return jwt;
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getInstace());
        return sp.getString(PREF_JWT, null);
    }

    /**
     * Logout from the application.
     */
    public void logout(Context context) {
        setJwt(null);
        setAuthenticatedUser(null);

        UIUtils.startActivity(context, LoginActivity.class);
    }
}
