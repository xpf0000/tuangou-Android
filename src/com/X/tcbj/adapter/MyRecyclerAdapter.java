package com.X.tcbj.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.X.tcbj.activity.R;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;



/**
 * Created by zpp on 2015/11/24 0004.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyViewHolders> {
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    private Context context;
    private OnItemClickListener mListener;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    public MyRecyclerAdapter(Context context, ArrayList<HashMap<String, String>> array) {
        this.context = context;
        this.array = array;
        this.context=context;
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
    }
    public interface OnItemClickListener {
        void ItemClickListener(View view, int postion);

        void ItemLongClickListener(View view, int postion);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public MyViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.likeitem, parent, false);
        MyViewHolders viewHolder = new MyViewHolders(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolders holder, int position) {
        final String url=array.get(position).get("PicID");
        holder.content.setText("￥"+array.get(position).get("TruePrice"));
        holder.contents.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.contents.setText("￥" + array.get(position).get("MarketPrice"));
        holder.date.setText(array.get(position).get("Title"));
        String str;
        try {
            if (Integer.parseInt(array.get(position).get("SellCount"))==0){
                holder.txt.setVisibility(View.GONE);
                holder.sell.setVisibility(View.GONE);
            }else if (Integer.parseInt(array.get(position).get("SellCount"))>=10000){
                String SellCount=Integer.parseInt(array.get(position).get("SellCount"))/10000+"";
                str =(Integer.parseInt(array.get(position).get("SellCount"))/10000)+"万件";
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder(str);
                stringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#ffff4040")), 0,SellCount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.sell.setText(stringBuilder);
            }
            else {
                holder.txt.setVisibility(View.VISIBLE);
                holder.sell.setVisibility(View.VISIBLE);
                str=array.get(position).get("SellCount")+"件";
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder(str);
                stringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#ffff4040")), 0, array.get(position).get("SellCount").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.sell.setText(stringBuilder);
            }
        }catch (Exception e){
            holder.txt.setVisibility(View.GONE);
            holder.sell.setVisibility(View.GONE);
            e.printStackTrace();
        }
        options=ImageUtils.setnoOptions();
        ImageLoader.displayImage(url, holder.lehuiimg, options,
                animateFirstListener);
        if (mListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mListener.ItemClickListener(holder.itemView, pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mListener.ItemLongClickListener(holder.itemView, pos);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return array.size();
    }
}

class MyViewHolders extends RecyclerView.ViewHolder {
    TextView content, date, contents,txt,sell;
    ImageView lehuiimg;
    public MyViewHolders(View itemView) {
        super(itemView);
        content = (TextView) itemView.findViewById(R.id.content);
        date = (TextView) itemView.findViewById(R.id.date);
        contents = (TextView) itemView.findViewById(R.id.contents);
        lehuiimg=(ImageView)itemView.findViewById(R.id.lehuiimg);
        txt=(TextView)itemView.findViewById(R.id.txt);
        sell=(TextView)itemView.findViewById(R.id.sell);
    }
}