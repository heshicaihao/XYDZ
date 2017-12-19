package com.ddiiyy.xydz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.base.BaseActivity;
import com.ddiiyy.xydz.cache.ACache;
import com.ddiiyy.xydz.constants.MyConstants;
import com.ddiiyy.xydz.pay.ALiPay;
import com.ddiiyy.xydz.pay.WechatPay;
import com.ddiiyy.xydz.utils.AndroidUtils;
import com.ddiiyy.xydz.utils.LogUtils;
import com.ddiiyy.xydz.utils.StringUtils;

/**
 * 订单支付
 * 
 * @author heshicaihao
 * 
 */
public class OrderPayActivity extends BaseActivity implements OnClickListener {

	private LinearLayout mAliPayLL;
	private LinearLayout mWechatPayLL;
	private TextView mTitle;
	private TextView mTotalMoney;
	private Button mPay;
	private ImageView mAliIV;
	private ImageView mWXIV;
	private ImageView mBack;

	private String mTotalStr;
	private String mGoodsNameStr;
	private String mGoodsInfoStr;
	private String mOutTradeNo;
	private boolean mIsAliPay = true;

	private String mAccountId;
	private String mAccountToken;
	private ALiPay alipay;
	private WechatPay wechatpay;
	private ACache mCache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_pay);
		initView();
		initData();
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.pay_text:
			if (mIsAliPay) {
				alipay.pay();
				LogUtils.logd(TAG, "alipay.pay()");
			} else {
				wechatpay.pay();
				LogUtils.logd(TAG, "wechatpay.pay()");
			}
			LogUtils.logd(TAG, "pay_text");
			break;
		case R.id.al_pay_ll:
			if (!mIsAliPay) {
				mAliIV.setImageResource(R.drawable.checkbox_pressed);
				mWXIV.setImageResource(R.drawable.checkbox_normal);
				mIsAliPay = true;
			}
			LogUtils.logd(TAG, "al_pay_ll");
			break;
		case R.id.wechat_pay_ll:
			if (mIsAliPay) {
				mAliIV.setImageResource(R.drawable.checkbox_normal);
				mWXIV.setImageResource(R.drawable.checkbox_pressed);
				mIsAliPay = false;
			}
			LogUtils.logd(TAG, "wechat_pay_ll");
			break;
		case R.id.back:
			AndroidUtils.finishActivity(this);
			break;
		default:
			break;
		}

	}

	private void initView() {
		mBack = (ImageView) findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.title);
		mTotalMoney = (TextView) findViewById(R.id.total_money);
		mPay = (Button) findViewById(R.id.pay_text);
		mAliIV = (ImageView) findViewById(R.id.al_iv);
		mWXIV = (ImageView) findViewById(R.id.wx_iv);
		mAliPayLL = (LinearLayout) findViewById(R.id.al_pay_ll);
		mWechatPayLL = (LinearLayout) findViewById(R.id.wechat_pay_ll);

		mTitle.setText(R.string.order_pay);
		mBack.setOnClickListener(this);
		mPay.setOnClickListener(this);
		mAliPayLL.setOnClickListener(this);
		mWechatPayLL.setOnClickListener(this);

	}

	private void initData() {
		mCache = ACache.get(this);
		mAccountId = mUserController.getUserInfo().getId();
		mAccountToken = mUserController.getUserInfo().getToken();
		getDataIntent();
		alipay = new ALiPay(this, this, mGoodsNameStr, mGoodsInfoStr,
				mOutTradeNo, mTotalStr);
		wechatpay = new WechatPay(this, this, mGoodsNameStr, mGoodsInfoStr,
				mOutTradeNo, mTotalStr, mAccountId, mAccountToken);

	}

	private void getDataIntent() {
		Intent intent = getIntent();
		mTotalStr = intent.getStringExtra("mTotalStr");
		// mGoodsNameStr = intent.getStringExtra("mGoodsNameStr");
		// mGoodsInfoStr = intent.getStringExtra("mGoodsInfoStr");
		mGoodsNameStr = "小丫定制手机壳";
		mGoodsInfoStr = "小丫定制手机壳";
		mOutTradeNo = intent.getStringExtra("mOutTradeNo");
		mCache.put(MyConstants.ORDERID, mOutTradeNo);
		String mTotalStr2point = StringUtils.format2point(mTotalStr);
		mTotalMoney.setText(mTotalStr2point);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		alipay = null;
		wechatpay = null;
	}

}
