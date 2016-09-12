package net.controly.controly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.controly.controly.R;
import net.controly.controly.model.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a list adapter for a list view of boxes.
 */
public class LocationsGridAdapter extends BaseAdapter {

    private final Context mContext;

    private List<Location> mLocationsList;
    private LayoutInflater mInflater;

    public LocationsGridAdapter(Context mContext) {
        this.mContext = mContext;
        this.mLocationsList = new ArrayList<>();
    }

    public void addAll(Location[] locations) {
        mLocationsList.addAll(Arrays.asList(locations));
        notifyDataSetChanged();
    }

    public void clear() {
        mLocationsList.clear();
    }

    @Override
    public int getCount() {
        return mLocationsList == null ? 0 : mLocationsList.size();
    }

    @Override
    public Location getItem(int i) {
        return mLocationsList.get(i);
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
            convertView = mInflater.inflate(R.layout.grid_item_locations, parent, false);
        }

        Location location = getItem(position);

        TextView title = (TextView) convertView.findViewById(R.id.location_title);
        title.setText(location.getTitle());

        return convertView;
    }

    public Location[] getAll() {
        return mLocationsList.toArray(new Location[mLocationsList.size()]);
    }
}
