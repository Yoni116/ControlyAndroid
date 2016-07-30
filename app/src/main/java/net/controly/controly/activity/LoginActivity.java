package net.controly.controly.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.http.response.LoginResponse;
import net.controly.controly.http.service.LoginService;
import net.controly.controly.util.HashUtils;
import net.controly.controly.util.Logger;
import net.controly.controly.util.PermissionUtils;
import net.controly.controly.util.UIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * This is an activity for logging in to the application.
 */
public class LoginActivity extends BaseActivity {

    //-------Views-------
    private AutoCompleteTextView mEmailEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Context context = this;

        //Configure the views of the layout.
        mEmailEditText = (AutoCompleteTextView) findViewById(R.id.email_field);
        mPasswordEditText = (EditText) findViewById(R.id.password_field);
        mLoginButton = (Button) findViewById(R.id.login_button);

        //If we have the read contacts permission setup email auto complete. If not, request it from the user.
        if (PermissionUtils.hasPermission(context, READ_CONTACTS)) {
            UIUtils.setupEmailAutoComplete(getApplicationContext(), mEmailEditText);
        } else {
            //TODO Write a user friendly message.
            PermissionUtils.requestPermission(context, READ_CONTACTS,
                    "The app needs a permission to read your contacts in order to use email autocomplete.");
        }

        ControlyApplication.getInstance()
                .logout();

        //Authenticate the user when he clicks on the login button.
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get the email and password from the views.
                String email = mEmailEditText.getText().toString();
                String password = HashUtils.MD5(mPasswordEditText.getText().toString());

                //Get the relevant API method for logging in.
                Call<LoginResponse> call = ControlyApplication.getInstance()
                        .getService(LoginService.class)
                        .login(email, password);

                //Asynchronous call for logging in to the application.
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                        LoginResponse loginResponse = response.body();

                        if (loginResponse.hasSucceeded()) {
                            Logger.info("Login succeeded! Continuing to main activity. " + loginResponse.toString());

                            //Set the authenticated user.
                            ControlyApplication.getInstance()
                                    .setAuthenticatedUser(loginResponse.getUser());

                            //Set the authenticated user's jwt token.
                            ControlyApplication.getInstance()
                                    .setJwt(loginResponse.getJwt());

                            //Continue to the main activity.
                            UIUtils.startActivity(context, MainActivity.class);
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

                        Toast.makeText(getApplicationContext(), "Controly encountered an error", Toast.LENGTH_SHORT)
                                .show();
                    }
                });

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.READ_CONTACTS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    UIUtils.setupEmailAutoComplete(getApplicationContext(), mEmailEditText);
                }
        }
    }
}
