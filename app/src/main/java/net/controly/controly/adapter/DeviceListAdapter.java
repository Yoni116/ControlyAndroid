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
import net.controly.controly.model.Device;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Itai on 08-Aug-16.
 */
public class DeviceListAdapter extends BaseAdapter {

    private final Context mContext;
    private List<Device> mDevices;
    private LayoutInflater mInflater;

    public DeviceListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(Device[] devices) {
        if (mDevices == null) {
            mDevices = new ArrayList<>(devices.length + 1);
            mDevices.add(new Device(0, "My Automations", null, null, null));
        }

        mDevices.addAll(Arrays.asList(devices));
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDevices == null ? 0 : mDevices.size();
    }

    @Override
    public Device getItem(int i) {
        return mDevices.get(i);
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
            convertView = mInflater.inflate(R.layout.list_item_devices, parent, false);
        }

        Device device = getItem(position);
        TextView deviceName = (TextView) convertView.findViewById(R.id.device_name);
        RelativeLayout deviceNameBackground = (RelativeLayout) convertView.findViewById(R.id.device_name_background);

        deviceName.setText(device.getName());

        if (position == 0) {
            deviceName.setTextColor(Color.BLACK);
            deviceNameBackground.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }
}
