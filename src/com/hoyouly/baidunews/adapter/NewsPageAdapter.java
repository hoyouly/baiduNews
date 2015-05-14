package com.hoyouly.baidunews.adapter;

import java.util.List;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hoyouly.baidunews.domain.Channel;
import com.hoyouly.baidunews.fragment.CommonFragment;

public class NewsPageAdapter extends FragmentPagerAdapter {
	private CommonFragment[] mFragments;
	private List<Channel> channels;

	public NewsPageAdapter(FragmentManager fm,List<Channel> channels){
		super(fm);
		mFragments = new CommonFragment[channels.size()];
		this.channels=channels;
	}
	
	@Override
	public CommonFragment getItem(int position) {
		CommonFragment fragment = mFragments[position];
		Channel channel = channels.get(position);
		
		if (fragment == null) {
			fragment = CommonFragment.newInstance(position, channel.getRssurl(), channel.getId());
			mFragments[position] = fragment;
		}
		return fragment;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return channels.get(position).getName();
	}

	@Override
	public int getCount() {
		return channels.size();
	}
	
	public void destory() {
		if (mFragments != null) {
			int size = mFragments.length;
			for (int i = 0; i < size; i++) {
				if (mFragments[i] != null) {
					mFragments[i].onDestroy();
					mFragments[i] = null;
				}
			}
		}
	}
}