package com.zzt.plan.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zzt.plan.app.Config;
import com.zzt.plan.app.R;
import com.zzt.plan.app.activity.ManageEventDetailActivity;
import com.zzt.plan.app.entity.EventBaseInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzt on 15-6-23.
 */
public class ManageEventAdapter extends BaseAdapter {
    private Context context;
    private List<EventBaseInfo> events = new ArrayList<>();

    public ManageEventAdapter(Context context) {
        this.context = context;
    }

    public void addAll(List<EventBaseInfo> events) {
        this.events.addAll(events);
        notifyDataSetChanged();
    }

    public void clear() {
        events.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public EventBaseInfo getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cell_event, null);

            holder = new ViewHolder();
            holder.tvInfo = (TextView) convertView.findViewById(R.id.tv_info);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);

            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        final EventBaseInfo event = getItem(position);

        holder.tvTitle.setText(event.getTitle());
        holder.tvInfo.setText(event.getInfo());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ManageEventDetailActivity.class);
                Bundle data = new Bundle();
                data.putSerializable(Config.KEY_EVENT, event);
                i.putExtra(Config.KEY_EVENT, data);
                context.startActivity(i);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView tvTitle;
        TextView tvInfo;
    }

}
