package com.hoyouly.baidunews.other;

import android.content.Context;

import com.hoyouly.baidunews.domain.Channel;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

public class InitDB {

	private Context context;
	public InitDB(Context context){
		this.context=context;
	}
	
	public void initChannel(){
		testChannel();
		testGuoNei();
		testGuoji();
		testJunshi();
		testCaijing();
		testHulianwang();
		testFangchang();
		testQiche();
		testTiyu();
		testYule();
		testYouxi();
		testJiaoyu();
		testNvren();
		testKeji();
		testShehui();
	}
	
	public void testChannel() {
		String[] names = new String[] {//
				"国内", "国际", "军事", "财经", "互联网" //
				, "房产", "汽车", "体育", "娱乐", "游戏"//
				, "教育", "女人", "科技", "社会", };
		
		String [] urls=new String[]{
			"http://news.baidu.com/n?cmd=1&class=civilnews&tn=rss",//国内
			"http://news.baidu.com/n?cmd=1&class=internews&tn=rss",//国际
			"http://news.baidu.com/n?cmd=1&class=mil&tn=rss",//军事
			"http://news.baidu.com/n?cmd=1&class=finannews&tn=rss",//财经
			"http://news.baidu.com/n?cmd=1&class=internet&tn=rss",//互联网
			"http://news.baidu.com/n?cmd=1&class=housenews&tn=rss",//房产
			"http://news.baidu.com/n?cmd=1&class=autonews&tn=rss",//汽车
			"http://news.baidu.com/n?cmd=1&class=sportnews&tn=rss",//体育
			"http://news.baidu.com/n?cmd=1&class=enternews&tn=rss",//娱乐
			"http://news.baidu.com/n?cmd=1&class=gamenews&tn=rss",//游戏
			"http://news.baidu.com/n?cmd=1&class=edunews&tn=rss",//教育
			"http://news.baidu.com/n?cmd=1&class=healthnews&tn=rss",//女人
			"http://news.baidu.com/n?cmd=1&class=technnews&tn=rss",//科技
			"http://news.baidu.com/n?cmd=1&class=socianews&tn=rss",//社会
		};
		insertData(names,urls,0);
	}

	public void testGuoNei() {
		String[] names = new String[] { "国内", "台湾", "港澳" };
		
		String []urls=new String[]{
			"http://news.baidu.com/n?cmd=1&class=civilnews&tn=rss",//	国内
			"http://news.baidu.com/n?cmd=1&class=taiwan&tn=rss",//	台湾
			"http://news.baidu.com/n?cmd=1&class=gangaotai&tn=rss",//港澳	
		};
		insertData(names,urls,1);
	}
	public void testGuoji(){
		String[] names = new String[] { "国际焦点", "环球视野", "国际人物" };
		String []urls=new String[]{
				"http://news.baidu.com/n?cmd=1&class=internews&tn=rss",//国际
				"http://news.baidu.com/n?cmd=1&class=hqsy&tn=rss",//环球视野
				"http://news.baidu.com/n?cmd=1&class=renwu&tn=rss",//国际人物
		};
		insertData(names,urls,2);
	}
	
	public void testJunshi() {
		String[] names = new String[] { "军事", "中国军情", "台湾聚焦","国际军情" };
		
		String []urls=new String[]{
				"http://news.baidu.com/n?cmd=1&class=mil&tn=rss",//军事
				"http://news.baidu.com/n?cmd=1&class=zhongguojq&tn=rss",//中国军情
				"http://news.baidu.com/n?cmd=1&class=taihaijj&tn=rss",//台湾聚焦
				"http://news.baidu.com/n?cmd=1&class=guojijq&tn=rss",//国际军情
				
		};
		insertData(names,urls,3);
	}
	
