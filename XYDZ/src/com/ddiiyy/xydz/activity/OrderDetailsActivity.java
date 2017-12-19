package com.ddiiyy.xydz.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddiiyy.xydz.MainActivity;
import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.adapter.OrderDetailsAdapter;
import com.ddiiyy.xydz.base.BaseActivity;
import com.ddiiyy.xydz.bean.UserBean;
import com.ddiiyy.xydz.common.UserController;
import com.ddiiyy.xydz.dialog.CustomDialog;
import com.ddiiyy.xydz.net.NetHelper;
import com.ddiiyy.xydz.utils.AndroidUtils;
import com.ddiiyy.xydz.utils.JSONUtil;
import com.ddiiyy.xydz.utils.LogUtils;
import com.ddiiyy.xydz.utils.StringUtils;
import com.ddiiyy.xydz.widget.SildingFinishLayout;
import com.ddiiyy.xydz.widget.SildingFinishLayout.OnSildingFinishListener;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 作品详情
 * 
 * @author heshicaihao
 * 
 */
public class OrderDetailsActivity extends BaseActivity implements
		OnClickListener {

	private TextView mTitle;
	private ImageView mBack;
	private String mOutTradeNo;
	private String mTotalStr;
	private ListView mListView;
	private TextView mOrderId;
	private TextView mOrderState;
	private TextView mGift;
	private TextView mShipping;
	private TextView mTotalMoney;
	private TextView mConsignee;
	private TextView mConsigneePhone;
	private TextView mShippingAddress;
	private TextView mCancelOrder;
	private TextView mPay;
	private LinearLayout mBottomLL;

	private UserBean mUser;
	public View mHeader;
	public View mFooter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_details);
		initView();
		initData();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			gotoMainActivity();
			break;
		case R.id.cancel_order_btn:
			showDialog(this);
			break;
		case R.id.pay_btn:
			gotoOrderPayActivity();
			break;
		default:
			break;
		}
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.title);
		mBack = (ImageView) findViewById(R.id.back);
		mCancelOrder = (TextView) findViewById(R.id.cancel_order_btn);
		mPay = (TextView) findViewById(R.id.pay_btn);
		mBottomLL = (LinearLayout) findViewById(R.id.bottom_ll);
		mListView = (ListView) findViewById(R.id.listview);
		mHeader = getLayoutInflater().inflate(
				R.layout.view_header_order_details, null);
		getLayoutInflater();
		mFooter = LayoutInflater.from(this).inflate(
				R.layout.view_footer_order_details, null);
		mListView.addHeaderView(mHeader, null, true);
		mListView.addFooterView(mFooter, null, true);
		// 显示头部出现分割线
		mListView.setHeaderDividersEnabled(false);
		// 禁止底部出现分割线
		mListView.setFooterDividersEnabled(false);
		mConsignee = (TextView) mHeader.findViewById(R.id.consignee);
		mConsigneePhone = (TextView) mHeader.findViewById(R.id.consignee_phone);
		mShippingAddress = (TextView) mHeader
				.findViewById(R.id.shipping_address);
		mShipping = (TextView) mFooter.findViewById(R.id.shipping);
		mGift = (TextView) mFooter.findViewById(R.id.gift);
		mOrderId = (TextView) mFooter.findViewById(R.id.order_id);
		mOrderState = (TextView) mFooter.findViewById(R.id.order_state);
		mTotalMoney = (TextView) mFooter.findViewById(R.id.real_payment);
		setSildingFinish();
		mTitle.setText(R.string.order_details);
		mBack.setOnClickListener(this);
		mCancelOrder.setOnClickListener(this);
		mPay.setOnClickListener(this);
	}

	private void initData() {
		mUser = UserController.getInstance(this).getUserInfo();
		getDataIntent();
		getOrderInfo();
	}
	
	
	/**
	 * 设置右划退出
	 */
	private void setSildingFinish() {
		SildingFinishLayout mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
		mSildingFinishLayout
				.setOnSildingFinishListener(new OnSildingFinishListener() {

					@Override
					public void onSildingFinish() {
						OrderDetailsActivity.this.finish();
					}
				});

		mSildingFinishLayout.setTouchView(mListView);
	}


	private void getOrderInfo() {

		NetHelper.getOrderDetail(mUser.getId(), mUser.getToken(), mOutTradeNo,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "onSuccess");
						resolveOrderDetail(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "onFailure");
					}
				});

	}

	private void resolveOrderDetail(byte[] responseBody) {
		try {
			String json = new String(responseBody, "UTF-8");
			LogUtils.logd(TAG, "json:" + json);
			JSONObject result = JSONUtil.resolveResult(responseBody);
			if (result != null) {
				showOrderInfo(result);
				setListAdapter(result);
				showConsignee(result);
			} else {
				JSONObject JSONObject = new JSONObject(json);
				String msg = JSONObject.optString("msg");
				Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
				AndroidUtils.finishActivity(this);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void setListAdapter(JSONObject result) throws JSONException {
		JSONObject goodsinfo = result.getJSONObject("goodsinfo");
		JSONArray goods = goodsinfo.getJSONArray("goods");

		OrderDetailsAdapter mAdapter = new OrderDetailsAdapter(this, this,
				goods);
		mListView.setAdapter(mAdapter);
	}

	private void showConsignee(JSONObject result) throws JSONException {
		JSONObject consignee = result.getJSONObject("consignee");
		String name = consignee.optString("name");
		mConsignee.setText(name);

		String mobile = consignee.optString("mobile");
		mConsigneePhone.setText(mobile);

		String area = consignee.optString("area");
		String addr = consignee.optString("addr");
		mShippingAddress.setText(area + addr);
	}

	private void showOrderInfo(JSONObject result) throws JSONException {
		mOutTradeNo = result.optString("order_id");
		mOrderId.setText(mOutTradeNo);

		String orderStatus = result.optString("orderStatus");
		String payStatus = result.optString("payStatus");
		if (this.getString(R.string.cancelled).equals(orderStatus)) {
			mOrderState.setText(orderStatus);
			mBottomLL.setVisibility(View.GONE);
		} else {
			mOrderState.setText(payStatus);
			if (this.getString(R.string.paymented).equals(payStatus)) {
				mBottomLL.setVisibility(View.GONE);
			} else {
				mBottomLL.setVisibility(View.VISIBLE);
			}
		}

		String gift = result.optString("gift");
		if (StringUtils.isEmpty(gift)) {
			mGift.setText("0.00");
		} else {
			String gift2point = StringUtils.format2point(gift);
			mGift.setText(gift2point);
		}

		JSONObject shipping = result.getJSONObject("shipping");
		String cost_shipping = shipping.optString("cost_shipping");
		String cost_shipping2point = StringUtils.format2point(cost_shipping);
		mShipping.setText(cost_shipping2point);

		mTotalStr = result.optString("total_amount");
		String mTotalStr2point = StringUtils.format2point(mTotalStr);
		mTotalMoney.setText(mTotalStr2point);
	}

	private void getDataIntent() {
		Intent intent = getIntent();
		mOutTradeNo = intent.getStringExtra("mOutTradeNo");
	}

	private void gotoMainActivity() {
		int CurrentTabNum = 2;
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("CurrentTabNum", CurrentTabNum);
		startActivity(intent);
		AndroidUtils.finishActivity(this);
	}

	private void gotoOrderPayActivity() {
		Intent intent = new Intent(this, OrderPayActivity.class);
		intent.putExtra("mTotalStr", mTotalStr);
		intent.putExtra("mGoodsNameStr", "");
		intent.putExtra("mGoodsInfoStr", "");
		intent.putExtra("mOutTradeNo", mOutTradeNo);

		startActivity(intent);
		AndroidUtils.enterActvityAnim(this);
	}

	/**
	 * 显示对话框
	 * 
	 * @param context
	 */
	private void showDialog(Context mContext) {
		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(mContext.getString(R.string.no_way_change));
		builder.setPositiveButton(mContext.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (AndroidUtils.isNoFastClick()) {
							dialog.dismiss();

						}
					}
				});

		builder.setNegativeButton(mContext.getString(R.string.ok),
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (AndroidUtils.isNoFastClick()) {
							dialog.dismiss();
							netCancelOrder();
						}
					}
				});

		builder.create().show();
	}

	/**
	 * 网络请求取消订单
	 * 
	 * @param position
	 */
	private void netCancelOrder() {
		NetHelper.cancelOrder(mUser.getId(), mUser.getToken(), mOutTradeNo,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "onSuccess");
						resolvecancelOrder(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "onFailure");
					}
				});
	}

	private void resolvecancelOrder(byte[] arg2) {
		try {
			String json = new String(arg2, "UTF-8");
			LogUtils.logd(TAG, "json:" + json);
			JSONObject obj = new JSONObject(json);
			String code = obj.optString("code");
			String msg = obj.optString("msg");
			if (code.equals("0")) {
				Toast.makeText(this, this.getString(R.string.cancel_order_ok),
						Toast.LENGTH_SHORT).show();
				mBottomLL.setVisibility(View.GONE);
				AndroidUtils.finishActivity(this);
			} else {
				Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
