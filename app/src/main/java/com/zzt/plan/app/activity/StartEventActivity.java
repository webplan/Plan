package com.zzt.plan.app.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.zzt.plan.app.Config;
import com.zzt.plan.app.R;
import com.zzt.plan.app.adapter.ChooseFriendsAdapter;
import com.zzt.plan.app.adapter.ChooseLocationsAdapter;
import com.zzt.plan.app.adapter.ChooseTimesAdapter;
import com.zzt.plan.app.entity.EventPlanEntity;
import com.zzt.plan.app.entity.LocationEntity;
import com.zzt.plan.app.entity.UserEntity;
import com.zzt.plan.app.net.LoadFriends;
import com.zzt.plan.app.net.StartEvent;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zzt on 15-6-20.
 */
public class StartEventActivity extends ActionBarActivity {
    private String account;
    private String token;
    private EditText etTitle;
    private EditText etInfo;
    private TextView tvDdl;
    private ListView lvTimes;
    private ListView lvLocations;
    private ListView lvMembers;
    private ImageButton btnSetDdl;
    private ImageButton btnAddTime;
    private ImageButton btnAddLocation;
    private ImageButton btnAddMember;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private List<Calendar> timeList = new ArrayList<>();
    private List<LocationEntity> locationList = new ArrayList<>();
    private List<UserEntity> memberList = new ArrayList<>();
    private List<UserEntity> friendList = new ArrayList<>();
    private ArrayList<String> unselectedFriendAccountList = new ArrayList<>();
    private Calendar resultCalendar;
    private Calendar ddlCalendar;
    private ChooseTimesAdapter timesAdapter = new ChooseTimesAdapter(this, timeList, ChooseTimesAdapter.TYPE_REMOVE);
    private ChooseLocationsAdapter locationsAdapter = new ChooseLocationsAdapter(this, locationList, ChooseLocationsAdapter.TYPE_REMOVE);
    private ChooseFriendsAdapter membersAdapter = new ChooseFriendsAdapter(this, memberList, ChooseFriendsAdapter.TYPE_REMOVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_event);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        account = Config.getCachedAccount(this);
        token = Config.getCachedToken(this);

        etTitle = (EditText) findViewById(R.id.et_title);
        etInfo = (EditText) findViewById(R.id.et_info);
        tvDdl = (TextView) findViewById(R.id.tv_ddl);
        lvTimes = (ListView) findViewById(R.id.lv_times);
        lvTimes.setAdapter(timesAdapter);
        lvLocations = (ListView) findViewById(R.id.lv_locations);
        lvLocations.setAdapter(locationsAdapter);
        lvMembers = (ListView) findViewById(R.id.lv_members);
        lvMembers.setAdapter(membersAdapter);
        btnSetDdl = (ImageButton) findViewById(R.id.btn_set_ddl);
        btnAddTime = (ImageButton) findViewById(R.id.btn_add_time);
        btnAddLocation = (ImageButton) findViewById(R.id.btn_add_location);
        btnAddMember = (ImageButton) findViewById(R.id.btn_add_member);

        loadFriends();

        final Calendar nextDayCalendar = Calendar.getInstance();
        nextDayCalendar.add(Calendar.DAY_OF_MONTH, 1);

        ddlCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                resultCalendar = Calendar.getInstance();
                resultCalendar.set(year, monthOfYear, dayOfMonth);
                timePickerDialog.show();
            }
        }, nextDayCalendar.get(Calendar.YEAR), nextDayCalendar.get(Calendar.MONTH), nextDayCalendar.get(Calendar.DAY_OF_MONTH));

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                resultCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                resultCalendar.set(Calendar.MINUTE, minute);
                resultCalendar.set(Calendar.SECOND, 0);
                resultCalendar.set(Calendar.MILLISECOND, 0);
                timeList.add(resultCalendar);
                Collections.sort(timeList, new Comparator<Calendar>() {
                    @Override
                    public int compare(Calendar lhs, Calendar rhs) {
                        return (int) (lhs.getTimeInMillis() - rhs.getTimeInMillis());
                    }
                });
                timesAdapter.notifyDataSetChanged();
            }
        }, 12, 0, false);

        btnSetDdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(StartEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        ddlCalendar.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(StartEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                ddlCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                ddlCalendar.set(Calendar.MINUTE, minute);
                                ddlCalendar.set(Calendar.SECOND, 0);
                                ddlCalendar.set(Calendar.MILLISECOND, 0);

                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CHINA);
                                String ddlStr = format.format(ddlCalendar.getTime());
                                tvDdl.setText(ddlStr);
                            }
                        }, 12, 0, false).show();
                    }
                }, nextDayCalendar.get(Calendar.YEAR), nextDayCalendar.get(Calendar.MONTH), nextDayCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.updateTime(12, 0);
                datePickerDialog.show();
            }
        });

        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationEntity location = chooseLocation();
                locationList.add(location);
                locationsAdapter.notifyDataSetChanged();
            }
        });

        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unselectedFriendAccountList.clear();
                for (UserEntity friend : friendList) {
                    if (!memberList.contains(friend))
                        unselectedFriendAccountList.add(friend.getAccount());
                }
                Intent i = new Intent(StartEventActivity.this, SelectFriendsActivity.class);
                i.putStringArrayListExtra(Config.KEY_UNSELECTED_FRIEND_ACCOUNT_LIST, unselectedFriendAccountList);
                startActivityForResult(i, 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_publish_event:
                postEvent();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void postEvent() {
        if (TextUtils.isEmpty(etTitle.getText())) {
            Toast.makeText(this, R.string.please_enter_event_title, Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(etInfo.getText())) {
            Toast.makeText(this, R.string.please_enter_event_info, Toast.LENGTH_LONG).show();
            return;
        }
        if (timeList.isEmpty()) {
            Toast.makeText(this, R.string.please_add_event_time, Toast.LENGTH_LONG).show();
            return;
        }
        if (locationList.isEmpty()) {
            Toast.makeText(this, R.string.please_add_event_location, Toast.LENGTH_LONG).show();
            return;
        }
        if (memberList.isEmpty()) {
            Toast.makeText(this, R.string.please_select_friends_to_invite, Toast.LENGTH_LONG).show();
            return;
        }

        EventPlanEntity eventPlan = new EventPlanEntity();
        eventPlan.setTitle(etTitle.getText().toString());
        eventPlan.setInfo(etTitle.getText().toString());
        eventPlan.setDdl(ddlCalendar.getTimeInMillis());
        for (Calendar calendar : timeList) {
            eventPlan.addTime(calendar);
        }
        for (LocationEntity location : locationList) {
            eventPlan.addLocation(location);
        }
        for (UserEntity member : memberList) {
            eventPlan.addInvitePeople(member);
        }

        final ProgressDialog pd = ProgressDialog.show(this, getString(R.string.now_posting), getString(R.string.please_waite));

        new StartEvent(account, token, eventPlan, new StartEvent.SuccessCallback() {
            @Override
            public void onSuccess() {
                pd.dismiss();
                Toast.makeText(StartEventActivity.this, R.string.post_success, Toast.LENGTH_LONG).show();
                finish();
            }
        }, new StartEvent.FailCallback() {
            @Override
            public void onFail() {
                onFail(Config.RESULT_STATUS_FAIL);
            }

            @Override
            public void onFail(int code) {
                pd.dismiss();
                switch (code) {
                    case Config.RESULT_STATUS_FAIL:
                        Toast.makeText(StartEventActivity.this, R.string.post_failed, Toast.LENGTH_LONG).show();
                        break;
                    case Config.RESULT_STATUS_INVALID_TOKEN:
                        Toast.makeText(StartEventActivity.this, R.string.invalid_token_please_login_again, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(StartEventActivity.this, LoginActivity.class));
                        break;
                    case Config.RESULT_STATUS_DUPLICATE_KEY:
                        Toast.makeText(StartEventActivity.this, R.string.duplicate_title_and_info, Toast.LENGTH_LONG).show();
                        return;
                }
            }
        });
    }

    private LocationEntity chooseLocation() {
        LocationEntity result = new LocationEntity();
        result.setName("temp");
        result.setLatitude(12.01);
        result.setLongitude(15.02);
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 1) {
            ArrayList<String> selectedFriendAccountList = data.getStringArrayListExtra(Config.KEY_SELECTED_FRIEND_ACCOUNT_LIST);
            for (String selectedFriendAccount : selectedFriendAccountList) {
                memberList.add(getUserByAccount(selectedFriendAccount));
            }
            membersAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadFriends() {
        final ProgressDialog pd = ProgressDialog.show(this, getString(R.string.now_loading), getString(R.string.please_waite));

        new LoadFriends(account, token, new LoadFriends.SuccessCallback() {
            @Override
            public void onSuccess(List<UserEntity> friends) {
                pd.dismiss();
                friendList.clear();
                unselectedFriendAccountList.clear();
                friendList.addAll(friends);
                for (UserEntity user : friendList) {
                    unselectedFriendAccountList.add(user.getAccount());
                }
            }
        }, new LoadFriends.FailCallback() {
            @Override
            public void onFail() {
                onFail(Config.RESULT_STATUS_FAIL);
            }

            @Override
            public void onFail(int failCode) {
                pd.dismiss();
                switch (failCode) {
                    case Config.RESULT_STATUS_FAIL:
                        Toast.makeText(StartEventActivity.this, R.string.loading_failed, Toast.LENGTH_LONG).show();
                        break;
                    case Config.RESULT_STATUS_INVALID_TOKEN:
                        Toast.makeText(StartEventActivity.this, R.string.invalid_token_please_login_again, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(StartEventActivity.this, LoginActivity.class));
                        break;
                }
            }
        });
    }

    private UserEntity getUserByAccount(String account) {
        for (UserEntity friend : friendList) {
            if (friend.getAccount().equals(account)) {
                return friend;
            }
        }
        return null;
    }
}
