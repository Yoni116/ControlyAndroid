package net.controly.controly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.controly.controly.R;
import net.controly.controly.model.Action;

/**
 * This is a list view adapter for a list of actions.
 */
public class ActionListAdapter extends BaseAdapter {
    private final Context mContext;
    private Action[] mActions;
    private LayoutInflater mInflater;

    public ActionListAdapter(Context mContext) {
        this.mContext = mContext;
        this.mActions = new Action[0];
    }

    public void addAll(Action[] actions) {
        mActions = actions;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mActions.length;
    }

    @Override
    public Action getItem(int i) {
        return mActions[i];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_action, parent, false);
        }

        Action action = getItem(position);

        TextView actionName = (TextView) convertView.findViewById(R.id.action_name);
        actionName.setText(action.getDescription());

        return convertView;
    }
}
