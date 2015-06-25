package com.zzt.plan.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.zzt.plan.app.Config;
import com.zzt.plan.app.R;
import com.zzt.plan.app.adapter.UsersAdapter;
import com.zzt.plan.app.entity.EventEntity;
import com.zzt.plan.app.entity.LocationEntity;
import com.zzt.plan.app.entity.UserEntity;
import com.zzt.plan.app.net.GetPosition;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by zzt on 15-6-23.
 */
public class JoinedEventDetailActivity extends ActionBarActivity {
    EventEntity event;
    private String account;
    private String token;
    private LocationEntity location;
    private List<UserEntity> memberList;

    private TextView tvTitle;
    private TextView tvInfo;
    private TextView tvTime;
    private TextView tvLocation;
    private ImageView btnViewOnMap;
    private ListView lvMembers;
    private UsersAdapter memberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_event_detail);

        account = Config.getCachedAccount(this);
        token = Config.getCachedToken(this);

        Intent i = getIntent();
        event = (EventEntity) i.getBundleExtra(Config.KEY_EVENT).getSerializable(Config.KEY_EVENT);
        location = event.getLocation();
        memberList = event.getMemberList();
        memberAdapter = new UsersAdapter(this);
        memberAdapter.addAll(memberList);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        btnViewOnMap = (ImageView) findViewById(R.id.btn_view_on_map);
        lvMembers = (ListView) findViewById(R.id.lv_members);

        btnViewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOnMap();
            }
        });
        lvMembers.setAdapter(memberAdapter);

        tvTitle.setText(event.getTitle());
        tvInfo.setText(event.getInfo());
        tvTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(event.getTime()));
        tvLocation.setText(event.getLocation().getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirmed_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_members_on_map:
                viewMembersOnMap();
                break;
            case R.id.action_navi:
                navi();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void viewOnMap() {
    }

    private void viewMembersOnMap() {
        new GetPosition(account, token, event.getEventID(), new GetPosition.SuccessCallback() {
            @Override
            public void onSuccess(double[] lat, double[] lon) {
                Intent i = new Intent(JoinedEventDetailActivity.this, LocationActivity.class);
                i.setAction("memberLocation");
                i.putExtra(Config.KEY_LATITUDE, lat);
                i.putExtra(Config.KEY_LONGITUDE, lon);
                startActivity(i);
            }
        }, new GetPosition.FailCallback() {
            @Override
            public void onFail() {
                onFail(Config.RESULT_STATUS_FAIL);
            }

            @Override
            public void onFail(int code) {
                switch (code) {
                    case Config.RESULT_STATUS_FAIL:
                        Toast.makeText(JoinedEventDetailActivity.this, R.string.repeat_failed, Toast.LENGTH_LONG).show();
                        break;
                    case Config.RESULT_STATUS_INVALID_TOKEN:
                        Toast.makeText(JoinedEventDetailActivity.this, R.string.invalid_token_please_login_again, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(JoinedEventDetailActivity.this, LoginActivity.class));
                        break;
                }
            }
        });
    }

    private void navi() {
        Intent i = new Intent(this, LocationActivity.class);
        i.setAction("navi");
        i.putExtra(Config.KEY_LATITUDE, location.getLatitude());
        i.putExtra(Config.KEY_LONGITUDE, location.getLongitude());
        startActivity(i);
    }
}
