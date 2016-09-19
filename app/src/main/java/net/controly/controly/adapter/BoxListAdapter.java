package net.controly.controly.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.controly.controly.R;
import net.controly.controly.model.BoxListItem;
import net.controly.controly.util.FontUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a list adapter for a list view of boxes.
 */
public class BoxListAdapter<T extends BoxListItem> extends BaseAdapter {

    private final Context mContext;
    private final List<T> mList;

    private LayoutInflater mInflater;
    private boolean invertFirstItemColor = false;

    public BoxListAdapter(Context mContext) {
        this.mContext = mContext;
        this.mList = new ArrayList<>();
    }

    public void addAll(List<T> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(T[] arr) {
        mList.addAll(Arrays.asList(arr));
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
    }

    public void addFirstItem(int index, T element) {
        mList.add(index, element);
    }

    public void remove(int index) {
        mList.remove(index);
        notifyDataSetChanged();
    }

    public ArrayList<T> getItems() {
        return new ArrayList<>(mList);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //One view type is the regular device, and one is for the 'My Automations' row.
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_boxes, parent, false);
        }

        T element = getItem(position);

        TextView title = (TextView) convertView.findViewById(R.id.box_text);
        title.setText(element.getTitle());
        FontUtils.setTextViewFont(title);

        RelativeLayout background = (RelativeLayout) convertView.findViewById(R.id.box_background);

        if (invertFirstItemColor && position == 0) {
            title.setTextColor(Color.BLACK);
            background.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }
}
