package com.hoyouly.baidunews;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.hoyouly.baidunews.adapter.NewsPageAdapter;
import com.hoyouly.baidunews.domain.Channel;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.viewpagerindicator.TabPageIndicator;

public class HomeActivity extends FragmentActivity {
	private DbUtils dbUtils;
	private NewsPageAdapter adapter;
	private ImageView back;
	private ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		dbUtils = DbUtils.create(HomeActivity.this, "baidu.db");

		back=(ImageView) findViewById(R.id.iv_back);//返回键的处理
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(HomeActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		Intent intent = getIntent();
		int parentId = intent.getIntExtra("parentId", 0);
		adapter = new NewsPageAdapter(getSupportFragmentManager(), getChannels(parentId));
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);

		// 实例化TabPageIndicator然后设置ViewPager与之关联
		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);

		// 如果我们要对ViewPager设置监听，用indicator设置就行了
		indicator.setOnPageChangeListener(new OnPageChangeListener() {
			private int mPageIndex;
			@Override
			public void onPageSelected(int position) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				if (arg1 == 0 || arg2 == 0) {
					return;
				}
				int index = -1;
				if (mPageIndex == 0) {
					index = mPageIndex + 1;
				} else {
					if (mPageIndex == arg0) {
						index = mPageIndex + 1;
					} else {
						index = arg0;
					}
				}
				adapter.getItem(index).check2UpdateListView();
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				switch (state) {
				case ViewPager.SCROLL_STATE_IDLE:// 换页结束
					int currentIndex = pager.getCurrentItem();
					if (currentIndex != mPageIndex) {
						mPageIndex = currentIndex;
						adapter.getItem(currentIndex).changePage2Refresh(false);
					}
					break;
				case ViewPager.SCROLL_STATE_DRAGGING:// 换页开始
					mPageIndex = pager.getCurrentItem();
					break;
				case ViewPager.SCROLL_STATE_SETTLING:
					break;
				}
			}
		});
		int position = getIntent().getIntExtra("position", 0);
		pager.setCurrentItem(position);
	}

	private List<Channel> getChannels(int parentId) {
		try {
			return dbUtils.findAll(Channel.class, WhereBuilder.b("parentId", "=", parentId));
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}
}
