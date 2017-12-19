package com.ddiiyy.xydz.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddiiyy.xydz.MainActivity;
import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.base.BaseActivity;
import com.ddiiyy.xydz.bean.UserBean;
import com.ddiiyy.xydz.cache.ACache;
import com.ddiiyy.xydz.common.UserController;
import com.ddiiyy.xydz.constants.MyConstants;
import com.ddiiyy.xydz.net.MyURL;
import com.ddiiyy.xydz.net.NetHelper;
import com.ddiiyy.xydz.utils.AndroidUtils;
import com.ddiiyy.xydz.utils.JSONUtil;
import com.ddiiyy.xydz.utils.LogUtils;
import com.ddiiyy.xydz.utils.StringUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

public class PaySuccessActivity extends BaseActivity implements OnClickListener {

	private TextView mTitle;
	private ImageView mBack;
	private ImageView mShare;
	private TextView mName;
	private TextView mPhone;
	private TextView mAddress;
	private Button mGotoHome;
	private Button mOrderIdLL;
	private String mOutTradeNo;

	private UMImage mUMImage;
	private ACache mCache;
	private UserBean mUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_success);
		initView();
		initData();
	}

	private void initView() {
		mBack = (ImageView) findViewById(R.id.back);
		mShare = (ImageView) findViewById(R.id.share);
		mTitle = (TextView) findViewById(R.id.title);
		mName = (TextView) findViewById(R.id.consignee);
		mPhone = (TextView) findViewById(R.id.consignee_phone);
		mAddress = (TextView) findViewById(R.id.shipping_address);
		mGotoHome = (Button) findViewById(R.id.goto_home);
		mOrderIdLL = (Button) findViewById(R.id.order_id_ll);

		mTitle.setText(R.string.pay_success);
		mOrderIdLL.setOnClickListener(this);
		mGotoHome.setOnClickListener(this);
		mShare.setOnClickListener(this);
		mBack.setOnClickListener(this);

	}

	private void initData() {
		mCache = ACache.get(this);
		mOutTradeNo = mCache.getAsString(MyConstants.ORDERID);
		mUser = UserController.getInstance(this).getUserInfo();
		
		getOrderInfo();

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.goto_home:
			gotoMainActivity();
			break;
		case R.id.share:
			share();
			break;
		case R.id.order_id_ll:
			gotoOrderDetailsActivity();
			break;
		case R.id.back:
			AndroidUtils.finishActivity(this);
			break;

		default:
			break;
		}
	}

	private void gotoMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		AndroidUtils.enterActvityAnim(this);
	}
	
	private void gotoOrderDetailsActivity() {
		Intent intent = new Intent(this, OrderDetailsActivity.class);
		intent.putExtra("mOutTradeNo", mOutTradeNo);
		startActivity(intent);
		AndroidUtils.enterActvityAnim(this);
	}

	/**
	 * 分享
	 */
	private void share() {
		new ShareAction(this)
				.setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
						SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
				.setListenerList(umShareListener, umShareListener)
				.setShareboardclickCallback(shareBoardlistener).open();
	}

	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			Toast.makeText(
					PaySuccessActivity.this,
					platform + getResources().getString(R.string.share_success),
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(PaySuccessActivity.this,
					platform + getResources().getString(R.string.share_error),
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(PaySuccessActivity.this,
					platform + getResources().getString(R.string.share_cancel),
					Toast.LENGTH_SHORT).show();
		}
	};

	private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {

		@Override
		public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
			new ShareAction(PaySuccessActivity.this).setPlatform(share_media)
					.setCallback(umShareListener)
					.withText(getResources().getString(R.string.app_name))
					.withText(getResources().getString(R.string.share_works_info))
					.withTargetUrl(MyURL.SHARE_WORK_URL)
					.withMedia(mUMImage).share();
		}
	};
	
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
				JSONObject goodsinfo = result.getJSONObject("goodsinfo");
				JSONArray goods = goodsinfo.getJSONArray("goods");
				if (goods.length()>0) {
					JSONObject object = (JSONObject) goods.get(0);
					String goods_pic = object.optString("goods_pic");
					if (StringUtils.isEmpty(goods_pic)) {
						mShare.setVisibility(View.GONE);
					}else {
						mUMImage = new UMImage(this, goods_pic);
						mShare.setVisibility(View.VISIBLE);
					}
				}else {
					mShare.setVisibility(View.GONE);
				}
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
	
	private void showConsignee(JSONObject result) throws JSONException {
		JSONObject consignee = result.getJSONObject("consignee");
		String name = consignee.optString("name");
		String mobile = consignee.optString("mobile");
		String area = consignee.optString("area");
		String addr = consignee.optString("addr");
		
		mName.setText(name);
		mPhone.setText(mobile);
		mAddress.setText(area + addr);
	}


}
