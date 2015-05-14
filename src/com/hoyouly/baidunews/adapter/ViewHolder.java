package com.hoyouly.baidunews.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hoyouly on 15/4/25.
 */
public class ViewHolder {

    private SparseArray<View> views;
    private Context context;
    private ViewGroup parents;
    private View convertView;
    private int position;
    private int layoutId;

    /**
     * 用来返回得到的convetView
     *
     * @return
     */
    public View getConvertView() {
        return convertView;
    }

    public ViewHolder(Context context, ViewGroup parents, int position, int layoutId) {
        this.context = context;
        this.layoutId = layoutId;
        this.views = new SparseArray<View>();
        convertView = LayoutInflater.from(context).inflate(layoutId, parents,false);
        convertView.setTag(this);

    }

    public static ViewHolder get(Context context, View convertView, ViewGroup parents, int position, int layoutId) {
        if (convertView == null) {
            return new ViewHolder(context, parents, position, layoutId);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.position = position;
            return holder;
        }
    }

    /**
     * 通过ViewId 获取控件，返回的是View的子类
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }


    /**
     * 为Textview控件设置值的方法
     * @param viewId TextView的id
     * @param text 设置的值
     * @return 返回一个viewholder对象，从而可以是用链式编程
     */
    public ViewHolder setText(int viewId,String text){
        TextView view= getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为ImageView 控件设置imageResource 的方法
     * @param viewId Iamgeview 的id
     * @param resId  资源id
     * @return 返回一个viewholder对象，从而可以是用链式编程
     */
    public ViewHolder setImageResource(int viewId,int resId){
        ImageView imageView=getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }

    /**
     * 为ImageView 控件设置bitmap 的方法
     * @param viewId Iamgeview 的id
     * @param bm  bitmap 对象
     * @return 返回一个viewholder对象，从而可以是用链式编程
     */
    public ViewHolder setImageBitmap(int viewId,Bitmap bm){
        ImageView imageView=getView(viewId);
        imageView.setImageBitmap(bm);
        return this;
    }

    /**
     * 为ImageView 控件设置bitmap 的方法
     * @param viewId Iamgeview 的id
     * @param uri  路径
     * @return 返回一个viewholder对象，从而可以是用链式编程
     */
    public ViewHolder setImageURI(int viewId,Uri uri){
//        ImageView imageView=getView(viewId);
       // imageView.setImageURI(uri);  TODO 是用一些第三方框架进行网络或者本地加载，一会要改成volley进行加载
        return this;
    }




}