	public void testCaijing() {
		String[] names = new String[] { "财经", "股票", "理财","宏观经济","产业经济" };
		String []urls=new String[]{
				"http://news.baidu.com/n?cmd=1&class=finannews&tn=rss",//财经
				"http://news.baidu.com/n?cmd=1&class=stock&tn=rss",//股票
				"http://news.baidu.com/n?cmd=1&class=money&tn=rss",//理财
				"http://news.baidu.com/n?cmd=1&class=hongguan&tn=rss",//宏观经济
				"http://news.baidu.com/n?cmd=1&class=chanye&tn=rss",//产业
		};
		insertData(names,urls,4);
	}
	
	public void testHulianwang() {
		String[] names = new String[] { "互联网", "人物动态", "公司动态","搜索引擎","电子商务","网络游戏" };
		
		String []urls=new String[]{
				"http://news.baidu.com/n?cmd=1&class=internet&tn=rss",//
				"http://news.baidu.com/n?cmd=1&class=rwdt&tn=rss",//
				"http://news.baidu.com/n?cmd=1&class=gsdt&tn=rss",//
				"http://news.baidu.com/n?cmd=1&class=search_engine&tn=rss",//
				"http://news.baidu.com/n?cmd=1&class=e_commerce&tn=rss",//
				"http://news.baidu.com/n?cmd=1&class=online_game&tn=rss",//
		};
		insertData(names,urls,5);
	}
	
	public void testFangchang() {
		String[] names = new String[] { "房产", "各地动态", "政策风向","市场走势","家具" };
		
		String []urls=new String[]{
				"http://news.baidu.com/n?cmd=1&class=housenews&tn=rss",//房产
				"http://news.baidu.com/n?cmd=1&class=gddt&tn=rss",//各地动态
				"http://news.baidu.com/n?cmd=1&class=zcfx&tn=rss",//政策风向
				"http://news.baidu.com/n?cmd=1&class=shichangzoushi&tn=rss",//市场走势
				"http://news.baidu.com/n?cmd=1&class=fitment&tn=rss",//家具
		};
		insertData(names,urls,6);
	}
	
	public void testQiche() {
		String[] names = new String[] { "汽车", "新车", "导购","各地行情","维修养护" };
		
		String []urls=new String[]{
				"http://news.baidu.com/n?cmd=1&class=autonews&tn=rss",//汽车
				"http://news.baidu.com/n?cmd=1&class=autobuy&tn=rss",// 新车
				"http://news.baidu.com/n?cmd=1&class=daogou&tn=rss",//导购
				"http://news.baidu.com/n?cmd=1&class=hangqing&tn=rss",//各地行情
				"http://news.baidu.com/n?cmd=1&class=weixiu&tn=rss",//维护养护
		};
		insertData(names,urls,7);
	}
	
	public void testTiyu() {
		String[] names = new String[] { "体育", "NBA", "国际足球","国内足球","CBA","综合体育" };
		
		String []urls=new String[]{
				"http://news.baidu.com/n?cmd=1&class=sportnews&tn=rss",//体育
				"http://news.baidu.com/n?cmd=1&class=nba&tn=rss",//NBA
				"http://news.baidu.com/n?cmd=1&class=worldsoccer&tn=rss",//国际足球
				"http://news.baidu.com/n?cmd=1&class=chinasoccer&tn=rss",//国内足球
				"http://news.baidu.com/n?cmd=1&class=cba&tn=rss",//CBA
				"http://news.baidu.com/n?cmd=1&class=othersports&tn=rss",//综合体育
		};
		insertData(names,urls,8);
	}
	
