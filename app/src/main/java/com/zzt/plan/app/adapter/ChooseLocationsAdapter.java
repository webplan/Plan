package com.zzt.plan.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import com.zzt.plan.app.R;
import com.zzt.plan.app.entity.LocationEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzt on 15-6-22.
 */
public class ChooseLocationsAdapter extends BaseAdapter {
    public static final int TYPE_SELECT = 1;
    public static final int TYPE_REMOVE = 2;
    private Context context;
    private List<LocationEntity> locationList;
    private int type;
    private Map<Integer, Boolean> isSelected;

    public ChooseLocationsAdapter(Context context, List<LocationEntity> locationList, int type) {
        this.context = context;
        this.locationList = locationList;
        isSelected = new HashMap<>();
        this.type = type;
        init();
    }

    public void init() {
        for (int i = 0; i < locationList.size(); i++) {
            isSelected.put(i, false);
        }
    }


    @Override
    public int getCount() {
        return locationList.size();
    }

    @Override
    public LocationEntity getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.cell_edit_location, null);

            holder = new ViewHolder();
            holder.tvLocation = (TextView) convertView.findViewById(R.id.tv_location);
            holder.btnDelete = (ImageButton) convertView.findViewById(R.id.btn_delete);
            holder.cbItem = (CheckBox) convertView.findViewById(R.id.cb_item);

            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();

        switch (type) {
            case TYPE_SELECT:
                holder.cbItem.setVisibility(View.VISIBLE);
                holder.btnDelete.setVisibility(View.GONE);
                holder.cbItem.setChecked(isSelected.get(position));

                holder.cbItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isSelected.put(position, !isSelected.get(position));
                        notifyDataSetChanged();
                    }
                });
                break;
            case TYPE_REMOVE:
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.cbItem.setVisibility(View.GONE);
                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        locationList.remove(position);
                        notifyDataSetChanged();
                    }
                });

                break;
        }

        LocationEntity location = getItem(position);

        holder.tvLocation.setText(location.getName());

        return convertView;
    }

    public List<LocationEntity> getSelectedLocationList() {
        List<LocationEntity> selectedLocationList = new ArrayList<>();
        for (int i = 0; i < locationList.size(); i++) {
            if (isSelected.get(i)) {
                selectedLocationList.add(locationList.get(i));
            }
        }
        return selectedLocationList;
    }

    private class ViewHolder {
        TextView tvLocation;
        ImageButton btnDelete;
        CheckBox cbItem;
    }
}
