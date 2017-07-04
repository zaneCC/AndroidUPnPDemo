package com.zane.androidupnpdemo.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zane.androidupnpdemo.R;
import com.zane.androidupnpdemo.entity.ClingDevice;

import org.fourthline.cling.model.meta.Device;

/**
 * 说明：
 * 作者：zhouzhan
 * 日期：17/6/28 15:50
 */

public class DevicesAdapter extends ArrayAdapter<ClingDevice> {
    private LayoutInflater mInflater;

    public DevicesAdapter(Context context) {
        super(context, 0);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.devices_items, null);

        ClingDevice item = getItem(position);
        if (item == null || item.getDevice() == null) {
            return convertView;
        }

        Device device = item.getDevice();

        ImageView imageView = (ImageView)convertView.findViewById(R.id.listview_item_image);
        imageView.setBackgroundResource(R.drawable.ic_action_dock);

        TextView textView = (TextView) convertView.findViewById(R.id.listview_item_line_one);
        textView.setText(device.getDetails().getFriendlyName());
        return convertView;
    }
}