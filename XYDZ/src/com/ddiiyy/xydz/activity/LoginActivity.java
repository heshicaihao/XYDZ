package com.ddiiyy.xydz.activity;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.base.BaseActivity;
import com.ddiiyy.xydz.bean.QQInfo;
import com.ddiiyy.xydz.bean.SinaInfo;
import com.ddiiyy.xydz.bean.WeixinInfo;
import com.ddiiyy.xydz.constants.MyConstants;
import com.ddiiyy.xydz.dialog.CustomProgressDialog;
import com.ddiiyy.xydz.net.NetHelper;
import com.ddiiyy.xydz.utils.AndroidUtils;
import com.ddiiyy.xydz.utils.FileUtil;
import com.ddiiyy.xydz.utils.LogUtils;
import com.ddiiyy.xydz.utils.SharedpreferncesUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private static final int REQUESTCODE = 1;
	private static final int REQUESTCODE02 = 2;
	private String mPhoneStr;
	private String mPasswordStr;

	private TextView mTitle;
	private TextView mSave;
	private ImageView mBack;
	private TextView mRegister;
	private TextView mFindPassword;
	private ImageView mAccountDelete;
	private ImageView mPassDelete;
	private ImageView mQQ;
	private ImageView mWechat;
	private ImageView mSina;

	private EditText mAccount;
	private EditText mPassword;
	private Button mLogin;

	private Gson gson;

	private UMShareAPI mShareAPI = null;
	private String mOpenid;
	private String mType;
	private String mUserName;
	private boolean mIsThreeLogin = false;
	private CustomProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		initData();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.save:
			gotoOtherActivity(RegisterActivity.class, REQUESTCODE);
			break;
		case R.id.register:
			gotoOtherActivity(RegisterActivity.class, REQUESTCODE);
			break;
		case R.id.find_password:
			gotoOtherActivity(ResetPasswordActivity.class, REQUESTCODE02);
			break;
		case R.id.qq_login:
			mType = "qzone";
			getOauth(SHARE_MEDIA.QQ, mType);
			break;
		case R.id.wechat_login:
			mType = "wechat";
			getOauth(SHARE_MEDIA.WEIXIN, mType);
			break;
		case R.id.sina_login:
			mType = "sina";
			getOauth(SHARE_MEDIA.SINA, mType);
			break;
		case R.id.account_delete:
			mAccount.setText("");
			mAccountDelete.setVisibility(View.GONE);
			break;
		case R.id.password_delete:
			mPassword.setText("");
			mPassDelete.setVisibility(View.GONE);
			break;
		case R.id.login:
			if (AndroidUtils.isNoFastClick()) {
				getInfoInput();
				mDialog.show();
				login();
			}
			break;
		case R.id.back:
			AndroidUtils.finishActivity(this);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mShareAPI.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUESTCODE:
			if (resultCode == RESULT_OK) {
				mPhoneStr = data.getStringExtra("mPhoneStr");
				mAccount.setText(mPhoneStr);
			}
			break;
		case REQUESTCODE02:
			if (resultCode == RESULT_OK) {
				mPhoneStr = data.getStringExtra("mPhoneStr");
				mAccount.setText(mPhoneStr);
			}
			break;
		default:
			break;
		}
	}

	private void getInfoInput() {
		mPhoneStr = mAccount.getText().toString().trim();
		mPasswordStr = mPassword.getText().toString().trim();

	}

	private void login() {
		if (StringUtils.isEmpty(mPhoneStr)) {
			Toast.makeText(this, getString(R.string.please_input_phone_null),
					Toast.LENGTH_SHORT).show();
			mDialog.dismiss();
			return;
		}
		if (!AndroidUtils.isPhoneNumberValid(mPhoneStr)) {
			Toast.makeText(this, getString(R.string.please_input_phone_again),
					Toast.LENGTH_SHORT).show();
			mAccount.setText("");
			mPhoneStr = null;
			mDialog.dismiss();
			return;
		}
		if (StringUtils.isEmpty(mPasswordStr)) {
			Toast.makeText(this,
					getString(R.string.please_input_password_null),
					Toast.LENGTH_SHORT).show();
			mDialog.dismiss();
			return;
		}
		NetHelper.loginUser(mPhoneStr, mPasswordStr,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "loginUser +onSuccess");
						resolveLoginUser(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						Toast.makeText(LoginActivity.this,
								getString(R.string.request_service_failure),
								Toast.LENGTH_SHORT).show();
						LogUtils.logd(TAG, "loginUser +onFailure");
						mDialog.dismiss();
					}
				});
	}

	private void resolveLoginUser(byte[] responseBody) {
		String json;
		try {
			json = new String(responseBody, "UTF-8");
			JSONObject obj = new JSONObject(json);
			String msg = obj.optString("msg");
			String code = obj.optString("code");
			JSONObject result = obj.optJSONObject("result");
			if ("0".equals(code)) {
				mIsThreeLogin = false;
				savaLoginInfo(result);
			} else {
				Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT)
						.show();
				mDialog.dismiss();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void initView() {

		mTitle = (TextView) findViewById(R.id.title);
		mSave = (TextView) findViewById(R.id.save);
		mBack = (ImageView) findViewById(R.id.back);
		mRegister = (TextView) findViewById(R.id.register);
		mFindPassword = (TextView) findViewById(R.id.find_password);
		mAccount = (EditText) findViewById(R.id.account);
		mPassword = (EditText) findViewById(R.id.password);
		mAccountDelete = (ImageView) findViewById(R.id.account_delete);
		mPassDelete = (ImageView) findViewById(R.id.password_delete);
		mQQ = (ImageView) findViewById(R.id.qq_login);
		mWechat = (ImageView) findViewById(R.id.wechat_login);
		mSina = (ImageView) findViewById(R.id.sina_login);
		mLogin = (Button) findViewById(R.id.login);

		mTitle.setText(R.string.account_login);
		mSave.setVisibility(View.VISIBLE);
		mSave.setText(R.string.register);
		mAccountDelete.setVisibility(View.GONE);
		mPassDelete.setVisibility(View.GONE);

		setTextChangedListener();
		mRegister.setOnClickListener(this);
		mFindPassword.setOnClickListener(this);
		mAccountDelete.setOnClickListener(this);
		mPassDelete.setOnClickListener(this);
		mLogin.setOnClickListener(this);
		mQQ.setOnClickListener(this);
		mWechat.setOnClickListener(this);
		mSina.setOnClickListener(this);
		mSave.setOnClickListener(this);
		mBack.setOnClickListener(this);
		
		mDialog = new CustomProgressDialog(this, this.getResources().getString(
				R.string.dialog_info_login));

	}

	private void initData() {
		mShareAPI = UMShareAPI.get(this);
	}

	private void gotoOtherActivity(Class<?> activity, int code) {
		Intent intent = new Intent(getApplication(), activity);
		startActivityForResult(intent, code);
		AndroidUtils.enterActvityAnim(this);
	}

	/**
	 * 添加编辑框监听
	 */
	private void setTextChangedListener() {
		mAccount.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (arg0.toString().length() > 0) {
					mAccountDelete.setVisibility(View.VISIBLE);
				} else {
					mAccountDelete.setVisibility(View.GONE);
				}
			}
		});
		mPassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (arg0.toString().length() > 0) {
					mPassDelete.setVisibility(View.VISIBLE);
				} else {
					mPassDelete.setVisibility(View.GONE);
				}
			}
		});
	}

	private void otherLogin(String openid, String type, String username) {
		NetHelper.threePartyLogin(openid, type, username,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "threePartyLogin +onSuccess");
						resolveOtherLogin(arg2);

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						mDialog.dismiss();
						LogUtils.logd(TAG, "threePartyLogin +onFailure");
					}
				});
	}

	private void resolveOtherLogin(byte[] responseBody) {
		String json;
		try {

			json = new String(responseBody, "UTF-8");
			JSONObject obj = new JSONObject(json);
			LogUtils.logd(TAG, "responseBody:" + json);
			String msg = obj.optString("msg");
			String code = obj.optString("code");
			JSONObject result = obj.optJSONObject("result");
			if ("0".equals(code)) {
				mIsThreeLogin = true;
				savaLoginInfo(result);
			} else {
				Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT)
						.show();
				mDialog.dismiss();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void getOauth(final SHARE_MEDIA platform, final String type) {
		LogUtils.logd(TAG, "getOauth");
		mShareAPI.doOauthVerify(this, platform, new UMAuthListener() {
			@Override
			public void onComplete(SHARE_MEDIA arg2, int action,
					Map<String, String> data) {
				LogUtils.logd(TAG, "getOauth+onComplete");
				// Toast.makeText(getApplicationContext(), "Authorize succeed",
				// Toast.LENGTH_SHORT).show();
				mDialog.show();
				LogUtils.logd(TAG, "threePartyLogin +onFailure");
				getUserInfo(platform, type);
			}

			@Override
			public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//				Toast.makeText(getApplicationContext(), "Authorize fail",
//						Toast.LENGTH_SHORT).show();
				LogUtils.logd(TAG, "getOauth+onError");
			}

			@Override
			public void onCancel(SHARE_MEDIA platform, int action) {
//				Toast.makeText(getApplicationContext(), "Authorize cancel",
//						Toast.LENGTH_SHORT).show();
				LogUtils.logd(TAG, "getOauth+onCancel");
			}
		});
	}

	private void getUserInfo(SHARE_MEDIA platform, final String type) {
		LogUtils.logd(TAG, "getPlatformInfo");
		mShareAPI.getPlatformInfo(LoginActivity.this, platform,
				new UMAuthListener() {

					@Override
					public void onCancel(SHARE_MEDIA arg0, int arg1) {
						LogUtils.logd(TAG, "getUserInfo+onCancel");
						mDialog.dismiss();
					}

					@Override
					public void onComplete(SHARE_MEDIA arg0, int status,
							Map<String, String> info) {
						LogUtils.logd(TAG, "getUserInfo+onComplete");
						if (info != null) {
							// Toast.makeText(LoginActivity.this,
							// info.toString(),
							// Toast.LENGTH_SHORT).show();
							LogUtils.logd(TAG, "info:" + info.toString());
							gson = new Gson();
							String json = gson.toJson(info);
							if ("qzone".equals(type)) {
								QQInfo qq = gson.fromJson(json, QQInfo.class);
								mOpenid = qq.getOpenid();
								mUserName = qq.getScreen_name();
							} else if ("wechat".equals(type)) {
								WeixinInfo wx = gson.fromJson(json,
										WeixinInfo.class);
								mOpenid = wx.getOpenid();
								mUserName = wx.getNickname();
							} else if ("sina".equals(type)) {
								SinaInfo sina = gson.fromJson(json,
										SinaInfo.class);
								mOpenid = sina.getUid();
								mUserName = sina.getScreen_name();
							}
							otherLogin(mOpenid, type, mUserName);
						}

					}

					@Override
					public void onError(SHARE_MEDIA arg0, int arg1,
							Throwable arg2) {
						mDialog.dismiss();
						LogUtils.logd(TAG, "getUserInfo+onError");
					}

				});
	}

	/**
	 * 登录成功后，保存用户信息
	 * 
	 * @param result
	 */
	private void savaLoginInfo(JSONObject result) {

		String token = result.optString("token");
		JSONObject accountinfo = result.optJSONObject("accountinfo");
		String id = accountinfo.optString("id");

		user.setUname(mPhoneStr);
		user.setPassword(mPasswordStr);
		user.setId(id);
		user.setToken(token);
		user.setIs_login(true);
		user.setOpen_id(mOpenid);
		user.setUsername(mUserName);
		user.setIs_three_login(mIsThreeLogin);

		mUserController.saveUserInfo(user);

		if (!SharedpreferncesUtil.getAlias(getApplication())) {
			LogUtils.logd(TAG, id);
			JPushInterface.setAliasAndTags(getApplicationContext(), id, null,
					new TagAliasCallback() {

						@Override
						public void gotResult(int code, String alias,
								Set<String> tags) {
						}

					});
		}
		getOrderInfo();
	}

	private void getOrderInfo() {
		LogUtils.logd(TAG, "user.getId()" + user.getId());
		LogUtils.logd(TAG, "user.getToken()" + user.getToken());

		NetHelper.getOrderList(user.getId(), user.getToken(), "all", 1, 5,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "getOrderList+onSuccess");
						resolveOrderList(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "getOrderList+onFailure");
						mDialog.dismiss();
					}
				});
	}

	private void resolveOrderList(byte[] arg2) {
		try {
			String json = new String(arg2, "UTF-8");
			LogUtils.logd(TAG, "arr== nulljson:" + json);
			JSONObject obj = new JSONObject(json);
			JSONObject result = obj.optJSONObject("result");
			JSONArray arr = result.optJSONArray("orderData");
			if (arr != null) {
				FileUtil.saveFile(arr.toString(), MyConstants.ORDERLIST,
						MyConstants.ORDER_LIST_INFO, MyConstants.TXT);
				SharedpreferncesUtil.setOrder(getApplication(), true);
			} else {
				SharedpreferncesUtil.setOrder(getApplication(), false);
			}
			mDialog.dismiss();
			finish();
			Toast.makeText(LoginActivity.this,
					this.getString(R.string.login_ok), Toast.LENGTH_SHORT)
					.show();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			mDialog.dismiss();
		} catch (JSONException e) {
			e.printStackTrace();
			mDialog.dismiss();
		}
	}
}
