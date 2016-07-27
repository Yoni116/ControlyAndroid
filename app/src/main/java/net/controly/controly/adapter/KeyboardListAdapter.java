package net.controly.controly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.controly.controly.ControlyApplication;
import net.controly.controly.R;
import net.controly.controly.model.Keyboard;
import net.controly.controly.view.CircularNetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is an adapter for the keyboard list view in the main activity.
 */
public class KeyboardListAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<Keyboard> mKeyboards;
    private LayoutInflater mInflater;

    public KeyboardListAdapter(Context mContext) {
        this.mContext = mContext;
        this.mKeyboards = new ArrayList<>();
    }

    public KeyboardListAdapter(Context mContext, List<Keyboard> mKeyboards) {
        this.mContext = mContext;
        this.mKeyboards = mKeyboards;
    }

    public void add(Keyboard keyboard) {
        mKeyboards.add(keyboard);
        notifyDataSetChanged();
    }

    public void addAll(List<Keyboard> keyboards) {
        mKeyboards.addAll(keyboards);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mKeyboards.size();
    }

    @Override
    public Keyboard getItem(int position) {
        return mKeyboards.get(position);
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
            convertView = mInflater.inflate(R.layout.list_item_keyboard, parent, false);
        }

        Keyboard keyboard = mKeyboards.get(position);

        CircularNetworkImageView keyboardImage = (CircularNetworkImageView) convertView.findViewById(R.id.keyboard_image);
        TextView keyboardText = (TextView) convertView.findViewById(R.id.keyboard_text);

        String keyboardImageUrl = ControlyApplication.getInstance().getBaseUrl() + keyboard.getPictureUrl();
        String keyboardName = keyboard.getName();

        keyboardImage.setImageUrl(keyboardImageUrl);
        keyboardText.setText(keyboardName);

        return convertView;
    }
}
