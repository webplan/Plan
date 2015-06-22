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
import com.zzt.plan.app.activity.EventPlanDetailVoteActivity;
import com.zzt.plan.app.entity.EventPlanEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzt on 15-6-22.
 */
public class InvitedEventsAdapter extends BaseAdapter {
    private Context context;
    private List<EventPlanEntity> invitationList = new ArrayList<>();

    public InvitedEventsAdapter(Context context) {
        this.context = context;
    }

    public void addAll(List<EventPlanEntity> invitationList) {
        this.invitationList.addAll(invitationList);
        notifyDataSetChanged();
    }

    public void clear() {
        invitationList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return invitationList.size();
    }

    @Override
    public EventPlanEntity getItem(int position) {
        return invitationList.get(position);
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
        final EventPlanEntity invitation = getItem(position);

        holder.tvTitle.setText(invitation.getTitle());
        holder.tvInfo.setText(invitation.getInfo());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, EventPlanDetailVoteActivity.class);
                Bundle data = new Bundle();
                data.putSerializable(Config.KEY_EVENT_PLAN, invitation);
                i.putExtra(Config.KEY_EVENT_PLAN, data);
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
