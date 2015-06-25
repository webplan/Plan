package com.zzt.plan.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import com.zzt.plan.app.R;
import com.zzt.plan.app.entity.TimeResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by zzt on 15-6-23.
 */
public class DecideTimeAdapter extends BaseAdapter {
    private Context context;
    private List<TimeResult> timeList = new ArrayList<>();
    private int selectedPosition;

    public DecideTimeAdapter(Context context) {
        this.context = context;
        notifyDataSetChanged();
    }

    public void addAll(List<TimeResult> timeList) {
        this.timeList.addAll(timeList);
        notifyDataSetChanged();
    }

    public void clear() {
        timeList.clear();
    }

    @Override
    public int getCount() {
        return timeList.size();
    }

    @Override
    public TimeResult getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.cell_decide_time, null);

            holder = new ViewHolder();
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
            holder.rbItem = (RadioButton) convertView.findViewById(R.id.rb_item);

            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();

        holder.tvTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(getItem(position).getTime()));
        holder.tvNum.setText("(" + getItem(position).getNum() + ")");
        holder.rbItem.setChecked(position == selectedPosition);
        holder.rbItem.setChecked(false);

        holder.rbItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
            }
        });

        return convertView;
    }

    public long getDecidedTime() {
        return getItem(selectedPosition).getTime();
    }

    private class ViewHolder {
        TextView tvTime;
        TextView tvNum;
        RadioButton rbItem;
    }
}
