package net.controly.controly.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import net.controly.controly.R;
import net.controly.controly.adapter.BoxListAdapter;
import net.controly.controly.model.Key;

import java.util.ArrayList;

/**
 * Created by Itai on 17-Sep-16.
 */
public class MyActionsActivity extends BaseActivity {

    public static final String MY_ACTIONS_EXTRA = "MY_ACTIONS";

    private Context mContext;
    private BoxListAdapter<Key> mActionsListAdapter;
    private SwipeMenuListView mActionsListView;
    private ArrayList<Key> mMyActions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_actions);

        mContext = this;

        configureToolbar(true, true);

        mActionsListAdapter = new BoxListAdapter<>(this);
        mActionsListView = (SwipeMenuListView) findViewById(R.id.my_actions_list);
        mActionsListView.setAdapter(mActionsListAdapter);
        setupSwipeMenu();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mMyActions = (ArrayList<Key>) extras.getSerializable(MY_ACTIONS_EXTRA);
        }

        mActionsListAdapter.addAll(mMyActions);
    }

    //The purpose is to update the keys list when the user clicks outside the activity.
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Rect dialogBounds = new Rect();
        getWindow().getDecorView().getHitRect(dialogBounds);

        //If the user clicked outside the activity, update the list.
        if (!dialogBounds.contains((int) ev.getX(), (int) ev.getY())) {
            onBackPressed();
            return true;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(SelectDeviceActivity.UPDATE_KEYS_LIST_EXTRA, mActionsListAdapter.getItems());
        setResult(RESULT_OK, intent);

        super.onBackPressed();
    }

    private void setupSwipeMenu() {
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteAction = new SwipeMenuItem(mContext);
                deleteAction.setBackground(android.R.color.holo_red_light);
                deleteAction.setTitleColor(Color.WHITE);
                deleteAction.setTitle("Delete");
                deleteAction.setTitleSize(15);
                deleteAction.setWidth(300);

                menu.addMenuItem(deleteAction);
            }
        };

        mActionsListView.setMenuCreator(swipeMenuCreator);
        mActionsListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                mActionsListAdapter.remove(position);
                return true;
            }
        });
    }
}
