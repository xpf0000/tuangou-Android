package com.hkkj.csrx.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.hkkj.csrx.MapUtils.DrivingRouteOverlay;
import com.hkkj.csrx.MapUtils.OverlayManager;
import com.hkkj.csrx.MapUtils.RouteLineAdapter;
import com.hkkj.csrx.MapUtils.TransitRouteOverlay;
import com.hkkj.csrx.MapUtils.WalkingRouteOverlay;
import com.hkkj.csrx.utils.Constant;
import com.hkkj.server.LocationService;
import com.hkkj.server.location;

import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by admins on 2016/7/27.
 */
public class BaiduMaps extends Activity implements OnGetRoutePlanResultListener, View.OnClickListener {
    MapView mapView;
    BaiduMap baiduMap;
    private LocationService locService;
    RoutePlanSearch mSearch = null;
    TransitRouteResult nowResult = null;
    DrivingRouteResult nowResultd = null;
    RouteLine route = null;
    PlanNode stNode;
    PlanNode enNode;
    TextView drive, busline, walk;
    boolean isLocation = false;
    boolean useDefaultIcon = false;
    OverlayManager routeOverlay = null;
    String city = "洛阳";
    int Type = 0;
    MyTransitDlg myTransitDlg;
    RouteLineAdapter.Type type;
    TextView adress, shopname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baidumaps);
        intview();
    }

    private void intview()
    {


        adress = (TextView) findViewById(R.id.adress);
        shopname = (TextView) findViewById(R.id.shopname);
        adress.setText( Constant.SHOP_ADDRESS);
        shopname.setText( Constant.SHOP_NAME);
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        LatLng latLng = new LatLng(Constant.SHOP_LATITUDE, Constant.SHOP_LONGITUDE);
        enNode = PlanNode.withLocation(latLng);
        locService = ((location) getApplication()).locationService;
        locService.setLocationOption(locService.getOption());
        locService.registerListener(bdLocationListener);
        locService.start();
        mapView = (MapView) findViewById(R.id.mapview);
        drive = (TextView) findViewById(R.id.drive);
        busline = (TextView) findViewById(R.id.busline);
        walk = (TextView) findViewById(R.id.walk);
        drive.setOnClickListener(this);
        busline.setOnClickListener(this);
        walk.setOnClickListener(this);
        baiduMap = mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setMyLocationEnabled(true);
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka);
        OverlayOptions option = new MarkerOptions()
                .position(latLng).icon(bitmap);
        baiduMap.addOverlay(option);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(u);

        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng).zoom(18.0f);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    BDLocationListener bdLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (null != bdLocation && bdLocation.getLocType() != BDLocation.TypeServerError) {
                locService.stop();
                isLocation = true;
                city = bdLocation.getCity();
                MyLocationData locData = new MyLocationData.Builder().accuracy(bdLocation.getRadius())
                        .direction(100).latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();
                baiduMap.setMyLocationData(locData);
                MyLocationConfiguration config = new MyLocationConfiguration(null, true, null);
                baiduMap.setMyLocationConfigeration(config);
                LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                stNode = PlanNode.withLocation(latLng);
//                baiduMap.setMaxAndMinZoomLevel();
            }
        }
    };

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BaiduMaps.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            baiduMap.clear();
            route = result.getRouteLines().get(0);
            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(baiduMap);
            baiduMap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BaiduMaps.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            baiduMap.clear();
            if (result.getRouteLines().size() > 1) {
                nowResult = result;
                if (myTransitDlg != null && myTransitDlg.isShowing()) {
                    type = RouteLineAdapter.Type.TRANSIT_ROUTE;
                    myTransitDlg.mtransitRouteLines = result.getRouteLines();
                    myTransitDlg.setlist(this);
                    myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                        public void onItemClick(int position) {
                            route = nowResult.getRouteLines().get(position);
                            TransitRouteOverlay overlay = new MyTransitRouteOverlay(baiduMap);
                            baiduMap.setOnMarkerClickListener(overlay);
                            routeOverlay = overlay;
                            overlay.setData(nowResult.getRouteLines().get(position));
                            overlay.addToMap();
                            overlay.zoomToSpan();
                        }

                    });
                } else {
                    type = RouteLineAdapter.Type.TRANSIT_ROUTE;
                    myTransitDlg = new MyTransitDlg(BaiduMaps.this,
                            result.getRouteLines(),
                            type);
                    myTransitDlg.getWindow().setWindowAnimations(R.style.PopupAnimation);
                    myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                        public void onItemClick(int position) {
                            route = nowResult.getRouteLines().get(position);
                            TransitRouteOverlay overlay = new MyTransitRouteOverlay(baiduMap);
                            baiduMap.setOnMarkerClickListener(overlay);
                            routeOverlay = overlay;
                            overlay.setData(nowResult.getRouteLines().get(position));
                            overlay.addToMap();
                            overlay.zoomToSpan();
                        }

                    });
                    myTransitDlg.show();
                }


            } else if (result.getRouteLines().size() == 1) {
                // 直接显示
                route = result.getRouteLines().get(0);
                TransitRouteOverlay overlay = new MyTransitRouteOverlay(baiduMap);
                baiduMap.setOnMarkerClickListener(overlay);
                routeOverlay = overlay;
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

            } else {
                Log.d("transitresult", "结果数<0");
                return;
            }
        }

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BaiduMaps.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            baiduMap.clear();
            if (result.getRouteLines().size() > 1) {
                if (myTransitDlg != null && myTransitDlg.isShowing()) {
                    nowResultd = result;
                    type = RouteLineAdapter.Type.DRIVING_ROUTE;
                    myTransitDlg.mtransitRouteLines = result.getRouteLines();
                    myTransitDlg.setlist(this);
                    myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                        public void onItemClick(int position) {
                            route = nowResultd.getRouteLines().get(position);
                            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(baiduMap);
                            baiduMap.setOnMarkerClickListener(overlay);
                            routeOverlay = overlay;
                            overlay.setData(nowResultd.getRouteLines().get(position));
                            overlay.addToMap();
                            overlay.zoomToSpan();
                        }

                    });
                } else {
                    nowResultd = result;
                    type = RouteLineAdapter.Type.DRIVING_ROUTE;
                    myTransitDlg = new MyTransitDlg(BaiduMaps.this,
                            result.getRouteLines(),
                            type);
                    myTransitDlg.getWindow().setWindowAnimations(R.style.PopupAnimation);
                    myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                        public void onItemClick(int position) {
                            route = nowResultd.getRouteLines().get(position);
                            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(baiduMap);
                            baiduMap.setOnMarkerClickListener(overlay);
                            routeOverlay = overlay;
                            overlay.setData(nowResultd.getRouteLines().get(position));
                            overlay.addToMap();
                            overlay.zoomToSpan();
                        }

                    });
                    myTransitDlg.show();
                }


            } else if (result.getRouteLines().size() == 1) {
                if (myTransitDlg != null) {
                    myTransitDlg.dismiss();
                }
                route = result.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(baiduMap);
                routeOverlay = overlay;
                baiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mSearch.destroy();
        mapView.onDestroy();
        locService.unregisterListener(bdLocationListener);
        locService.stop();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drive:
                if (isLocation&&stNode!=null) {
                    mSearch.drivingSearch((new DrivingRoutePlanOption())
                            .from(stNode).to(enNode));
                } else {
                    Toast.makeText(BaiduMaps.this, "还未定位成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.busline:
                if (isLocation&&stNode!=null&&city!=null) {
                    mSearch.transitSearch((new TransitRoutePlanOption())
                            .from(stNode).city(city).to(enNode));
                } else {
                    Toast.makeText(BaiduMaps.this, "还未定位成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.walk:
                if (isLocation&&stNode!=null) {
                    mSearch.walkingSearch((new WalkingRoutePlanOption())
                            .from(stNode).to(enNode));
                } else {
                    Toast.makeText(BaiduMaps.this, "还未定位成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    // 响应DLg中的List item 点击
    interface OnItemInDlgClickListener {
        public void onItemClick(int position);
    }

    class MyTransitDlg extends Dialog {

        private List<? extends RouteLine> mtransitRouteLines;
        private ListView transitRouteList;
        private RouteLineAdapter mTransitAdapter;
        private SegmentedGroup segmentedGroup;
        private RadioButton button1, button2, button3;
        OnItemInDlgClickListener onItemInDlgClickListener;
        TextView postion;

        public MyTransitDlg(Context context, int theme) {
            super(context, theme);
        }

        public MyTransitDlg(Context context, List<? extends RouteLine> transitRouteLines, RouteLineAdapter.Type
                type) {
            this(context, R.style.AppTheme);
            setOwnerActivity((Activity) context);
            mtransitRouteLines = transitRouteLines;
            mTransitAdapter = new RouteLineAdapter(context, mtransitRouteLines, type);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_transit_dialog);
            segmentedGroup = (SegmentedGroup) findViewById(R.id.segmented2);
            button1 = (RadioButton) findViewById(R.id.button1);
            button2 = (RadioButton) findViewById(R.id.button2);
            button3 = (RadioButton) findViewById(R.id.button3);
            postion = (TextView) findViewById(R.id.postion);
            postion.setText(shopname.getText());
            transitRouteList = (ListView) findViewById(R.id.transitList);
            transitRouteList.setAdapter(mTransitAdapter);
            if (type == RouteLineAdapter.Type.DRIVING_ROUTE) {
                button2.setChecked(true);
            } else if (type == RouteLineAdapter.Type.TRANSIT_ROUTE) {
                button1.setChecked(true);
            }
            segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.button1:
                            mtransitRouteLines.clear();
                            mTransitAdapter.notifyDataSetChanged();
                            mSearch.transitSearch((new TransitRoutePlanOption())
                                    .from(stNode).city(city).to(enNode));

                            break;
                        case R.id.button2:
                            mtransitRouteLines.clear();
                            mTransitAdapter.notifyDataSetChanged();
                            mSearch.drivingSearch((new DrivingRoutePlanOption())
                                    .from(stNode).to(enNode));
                            break;
                        case R.id.button3:
                            dismiss();
                            mSearch.walkingSearch((new WalkingRoutePlanOption())
                                    .from(stNode).to(enNode));
                            break;
                    }
                }
            });
            transitRouteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemInDlgClickListener.onItemClick(position);
                    dismiss();

                }
            });
        }

        public void setOnItemInDlgClickLinster(OnItemInDlgClickListener itemListener) {
            onItemInDlgClickListener = itemListener;
        }

        public void setlist(Context context) {
            mTransitAdapter = new RouteLineAdapter(context, mtransitRouteLines, type);
            transitRouteList.setAdapter(mTransitAdapter);
        }

    }
}