	public void testYule() {
		String[] names = new String[] { "娱乐", "明星", "电影","电视","音乐","综艺","演出","奖项" };
		String []urls=new String[]{
				"http://news.baidu.com/n?cmd=1&class=enternews&tn=rss",//娱乐
				"http://news.baidu.com/n?cmd=1&class=star&tn=rss",//明星
				"http://news.baidu.com/n?cmd=1&class=film&tn=rss",//电影
				"http://news.baidu.com/n?cmd=1&class=tv&tn=rss",//电视
				"http://news.baidu.com/n?cmd=1&class=music&tn=rss",//音乐
				"http://news.baidu.com/n?cmd=1&class=zongyi&tn=rss",//综艺
				"http://news.baidu.com/n?cmd=1&class=yanchu&tn=rss",//演出
				"http://news.baidu.com/n?cmd=1&class=jiangxiang&tn=rss",//奖项
		};
		insertData(names,urls,9);
	}
	public void testYouxi() {
		String[] names = new String[] { "游戏","网络游戏","电视游戏","电子竞技","热门游戏","魔兽世界" };
		
		String []urls=new String[]{
			"http://news.baidu.com/n?cmd=1&class=gamenews&tn=rss",//	游戏
			"http://news.baidu.com/n?cmd=1&class=netgames&tn=rss",//	网络游戏
			"http://news.baidu.com/n?cmd=1&class=tvgames&tn=rss",//		电视游戏
			"http://news.baidu.com/n?cmd=1&class=dianzijingji&tn=rss",//	电子竞技
			"http://news.baidu.com/n?cmd=1&class=remenyouxi&tn=rss",//	热门游戏
			"http://news.baidu.com/n?cmd=1&class=WOW&tn=rss",//	魔兽世界
		};
		insertData(names,urls,10);
	}
	
	public void testJiaoyu() {
		String[] names = new String[] { "教育","考试","留学","就业" };
		
		String []urls=new String[]{
				"http://news.baidu.com/n?cmd=1&class=edunews&tn=rss",//教育
				"http://news.baidu.com/n?cmd=1&class=exams&tn=rss",//考试
				"http://news.baidu.com/n?cmd=1&class=abroad&tn=rss",//留学
				"http://news.baidu.com/n?cmd=1&class=jiuye&tn=rss",//就业
		};
		insertData(names,urls,11);
	}
	
	public void testNvren() {
		String[] names = new String[] { "女人","潮流服饰","美容护肤","减肥","情感","健康" };
		
		String []urls=new String[]{
				"http://news.baidu.com/n?cmd=1&class=healthnews&tn=rss",//
				"http://news.baidu.com/n?cmd=1&class=chaoliufs&tn=rss",//
				"http://news.baidu.com/n?cmd=1&class=meironghf&tn=rss",//
				"http://news.baidu.com/n?cmd=1&class=jianfei&tn=rss",//
				"http://news.baidu.com/n?cmd=1&class=qinggan&tn=rss",//
				"http://news.baidu.com/n?cmd=1&class=jiankang&tn=rss",//
		};
		insertData(names,urls,12);
	}
	
	
	public void testKeji() {
		String[] names = new String[] { "科技","手机","数码","电脑","科普" };
		
		String []urls=new String[]{
				"http://news.baidu.com/n?cmd=1&class=technnews&tn=rss",//科技
				"http://news.baidu.com/n?cmd=1&class=cell&tn=rss",//手机
				"http://news.baidu.com/n?cmd=1&class=digital&tn=rss",//数码
				"http://news.baidu.com/n?cmd=1&class=computer&tn=rss",//电脑
				"http://news.baidu.com/n?cmd=1&class=discovery&tn=rss",//科普
		};
		insertData(names,urls,13);
	}
	
	
	public void testShehui() {
		String[] names = new String[] { "社会","社会与法","社会万象","真情时刻","奇闻异事" };
		
		String []urls=new String[]{
				"http://news.baidu.com/n?cmd=1&class=socianews&tn=rss",//社会
				"http://news.baidu.com/n?cmd=1&class=shyf&tn=rss",//社会与法
				"http://news.baidu.com/n?cmd=1&class=shwx&tn=rss",//社会万象
				"http://news.baidu.com/n?cmd=1&class=zqsk&tn=rss",//真情时刻
				"http://news.baidu.com/n?cmd=1&class=qwys&tn=rss",//奇闻异事
		};
		insertData(names,urls,14);
	}
	
	public void insertData(String[] names,String[] urls,int parentId) {
		try {
			DbUtils dbUtils = DbUtils.create(context, "baidu.db");
			Channel channel = null;
			for (int i = 0; i < names.length; i++) {
				channel = new Channel(names[i],urls[i] ,parentId);
				dbUtils.save(channel);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
}
