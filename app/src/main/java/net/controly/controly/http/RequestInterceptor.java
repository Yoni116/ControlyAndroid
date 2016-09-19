package net.controly.controly.http;

import net.controly.controly.ControlyApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This class is used to add the OAuth header when authenticated.
 */
public class RequestInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (ControlyApplication.getInstance()
                .isAuthenticated()) {
            String jwt = ControlyApplication.getInstance()
                    .getJwt();

            request = request.newBuilder()
                    .removeHeader("Authorization") //Just in case
                    .addHeader("Authorization", "Bearer " + jwt)
                    .build();
        }

        Response response = chain.proceed(request);

        if (response.isSuccessful()) {
            return response;
        }

        throw new IOException("Network error. User might not be authenticated.");
    }
}