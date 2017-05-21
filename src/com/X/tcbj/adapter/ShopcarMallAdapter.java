package com.X.tcbj.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csrx.data.PreferencesUtils;
import com.X.tcbj.activity.MallInfo;
import com.X.tcbj.activity.R;
import com.X.tcbj.utils.Constant;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2016/1/4.
 */
public class ShopcarMallAdapter extends BaseAdapter {
    ArrayList<HashMap<String, Object>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    Handler handler = new Handler();
    int positions = 0;
    ArrayList<HashMap<String, Object>> exmoney = new ArrayList<>();
    ArrayList<HashMap<String, String>> expressarray = new ArrayList<HashMap<String, String>>();
    public ShopcarMallAdapter(ArrayList<HashMap<String, Object>> abscure_list,
                              Context context, Handler handler, int position) {
        this.abscure_list = abscure_list;
        this.context = context;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        this.handler = handler;
        this.positions = position;
    }

    @Override
    public int getCount() {
        return abscure_list.size();
    }

    @Override
    public Object getItem(int position) {
        return abscure_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final getItemView getItemView = new getItemView();
        setshaopcar();
        convertView = LayoutInflater.from(context).inflate(R.layout.shopcarmall, null);
        getItemView.mallcheck = (CheckBox) convertView.findViewById(R.id.mallcheck);
        getItemView.mallimg = (ImageView) convertView.findViewById(R.id.mallimg);
        getItemView.title = (TextView) convertView.findViewById(R.id.title);
        getItemView.spead = (TextView) convertView.findViewById(R.id.spead);
        getItemView.price = (TextView) convertView.findViewById(R.id.price);
        getItemView.jia = (Button) convertView.findViewById(R.id.jia);
        getItemView.jian = (Button) convertView.findViewById(R.id.jian);
        getItemView.delect = (ImageView) convertView.findViewById(R.id.delect);
        getItemView.malllay = (RelativeLayout) convertView.findViewById(R.id.malllay);
        getItemView.number = (TextView) convertView.findViewById(R.id.number);
        getItemView.title.setText(abscure_list.get(position).get("titles").toString());
        getItemView.number.setText(abscure_list.get(position).get("Num").toString());
        if (abscure_list.get(position).get("spedname").toString().equals("暂无规格")){
            getItemView.spead.setText("");
        }else {
            getItemView.spead.setText(abscure_list.get(position).get("spedname").toString());
        }

        getItemView.malllay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, MallInfo.class);
                intent.putExtra("id", abscure_list.get(position).get("ID").toString());
                context.startActivity(intent);
            }
        });
        String url = abscure_list.get(position).get("Picture").toString();

        if (Integer.parseInt(abscure_list.get(position).get("ischeck").toString()) == 0) {
            getItemView.mallcheck.setChecked(false);
        } else {
            getItemView.mallcheck.setChecked(true);
        }
        getItemView.jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(abscure_list.get(position).get("Num").toString()) + 1;
              if (a>Integer.parseInt(abscure_list.get(position).get("kucuns").toString())){

              }else {
                abscure_list.get(position).put("Num", a);
                getItemView.number.setText(abscure_list.get(position).get("Num").toString());
                Double trueprice = Double.parseDouble(abscure_list.get(position).get("price").toString()) * a;
                abscure_list.get(position).put("trueprice", trueprice);
//                double price = Double.parseDouble(abscure_list.get(position).get("Price").toString());
//                int addnum = Integer.parseInt(abscure_list.get(position).get("AddNum").toString());
//                double addprice = Double.parseDouble(abscure_list.get(position).get("AddPrice").toString());
//                  double esprice = getEsprice(a, price, addnum, addprice);
//                  abscure_list.get(position).put("esprice", esprice);
                newgetEsprice(position,a);
                abscure_list.get(position).put("eslist",exmoney);
                getItemView.price.setText("￥" + trueprice);
                setshaopcar();
                handler.sendEmptyMessage(6);
              }
            }
        });
        getItemView.jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(abscure_list.get(position).get("Num").toString()) - 1 < 1) {

                } else {
                    int a = Integer.parseInt(abscure_list.get(position).get("Num").toString()) - 1;
                    abscure_list.get(position).put("Num", a);
                    getItemView.number.setText(abscure_list.get(position).get("Num").toString());
                    Double trueprice = Double.parseDouble(abscure_list.get(position).get("price").toString()) * a;
                    abscure_list.get(position).put("trueprice", trueprice);
//                    double price = Double.parseDouble(abscure_list.get(position).get("Price").toString());
//                    int addnum = Integer.parseInt(abscure_list.get(position).get("AddNum").toString());
//                    double addprice = Double.parseDouble(abscure_list.get(position).get("AddPrice").toString());
//                   double esprice = getEsprice(a, price, addnum, addprice);
//                   abscure_list.get(position).put("esprice", esprice);
                    newgetEsprice(position,a);
                    abscure_list.get(position).put("eslist",exmoney);
                    getItemView.price.setText("￥" + trueprice);
                    setshaopcar();
                    handler.sendEmptyMessage(6);
                }
            }
        });
        options = ImageUtils.setnoOptions();
        ImageLoader.displayImage(url, getItemView.mallimg, options,
                animateFirstListener);
        double truepricr = Double.parseDouble(abscure_list.get(position).get("trueprice").toString());
        getItemView.price.setText("￥" + truepricr);
        getItemView.mallcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Constant.position = positions;
                Constant.all = false;
                if (isChecked) {
                    abscure_list.get(position).put("ischeck", 1);
                    setshaopcar();
                    if (setFathercheck()) {
                        handler.sendEmptyMessage(3);
                    } else {
                        handler.sendEmptyMessage(5);
                    }
                } else {
                    abscure_list.get(position).put("ischeck", 0);
//                    Constant.array=abscure_list;
                    setshaopcar();
                    handler.sendEmptyMessage(4);

                }
