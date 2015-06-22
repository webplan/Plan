package com.zzt.plan.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import com.zzt.plan.app.Config;
import com.zzt.plan.app.R;
import com.zzt.plan.app.adapter.ChooseFriendsAdapter;
import com.zzt.plan.app.entity.UserEntity;
import com.zzt.plan.app.net.UserInfo;

import java.util.ArrayList;

/**
 * Created by zzt on 15-6-22.
 */
public class SelectFriendsActivity extends ActionBarActivity {

    private ListView lvFriends;
    private String token;
    private String account;
    private ArrayList<UserEntity> friendList = new ArrayList<>();
    private ArrayList<String> unselectedFriendAccountList;
    private ChooseFriendsAdapter adapter = new ChooseFriendsAdapter(this, friendList, ChooseFriendsAdapter.TYPE_SELECT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friends);

        account = Config.getCachedAccount(this);
        token = Config.getCachedToken(this);

        lvFriends = (ListView) findViewById(R.id.lv_friends);
        lvFriends.setAdapter(adapter);

        Intent i = getIntent();
        unselectedFriendAccountList = i.getStringArrayListExtra(Config.KEY_UNSELECTED_FRIEND_ACCOUNT_LIST);

        loadUsersByAccounts(unselectedFriendAccountList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selecet_friend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_event_member:
                Intent data = new Intent();
                data.putStringArrayListExtra(Config.KEY_SELECTED_FRIEND_ACCOUNT_LIST, adapter.getSelectedFriends());
                setResult(1, data);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadUsersByAccounts(ArrayList<String> accountList) {
        for (String userAccount : accountList) {
            new UserInfo(account, token, userAccount, new UserInfo.SuccessCallback() {
                @Override
                public void onSuccess(UserEntity user) {
                    friendList.add(user);
                    adapter.init();
                    adapter.notifyDataSetChanged();
                }
            }, new UserInfo.FailCallback() {
                @Override
                public void onFail() {
                    onFail(Config.RESULT_STATUS_FAIL);
                }

                @Override
                public void onFail(int code) {
                    switch (code) {
                        case Config.RESULT_STATUS_FAIL:
                            Toast.makeText(SelectFriendsActivity.this, R.string.loading_failed, Toast.LENGTH_LONG).show();
                            break;
                        case Config.RESULT_STATUS_INVALID_TOKEN:
                            Toast.makeText(SelectFriendsActivity.this, R.string.invalid_token_please_login_again, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SelectFriendsActivity.this, LoginActivity.class));
                            break;
                    }
                }
            });
        }
    }
}
