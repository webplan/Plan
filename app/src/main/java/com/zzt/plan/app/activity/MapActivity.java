package com.zzt.plan.app.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.*;
import com.zzt.plan.app.Config;
import com.zzt.plan.app.R;

public class MapActivity extends Activity {
    MapView mMapView = null;
    BaiduMap mBaiduMap = null;
    RoutePlanSearch mSearch = null;
    LatLng cailunRd1433 = new LatLng(31.196896, 121.600555);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplication());
        setContentView(R.layout.activity_map);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        LatLng initPoint = cailunRd1433;

        MapStatus mMapStatus = new MapStatus.Builder()
                .target(initPoint)
                .zoom(12)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        Bundle Bundle = this.getIntent().getExtras();

        Long mapMode = Bundle.getLong("mode");

        if (mapMode == 1) {

            double mLat[] = Bundle.getDoubleArray("atLat");
            double mLon[] = Bundle.getDoubleArray("atLon");

            LatLng tempPoint = null;
            BitmapDescriptor fooBitmap = BitmapDescriptorFactory.fromResource(R.drawable.foohead);
            OverlayOptions fooOption = null;
            for (int i = 0; i < mLat.length; i++) {
                tempPoint = new LatLng(mLat[i], mLon[i]);
                fooOption = new MarkerOptions()
                        .draggable(false)
                        .position(tempPoint)
                        .icon(fooBitmap);
                mBaiduMap.addOverlay(fooOption);
            }
        }


        if (mapMode == 2) {
            mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
                public void onMapClick(LatLng point) {
                    BitmapDescriptor fooChosen = BitmapDescriptorFactory.fromResource(R.drawable.foomarker);
                    OverlayOptions fooOption = null;
                    Bundle info = new Bundle();
                    info.putLong("mode", 1);
                    fooOption = new MarkerOptions()
                            .position(point)
                            .icon(fooChosen)
                            .extraInfo(info)
                            .draggable(false);
                    mBaiduMap.addOverlay(fooOption);
                }

                public boolean onMapPoiClick(MapPoi poi) {
                    return false;
                }
            });

            mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(final Marker marker) {

                    final LatLng mPoint = marker.getPosition();

                    final Bundle markerInfo = marker.getExtraInfo();
                    Long markerMode = markerInfo.getLong("mode");

                    if (markerMode == 1) {
                        OnClickListener markermenu = new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                switch (which) {
                                    case 0:
                                        markerInfo.putLong("mode", 2);
                                        marker.setExtraInfo(markerInfo);
                                        BitmapDescriptor fooicon = BitmapDescriptorFactory.fromResource(R.drawable.foohead);
                                        marker.setIcon(fooicon);
                                        break;
                                    case 1:
                                        marker.remove();
                                        break;
                                    case 2:
                                        //do nothing
                                        break;
                                }
                            }
                        };
                        String choices[] = {"设为候选地点", "删除该标记", "什么都不干"};
                        AlertDialog dialog = new Builder(MapActivity.this)
                                .setTitle("对该标记做什么？")
                                .setItems(choices, markermenu).create();
                        dialog.show();
                    }


                    if (markerMode == 2) {


                        final EditText commentBox = new EditText(MapActivity.this);
                        commentBox.setText("地点名称或注释");

                        OnClickListener markermenu = new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                switch (which) {
                                    case 0:
                                        String comment = commentBox.getText().toString();
                                        double pLat = mPoint.latitude;
                                        double pLon = mPoint.longitude;

                                        fooChoosePosition(pLat, pLon, comment);

                                        break;
                                    case 1:
                                        marker.remove();
                                        break;
                                    case 2:
                                        //do nothing
                                        break;
                                }
                            }
                        };


                        String choices[] = {"添加注释并设为候选地点", "删除该标记", "什么都不干"};

                        AlertDialog dialog = new Builder(MapActivity.this)
                                .setTitle("请输入该地点的注释")
                                .setItems(choices, markermenu)
                                .setView(commentBox)
                                .create();

                        dialog.show();


                    }


                    return false;
                }

            });

        }

        if (mapMode == 3) {
            double mLat[] = Bundle.getDoubleArray("atLat");
            double mLon[] = Bundle.getDoubleArray("atLon");
            long mNum[] = Bundle.getLongArray("atNum");
            String mName[] = Bundle.getStringArray("atName");
            LatLng tempPoint = null;
            BitmapDescriptor fooBitmap = BitmapDescriptorFactory.fromResource(R.drawable.foopos);
            OverlayOptions fooOption = null;
            Bundle bundle = null;
            for (int i = 0; i < mLat.length; i++) {
                tempPoint = new LatLng(mLat[i], mLon[i]);
                bundle = new Bundle();
                bundle.putString("comment", mName[i]);
                bundle.putLong("voteNum", mNum[i]);
                fooOption = new MarkerOptions()
                        .draggable(false)
                        .position(tempPoint)
                        .extraInfo(bundle)
                        .icon(fooBitmap);
                mBaiduMap.addOverlay(fooOption);
            }

            mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(final Marker marker) {
                    final Bundle bundle = marker.getExtraInfo();
                    String comment = bundle.getString("comment");
                    Long voteNum = bundle.getLong("voteNum");
                    AlertDialog dialog = new Builder(MapActivity.this)
                            .setIcon(android.R.drawable.btn_star)
                            .setTitle(comment)
                            .setMessage("投票人数：" + voteNum.toString() + "人")
                            .create();
                    dialog.show();
                    return false;
                }
            });

        }

        if (mapMode == 4) {
            double tLat = Bundle.getDouble("tLat");
            double tLon = Bundle.getDouble("tLon");
            double mLat = Bundle.getDouble("myLat");
            double mLon = Bundle.getDouble("myLon");
            LatLng mPoint = new LatLng(mLat, mLon);
            LatLng tPoint = new LatLng(tLat, tLon);
            BitmapDescriptor fooIcon = BitmapDescriptorFactory.fromResource(R.drawable.foouser);
            OverlayOptions fooOption = null;
            fooOption = new MarkerOptions()
                    .position(mPoint)
                    .icon(fooIcon)
                    .draggable(false);
            mBaiduMap.addOverlay(fooOption);
            fooIcon = BitmapDescriptorFactory.fromResource(R.drawable.foopos);
            fooOption = new MarkerOptions()
                    .position(tPoint)
                    .icon(fooIcon)
                    .draggable(false);
            mBaiduMap.addOverlay(fooOption);
            //路径规划
            PlanNode mNode = PlanNode.withLocation(mPoint);
            PlanNode tNode = PlanNode.withLocation(tPoint);

            mSearch = RoutePlanSearch.newInstance();

            OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
                public void onGetWalkingRouteResult(WalkingRouteResult result) {
                    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    }
                    if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                        //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                        //result.getSuggestAddrInfo()
                        return;
                    }
                    if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                        WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
                        mBaiduMap.setOnMarkerClickListener(overlay);
                        overlay.setData(result.getRouteLines().get(0));
                        overlay.addToMap();
                        overlay.zoomToSpan();
                    }
                }

                public void onGetTransitRouteResult(TransitRouteResult result) {
                    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    }
                    if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                        //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                        //result.getSuggestAddrInfo()
                        return;
                    }
                    if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                        TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
                        mBaiduMap.setOnMarkerClickListener(overlay);
                        overlay.setData(result.getRouteLines().get(0));
                        overlay.addToMap();
                        overlay.zoomToSpan();
                    }
                }

                public void onGetDrivingRouteResult(DrivingRouteResult result) {
                    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    }
                    if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                        //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                        //result.getSuggestAddrInfo()
                        return;
                    }
                    if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                        DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
                        mBaiduMap.setOnMarkerClickListener(overlay);
                        overlay.setData(result.getRouteLines().get(0));
                        overlay.addToMap();
                        overlay.zoomToSpan();
                    }
                }
            };

            mSearch.setOnGetRoutePlanResultListener(listener);

            mSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(mNode)
                    .to(tNode));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        // mSearch.destroy();
        mMapView.onDestroy();
    }

    private void fooChoosePosition(double lat, double lon, String comment) {
        Intent data = new Intent();
        data.putExtra(Config.KEY_LATITUDE, lat);
        data.putExtra(Config.KEY_LONGITUDE, lon);
        data.putExtra(Config.KEY_NAME, comment);
        setResult(RESULT_OK, data);
        finish();
    }
}