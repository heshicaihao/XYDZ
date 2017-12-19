package com.ddiiyy.xydz.pay;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.ddiiyy.xydz.constants.MyConstants;
import com.ddiiyy.xydz.net.NetHelper;
import com.ddiiyy.xydz.utils.JSONUtil;
import com.ddiiyy.xydz.utils.LogUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WechatPay {
	private String TAG = "WechatPay";
	private String APPID = MyConstants.WX_APP_ID;
	private IWXAPI mApi;
	private String mAccountId;
	private String mAccountToken;

	@SuppressWarnings("unused")
	private Activity mActivity;
	private Context mContext;
	private String mGoodsNameStr;
	@SuppressWarnings("unused")
	private String mGoodsInfoStr;
	private String mOutTradeNo;
	private String mTotalStr;

	public WechatPay() {
		super();
	}

	public WechatPay(Activity mActivity, Context mContext,
			String mGoodsNameStr, String mGoodsInfoStr, String mOutTradeNo,
			String mTotalStr, String mAccountId, String mAccountToken) {
		super();
		this.mActivity = mActivity;
		this.mContext = mContext;
		this.mGoodsNameStr = mGoodsNameStr;
		this.mGoodsInfoStr = mGoodsInfoStr;
		this.mOutTradeNo = mOutTradeNo;
		this.mTotalStr = mTotalStr;
		this.mAccountId = mAccountId;
		this.mAccountToken = mAccountToken;
		mApi = WXAPIFactory.createWXAPI(mContext, APPID);
		mApi.registerApp(APPID);
	}

	public void pay() {

		boolean isPaySupported = mApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
		if (isPaySupported) {
			sendPay();
		} else {
			Toast.makeText(mContext, "没有微信或者微信版本不支持支付功能", Toast.LENGTH_SHORT)
					.show();
			// mActivity.finish();
		}

	}

	private void sendPay() {
		LogUtils.logd(TAG, "mOutTradeNo:" + mOutTradeNo);
		LogUtils.logd(TAG, "mGoodsNameStr:" + mGoodsNameStr);
		LogUtils.logd(TAG, "mTotalStr:" + mTotalStr);
		LogUtils.logd(TAG, "mAccountId:" + mAccountId);
		LogUtils.logd(TAG, "mAccountToken:" + mAccountToken);
		NetHelper.wechatPay(mOutTradeNo, mGoodsNameStr, mTotalStr, mAccountId,
				mAccountToken, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						LogUtils.logd(TAG, "sendPay+Success");
						resolveSendPay(responseBody);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "sendPay+onFailure");
						Toast.makeText(mContext, "服务器请求错误", Toast.LENGTH_SHORT)
								.show();
					}
				});
	}

	private void resolveSendPay(byte[] responseBody) {
		try {
			String json = new String(responseBody, "UTF-8");
			LogUtils.logd(TAG, "json:" + json.toString());
			JSONObject result = JSONUtil.resolveResult(responseBody);
			LogUtils.logd(TAG, "result:" + result.toString());
			PayReq request = new PayReq();
			request.appId = APPID;
			request.partnerId = result.optString("partener_id");
			request.prepayId = result.optString("prepay_id");
			request.packageValue = result.optString("package");
			request.nonceStr = result.optString("nonce_str");
			request.timeStamp = result.optString("timestamp");
			request.sign = result.optString("sign");
			request.extData = "app data";
			mApi.sendReq(request);
			// mActivity.finish();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
