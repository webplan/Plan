package com.zzt.plan.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import com.zzt.plan.app.R;
import com.zzt.plan.app.entity.LocationEntity;
import com.zzt.plan.app.entity.LocationResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzt on 15-6-23.
 */
public class DecideLocationAdapter extends BaseAdapter {
    private Context context;
    private List<LocationResult> locationList = new ArrayList<>();
    private int selectedPosition;

    public DecideLocationAdapter(Context context) {
        this.context = context;
    }

    public void addAll(List<LocationResult> timeList) {
        this.locationList.addAll(timeList);
        notifyDataSetChanged();
    }

    public void clear() {
        locationList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return locationList.size();
    }

    @Override
    public LocationResult getItem(int position) {
        return locationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cell_decide_location, null);

            holder = new ViewHolder();
            holder.tvLocation = (TextView) convertView.findViewById(R.id.tv_location);
            holder.btnViewOnMap = (Button) convertView.findViewById(R.id.btn_view_on_map);
            holder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
            holder.rbItem = (RadioButton) convertView.findViewById(R.id.rb_item);

            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();

        holder.tvLocation.setText(getItem(position).getLocation().getName());
        holder.rbItem.setChecked(selectedPosition == position);
        holder.tvNum.setText("(" + getItem(position).getNum() + ")");
        holder.btnViewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOnMap(position);
            }
        });
        holder.rbItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
            }
        });
        return convertView;
    }

    public LocationEntity getDecidedLocation() {
        return getItem(selectedPosition).getLocation();
    }

    private void viewOnMap(int position) {
    }

    private class ViewHolder {
        TextView tvLocation;
        TextView tvNum;
        Button btnViewOnMap;
        RadioButton rbItem;
    }
}
