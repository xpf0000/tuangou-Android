package com.X.tcbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.X.tcbj.adapter.MyCityAdapter;
import com.X.tcbj.adapter.MyCountyAdapter;
import com.X.tcbj.adapter.MyProvinceAdapter;
import com.X.tcbj.domain.Area;
import com.X.tcbj.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/12.
 */
public class MyChooseAddreActivity extends Activity {
    private ImageView imageView;
    private MyProvinceAdapter myProvinceAdapter;
    private MyCityAdapter myCityAdapter;
    private MyCountyAdapter myCountyAdapter;
    private List<Area.Province> provinceList=new ArrayList<Area.Province>();
    private List<Area.Province.Citys> citysList=new ArrayList<Area.Province.Citys>();
    private List<Area.Province.Citys.Countys> countysList=new ArrayList<Area.Province.Citys.Countys>();
    private ListView lv;
    private Area area;
    private int provinceposition,cityposition,countyposition;
    private int state;
    private String provinceName,cityName,countyName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xuanzhesheng);
        initView();
        loadData();
        doEvent();
    }
    public void initView(){
        lv=(ListView)findViewById(R.id.lv);
        imageView=(ImageView)findViewById(R.id.back);
        myProvinceAdapter=new MyProvinceAdapter(this,provinceList);
        myCityAdapter=new MyCityAdapter(this,citysList);
        myCountyAdapter=new MyCountyAdapter(this,countysList);
        lv.setAdapter(myProvinceAdapter);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    public void doEvent(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                if (state==1){
                    if (area.getList().get(i).getCitys()!=null){
                        provinceList.clear();
                        lv.setAdapter(myCityAdapter);
                        provinceposition=i;
                        provinceName=area.getList().get(provinceposition).getName();
                        citysList.addAll(area.getList().get(provinceposition).getCitys());
                        myCityAdapter.notifyDataSetChanged();
                        state=2;
                    }
                }else if (state==2){
                    citysList.clear();
                    lv.setAdapter(myCountyAdapter);
                    cityposition=i;
                    cityName=area.getList().get(provinceposition).getCitys().get(cityposition).getName();
                    countysList.addAll(area.getList().get(provinceposition).getCitys().get(cityposition).getCountys());
                    myCountyAdapter.notifyDataSetChanged();
                    state=3;
                }else if (state==3){
                    countyposition=i;
                    Toast.makeText(MyChooseAddreActivity.this, area.getList().get(provinceposition).getCitys().get(cityposition).getCountys().get(countyposition).getName(), Toast.LENGTH_SHORT).show();
                    String cityCode=area.getList().get(provinceposition).getCitys().get(cityposition).getCountys().get(countyposition).getClassList();
                    countyName=area.getList().get(provinceposition).getCitys().get(cityposition).getCountys().get(countyposition).getName();
                    Intent intent=new Intent();
                    intent.putExtra("cityCode",cityCode);

                    intent.putExtra("area",provinceName+cityName+countyName);
                    setResult(200, intent);
                    MyChooseAddreActivity.this.finish();
                }
            }
        });
    }
    public void loadData(){
        String url= Constant.url+"area/getAllArea";
//        AsyncHttpHelper.getAbsoluteUrl(url, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
//
//            }
//
//            @Override
//            public void onSuccess(int i, Header[] headers, String s) {
//                Log.i("数据",s);
//                area= JSONObject.parseObject(s,Area.class);
//                if (area.getList()!=null){
//                    provinceList.addAll(area.getList());
//                    Log.i("省数据",area.getList().get(1).getName());
//                    myProvinceAdapter.notifyDataSetChanged();
//                    state=1;
//                }
//
//            }
//        });
    }

}
