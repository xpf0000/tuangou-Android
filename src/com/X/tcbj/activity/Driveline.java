package com.X.tcbj.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkRouteResult;
import com.X.tcbj.activity.RouteSearchPoiDialog.OnListItemClick;
import com.X.tcbj.utils.Constant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Driveline extends Activity implements OnPoiSearchListener,
		OnRouteSearchListener {
	ListView listView, carlist, walklist;
	String city;
	SimpleAdapter adapter, busadAdapter, walkAdapter;
	private BusRouteResult busRouteResult;// 公交模式查询结果
	private DriveRouteResult driveRouteResult;// 驾车模式查询结果
	private WalkRouteResult walkRouteResult;// 步行模式查询结果
	private PoiSearch.Query startSearchQuery;
	private PoiSearch.Query endSearchQuery;
	private int routeType = 1;// 1代表公交模式，2代表驾车模式，3代表步行模式
	private int busMode = RouteSearch.BusDefault;// 公交默认模式
	private int drivingMode = RouteSearch.DrivingDefault;// 驾车默认模式
	private int walkMode = RouteSearch.WalkDefault;// 步行默认模式
	private LatLonPoint endPoint = null;
	private LatLonPoint startPoint = null;
	private String strStart;
	private String strEnd;
	private RouteSearch routeSearch;
	TextView driname;
	List<Map<String, String>> caritem = new ArrayList<Map<String, String>>();
	List<Map<String, String>> busitem = new ArrayList<Map<String, String>>();
	List<Map<String, String>> walkitem = new ArrayList<Map<String, String>>();
	HashMap<String, String> walkhMap;
	HashMap<String, String> drivehMap;
	HashMap<String, String> quanhMap;
	Button car, bus, walk;
	 Constant constant;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.driveline);
		constant=new Constant();
		listView = (ListView) findViewById(R.id.busline);
		carlist = (ListView) findViewById(R.id.carline);
		walklist = (ListView) findViewById(R.id.walkline);
		driname=(TextView)findViewById(R.id.driname);
		car = (Button) findViewById(R.id.car);
		bus = (Button) findViewById(R.id.bus);
		walk = (Button) findViewById(R.id.walk);
		city=getIntent().getStringExtra("city");
		driname.setText(constant.SHOP_NAME);
		routeSearch = new RouteSearch(this);
		routeSearch.setRouteSearchListener(this);
		String linename = getIntent().getStringExtra("linename");
		if (linename.equals("busline")) {
			bus.setBackgroundResource(R.drawable.tbus);
			car.setBackgroundResource(R.drawable.tcard);
			walk.setBackgroundResource(R.drawable.twalkd);
			busline();
		} else if (linename.equals("carline")) {
			car.setBackgroundResource(R.drawable.tcar);
			bus.setBackgroundResource(R.drawable.tbusd);
			walk.setBackgroundResource(R.drawable.twalkd);
			carline();
		} else {
			walk.setBackgroundResource(R.drawable.twalk);
			car.setBackgroundResource(R.drawable.tcard);
			bus.setBackgroundResource(R.drawable.tbusd);
			walkline();
		}
		car.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				car.setBackgroundResource(R.drawable.tcar);
				bus.setBackgroundResource(R.drawable.tbusd);
				walk.setBackgroundResource(R.drawable.twalkd);
				carline();
			}
		});
		bus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				bus.setBackgroundResource(R.drawable.tbus);
				car.setBackgroundResource(R.drawable.tcard);
				walk.setBackgroundResource(R.drawable.twalkd);
				busline();
			}
		});
		walk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				walk.setBackgroundResource(R.drawable.twalk);
				car.setBackgroundResource(R.drawable.tcard);
				bus.setBackgroundResource(R.drawable.tbusd);
				walkline();

			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra("plan", arg2);
				intent.putExtra("lineplan", 1);
				intent.setClass(Driveline.this, BusLine.class);
				Driveline.this.startActivity(intent);
			}
		});
		carlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra("plan", arg2);
				intent.putExtra("lineplan", 2);
				intent.setClass(Driveline.this, BusLine.class);
				Driveline.this.startActivity(intent);

			}
		});
		walklist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra("plan", arg2);
				intent.putExtra("lineplan", 3);
				intent.setClass(Driveline.this, BusLine.class);
				Driveline.this.startActivity(intent);

			}
		});
	}

	public void busline() {
		carlist.setVisibility(View.GONE);
		listView.setVisibility(View.VISIBLE);
		walklist.setVisibility(View.GONE);
		busRoute();
		startSearchResult();// 开始搜终点
	}

	public void carline() {
		caritem.clear();
		carlist.setVisibility(View.VISIBLE);
		listView.setVisibility(View.GONE);
		walklist.setVisibility(View.GONE);
		drivingRoute();
		startSearchResult();// 开始搜终点
	}

	public void walkline() {
		walkitem.clear();
		walklist.setVisibility(View.VISIBLE);
		listView.setVisibility(View.GONE);
		carlist.setVisibility(View.GONE);
		walkRoute();
		startSearchResult();// 开始搜终点
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			caritem.clear();
			walkitem.clear();
			Driveline.this.finish();
		}
		return false;
	}
