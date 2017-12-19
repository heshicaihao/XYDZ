package com.ddiiyy.xydz.common;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;

import com.ddiiyy.xydz.base.BaseApplication;
import com.ddiiyy.xydz.constants.MyConstants;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.socialize.PlatformConfig;

public class AppContext extends BaseApplication {

	private static AppContext mInstance;
	// 用于存放倒计时时间
	public static Map<String, Long> TimeMap;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		// ImageLoader 初始化
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration
				.createDefault(this);
		ImageLoader.getInstance().init(configuration);
		
        JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
	}

	/**
	 * 获得当前app运行的AppContext
	 * 
	 * @return
	 */
	public static AppContext getInstance() {
		return mInstance;
	}
	

	{
		
		// 微信 appid appsecret
		PlatformConfig.setWeixin(MyConstants.WX_APP_ID,
				MyConstants.WX_APP_SECRET);
		
		// 新浪微博 appkey appsecret
		PlatformConfig.setSinaWeibo(MyConstants.SINA_APP_KEY,
				MyConstants.SINA_APP_SECRET);
		
		// QQ和Qzone appid appkey
		PlatformConfig.setQQZone(MyConstants.QQ_APP_ID,
				MyConstants.QQ_APP_KEY);
		
	}

}
