package com.baishiming.meta.metaapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * @author bsm
 * @name MetaApp
 * @class name：com.baishiming.meta.metaapp
 * @class describe
 * @time 2019/12/17 17:58
 */
public class PicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PicBean> list;
    private Context context;

    public PicAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LinearLayout.inflate(parent.getContext(), R.layout.item_pic, null);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        //控件设置值
        PicBean person = list.get(position);
        ViewHolder holder = (ViewHolder) viewHolder;
        Glide.with(context).load(person.getThumb()).into(holder.iv_pic);
        Log.e("reload","person.getThumb()=="+person.getThumb());
        holder.tv_pic_title.setText(MyStringUtils.subReplace(person.getTitle()));
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_pic;
        public TextView tv_pic_title;
        public ViewHolder(@NonNull View rootView) {
            super(rootView);
            this.iv_pic = (ImageView) rootView.findViewById(R.id.iv_pic);
            this.tv_pic_title = (TextView) rootView.findViewById(R.id.tv_pic_title);
        }
    }

    public void setData(List<PicBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

}
