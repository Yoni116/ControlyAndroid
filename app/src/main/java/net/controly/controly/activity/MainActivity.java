package net.controly.controly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.http.service.UserService;
import net.controly.controly.model.User;
import net.controly.controly.util.Logger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private FloatingActionButton mMenuButton;
    private ListView mKeyboardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMenuButton = (FloatingActionButton) findViewById(R.id.open_menu_button);
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenuIntent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(mainMenuIntent);
            }
        });

        showWaitDialog();

        String jwt = ControlyApplication.getInstace()
                .getJwt();

        User authenticatedUser = ControlyApplication.getInstace()
                .getAuthenticatedUser();

        Call<ResponseBody> call = ControlyApplication.getInstace()
                .getService(UserService.class)
                .getAllUserKeyboards("Bearer " + jwt, authenticatedUser.getId());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissDialog();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Logger.error(t.getMessage());
                dismissDialog();
            }
        });
    }

}
