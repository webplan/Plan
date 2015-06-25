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

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zzt on 15-6-21.
 */
public class ChooseTimesAdapter extends BaseAdapter {
    public static final int TYPE_SELECT = 1;
    public static final int TYPE_REMOVE = 2;
    private Context context;
    private List<Calendar> timeList;
    private int type;
    private Map<Integer, Boolean> isSelected;

    public ChooseTimesAdapter(Context context, List<Calendar> timeList, int type) {
        this.context = context;
        this.timeList = timeList;
        isSelected = new HashMap<>();
        this.type = type;
        init();
    }

    public void init() {
        for (int i = 0; i < timeList.size(); i++) {
            isSelected.put(i, false);
        }
    }

    @Override
    public int getCount() {
        return timeList.size();
    }

    @Override
    public Calendar getItem(int position) {
        return timeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cell_edit_time, null);

            holder = new ViewHolder();
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
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
                        timeList.remove(position);
                        notifyDataSetChanged();
                    }
                });

                break;
        }

        final Calendar calendar = getItem(position);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String timeStr = format.format(calendar.getTime());

        holder.tvTime.setText(timeStr);
        return convertView;
    }

    public List<Calendar> getSelectedTimeList() {
        List<Calendar> selectedTimeList = new ArrayList<>();
        for (int i = 0; i < timeList.size(); i++) {
            if (isSelected.get(i)) {
                selectedTimeList.add(timeList.get(i));
            }
        }
        return selectedTimeList;
    }

    private class ViewHolder {
        TextView tvTime;
        ImageButton btnDelete;
        CheckBox cbItem;
    }
}