//                Constant.array=abscure_list;
            }
        });
        getItemView.delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delect(position);
                handler.sendEmptyMessage(6);
            }
        });
        return convertView;
    }

    private class getItemView {
        ImageView mallimg, delect;
        TextView title, price, number,spead;
        CheckBox mallcheck;
        Button jia, jian;
        RelativeLayout malllay;
    }

    private boolean setFathercheck() {
        boolean all = true;
        for (int i = 0; i < abscure_list.size(); i++) {
            if (Integer.parseInt(abscure_list.get(i).get("ischeck").toString()) == 0) {
                all = false;
                break;
            }
        }
        return all;
    }

    private void setshaopcar() {
        String json = PreferencesUtils.getString(context, "shopcar");
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("calist");
        JSONObject jsonObject1 = jsonArray.getJSONObject(positions);
        jsonObject1.put("Productlist", abscure_list);
        if (setFathercheck()) {
            jsonObject1.put("ischeck", 1);
        } else {
            jsonObject1.put("ischeck", 0);
        }
        String str = jsonObject.toJSONString();
        PreferencesUtils.putString(context, "shopcar", jsonObject.toString());
    }

    //计算邮费
//    public void getEsprice(int num, double Price, int addnum, double addprice) {
////        double Esprice = 0.0;
////        Esprice = Price + (num - 1) * (addprice / addnum);
////        return Esprice;
//    }
    private void newgetEsprice(int position,int num){
        setExArray(position);
        if (exmoney.size()==0){

        }else {
           if (Integer.parseInt( exmoney.get(0).get("OutTypesID").toString())!=0){
               exmoney.clear();
               expressarray.clear();
               setEsarray(position);
               HashMap<String,Object> hashMap=new HashMap<>();
               Double price,addprice,total = 0.0;
               int addnum=0,prnum=0;
               for (int i=0;i<expressarray.size();i++){
                   hashMap=new HashMap<>();
                   addprice = Double.parseDouble(expressarray.get(i).get("AddPrice"));
                   price = Double.parseDouble(expressarray.get(i).get("Price"));
                   addnum = Integer.parseInt(expressarray.get(i).get("AddNum"));
                   prnum= Integer.parseInt(expressarray.get(i).get("Num"));
                   hashMap.put("OutTypesID",expressarray.get(i).get("OutTypesID"));
                   hashMap.put("OutTypesName",expressarray.get(i).get("OutTypesName"));
                   if (num<prnum){
                       hashMap.put("exprice",price);
                       exmoney.add(hashMap);
                   }else {
                       if (addnum==0){
                           total=price;
                           hashMap.put("exprice",total);
                           exmoney.add(hashMap);
                       }else {
                           int a= (num-prnum)%addnum;
                           if (a==0){
                               total=price+(num-prnum)/addnum*(addprice);
                               hashMap.put("exprice",total);
                               exmoney.add(hashMap);
                           }else {
                               total=price+((num-prnum)/addnum+1)*(addprice);
                               hashMap.put("exprice",total);
                               exmoney.add(hashMap);
                           }
                       }

                   }
               }
           }
            System.out.println("aaa");
        }
    }

    private void delect(int position) {
        abscure_list.remove(position);
        String json = PreferencesUtils.getString(context, "shopcar");
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("calist");
        JSONObject jsonObject1 = jsonArray.getJSONObject(positions);
        if (abscure_list.size() == 0) {
            jsonArray.remove(positions);
            if (jsonArray.size() == 0) {
                PreferencesUtils.putString(context, "shopcar", null);
            } else {
                if (setFathercheck()) {
                    jsonObject1.put("ischeck", 1);
                } else {
                    jsonObject1.put("ischeck", 0);
                }
                String str = jsonObject.toJSONString();
                PreferencesUtils.putString(context, "shopcar", jsonObject.toString());
            }
        } else {
            jsonObject1.put("Productlist", abscure_list);
            if (setFathercheck()) {
                jsonObject1.put("ischeck", 1);
            } else {
                jsonObject1.put("ischeck", 0);
            }
            String str = jsonObject.toJSONString();
            PreferencesUtils.putString(context, "shopcar", jsonObject.toString());
        }
    }

    private void setExArray(int position) {
        exmoney.clear();
        String json = abscure_list.get(position).get("eslist").toString();
        JSONArray jsonArray = JSON.parseArray(json);
        HashMap<String, Object> hashMap = new HashMap<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            hashMap = new HashMap<>();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            hashMap.put("OutTypesID",jsonObject.get("OutTypesID"));
            hashMap.put("OutTypesName",jsonObject.get("OutTypesName"));
            hashMap.put("exprice",jsonObject.get("exprice"));
            exmoney.add(hashMap);
        }
    }
    private void setEsarray(int position){
        String json = abscure_list.get(position).get("expressarray").toString();
        JSONArray jsonArray = JSON.parseArray(json);
        HashMap<String, String> hashMap = new HashMap<>();
        for (int i=0;i<jsonArray.size();i++){
            hashMap = new HashMap<>();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            hashMap.put("AddNum",jsonObject.getString("AddNum"));
            hashMap.put("AddPrice",jsonObject.getString("AddPrice"));
            hashMap.put("DistributionID",jsonObject.getString("DistributionID"));
            hashMap.put("ID", jsonObject.getString("ID"));
            hashMap.put("Num",jsonObject.getString("Num"));
            hashMap.put("OutTypesID", jsonObject.getString("OutTypesID"));
            hashMap.put("OutTypesName", jsonObject.getString("OutTypesName"));
            hashMap.put("Price", jsonObject.getString("Price"));
            hashMap.put("type", jsonObject.getString("type"));
            expressarray.add(hashMap);
        }
    }
}
