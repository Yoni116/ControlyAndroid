package net.controly.controly.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.http.response.LoginResponse;
import net.controly.controly.http.service.LoginService;
import net.controly.controly.util.HashUtils;
import net.controly.controly.util.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is an activity for logging in to the application.
 */
public class LoginActivity extends BaseActivity {

    //-------Views-------
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Configure the views of the layout.
        mEmailEditText = (EditText) findViewById(R.id.email_field);
        mPasswordEditText = (EditText) findViewById(R.id.password_field);
        mLoginButton = (Button) findViewById(R.id.login_button);

        //Authenticate the user when he clicks on the login button.
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get the email and password from the views.
                String email = mEmailEditText.getText().toString();
                String password = HashUtils.MD5(mPasswordEditText.getText().toString());

                //Get the relevant API method for logging in.
                Call<LoginResponse> call = ControlyApplication.getsInstance()
                        .getService(LoginService.class)
                        .login(email, password);

                //Asynchronous call for logging in to the application.
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                        LoginResponse loginResponse = response.body();

                        if (loginResponse.hasSucceeded()) {
                            //TODO Continue to main activity.
                            Logger.info("Login succeeded! Continuing to main activity. " + loginResponse.toString());
                        } else {
                            Logger.error("Login failed! " + loginResponse.toString());

                            Toast.makeText(getApplicationContext(), loginResponse.getReason(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Logger.error("Error while trying to log in:");
                        Logger.error(t.getMessage());
                    }
                });

            }
        });
    }
}
