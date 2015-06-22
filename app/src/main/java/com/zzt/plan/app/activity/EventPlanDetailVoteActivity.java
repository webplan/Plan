package com.zzt.plan.app.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.zzt.plan.app.Config;
import com.zzt.plan.app.R;
import com.zzt.plan.app.adapter.ChooseLocationsAdapter;
import com.zzt.plan.app.adapter.ChooseTimesAdapter;
import com.zzt.plan.app.adapter.UsersAdapter;
import com.zzt.plan.app.entity.EventPlanEntity;
import com.zzt.plan.app.entity.LocationEntity;
import com.zzt.plan.app.entity.UserEntity;
import com.zzt.plan.app.net.VotePlan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by zzt on 15-6-22.
 */
public class EventPlanDetailVoteActivity extends ActionBarActivity {
    EventPlanEntity eventPlan;
    private String account;
    private String token;
    private List<Calendar> timeList = new ArrayList<>();
    private List<LocationEntity> locationList = new ArrayList<>();
    private List<UserEntity> memberList = new ArrayList<>();

    private TextView tvTitle;
    private TextView tvInfo;
    private TextView tvDdl;
    private ListView lvTimes;
    private ListView lvLocations;
    private ListView lvMembers;
    private ChooseTimesAdapter timesAdapter;
    private ChooseLocationsAdapter locationsAdapter;
    private UsersAdapter memberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_plan_detail);

        account = Config.getCachedAccount(this);
        token = Config.getCachedToken(this);

        Intent i = getIntent();
        eventPlan = (EventPlanEntity) i.getBundleExtra(Config.KEY_EVENT_PLAN).getSerializable(Config.KEY_EVENT_PLAN);
        timeList = eventPlan.getTimeList();
        locationList = eventPlan.getLocationList();
        memberList = eventPlan.getInvitePeopleList();

        timesAdapter = new ChooseTimesAdapter(this, timeList, ChooseTimesAdapter.TYPE_SELECT);
        locationsAdapter = new ChooseLocationsAdapter(this, locationList, ChooseLocationsAdapter.TYPE_SELECT);
        memberAdapter = new UsersAdapter(this);
        memberAdapter.addAll(memberList);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        tvDdl = (TextView) findViewById(R.id.tv_ddl);
        lvTimes = (ListView) findViewById(R.id.lv_times);
        lvLocations = (ListView) findViewById(R.id.lv_locations);
        lvMembers = (ListView) findViewById(R.id.lv_members);

        lvTimes.setAdapter(timesAdapter);
        lvLocations.setAdapter(locationsAdapter);
        lvMembers.setAdapter(memberAdapter);

        tvTitle.setText(eventPlan.getTitle());
        tvInfo.setText(eventPlan.getInfo());
        tvDdl.setText(new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CHINA).format(eventPlan.getDdl()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_vote:
                votePlan();
        }
        return super.onOptionsItemSelected(item);
    }

    private void votePlan() {
        final List<Long> selectedTimeList = new ArrayList<>();
        for (Calendar time : timesAdapter.getSelectedTimeList()) {
            selectedTimeList.add(time.getTimeInMillis());
        }
        final List<LocationEntity> selectedLocationList = locationsAdapter.getSelectedLocationList();
        if (selectedLocationList.isEmpty() || selectedTimeList.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.attention)
                    .setMessage(R.string.you_have_not_selected_time_or_location_continue_to_reject_this_event)
                    .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            acceptPlan(false, selectedTimeList, selectedLocationList);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).show();

        } else
            acceptPlan(true, selectedTimeList, selectedLocationList);
    }

    private void acceptPlan(Boolean accept, List<Long> selectedTimeList, List<LocationEntity> selectedLocationList) {
        final ProgressDialog pd = ProgressDialog.show(this, getString(R.string.now_repeating), getString(R.string.please_waite));

        new VotePlan(account, token, eventPlan.getEventPlanID(), accept, selectedLocationList, selectedTimeList, new VotePlan.SuccessCallback() {
            @Override
            public void onSuccess() {
                pd.dismiss();
                Toast.makeText(EventPlanDetailVoteActivity.this, R.string.repeat_success, Toast.LENGTH_LONG).show();
                finish();
            }
        }, new VotePlan.FailCallback() {
            @Override
            public void onFail() {
                onFail(Config.RESULT_STATUS_FAIL);
            }

            @Override
            public void onFail(int code) {
                pd.dismiss();
                switch (code) {
                    case Config.RESULT_STATUS_FAIL:
                        Toast.makeText(EventPlanDetailVoteActivity.this, R.string.repeat_failed, Toast.LENGTH_LONG).show();
                        break;
                    case Config.RESULT_STATUS_INVALID_TOKEN:
                        Toast.makeText(EventPlanDetailVoteActivity.this, R.string.invalid_token_please_login_again, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(EventPlanDetailVoteActivity.this, LoginActivity.class));
                }
            }
        });
    }
}
