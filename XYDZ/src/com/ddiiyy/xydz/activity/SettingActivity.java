package com.ddiiyy.xydz.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.base.BaseActivity;
import com.ddiiyy.xydz.bean.UserBean;
import com.ddiiyy.xydz.common.UpdateManager;
import com.ddiiyy.xydz.common.UserController;
import com.ddiiyy.xydz.constants.MyConstants;
import com.ddiiyy.xydz.net.MyURL;
import com.ddiiyy.xydz.net.NetHelper;
import com.ddiiyy.xydz.utils.AndroidUtils;
import com.ddiiyy.xydz.utils.FileUtil;
import com.ddiiyy.xydz.utils.JSONUtil;
import com.ddiiyy.xydz.utils.LogUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

public class SettingActivity extends BaseActivity implements OnClickListener {

	private TextView mTitle;
	private ImageView mBack;
	private LinearLayout mGoodsUpdateLL;
	private LinearLayout mHelpLL;
	private LinearLayout mAppUpdateLL;
	private LinearLayout mShareLL;
	private LinearLayout mSuggestionsLL;
	private LinearLayout mContactUsLL;
	private UMImage mUMImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		initView();
		initData();
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.title);
		mBack = (ImageView) findViewById(R.id.back);

		mGoodsUpdateLL = (LinearLayout) findViewById(R.id.goods_update_ll);
		mHelpLL = (LinearLayout) findViewById(R.id.help_ll);
		mAppUpdateLL = (LinearLayout) findViewById(R.id.app_update_ll);
		mShareLL = (LinearLayout) findViewById(R.id.app_share_ll);
		mSuggestionsLL = (LinearLayout) findViewById(R.id.suggestions_ll);
		mContactUsLL = (LinearLayout) findViewById(R.id.contact_us_ll);

		mTitle.setText(R.string.setting);
		mBack.setOnClickListener(this);
		
		mGoodsUpdateLL.setOnClickListener(this);
		mHelpLL.setOnClickListener(this);
		mAppUpdateLL.setOnClickListener(this);
		mShareLL.setOnClickListener(this);
		mSuggestionsLL.setOnClickListener(this);
		mContactUsLL.setOnClickListener(this);

	}

	private void initData() {
		String mImageurl = MyURL.LOGO_URL;
		mUMImage = new UMImage(this, mImageurl);
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.goods_update_ll:
			getGoodsInfo("0");
			break;
		case R.id.help_ll:
			startOtherWeb(this, this.getString(R.string.help),
					MyURL.HELP_URL);
			break;
		case R.id.app_update_ll:
			onClickUpdate();
			break;
		case R.id.app_share_ll:
			share();
			break;
		case R.id.suggestions_ll:
			UserBean mUser = UserController.getInstance(this).getUserInfo();
			String url = MyURL.SUGGESTIONS_URL+mUser.getId();
			LogUtils.logd(TAG, "url:"+url);
			startOtherWeb(this, this.getString(R.string.suggestions),
					url);
			break;
		case R.id.contact_us_ll:
			startOtherWeb(this, this.getString(R.string.contact_us),
					MyURL.CONTACT_US_URL);
			break;
		case R.id.back:
			AndroidUtils.finishActivity(this);
			break;
		default:
			break;
		}

	}

	/**
	 * 检查更新
	 */
	private void onClickUpdate() {
		new UpdateManager(this, false);
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
					SettingActivity.this,
					platform + getResources().getString(R.string.share_success),
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(SettingActivity.this,
					platform + getResources().getString(R.string.share_error),
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(SettingActivity.this,
					platform + getResources().getString(R.string.share_cancel),
					Toast.LENGTH_SHORT).show();
		}
	};

	private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {

		@Override
		public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
			new ShareAction(SettingActivity.this).setPlatform(share_media)
					.setCallback(umShareListener)
					.withText(getResources().getString(R.string.share_app_info))
					.withTargetUrl(MyURL.SHARE_APP_URL)
					.withMedia(mUMImage).share();
		}
	};

	private void getGoodsInfo(String time) {
		NetHelper.getGoodsList(time, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				LogUtils.logd(TAG, "getGoodsList+onSuccess");
				resolveGoodsInfo(arg2);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				LogUtils.logd(TAG, "getGoodsList+onFailure");
			}
		});
	}

	private void resolveGoodsInfo(byte[] responseBody) {
		try {
			JSONObject result = JSONUtil.resolveResult(responseBody);

			String update_time = result.optString("update_time");
			FileUtil.saveFile(update_time, MyConstants.TIME,
					MyConstants.UPDATE_TIME, MyConstants.TXT);

			JSONArray items = result.getJSONArray("items");
			FileUtil.saveFile(items.toString(), MyConstants.CATLIST,
					MyConstants.CAT_LIST_INFO, MyConstants.TXT);
			
			Toast.makeText(SettingActivity.this,
					getResources().getString(R.string.goods_update_success),
					Toast.LENGTH_SHORT).show();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
