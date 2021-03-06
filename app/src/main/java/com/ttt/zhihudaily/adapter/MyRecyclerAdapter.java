package com.ttt.zhihudaily.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ttt.zhihudaily.R;
import com.ttt.zhihudaily.entity.Title;
import com.ttt.zhihudaily.util.DensityUtil;
import com.tttqiu.library.TUtil;
import com.tttqiu.library.network.RequestQueue;
import com.tttqiu.library.network.Response;
import com.tttqiu.library.request.BitmapRequest;
import com.tttqiu.library.request.Request;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    private List<Title> list;
    private Context context;
    private MyOnItemClickListener myOnItemClickListener;
    private int heightsPX[]=new int[25];
    private RequestQueue mRequestQueue;

    public MyRecyclerAdapter(Context context, List<Title> list) {
        super();
        this.context = context;
        this.list = list;
    }

    public void setMyOnItemClickListener(MyOnItemClickListener myOnItemClickListener) {
        this.myOnItemClickListener = myOnItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        View view;
        if (pref.getBoolean("isNightMode", false)) {
            view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item_night, parent, false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item_day, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.textView.setText(list.get(position).getName());
        // 为imageView设置随机高度
        ViewGroup.LayoutParams params = holder.imageView.getLayoutParams();
        for (int i=0;i<25;i++){
            heightsPX[i]=DensityUtil.dip2px(context,150+(int)(Math.random()*60));
        }
        if (position<25){
            params.height= heightsPX[position];
        }
        holder.imageView.setLayoutParams(params);
//        Glide
//                .with(context)
//                .load(list.get(position).getImage())
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .into(holder.imageView);
        mRequestQueue=TUtil.startRequestQueue(context,RequestQueue.DEFAULT_THREAD_NUM);
        BitmapRequest request=new BitmapRequest(Request.GET,true,true,list.get(position).getImage(),
                new Request.RequestListener<Bitmap>() {
                    @Override
                    public void onComplete(Bitmap result) {
                        holder.imageView.setImageBitmap(result);
                    }

                    @Override
                    public void onError(Response response) {

                    }
                });
        mRequestQueue.addRequest(request);

        final int pos = holder.getAdapterPosition();
        if (myOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myOnItemClickListener.onItemClick(v, pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    myOnItemClickListener.onItemLongClick(v, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;

        MyViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.recycler_title_text);
            imageView = (ImageView) itemView.findViewById(R.id.recycler_title_image);
        }
    }

    public interface MyOnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}
