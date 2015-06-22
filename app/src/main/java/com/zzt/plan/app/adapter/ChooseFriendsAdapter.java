package com.zzt.plan.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zzt.plan.app.Config;
import com.zzt.plan.app.R;
import com.zzt.plan.app.entity.UserEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzt on 15-6-22.
 */
public class ChooseFriendsAdapter extends BaseAdapter {

    public static final int TYPE_SELECT = 1;
    public static final int TYPE_REMOVE = 2;
    private Context context;
    private List<UserEntity> users;
    private ImageLoader imageLoader;
    private int type;
    private Map<Integer, Boolean> isSelected;

    public ChooseFriendsAdapter(Context context, List<UserEntity> users, int type) {
        this.context = context;
        this.users = users;
        imageLoader = ImageLoader.getInstance();
        isSelected = new HashMap<>();
        this.type = type;
        init();
    }

    public void init() {
        for (int i = 0; i < users.size(); i++) {
            isSelected.put(i, false);
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cell_select_member, null);
            holder = new ViewHolder();

            holder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            holder.tvNickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            holder.cbItem = (CheckBox) convertView.findViewById(R.id.cb_item);
            holder.btnDelete = (ImageButton) convertView.findViewById(R.id.btn_delete);

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
                        users.remove(position);
                        notifyDataSetChanged();
                    }
                });

                break;
        }

        UserEntity user = getItem(position);
        imageLoader.displayImage(Config.SERVER_URL + user.getAvatarURL(), holder.ivAvatar);
        holder.tvNickname.setText(user.getNickname());


        return convertView;
    }

    public ArrayList<String> getSelectedFriends() {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            if (isSelected.get(i)) {
                result.add(getItem(i).getAccount());
            }
        }
        return result;
    }

    private class ViewHolder {
        ImageView ivAvatar;
        TextView tvNickname;
        CheckBox cbItem;
        ImageButton btnDelete;
    }
}
