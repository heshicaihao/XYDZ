package com.ddiiyy.xydz.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import cn.jpush.android.api.JPushInterface;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.ddiiyy.xydz.MainActivity;
import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.base.BaseActivity;
import com.ddiiyy.xydz.utils.SharedpreferncesUtil;

/**
 * 欢迎页
 * 
 * @author heshicaihao
 */
public class StartActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start);
		setBaiduCount();
		DelayedJumpNext();

	}

	private void setBaiduCount() {
		// StatService.setAppKey("rSMwM3FYB4tLgdqIbTL96srUBIXkZeBX");
		StatService.setAppChannel(this, "XYDZ", true);
		StatService.setSessionTimeOut(30);
		StatService.setOn(this, StatService.EXCEPTION_LOG);
		StatService.setLogSenderDelayed(0);
		StatService.setSendLogStrategy(this,
				SendStrategyEnum.SET_TIME_INTERVAL, 1, false);
		StatService.setDebugOn(true);
		// String sdkVersion = StatService.getSdkVersion();

	}

	/**
	 * 延时跳转下一页
	 */
	private void DelayedJumpNext() {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				runOnUiThread(new Runnable() {

					public void run() {
						if (SharedpreferncesUtil
								.getGuided(getApplicationContext())) {
							gotoMainActivity();
						} else {
							gotoWelcomeActivity();
						}
					}
				});
			}
		}, 2000);
	}

	private void gotoMainActivity() {
		int CurrentTabNum = 0;
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.putExtra("CurrentTabNum", CurrentTabNum);
		startActivity(intent);
		StartActivity.this.finish();
	}

	private void gotoWelcomeActivity() {
		Intent intent = new Intent(StartActivity.this, WelcomeActivity.class);
		StartActivity.this.startActivity(intent);
		StartActivity.this.finish();
	}

	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();

	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();

	}

}
