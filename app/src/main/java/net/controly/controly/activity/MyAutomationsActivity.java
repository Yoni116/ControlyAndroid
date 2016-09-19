package net.controly.controly.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.adapter.BoxListAdapter;
import net.controly.controly.http.response.SearchAutomationsResponse;
import net.controly.controly.http.service.EventService;
import net.controly.controly.model.User;
import net.controly.controly.model.UserKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Itai on 18-Sep-16.
 */
public class MyAutomationsActivity extends BaseActivity {

    private Context mContext;

    private ListView mAutomationsListView;
    private BoxListAdapter<UserKey> mAutomationsListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_automations);

        configureToolbar(true, false);

        mAutomationsListAdapter = new BoxListAdapter<>(this);
        mAutomationsListView = (ListView) findViewById(R.id.automations_list);
        mAutomationsListView.setAdapter(mAutomationsListAdapter);

        loadAutomations();
    }

    private void loadAutomations() {
        ControlyApplication instance = ControlyApplication.getInstance();
        User authenticated = instance.getAuthenticatedUser();

        Call<SearchAutomationsResponse> call = instance.getService(EventService.class)
                .searchAutomations(authenticated.getId(), "", 0, 1);

        call.enqueue(new Callback<SearchAutomationsResponse>() {
            @Override
            public void onResponse(Call<SearchAutomationsResponse> call, Response<SearchAutomationsResponse> response) {
                mAutomationsListAdapter.addAll(response.body().getAutomations());
            }

            @Override
            public void onFailure(Call<SearchAutomationsResponse> call, Throwable t) {

            }
        });
    }
}
