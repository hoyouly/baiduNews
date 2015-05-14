package com.hoyouly.baidunews;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoyouly.baidunews.adapter.DateAdapter;
import com.hoyouly.baidunews.dao.DBHelper;
import com.hoyouly.baidunews.domain.Channel;
import com.hoyouly.baidunews.other.Configure;
import com.hoyouly.baidunews.view.ScrollLayout;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

public class MainActivity extends Activity {

	private LinearLayout linear;
	private GridView gridView;
	private ScrollLayout lst_views;
	private TextView tv_page;// int oldPage=1;
	private LinearLayout.LayoutParams param;
	public static final int PAGE_SIZE = 8;
	ArrayList<GridView> gridviews = new ArrayList<GridView>();
	//定义一个集合，里面的元素也是一个集合，每个元素里面存放的是每一页的的所包含的频道
	ArrayList<ArrayList<String>> lists = new ArrayList<ArrayList<String>>();// 全部数据的集合集lists.size()==countpage;
	ArrayList<String> lstDate = new ArrayList<String>();// 每一页的数据

	boolean isClean = false;
	int rockCount = 0;

	private Long mFirstTime = (long) 0;
	private DbUtils dbUtils;
	private List<Channel> channels;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setView();//设置布局
		initView();
	}

	public void setView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//得到频道列表名字，并添加到集合中
		dbUtils=DBHelper.getDBUtils(MainActivity.this);
		channels = getChannels();
		for (Channel channel : channels) {
			lstDate.add(channel.getName());//此时集合中应该有14个元素了
		}
	}

	private List<Channel> getChannels() {
		try {
			return dbUtils.findAll(Channel.class, WhereBuilder.b("parentId", "=", 0));
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void initView() {
		lst_views = (ScrollLayout) findViewById(R.id.views);
		tv_page = (TextView) findViewById(R.id.tv_page);
		Configure.init(MainActivity.this);//初始化一些数据，主要是得到屏幕的宽和高
		param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		param.rightMargin = 50;
		param.leftMargin = 50;

		if (gridView != null) {
			lst_views.removeAllViews();
		}
		initData();//初始化数据
		for (int i = 0; i < Configure.countPages; i++) {
			lst_views.addView(addGridView(i));
		}
		//页面改变的时候，设置右上角的页码，以及旋转动画
		lst_views.setPageListener(new ScrollLayout.PageListener() {
			@Override
			public void page(int page) {
				setCurPage(page);
			}
		});
	}

	public void initData() {
		// ceil 返回大于等于最小表达式的整数
		Configure.countPages = (int) Math.ceil(lstDate.size() / (float) PAGE_SIZE);// 2
		lists = new ArrayList<ArrayList<String>>();
		//往集合中添加数据
		for (int i = 0; i < Configure.countPages; i++) {
			lists.add(new ArrayList<String>());
			for (int j = PAGE_SIZE * i; j < (PAGE_SIZE * (i + 1) > lstDate.size() ? lstDate.size() : PAGE_SIZE * (i + 1)); j++) {
				lists.get(i).add(lstDate.get(j));
			}
		}
		//判断是不是最后一个。
		boolean isLast = true;
		for (int i = lists.get(Configure.countPages - 1).size(); i < PAGE_SIZE; i++) {
			if (isLast) {
				lists.get(Configure.countPages - 1).add(null);
				isLast = false;
			} else
				lists.get(Configure.countPages - 1).add("none");
		}
	}

	/**
	 * 向线性布局中添加一个Gridview
	 * @param i
	 * @return
	 */
	public LinearLayout addGridView(int i) {
		linear = new LinearLayout(MainActivity.this);
		gridView = new GridView(MainActivity.this);
		gridView.setAdapter(new DateAdapter(MainActivity.this, lists.get(i)));
		gridView.setNumColumns(2);//设置两行
		gridView.setHorizontalSpacing(0);//两列的间距
		gridView.setVerticalSpacing(0);//两行之间的间距
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
				Channel channel = channels.get(Configure.currentPage*PAGE_SIZE+arg2);
				Intent intent = new Intent(MainActivity.this, HomeActivity.class);
				intent.putExtra("parentId", channel.getId());
				startActivity(intent);
			}
		});
		//设置选择的动画效果，
		gridView.setSelector(R.anim.mi_laucher_grid_light);
		gridviews.add(gridView);
		linear.addView(gridviews.get(i), param);
		return linear;
	}

	// 设置当前页码动画的效果
	public void setCurPage(final int page) {
		Animation a = AnimationUtils.loadAnimation(MainActivity.this, R.anim.mi_laucher_scale_in);
		a.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				tv_page.setText((page + 1) + "");
				tv_page.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.mi_laucher_scale_out));
			}
		});
		tv_page.startAnimation(a);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 退出功能，连续双击退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Long secondTime = System.currentTimeMillis();
			if (secondTime - mFirstTime > 800) {
				Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
				mFirstTime = secondTime;
				return true;
			} else {
				System.exit(0);
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
