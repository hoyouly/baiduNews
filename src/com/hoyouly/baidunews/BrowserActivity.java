package com.hoyouly.baidunews;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
public class BrowserActivity extends Activity implements OnClickListener {

	public static final String ACTION_URL = "com.hoyouly.baidunews.BrowserActivity.URL_IMG";
	public static final String ACTION_NEW_ITEM_ID = "com.hoyouly.baidunews.BrowserActivity.ACTION_NEW_ITEM_ID";
	public static final String ACTION_TITLE = "com.hoyouly.baidunews.BrowserActivity.ACTION_TITLE";
	public static final String ACTION_RESULT = "com.hoyouly.baidunews.BrowserActivity.ACTION_RESULT";

	private ViewGroup mParent;
	private WebView webView;
	private ProgressBar mViewProgress;
	private String mHomeURL;
	private long _id;
	private int mResult = -1;

	private View mBackButton;
	private TextView mTitle;
	private ImageView mHome;
	private ImageView mBack;
	private ImageView mFroward;
	private ImageView mRefresh;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_browser);
		mBackButton = findViewById(R.id.imageview_left);
		mBackButton.setVisibility(View.VISIBLE);
		mBackButton.setOnClickListener(this);
		mTitle = (TextView) findViewById(R.id.textview_title);
		mViewProgress = (ProgressBar) findViewById(R.id.webview_progressbar);

		mParent = (ViewGroup) findViewById(R.id.web_frame_layout);
		webView = (WebView) findViewById(R.id.webview_browser);
		//WebViewClient就是帮助WebView处理各种通知、请求事件的，加载的还是该应用程序的客户端
		webView.setWebViewClient(new WebViewClient() {
			/**
			 * 在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.removeAllViews();
				view.loadUrl(url);
				return true;
			}

			@Override//在页面加载开始时调用。
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				mViewProgress.setVisibility(View.VISIBLE);
				mFroward.setEnabled(view.canGoForward());
				mBack.setEnabled(view.canGoBack());
			}

			@Override//在页面加载结束时调用。
			public void onPageFinished(WebView view, String url) {
				mViewProgress.setVisibility(View.GONE);
				mFroward.setEnabled(view.canGoForward());
				mBack.setEnabled(view.canGoBack());
			}
		});

		//WebChromeClient是辅助WebView处理Javascript的对话框，网站图标，网站title，加载进度等 
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progress) {
				mViewProgress.setProgress(progress);
				super.onProgressChanged(view, progress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				//设置当前activity的标题栏
				if(TextUtils.isEmpty(getIntent().getExtras().getString(ACTION_TITLE)) && title != null) {
					if (mTitle != null) {
						mTitle.setText(title);
					}
				}
				super.onReceivedTitle(view, title);
			}
		});

		WebSettings webSettings = webView.getSettings();
		
		webSettings.setBuiltInZoomControls(true);//显示方法缩小按钮
		
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);//设置是否支持变焦
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过js打开新的窗口
		webSettings.setPluginsEnabled(true);//是否支持插件，如flashPlayer \ activityX
		webSettings.setBlockNetworkImage(false);//是否显示网络图像
		webSettings.setUseWideViewPort(true);//无限缩放
		webSettings.setLoadWithOverviewMode(true);

		mHome = (ImageView) findViewById(R.id.imgview_browser_home);
		mBack = (ImageView) findViewById(R.id.imgview_browser_back);
		mFroward = (ImageView) findViewById(R.id.imgview_browser_forward);
		mRefresh = (ImageView) findViewById(R.id.imgview_browser_refresh);
		mHome.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mFroward.setOnClickListener(this);
		mRefresh.setOnClickListener(this);

		Bundle bundle = getIntent().getExtras();
		String url = bundle.getString(ACTION_URL);
		_id = bundle.getLong(ACTION_NEW_ITEM_ID, -1);
		mHomeURL = url;
		webView.loadUrl(url);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		    case R.id.imageview_left: {
		    	onBackPressed();
		    }
			break;
			case R.id.imgview_browser_home: {
				if(mHomeURL != null) {
					Intent intent=new Intent();
					intent.setClass(BrowserActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			}
			break;
			case R.id.imgview_browser_refresh: {
				webView.reload();
			}
			break;
			case R.id.imgview_browser_forward: {
				if(webView.canGoForward()){
					webView.goForward();
				}else{
					Toast.makeText(this, "亲，不能前进了！", Toast.LENGTH_SHORT).show();
				}
			}
			break;
			case R.id.imgview_browser_back: {
				if(webView.canGoBack()){
					webView.goBack();
				}else{
					Toast.makeText(this, "亲，不能后退了。", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (webView != null) {
			mParent.removeView(webView);
			WebView w = webView;
			w.removeAllViews();
			w.destroy();
			w = null;
			webView = null;
		}
	}
	
	@Override
	public void finish() {
		if (_id > 0) {
			setResult(mResult, getIntent().putExtra(ACTION_RESULT, mResult));
		}
		super.finish();
	}

}