package com.hoyouly.baidunews.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hoyouly.baidunews.R;

public class DateAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<String> lstDate;
	private TextView txtAge;

	public DateAdapter(Context mContext, ArrayList<String> list) {
		this.context = mContext;
		lstDate = list;
	}

	@Override
	public int getCount() {
		return lstDate.size();
	}

	@Override
	public Object getItem(int position) {
		return lstDate.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.activity_main_item, null);
		txtAge = (TextView) convertView.findViewById(R.id.txt_userAge);
		if (lstDate.get(position) == null || lstDate.get(position).equals("none")) {
			txtAge.setText("");
			txtAge.setBackgroundDrawable(null);
		} else
			txtAge.setText(lstDate.get(position));
		return convertView;
	}

}
