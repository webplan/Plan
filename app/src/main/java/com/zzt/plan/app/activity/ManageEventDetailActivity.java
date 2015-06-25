package com.zzt.plan.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.zzt.plan.app.Config;
import com.zzt.plan.app.R;
import com.zzt.plan.app.adapter.DecideLocationAdapter;
import com.zzt.plan.app.adapter.DecideTimeAdapter;
import com.zzt.plan.app.entity.EventBaseInfo;
import com.zzt.plan.app.entity.LocationEntity;
import com.zzt.plan.app.entity.LocationResult;
import com.zzt.plan.app.entity.TimeResult;
import com.zzt.plan.app.net.ConfirmPlan;
import com.zzt.plan.app.net.ViewStat;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by zzt on 15-6-23.
 */
public class ManageEventDetailActivity extends ActionBarActivity {
    private String account;
    private String token;

    private EventBaseInfo event;

    private TextView tvTitle;
    private TextView tvInfo;
    private TextView tvDdl;
    private ListView lvTimes;
    private ListView lvLocations;
    private DecideTimeAdapter timeAdapter;
    private DecideLocationAdapter locationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_plan_detail);

        account = Config.getCachedAccount(this);
        token = Config.getCachedToken(this);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        tvDdl = (TextView) findViewById(R.id.tv_ddl);
        lvTimes = (ListView) findViewById(R.id.lv_times);
        lvLocations = (ListView) findViewById(R.id.lv_locations);

        timeAdapter = new DecideTimeAdapter(this);
        locationAdapter = new DecideLocationAdapter(this);

        Intent i = getIntent();
        event = (EventBaseInfo) i.getBundleExtra(Config.KEY_EVENT).getSerializable(Config.KEY_EVENT);
        tvTitle.setText(event.getTitle());
        tvInfo.setText(event.getInfo());

        lvTimes.setAdapter(timeAdapter);
        lvLocations.setAdapter(locationAdapter);

        findViewById(R.id.layout_member).setVisibility(View.GONE);

        viewStat();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm:
                confirm();
        }
        return super.onOptionsItemSelected(item);
    }

    private void viewStat() {
        final ProgressDialog pd = ProgressDialog.show(this, getString(R.string.now_loading), getString(R.string.please_waite));

        new ViewStat(account, token, event.getEventID(), new ViewStat.SuccessCallback() {
            @Override
            public void onSuccess(String title, String info, long ddl, List<TimeResult> timeList, List<LocationResult> locationList) {
                pd.dismiss();
                tvTitle.setText(title);
                tvInfo.setText(info);
                tvDdl.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(ddl));
                timeAdapter.clear();
                timeAdapter.addAll(timeList);
                locationAdapter.clear();
                locationAdapter.addAll(locationList);
            }
        }, new ViewStat.FailCallback() {
            @Override
            public void onFail() {
                onFail(Config.RESULT_STATUS_FAIL);
            }

            @Override
            public void onFail(int failCode) {
                pd.dismiss();
                switch (failCode) {
                    case Config.RESULT_STATUS_FAIL:
                        Toast.makeText(ManageEventDetailActivity.this, R.string.loading_failed, Toast.LENGTH_LONG).show();
                        break;
                    case Config.RESULT_STATUS_INVALID_TOKEN:
                        Toast.makeText(ManageEventDetailActivity.this, R.string.invalid_token_please_login_again, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ManageEventDetailActivity.this, LoginActivity.class));
                        break;
                }
            }
        });
    }

    private void confirm() {
        long decidedTime = timeAdapter.getDecidedTime();
        LocationEntity decidedLocation = locationAdapter.getDecidedLocation();

        new ConfirmPlan(account, token, event.getEventID(), decidedTime, decidedLocation, new ConfirmPlan.SuccessCallback() {
            @Override
            public void onSuccess() {
                finish();
            }
        }, new ConfirmPlan.FailCallback() {
            @Override
            public void onFail() {
                onFail(Config.RESULT_STATUS_FAIL);
            }

            @Override
            public void onFail(int code) {

                switch (code) {
                    case Config.RESULT_STATUS_FAIL:
                        Toast.makeText(ManageEventDetailActivity.this, R.string.repeat_failed, Toast.LENGTH_LONG).show();
                        break;
                    case Config.RESULT_STATUS_INVALID_TOKEN:
                        Toast.makeText(ManageEventDetailActivity.this, R.string.invalid_token_please_login_again, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ManageEventDetailActivity.this, LoginActivity.class));
                        break;
                    case Config.RESULT_STATUS_INVALID_CONFIRM_TIME:
                        Toast.makeText(ManageEventDetailActivity.this, R.string.invalid_confirm_time, Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }
}