//-----------------------
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
	//-----------------------------
	public void startSearchResult() {
		strStart = "河南省洛阳市西工区王城大道健康路口";
		if (Mymap.startPoint != null) {
			endSearchResult();
		} else {
			startSearchQuery = new PoiSearch.Query(strStart, "", city); // 第一个参数表示查询关键字，第二参数表示poi搜索类型，第三个参数表示城市区号或者城市名
			startSearchQuery.setPageNum(0);// 设置查询第几页，第一页从0开始
			startSearchQuery.setPageSize(20);// 设置每页返回多少条数据
			PoiSearch poiSearch = new PoiSearch(Driveline.this,
					startSearchQuery);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.searchPOIAsyn();// 异步poi查询
		}
	}

	//
	public void endSearchResult() {
		strEnd = "洛阳市老城保安服务公司";
		endPoint=Mymap.endPoint;
		if ( endPoint!= null) {
			searchRouteResult(Mymap.startPoint, endPoint);
		} else {
			endSearchQuery = new PoiSearch.Query(strEnd, "", city); // 第一个参数表示查询关键字，第二参数表示poi搜索类型，第三个参数表示城市区号或者城市名
			endSearchQuery.setPageNum(0);// 设置查询第几页，第一页从0开始
			endSearchQuery.setPageSize(20);// 设置每页返回多少条数据

			PoiSearch poiSearch = new PoiSearch(Driveline.this,
					endSearchQuery);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.searchPOIAsyn(); // 异步poi查询
		}
	}

	public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
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
//-----------------------	
	@Override
	public void onBusRouteSearched(BusRouteResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				busRouteResult = result;
				BusPath busPath = busRouteResult.getPaths().get(0);
				busitem = new ArrayList<Map<String, String>>();
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
									.getBusStationName().toString() + "上车");
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
									.getBusStationName().toString() + "下");
					quanhMap.put("Duration", "全程" + Integer.toString(a) + "分钟");
					busitem.add(quanhMap);
				}
				adapter = new SimpleAdapter(Driveline.this, busitem,
						R.layout.busplan_txt, new String[] { "BusLine",
								"DepartureBusStation", "PassStationNum",
								"ArrivalBusStation", "Duration" }, new int[] {
								R.id.bussastion, R.id.startbussastion,
								R.id.numberstation, R.id.endstation, R.id.time });
				listView.setAdapter(adapter);
			} else {
				Toast.makeText(Driveline.this, "无搜索结果", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (rCode == 27) {
			Toast.makeText(Driveline.this, "搜索失败,请检查网络连接", Toast.LENGTH_SHORT)
					.show();
		} else if (rCode == 32) {
			Toast.makeText(Driveline.this, "key验证无效", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(Driveline.this, "未知错误", Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				driveRouteResult = result;
				DrivePath drivePath = driveRouteResult.getPaths().get(0);
				String lineStr = "我的位置";
				for (int i = 0; i < drivePath.getSteps().size(); i++) {
					DriveStep step = driveRouteResult.getPaths().get(0)
							.getSteps().get(i);
					String roadStr = step.getRoad();
					if (roadStr != null && !roadStr.equals("")
							&& roadStr.length() > 0) {
						lineStr += "-" + step.getRoad();
					}
				}
//				System.out.println(lineStr);
				drivehMap = new HashMap<String, String>();
				drivehMap.put("driveline", lineStr);
				caritem.add(drivehMap);
				adapter = new SimpleAdapter(Driveline.this, caritem,
						R.layout.driveline_txt, new String[] { "driveline" },
						new int[] { R.id.mydriveline });
				carlist.setAdapter(adapter);

			} else {
				Toast.makeText(Driveline.this, "无搜索结果", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (rCode == 27) {
			Toast.makeText(Driveline.this, "搜索失败,请检查网络连接", Toast.LENGTH_SHORT)
					.show();
		} else if (rCode == 32) {
			Toast.makeText(Driveline.this, "key验证无效", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(Driveline.this, "未知错误", Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				walkRouteResult = result;
				String walkline = null;
				WalkPath walkPath = walkRouteResult.getPaths().get(0);
				List list = new ArrayList();
				String walkliner = "我的位置";
				for (int i = 0; i < walkPath.getSteps().size(); i++) {
					String walkstr = walkPath.getSteps().get(i).getRoad();
					if (walkstr != null && !walkstr.equals("")
							&& walkstr.length() > 0) {
						list.add(walkstr);
					}

				}
				String walk = "我的位置" + "-" + list.get(0) + "-"
						+ list.get(list.size() - 1);
				walkhMap = new HashMap<String, String>();
				walkhMap.put("walkline", walk);
				walkitem.add(walkhMap);
				adapter = new SimpleAdapter(Driveline.this, walkitem,
						R.layout.driveline_txt, new String[] { "walkline" },
						new int[] { R.id.mydriveline });
				walklist.setAdapter(adapter);
			} else {
				Toast.makeText(Driveline.this, "无搜索结果", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (rCode == 27) {
			Toast.makeText(Driveline.this, "搜索失败,请检查网络连接", Toast.LENGTH_SHORT)
					.show();
		} else if (rCode == 32) {
			Toast.makeText(Driveline.this, "key验证无效", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(Driveline.this, "未知错误", Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		if (rCode == 0) {// 返回成功
			if (result != null && result.getQuery() != null
					&& result.getPois() != null && result.getPois().size() > 0) {// 搜索poi的结果
				if (result.getQuery().equals(startSearchQuery)) {
					List<PoiItem> poiItems = result.getPois();// 取得poiitem数据
					RouteSearchPoiDialog dialog = new RouteSearchPoiDialog(
							Driveline.this, poiItems);
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
							Driveline.this, poiItems);
					dialog.setTitle("您要找的终点是:");
					dialog.show();
					dialog.setOnListClickListener(new OnListItemClick() {
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
				Toast.makeText(Driveline.this, "无搜索结果", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (rCode == 27) {
			Toast.makeText(Driveline.this, "搜索失败,请检查网络连接", Toast.LENGTH_SHORT)
					.show();
		} else if (rCode == 32) {
			Toast.makeText(Driveline.this, "key验证无效", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(Driveline.this, "未知错误", Toast.LENGTH_SHORT).show();
		}

	}
}
