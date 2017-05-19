package com.hkkj.csrx.test;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.csrx.data.AssetDatabaseOpenHelper;
import com.hkkj.csrx.domain.CityModel;

public class TestData {
	private AssetDatabaseOpenHelper dbHelper;
	private String databaseName="csrx.db";
	SQLiteDatabase sqlDb;
	public ArrayList<Map<String, Object>> tempList;
	
	public TestData(Context context) {
//		System.out.println("创建数据库");
		dbHelper=new AssetDatabaseOpenHelper(context, databaseName);
	}
	
	/**
	 * 同步城市列表到本地数据库中
	 * @param tipsArray
	 * @return
	 */
	public boolean syncCityToLocal(JSONArray cityArray) {
		sqlDb = dbHelper.getWritableDatabase();
//		System.out.println("获取读写权限"+sqlDb);
		ContentValues citysValue;
		JSONObject jsonCity;
		try {
			for (int i = 0; i < cityArray.length(); i++) {
				jsonCity = cityArray.getJSONObject(i);
//				System.out.println(jsonCity);
				citysValue = new ContentValues();
				citysValue.put("cityId",
						cityArray.getJSONObject(i).getString("id"));
				citysValue.put("cityName",
						cityArray.getJSONObject(i).getString("name"));
//				citysValue.put("cityFirstShort",
//						cityArray.getJSONObject(i).getString("s"));
				sqlDb.insert("cityList", null, citysValue);
//				System.out.println("插入成功");
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		dbHelper.close(sqlDb);
		return false;
	}
	
	/**
	 * 获取城市列表数据
	 * @return 城市
	 */
	ArrayList<CityModel> getCityNames() {
		sqlDb = dbHelper.getReadableDatabase();
		ArrayList<CityModel> names = new ArrayList<CityModel>();
		Cursor cursor = sqlDb.rawQuery(
				"SELECT * FROM T_City ORDER BY NameSort", null);
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			CityModel cityModel = new CityModel();
			cityModel.setCityName(cursor.getString(cursor
					.getColumnIndex("CityName")));
			cityModel.setNameSort(cursor.getString(cursor
					.getColumnIndex("NameSort")));
			names.add(cityModel);
		}
		return names;
	}

}
