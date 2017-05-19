package com.hkkj.csrx.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusStep;
import com.hkkj.csrx.utils.Constant;

public class Mymap extends Activity implements LocationSource,
		AMapLocationListener, 
		OnMarkerClickListener {
	private MapView mapView;
	private AMap aMap;
	MarkerOptions markerOptions;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	LatLng latLng, mylatLng;
	public static LatLonPoint endPoint = null;
	public static LatLonPoint startPoint = null;
	public static String strStart;
	public static String strEnd;
	Button button, drive, walk,mapback;
	BusStep busStep;
	String city;
	int s = 0;
	TextView shopname,shopadress;
    Constant constant;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// R 需要引用包import com.amapv2.apis.R;
		setContentView(R.layout.map);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 必须要写
		constant=new Constant();
		latLng = new LatLng(constant.SHOP_LATITUDE-0.006400999450000,constant.SHOP_LONGITUDE-0.006368999851000);
//		System.out.println("商家位置"+latLng);
		endPoint = new LatLonPoint(constant.SHOP_LATITUDE-0.006400999450000, constant.SHOP_LONGITUDE-0.006368999851000);
		markerOptions = new MarkerOptions();
		markerOptions.position(latLng);
		init();
		aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
		aMap.addMarker(markerOptions);
		button = (Button) findViewById(R.id.bus);
		drive = (Button) findViewById(R.id.drive);
		walk = (Button) findViewById(R.id.walk);
		mapback=(Button)findViewById(R.id.mapback);
		shopname=(TextView)findViewById(R.id.shopname);
		shopadress=(TextView)findViewById(R.id.shopadress);
		shopname.setText(constant.SHOP_NAME);
		shopadress.setText(constant.SHOP_ADDRESS);
		mapback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Mymap.this.finish();
				
			}
		});
		drive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("linename", "carline");
				intent.putExtra("city", city);
				intent.setClass(Mymap.this, Driveline.class);
				Mymap.this.startActivity(intent);
			}
		});
		walk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("linename", "walkline");
				intent.putExtra("city", city);
				intent.setClass(Mymap.this, Driveline.class);
				Mymap.this.startActivity(intent);
			}
		});
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("linename", "busline");
				intent.putExtra("city", city);
				intent.setClass(Mymap.this, Driveline.class);
				Mymap.this.startActivity(intent);
			}
		});
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		aMap.setOnMarkerClickListener(this);
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
//			System.out.println(city);
			deactivate();
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
	@Override
	public boolean onMarkerClick(Marker marker) {
		if (marker.isInfoWindowShown()) {
			marker.hideInfoWindow();
		} else {
			marker.showInfoWindow();
		}
		return false;
	}

}
