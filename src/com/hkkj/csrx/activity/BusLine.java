package com.hkkj.csrx.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.BusRouteOverlay;
import com.amap.api.maps2d.overlay.DrivingRouteOverlay;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkRouteResult;
import com.hkkj.csrx.activity.RouteSearchPoiDialog.OnListItemClick;
import com.hkkj.csrx.utils.Constant;

public class BusLine extends Activity implements LocationSource,
		AMapLocationListener, OnPoiSearchListener, OnRouteSearchListener {
	private MapView mapView;
	private AMap aMap;
	MarkerOptions markerOptions;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	LatLng latLng, mylatLng;
	private ProgressDialog progDialog = null;// 搜索时进度条
	private BusRouteResult busRouteResult;// 公交模式查询结果
	private DriveRouteResult driveRouteResult;// 驾车模式查询结果
	private WalkRouteResult walkRouteResult;// 步行模式查询结果
	private PoiSearch.Query startSearchQuery;
	private PoiSearch.Query endSearchQuery;
	private LatLonPoint endPoint = null;
	private LatLonPoint startPoint = null;
	private String strStart;
	private String strEnd;
	private int routeType = 1;// 1代表公交模式，2代表驾车模式，3代表步行模式
	private RouteSearch routeSearch;
	private int busMode = RouteSearch.BusDefault;// 公交默认模式
	private int drivingMode = RouteSearch.DrivingDefault;// 驾车默认模式
	private int walkMode = RouteSearch.WalkDefault;// 步行默认模式
	BusStep busStep;
	String city;
	int s = 0;
	Button mapback;
	int plan,lineplan;
	LinearLayout layout, shop;
	public static List<Map<String, String>> item1 = new ArrayList<Map<String, String>>();
	HashMap<String, String> quanhMap;
	 Constant constant;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// R 需要引用包import com.amapv2.apis.R;
		setContentView(R.layout.map);
		layout = (LinearLayout) findViewById(R.id.myplan);
		layout.setVisibility(View.GONE);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 必须要写
		constant=new Constant();
		latLng = new LatLng(constant.SHOP_LATITUDE-0.006400999450000,constant.SHOP_LONGITUDE-0.006368999851000);
		endPoint = new LatLonPoint(constant.SHOP_LATITUDE-0.006400999450000, constant.SHOP_LONGITUDE-0.006368999851000);
		markerOptions = new MarkerOptions();
		shop = (LinearLayout) findViewById(R.id.shopmark);
		mapback=(Button)findViewById(R.id.mapback);
		mapback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
		markerOptions.position(latLng);
		shop.setVisibility(View.GONE);
		init();
		plan = getIntent().getIntExtra("plan", 0);
		aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
		aMap.addMarker(markerOptions);

	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		routeSearch = new RouteSearch(this);
		routeSearch.setRouteSearchListener(this);
	}

	private void setUpMap() {
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.ic_launcher));
		myLocationStyle.strokeColor(Color.BLACK);
		myLocationStyle.strokeWidth(5);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	private void busRoute() {
		routeType = 1;// 标识为公交模式
		busMode = RouteSearch.BusDefault;
	}
	/**
	 * 选择驾车模式
	 */
	private void drivingRoute() {
		routeType = 2;// 标识为驾车模式
		drivingMode = RouteSearch.DrivingSaveMoney;
	}

	/**
	 * 选择步行模式
	 */
	private void walkRoute() {
		routeType = 3;// 标识为步行模式
		walkMode = RouteSearch.WalkMultipath;
	}

	// ---------------------
	@Override
	public void onLocationChanged(Location arg0) {

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation arg0) {
		if (mListener != null && arg0 != null) {
			mListener.onLocationChanged(arg0);// 显示系统小蓝点
			mylatLng = new LatLng(arg0.getLatitude(), arg0.getLongitude());
			startPoint = new LatLonPoint(arg0.getLatitude(),
					arg0.getLongitude());
//			System.out.println(mylatLng);
			float bearing = aMap.getCameraPosition().bearing;
			aMap.setMyLocationRotateAngle(bearing);// 设置小蓝点旋转角度
			aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
			city = arg0.getCity();
			deactivate();
			lineplan = getIntent().getIntExtra("lineplan", 0);
			if(lineplan==1){
				busRoute();
				startSearchResult();// 开始搜终点
			}else if(lineplan==2){
				drivingRoute();
				startSearchResult();// 开始搜终点
			}else{
				walkRoute();
				startSearchResult();// 开始搜终点
			}
			
		}
	}

	@Override
	public void activate(OnLocationChangedListener arg0) {
		mListener = arg0;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}

	}

	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	//
	public void startSearchResult() {
		strStart = "河南省洛阳市西工区王城大道健康路口";
		if (startPoint != null) {
			endSearchResult();
		} else {
			showProgressDialog();
			startSearchQuery = new PoiSearch.Query(strStart, "", city); // 第一个参数表示查询关键字，第二参数表示poi搜索类型，第三个参数表示城市区号或者城市名
			startSearchQuery.setPageNum(0);// 设置查询第几页，第一页从0开始
			startSearchQuery.setPageSize(20);// 设置每页返回多少条数据
			PoiSearch poiSearch = new PoiSearch(BusLine.this, startSearchQuery);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.searchPOIAsyn();// 异步poi查询
		}
	}

	//
	public void endSearchResult() {
		strEnd = "洛阳市老城保安服务公司";
		if (endPoint != null) {
			searchRouteResult(startPoint, endPoint);
		} else {
			showProgressDialog();
			endSearchQuery = new PoiSearch.Query(strEnd, "", city); // 第一个参数表示查询关键字，第二参数表示poi搜索类型，第三个参数表示城市区号或者城市名
			endSearchQuery.setPageNum(0);// 设置查询第几页，第一页从0开始
			endSearchQuery.setPageSize(20);// 设置每页返回多少条数据

			PoiSearch poiSearch = new PoiSearch(BusLine.this, endSearchQuery);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.searchPOIAsyn(); // 异步poi查询
		}
	}

	public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
		showProgressDialog();
		final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
				startPoint, endPoint);
		if (routeType == 1) {// 公交路径规划
			BusRouteQuery query = new BusRouteQuery(fromAndTo, busMode, city, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
			routeSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
		} else if (routeType == 2) {// 驾车路径规划
			DriveRouteQuery query = new DriveRouteQuery(fromAndTo, drivingMode,
					null, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
			routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
		} else if (routeType == 3) {// 步行路径规划
			WalkRouteQuery query = new WalkRouteQuery(fromAndTo, walkMode);
			routeSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
		}
	}

	//
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在搜索");
		progDialog.show();
	}

	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	@Override
	public void onBusRouteSearched(BusRouteResult result, int rCode) {
		dissmissProgressDialog();
		if (rCode == 0) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				busRouteResult = result;
				BusPath busPath = busRouteResult.getPaths().get(plan);
				item1 = new ArrayList<Map<String, String>>();
				s = result.getPaths().size();
				for (int i = 0; i < result.getPaths().size(); i++) {
					int a = (int) busRouteResult.getPaths().get(i).getSteps()
							.get(0).getBusLine().getDuration() / 50;
					quanhMap = new HashMap<String, String>();
					quanhMap.put("BusLine", busRouteResult.getPaths().get(i)
							.getSteps().get(0).getBusLine().getBusLineName());
					quanhMap.put("DepartureBusStation", "从"
							+ busRouteResult.getPaths().get(i).getSteps()
									.get(0).getBusLine()
									.getDepartureBusStation()
									.getBusStationName().toString() + "站上车");
					quanhMap.put(
							"PassStationNum",
							"经过"
									+ Integer.toString(busRouteResult
											.getPaths().get(i).getSteps()
											.get(0).getBusLine()
											.getPassStationNum()) + "站");
					quanhMap.put("ArrivalBusStation", "到"
							+ busRouteResult.getPaths().get(i).getSteps()
									.get(0).getBusLine().getArrivalBusStation()
									.getBusStationName().toString() + "下车");
					quanhMap.put("Duration", "全程" + Integer.toString(a) + "分钟");
					item1.add(quanhMap);
				}
				aMap.clear();// 清理地图上的所有覆盖物
				BusRouteOverlay routeOverlay = new BusRouteOverlay(this, aMap,
						busPath, busRouteResult.getStartPos(),
						busRouteResult.getTargetPos());
				routeOverlay.removeFromMap();
				routeOverlay.addToMap();
				routeOverlay.zoomToSpan();
			} else {
				Toast.makeText(BusLine.this, "无搜索结果", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (rCode == 27) {
			Toast.makeText(BusLine.this, "搜索失败,请检查网络连接", Toast.LENGTH_SHORT)
					.show();
		} else if (rCode == 32) {
			Toast.makeText(BusLine.this, "key验证无效", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(BusLine.this, "未知错误", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
		dissmissProgressDialog();
		if (rCode == 0) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				driveRouteResult = result;
				DrivePath drivePath = driveRouteResult.getPaths().get(0);
				aMap.clear();// 清理地图上的所有覆盖物
				DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
						this, aMap, drivePath, driveRouteResult.getStartPos(),
						driveRouteResult.getTargetPos());
				drivingRouteOverlay.removeFromMap();
				drivingRouteOverlay.addToMap();
				drivingRouteOverlay.zoomToSpan();
			} else {
				Toast.makeText(BusLine.this, "无搜索结果", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (rCode == 27) {
			Toast.makeText(BusLine.this, "搜索失败,请检查网络连接", Toast.LENGTH_SHORT)
					.show();
		} else if (rCode == 32) {
			Toast.makeText(BusLine.this, "key验证无效", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(BusLine.this, "未知错误", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
		dissmissProgressDialog();
		if (rCode == 0) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				walkRouteResult = result;
				WalkPath walkPath = walkRouteResult.getPaths().get(0);
				aMap.clear();// 清理地图上的所有覆盖物
				WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(this,
						aMap, walkPath, walkRouteResult.getStartPos(),
						walkRouteResult.getTargetPos());
				walkRouteOverlay.removeFromMap();
				walkRouteOverlay.addToMap();
				walkRouteOverlay.zoomToSpan();
			} else {
				Toast.makeText(BusLine.this, "无搜索结果", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (rCode == 27) {
			Toast.makeText(BusLine.this, "搜索失败,请检查网络连接", Toast.LENGTH_SHORT)
					.show();
		} else if (rCode == 32) {
			Toast.makeText(BusLine.this, "key验证无效", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(BusLine.this, "未知错误", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		dissmissProgressDialog();
		if (rCode == 0) {// 返回成功
			if (result != null && result.getQuery() != null
					&& result.getPois() != null && result.getPois().size() > 0) {// 搜索poi的结果
				if (result.getQuery().equals(startSearchQuery)) {
					List<PoiItem> poiItems = result.getPois();// 取得poiitem数据
					RouteSearchPoiDialog dialog = new RouteSearchPoiDialog(
							BusLine.this, poiItems);
					dialog.setTitle("您要找的起点是:");
					dialog.show();
					dialog.setOnListClickListener(new OnListItemClick() {
						@Override
						public void onListItemClick(
								RouteSearchPoiDialog dialog,
								PoiItem startpoiItem) {
							startPoint = startpoiItem.getLatLonPoint();
							strStart = startpoiItem.getTitle();
							endSearchResult();// 开始搜终点
						}

					});
				} else if (result.getQuery().equals(endSearchQuery)) {
					List<PoiItem> poiItems = result.getPois();// 取得poiitem数据
					RouteSearchPoiDialog dialog = new RouteSearchPoiDialog(
							BusLine.this, poiItems);
					dialog.setTitle("您要找的终点是:");
					dialog.show();
					dialog.setOnListClickListener(new com.hkkj.csrx.activity.RouteSearchPoiDialog.OnListItemClick() {
						@Override
						public void onListItemClick(
								RouteSearchPoiDialog dialog, PoiItem endpoiItem) {
							endPoint = endpoiItem.getLatLonPoint();
							strEnd = endpoiItem.getTitle();
							searchRouteResult(startPoint, endPoint);// 进行路径规划搜索
						}

					});
				}
			} else {
				Toast.makeText(BusLine.this, "无搜索结果", Toast.LENGTH_SHORT)
						.show();
			}

		} else if (rCode == 27) {
			Toast.makeText(BusLine.this, "搜索失败", Toast.LENGTH_SHORT).show();
		} else if (rCode == 32) {
			Toast.makeText(BusLine.this, "key认证失败", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(BusLine.this, "未知错误", Toast.LENGTH_SHORT).show();
		}

	}

}
