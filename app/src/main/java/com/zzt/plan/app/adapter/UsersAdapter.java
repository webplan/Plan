package com.zzt.plan.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzt.plan.app.Config;
import com.zzt.plan.app.R;
import com.zzt.plan.app.activity.UserInfoActivity;
import com.zzt.plan.app.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzt on 15-6-6.
 */
public class UsersAdapter extends BaseAdapter {

    private List<UserEntity> users = new ArrayList<UserEntity>();
    private Context context;
    private ImageLoader imageLoader;

    public UsersAdapter(Context context) {
        this.context = context;
        imageLoader = ImageLoader.getInstance();
    }

    public void addAll(List<UserEntity> data) {
        users.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public UserEntity getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cell_friend, null);

            holder = new ViewHolder();
            holder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            holder.tvNickname = (TextView) convertView.findViewById(R.id.tv_nickname);

            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();

        final UserEntity user = getItem(position);

        imageLoader.displayImage(Config.SERVER_URL + user.getAvatarURL(), holder.ivAvatar);
        holder.tvNickname.setText(user.getNickname());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, UserInfoActivity.class);
                i.putExtra(Config.KEY_ACCOUNT, user.getAccount());
                i.putExtra(Config.KEY_NICKNAME, user.getNickname());
                i.putExtra(Config.KEY_AVATAG, user.getAvatarURL());
                context.startActivity(i);
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        ImageView ivAvatar;
        TextView tvNickname;
    }
}
