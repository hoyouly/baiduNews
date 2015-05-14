package com.hoyouly.baidunews.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hoyouly.baidunews.BrowserActivity;
import com.hoyouly.baidunews.R;
import com.hoyouly.baidunews.adapter.NewsListAdapter;
import com.hoyouly.baidunews.dao.NewsDao;
import com.hoyouly.baidunews.dao.impl.NewsDaoImpl;
import com.hoyouly.baidunews.domain.News;
import com.hoyouly.baidunews.util.HttpClientUtil;
import com.hoyouly.baidunews.util.SharedPreferencesHelper;
import com.hoyouly.baidunews.util.XmlUtils;
import com.hoyouly.baidunews.view.MyListView;
import com.hoyouly.baidunews.view.MyListView.MyListViewListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommonFragment extends Fragment implements MyListViewListener, Runnable {
	private String PREFERENCES_FILE_NAME = "channel_update_times";
	private static final long DIFF_UPDATE_TIME = 30 * 1000 * 60;// 滑动界面更新间隔30分钟
	private static final long DIFF_LIST_REFRESH_UPDATE_TIME = 1000 * 60;// 列表下拉更新时间间隔1分钟
	private static final int DATE_CHANGE_NOTIFY = 1;
	private static final int LIST_VIEW_TO_SHOW_REFRESH = 2;
	private static final int LIST_VIEW_TO_SHOW_UPDATE_TIME = 3;
	private static final int DATE_CHANGE_FAIL = 4;
	private static final int LIST_VIEW_TO_STOP_REFRESH = 5;

	private View parentView;
	private MyListView listView;
	private NewsListAdapter adapter;
	protected String channerlUrl;
	protected int channelId;
	protected int position;
	private Boolean isRefresh = false;// 是否在刷新
	private Handler handler;
	private SharedPreferencesHelper sphelper;//持久化的工具类，
	private long updateDateTime;// 最近一次的更新时间
	private SimpleDateFormat mDateFormat;
	private boolean canLoadMore;// 是否还有足够的数据加载
	private boolean isFristCount;// 是否是第一次

	private NewsDao dao;
    private RequestQueue mQueue;

	/**
	 * @param position   Fragment的索引
	 * @param channelUrl  该Fragment对应的频道的uri
	 * @param channelId   该Fragment对应的频道id
	 * @return
	 */
	public static CommonFragment newInstance(int position, String channelUrl, int channelId) {
		CommonFragment fragment = new CommonFragment();
		fragment.channerlUrl = channelUrl;
		fragment.channelId = channelId;
		fragment.position = position;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mDateFormat == null) {//创建格式化日期的类
			mDateFormat = new SimpleDateFormat("MM月dd日");
		}

        mQueue = Volley.newRequestQueue(getActivity());

        //得到本地持久化对象
		sphelper = SharedPreferencesHelper.newInstance(getActivity());
		
		if (handler == null) {
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case DATE_CHANGE_NOTIFY:
						stopListRefresh();
						adapter.notifyDataSetChanged(listView, true);
						updateTextTime();
						break;
					case LIST_VIEW_TO_SHOW_REFRESH:
						listView.startRefresh(true);
						break;
					case LIST_VIEW_TO_SHOW_UPDATE_TIME:
						updateTextTime();
						break;
					case DATE_CHANGE_FAIL:
						if(!isFristCount&&dao.selectByChannel(channelId, 0, 10).size()<=0){
							Toast.makeText(getActivity(), "加载失败，请检查网络！", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getActivity(), "更新数据失败！", Toast.LENGTH_SHORT).show();
						}
						listView.stopRefresh();
						break;
					case LIST_VIEW_TO_STOP_REFRESH:
						stopListRefresh();
						adapter.notifyDataSetChanged(listView);
						break;
					default:
						break;
					}
				}
			};
		}
		if ((savedInstanceState != null)) {
			if (savedInstanceState.containsKey("channerlUrl")) {
				channerlUrl = savedInstanceState.getString("channerlUrl");
			}
			if (savedInstanceState.containsKey("channelId")) {
				channelId = savedInstanceState.getInt("channelId");
			}
		}
		dao = new NewsDaoImpl(getActivity());
	}

	/**
	 * onSaveInstanceState() 和 onRestoreInstanceState()并不是生命周期方法们不同于 onCreate()、onPause()等生命周期方法，它们并不一定会被触发。
	 * 当应用遇到意外情况（如：内存不足、用户直接按Home键）由系统销毁一个Activity时，onSaveInstanceState() 会被调用。
	 * 但是当用户主动去销毁一个Activity时，例如在应用中按返回键，onSaveInstanceState()就不会被调用。因为在这种情况下，用户的行为决定了不需要保存Activity的状态。
	 * 通常onSaveInstanceState()只适合用于保存一些临时性的状态，而onPause()适合用于数据的持久化保存。
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("channerlUrl", channerlUrl);
		outState.putInt("channelId", channelId);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.fragment_news_list, container, false);
		//对listView的设置
		listView = (MyListView) parentView.findViewById(R.id.news_list);
		listView.setSelector(R.drawable.transparent);
		listView.setDividerHeight(0);
		listView.addFooterView(inflater.inflate(R.layout.list_foot_view, null));
		listView.setCanLoadMore(true);
		listView.setLoadMoreView(inflater.inflate(R.layout.load_more_view, null));
		adapter = new NewsListAdapter(getActivity(),null,R.layout.news_item);
		return parentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init(savedInstanceState);
	}

	public void init(Bundle savedInstanceState) {
		new Thread() {
			@Override
			public void run() {
				List<News> items = adapter.getDatas();
				if (items == null) {
					//从数据库中查询10条记录
					items = dao.selectByChannel(channelId, 0, 10);
					if (items == null) {//如果数据库中没有记录的话，那么就创建一个集合
						items = new ArrayList<News>();
					}
					if (items.size() == News.LIMIT_SIZE) {
						canLoadMore = true;
					}
					adapter.setData(items);
					//把这个线程添加到消息队列中
					handler.post(CommonFragment.this);
				}
				updateDateTime = getUpdateDateTime();
				if (position == 0) {// 1、还得时间间隔判断是否需要刷新 2、刷新的UI状态显示
					reflush2UpdateTextTime();
					isFristCount = true;
					reflush2UpdateListView();
					refreshNews(items, false);
				}
			}
		}.start();
	}

	@Override
	public void run() {
		listView.setCanLoadMore(canLoadMore);
		listView.setAdapter(adapter);
		listView.setListViewListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(), BrowserActivity.class);
				 News news = adapter.getDatas().get(position-1);
				intent.putExtra(BrowserActivity.ACTION_URL, news.getLink());
				intent.putExtra(BrowserActivity.ACTION_NEW_ITEM_ID, news.get_id());
				startActivity(intent);
			}
		});
	}

	/**
	 * 刷新新闻，
	 * @param items  新闻的集合
	 * @param isListRefresh  判断是不是下拉刷新
	 */
	public void refreshNews(  List<News> items, boolean isListRefresh) {
		//检测是不是已经超过了刷新间隔时间
		boolean isTimeOver2Update = checkTimeIsOver(isListRefresh);
		if (!isTimeOver2Update) {// 是否超过时间间隔，从而更新
			if (items == null || items.size() != 0) {
				handler.removeMessages(DATE_CHANGE_NOTIFY);
				handler.sendEmptyMessageDelayed(DATE_CHANGE_NOTIFY, 500);
			}
			return;
		} else {
			if (items == null) {
				reflush2UpdateTextTime();
				reflush2UpdateListView();
			}
		}
		synchronized (isRefresh) {
			if (isRefresh) {
				return;
			}
			isRefresh = true;
		}
		// 访问网络处理数据

       StringRequest  request = new StringRequest("http://www.baidu.com", new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                if (!TextUtils.isEmpty(response)) {
                    if (null == items) items = adapter.getDatas();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // TODO 设置数据
                            List<News> newss = XmlUtils.pullParseXML(response, channelId, getActivity());
                            adapter.setData(newss);
                        }
                    });
                    setUpdateDateTime(System.currentTimeMillis());
                    handler.removeMessages(DATE_CHANGE_NOTIFY);
                    handler.sendEmptyMessage(DATE_CHANGE_NOTIFY);
                } else {
                    handler.removeMessages(DATE_CHANGE_FAIL);
                    handler.sendEmptyMessage(DATE_CHANGE_FAIL);
                }
                synchronized (isRefresh) {
                    isRefresh = false;
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
	}

	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			refreshNews(null, false);
		}
	};

	private Runnable mRunnableListReFresh = new Runnable() {
		@Override
		public void run() {
			refreshNews(null, true);
		}
	};

	public void changePage2Refresh(boolean isListRefresh) {
		if (isRefresh) {
			return;
		}
		if (checkTimeIsOver(isListRefresh)) {
			new Thread(isListRefresh ? mRunnableListReFresh : mRunnable).start();
		} else {
			handler.removeMessages(LIST_VIEW_TO_STOP_REFRESH);
			handler.sendEmptyMessageDelayed(LIST_VIEW_TO_STOP_REFRESH, 300);
		}
	}
	private String result;
	public void onDestroy() {
		super.onDestroy();
	}

	/** 获取channel的刷新时间 **/
	private long getUpdateDateTime() {
		return sphelper.getLong(PREFERENCES_FILE_NAME + "_" + channelId);
	}

	/** 设置channel的刷新时间 **/
	private void setUpdateDateTime(long nowTime) {
		if (sphelper.putLong(PREFERENCES_FILE_NAME + "_" + channelId, nowTime)) {
			updateDateTime = nowTime;
		}
	}

	@Override
	public void onBeforeChangeHeight() {
		updateTextTime();
	}

	@Override
	public void onRefresh() {
		if (isFristCount) {
			isFristCount = false;
			changePage2Refresh(isFristCount);
		} else {
			changePage2Refresh(true);
		}
	}

	@Override
	public void onLoadMore() {
		int size = adapter.getCount();
//		News lastNews = (News) adapter.getItem(size-1);
		List<News> items = dao.selectByChannel(channelId, size, 10);
		
		size = (items == null ? 0 : items.size());

		if (size != 0) {
			adapter.addDatas(items);
			listView.stopLoadMore();
			adapter.notifyDataSetChanged(listView, false);
		} else {
			listView.stopLoadMore();
			Toast.makeText(getActivity(), "没有更多的数据了", Toast.LENGTH_SHORT).show();
		}
		listView.setCanLoadMore(canLoadMore);
	}

	/** 非ui线程中调用，更新ui **/
	private void reflush2UpdateListView() {
		handler.removeMessages(LIST_VIEW_TO_SHOW_REFRESH);
		handler.sendEmptyMessage(LIST_VIEW_TO_SHOW_REFRESH);
	}

	/** ui线程中检查是否要显示进度 **/
	public void check2UpdateListView() {
		if ((System.currentTimeMillis() - updateDateTime) >= DIFF_UPDATE_TIME) {
			updateTextTime();
			listView.startRefreshNot2OnRefresh();
		}
	}

	private void reflush2UpdateTextTime() {
		handler.removeMessages(LIST_VIEW_TO_SHOW_UPDATE_TIME);
		handler.sendEmptyMessage(LIST_VIEW_TO_SHOW_UPDATE_TIME);
	}

	private void updateTextTime() {
		boolean isAdded = isAdded();
		if (!isAdded) {
			return;
		}
		if (updateDateTime == 0) {// 初始化更新
			listView.setRefreshTime(getResources().getString(R.string.listview_header_last_time));
		} else {// 其他
			long diffTimeSecs = (System.currentTimeMillis() - updateDateTime) / 1000;
			// 1min = 60s ; 1h = 60min
			if (diffTimeSecs < 3600) {// 一小时内，显示分钟
				Resources resources = getResources();
				if (resources != null) {
					listView.setRefreshTime(resources.getString(R.string.listview_header_last_time_for_min, diffTimeSecs < 60 ? 1 : diffTimeSecs / 60));
				}
			} else {
				long diffTimeHours = diffTimeSecs / 3600;
				if (diffTimeHours < 24) {// 一天内更新，显示小时
					listView.setRefreshTime(getResources().getString(R.string.listview_header_last_time_for_hour, diffTimeHours));
				} else if (diffTimeHours == 24) {// 一天更新，显示1天
					listView.setRefreshTime(getResources().getString(R.string.listview_header_last_time_for_day, 1));
				} else {// 大于24小时显示xx月xx日
					listView.setRefreshTime(getResources().getString(R.string.listview_header_last_time_for_date, mDateFormat.format(new Date(updateDateTime))));
				}
			}
		}
	}
	/**
	 * 检查是不是超过了默认的更新时间
	 * @param isListRefresh
	 * @return
	 */
	private boolean checkTimeIsOver(boolean isListRefresh) {
		long diffUpdateTime = isListRefresh ? DIFF_LIST_REFRESH_UPDATE_TIME : DIFF_UPDATE_TIME;
		return (System.currentTimeMillis() - updateDateTime) >= diffUpdateTime;
	}

	/**
	 * 停止刷新
	 */
	private void stopListRefresh() {
		listView.stopRefresh();
		canLoadMore = true;
		listView.setCanLoadMore(canLoadMore);
		listView.stopLoadMore();
	}
}
