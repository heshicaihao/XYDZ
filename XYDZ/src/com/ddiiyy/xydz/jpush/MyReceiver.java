package com.ddiiyy.xydz.jpush;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

import com.ddiiyy.xydz.MainActivity;
import com.ddiiyy.xydz.activity.OrderDetailsActivity;
import com.ddiiyy.xydz.utils.LogUtils;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	public static String TAG = "MyReceiver";
	private String mOrderIdStr;
	private String mPage_code;

	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle bundle = intent.getExtras();
		getExtrasData(bundle);

		LogUtils.logd(TAG, "1[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			LogUtils.logd(TAG, "2[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			LogUtils.logd(
					TAG,
					"3[MyReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			LogUtils.logd(TAG, "4[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			LogUtils.logd(TAG, "5[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			LogUtils.logd(TAG, "6[MyReceiver] 用户点击打开了通知");
			// gotoOther(context);
			gotoOtherActivity(mPage_code, context);
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			LogUtils.logd(TAG, "7[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
					+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			LogUtils.logd(TAG, "8[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
		} else {
			LogUtils.logd(TAG,
					"9[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	@SuppressWarnings("unchecked")
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
					LogUtils.logd(TAG, "This message has no Extra data");
					continue;
				}
				try {
					JSONObject json = new JSONObject(
							bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it = json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" + myKey + " - "
								+ json.optString(myKey) + "]");
						LogUtils.logd(TAG, "json:" + "myKey:" + myKey);
						LogUtils.logd(TAG, "json:" + "json.optString(myKey):"
								+ json.optString(myKey));
					}
				} catch (JSONException e) {
					LogUtils.logd(TAG, "10Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	// send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		if (MainActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			context.sendBroadcast(msgIntent);
		}
	}

	/**
	 * 获取服务器发送 Extras 附加信息
	 * 
	 * @param bundle
	 */
	private void getExtrasData(Bundle bundle) {
		for (String key : bundle.keySet()) {
			LogUtils.logd(TAG, "key:"+key);
			LogUtils.logd(TAG, "V:"+bundle.getString(key));
			if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				String jsonStr = bundle.getString(JPushInterface.EXTRA_EXTRA);
				if (jsonStr.isEmpty()) {
					continue;
				}
				resolveData(jsonStr);
				LogUtils.logd(TAG, "jsonStr:"+jsonStr.toString());
			}
		}
	}

	/**
	 * 解析 Extras 附加信息
	 * 
	 * @param jsonStr
	 */
	private void resolveData(String jsonStr) {
		try {
			JSONObject json = new JSONObject(jsonStr);
			mPage_code = json.optString("page_code");
			mOrderIdStr = json.optString("order_id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打开自定义的Activity
	 * 
	 * @param code
	 * @param context
	 */
	private void gotoOtherActivity(String code, Context context) {
		LogUtils.logd(TAG, "mPage_code:" + code);
		if ("0".equals(mPage_code)) {
			// gotoHome(context);
			gotoOrderDetails(context);
		} else if ("1.1".equals(mPage_code)) {
			gotoOrderDetails(context);
		} else if ("1.2".equals(mPage_code)) {
			gotoHome(context);
		} else if ("2.1".equals(mPage_code)) {
			gotoHome(context);
		} else if ("3.1".equals(mPage_code)) {
			gotoHome(context);
		} else if ("3.2".equals(mPage_code)) {
			gotoHome(context);
		} else if ("4.1".equals(mPage_code)) {
			gotoHome(context);
		} else {
			gotoHome(context);
			LogUtils.logd(TAG, "mPage_code不存在");
		}
	}

	/**
	 * 调到 去 MainActivity
	 * 
	 * @param context
	 */
	private void gotoHome(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

	/**
	 * 调到 去 OrderDetailsActivity
	 * 
	 * @param context
	 */
	private void gotoOrderDetails(Context context) {
		Intent intent = new Intent(context, OrderDetailsActivity.class);
		intent.putExtra("mOutTradeNo", mOrderIdStr);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}
}
