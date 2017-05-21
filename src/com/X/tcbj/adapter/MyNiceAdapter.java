package com.X.tcbj.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.X.tcbj.activity.R;
import com.X.tcbj.domain.NiceShop;
import com.X.tcbj.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by admins on 2016/8/10.
 */
public class MyNiceAdapter  extends RecyclerView.Adapter<MyNiceViewHolders> {
    ArrayList<NiceShop.ListBean> array = new ArrayList<NiceShop.ListBean>();
    private Context context;
    private OnItemClickListener mListener;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    com.X.tcbj.utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    public MyNiceAdapter(Context context,ArrayList<NiceShop.ListBean> array) {
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
    public MyNiceViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.niceshop, parent, false);
        MyNiceViewHolders viewHolder = new MyNiceViewHolders(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyNiceViewHolders holder, int position) {
        final String url=array.get(position).getLogoPic();
        holder.content.setText(array.get(position).getName());
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

class MyNiceViewHolders extends RecyclerView.ViewHolder {
    TextView content;
    ImageView lehuiimg;

    public MyNiceViewHolders(View itemView) {
        super(itemView);
        content = (TextView) itemView.findViewById(R.id.shopname);
        lehuiimg = (ImageView) itemView.findViewById(R.id.shopimg);
    }
}
