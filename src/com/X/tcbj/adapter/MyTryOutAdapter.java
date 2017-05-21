package com.X.tcbj.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.X.tcbj.activity.R;
import com.X.tcbj.domain.MyTryOut;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2015/12/30.
 */
public class MyTryOutAdapter extends BaseAdapter {
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    //异步加载图片的显示参数
    private DisplayImageOptions options = new ImageUtils().setnoOptions();

    private LayoutInflater layoutInflater;
    private List<MyTryOut.TryOut> tryOutList;
    private Context ctx;

    public MyTryOutAdapter(Context ctx,List<MyTryOut.TryOut> tryOutList){
        this.ctx=ctx;
        layoutInflater=LayoutInflater.from(ctx);
        this.tryOutList=tryOutList;

    }
    @Override
    public int getCount() {
        return tryOutList.size();
    }

    @Override
    public Object getItem(int i) {
        return tryOutList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return tryOutList.get(i).getID();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder vh=null;
        if (view ==null){
            vh=new ViewHolder();
            view=layoutInflater.inflate(R.layout.item_mytryout,null);
//            vh.left_day=(TextView)view.findViewById(R.id.tv_leftday);
            vh.state=(Button)view.findViewById(R.id.tv_sbmit);
            vh.tv_sbmits=(Button)view.findViewById(R.id.tv_sbmits);
            vh.img=(ImageView)view.findViewById(R.id.iv);
            vh.title=(TextView)view.findViewById(R.id.tv_title);
            vh.apply_date=(TextView)view.findViewById(R.id.tv_date);
            view.setTag(vh);
        }else {
            vh=(ViewHolder)view.getTag();
        }
        MyTryOut.TryOut tryOut=tryOutList.get(i);

        String time=tryOut.getSurplusTime();
        String times[]=time.split(":");


//        int day=Integer.parseInt(times[0]);
//        int hour=Integer.parseInt(times[1]);
//        if (day>0){
//            vh.left_day.setText("距离结束还有"+day+"天");
//        }else if(hour>0){
//            vh.left_day.setText("距离结束还有" + hour + "小时");
//        }else {
//            vh.left_day.setText("已结束");
//        }
//
         if (tryOutList.get(i).getState()==0){
            vh.state.setBackgroundResource(R.drawable.submit_btn_2);
            vh.state.setTextColor(Color.RED);
            vh.state.setText("等待审核");
             vh.tv_sbmits.setVisibility(View.GONE);
        }else if (tryOutList.get(i).getState()==-1){
             vh.state.setText("申请失败");
             vh.state.setTextColor(Color.RED);
             vh.state.setBackgroundResource(R.drawable.submit_btn_2);
             vh.tv_sbmits.setVisibility(View.GONE);
        }else if(tryOutList.get(i).getState()==1)
         {
             vh.state.setBackgroundResource(R.drawable.submit_btn_2);
             vh.state.setTextColor(Color.RED);
             vh.state.setText("申请成功");
             vh.tv_sbmits.setVisibility(View.VISIBLE);
         }
//
//        vh.state.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (tryOutList.get(i).getState() == 3||tryOutList.get(i).getState()==2) {
//                    Intent intent = new Intent(ctx, tijiaobaogao.class);
//                    intent.putExtra("id", tryOutList.get(i).getFreeID() + "");
//                    ctx.startActivity(intent);
//                } else if (tryOutList.get(i).getState() == 1) {
//                    Intent intent = new Intent(ctx, Mianfei_jieshu.class);
//                    intent.putExtra("ID", tryOutList.get(i).getFreeID() + "");
//                    ctx.startActivity(intent);
//                } else {
//
//                }
//            }
//        });


        imageLoader.displayImage(tryOut.getPicID(), vh.img, options);

        vh.title.setText(tryOut.getTitle());
        String addTime=tryOut.getAddTime();
        int index=addTime.indexOf(" ");

        vh.apply_date.setText(addTime.substring(0,index));

        return view;
    }
    class ViewHolder{
        private TextView left_day;
        private Button state,tv_sbmits;
        private ImageView img;
        private TextView title;
        private TextView apply_date;
    }
}
