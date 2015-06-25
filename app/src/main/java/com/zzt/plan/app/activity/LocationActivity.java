package com.zzt.plan.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.zzt.plan.app.Config;
import com.zzt.plan.app.net.UploadPosition;

public class LocationActivity extends Activity {

    private static final int UPDATE_TIME = 60000;
    private static int LOCATION_COUTNS = 0;
    private TextView locationInfoTextView = null;
    private Button mapButton = null;
    private Button chooseButton = null;
    private Button voteButton = null;
    private Button naviButton = null;
    private LocationClient locationClient = null;
    private double currentLatitude = 31.196956; //本人坐标
    private double currentLongitude = 121.599178;
    private LocationMode tempMode = LocationMode.Hight_Accuracy;
    private double atLat[], atLon[], tLat, tLon;
    private long atNum[];
    private String atName[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        locationClient = new LocationClient(this);
        //设置定位条件
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);        //是否打开GPS
        option.setCoorType("bd09ll");       //设置返回值的坐标类型。
        option.setLocationMode(tempMode);//设置定位模式
        option.setScanSpan(UPDATE_TIME);    //设置定时定位的时间间隔。单位毫秒
        locationClient.setLocOption(option);

        //注册位置监听器
        locationClient.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {
                // TODO Auto-generated method stub
                if (location == null) {
                    return;
                }

                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();

                fooUpdateAxis(currentLatitude, currentLongitude); //即时上传本人坐标

            }

        });
        locationClient.start();

        Intent i = getIntent();
        String action = i.getAction();
        if (action.equals("memberLocation")) {
            double[] lat = i.getDoubleArrayExtra(Config.KEY_LATITUDE);
            double[] lon = i.getDoubleArrayExtra(Config.KEY_LONGITUDE);
            memberLocation(lat, lon);
            finish();
        } else if (action.equals("choose")) {
            choose();
        } else if (action.equals("navi")) {
            double tLat = i.getDoubleExtra(Config.KEY_LATITUDE, 0);
            double tLon = i.getDoubleExtra(Config.KEY_LONGITUDE, 0);
            navi(tLat, tLon);
            finish();
        } else if (action.equals("viewVote")) {
            String[] name = i.getStringArrayExtra(Config.KEY_NAME);
            double[] lat = i.getDoubleArrayExtra(Config.KEY_LATITUDE);
            double[] lon = i.getDoubleArrayExtra(Config.KEY_LONGITUDE);
            long[] num = i.getLongArrayExtra(Config.KEY_NUM);
            viewVote(lat, lon, num, name);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationClient != null && locationClient.isStarted()) {
            locationClient.stop();
            locationClient = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 2) {
                setResult(RESULT_OK, data);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void memberLocation(double[] lat, double[] lon) {
        fooGetAttendantAxis();
        double fooLat[] = lat;
        double fooLon[] = lon;
        atLat = fooLat;
        atLon = fooLon;
        Bundle Bundle = new Bundle();
        Bundle.putDoubleArray("atLat", atLat);
        Bundle.putDoubleArray("atLon", atLon);
        Bundle.putLong("mode", 1);
        Intent mapIntent = new Intent();
        mapIntent.setClass(LocationActivity.this, MapActivity.class);
        mapIntent.putExtras(Bundle);
        startActivity(mapIntent);
    }

    private void choose() {
        Bundle Bundle = new Bundle();
        Bundle.putLong("mode", 2);
        Intent mapIntent = new Intent();
        mapIntent.setClass(LocationActivity.this, MapActivity.class);
        mapIntent.putExtras(Bundle);
        startActivityForResult(mapIntent, 2);
    }

    private void viewVote(double[] lat, double[] lon, long[] num, String[] name) {
        fooGetPositionAxis();
        double fooLat[] = lat;
        double fooLon[] = lon;
        long fooNum[] = num;
        String fooName[] = name;
        atLat = fooLat;
        atLon = fooLon;
        atNum = fooNum;
        atName = fooName;
        Bundle Bundle = new Bundle();
        Bundle.putDoubleArray("atLat", atLat);
        Bundle.putDoubleArray("atLon", atLon);
        Bundle.putLongArray("atNum", atNum);
        Bundle.putStringArray("atName", atName);
        Bundle.putLong("mode", 3);
        Intent mapIntent = new Intent();
        mapIntent.setClass(LocationActivity.this, MapActivity.class);
        mapIntent.putExtras(Bundle);
        startActivity(mapIntent);
    }

    private void navi(double tLat, double tLon) {
        fooGetTarget();
        double fooTLat = tLat;
        double fooTLon = tLon;
        tLat = fooTLat;
        tLon = fooTLon;
        Bundle Bundle = new Bundle();
        Bundle.putDouble("myLat", currentLatitude);
        Bundle.putDouble("myLon", currentLongitude);
        Bundle.putDouble("tLat", tLat);
        Bundle.putDouble("tLon", tLon);
        Bundle.putLong("mode", 4);
        Intent mapIntent = new Intent();
        mapIntent.setClass(LocationActivity.this, MapActivity.class);
        mapIntent.putExtras(Bundle);
        startActivity(mapIntent);
    }

    private void fooGetAttendantAxis() {
        //取得数据库存储的参与者的坐标
        //返回存储坐标对的double数组
        return;
    }

    private void fooGetPositionAxis() {
        //取得数据库存储的待投票地点的坐标等乱七八糟的东西
        return;
    }

    private void fooGetTarget() {
        //获得导航目的地坐标的空方法
        return;
    }

    private void fooUpdateAxis(double lat, double lon) {
        new UploadPosition(Config.getCachedAccount(this), Config.getCachedToken(this), lat, lon, new UploadPosition.SuccessCallback() {
            @Override
            public void onSuccess() {

            }
        }, new UploadPosition.FailCallback() {
            @Override
            public void onFail() {

            }

            @Override
            public void onFail(int code) {

            }
        });
    }

}
