package com.hoyouly.baidunews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoyouly on 15/4/25.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context context;
    protected List<T> datas;
    protected int loyoutId;
    protected LayoutInflater inflater;

    public List<T> getDatas() {
        return datas;
    }

    protected CommonAdapter(Context context, List<T> datas, int layoutId) {
        this.context = context;
        this.datas = datas;
        this.loyoutId = layoutId;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public T getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder(context, viewGroup, i, loyoutId);
        convert(holder, getItem(i));
        return holder.getConvertView();
    }

    protected abstract void convert(ViewHolder holder, T t);

    /**
     * 设置数据，这个可以用于上拉刷新数据
     * @param newDatas
     */
    public void setData(List<T> newDatas) {
        if (datas == null) {
            datas = new ArrayList<T>();
        } else {
            datas.clear();
        }
        datas.addAll(newDatas);
        notifyDataSetChanged();
    }


    /**
     * 添加数据，这个可以用于上拉加载数据
     * @param newDatas
     */
    public void addDatas(List<T> newDatas){
        if(newDatas==null||newDatas.size()==0){
            return;
        }
        if(datas==null){
            datas=new ArrayList<T>();
        }
        datas.addAll(newDatas);
        notifyDataSetChanged();
    }

}
