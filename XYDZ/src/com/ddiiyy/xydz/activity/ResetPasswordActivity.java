package com.ddiiyy.xydz.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.base.BaseActivity;
import com.ddiiyy.xydz.net.NetHelper;
import com.ddiiyy.xydz.utils.AndroidUtils;
import com.ddiiyy.xydz.utils.LogUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ta.utdid2.android.utils.StringUtils;

@SuppressLint("HandlerLeak")
public class ResetPasswordActivity extends BaseActivity implements
		OnClickListener {

	private TextView mTitle;
	private ImageView mBack;
	private ImageView mAccountDelete;
	private ImageView mPassDelete;
	private ImageView mCodeDelete;
	private EditText mAccount;
	private EditText mPassword;
	private EditText mCode;
	private Button mResetPassword;
	private Button mGetCode;

	private String mPhoneStr;
	private String mAuthcodeStr;
	private String mPasswordStr;

	private TimeCount mTime;
	private InputMethodManager mImm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);
		initView();
		initData();
	}

	@Override
	public void onClick(View view) {
		mPhoneStr = mAccount.getText().toString().trim();
		mPasswordStr = mPassword.getText().toString().trim();
		mAuthcodeStr = mCode.getText().toString().trim();

		switch (view.getId()) {
		case R.id.get_code:
			getCode();
			break;
		case R.id.reset_password:
			mImm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
			if (AndroidUtils.isNoFastClick()) {
				if (!proofInputInfo()) {
					return;
				}
				resetPW(mPhoneStr, mAuthcodeStr, mPasswordStr);
			}
			break;
		case R.id.account_delete:
			mAccount.setText("");
			mAccountDelete.setVisibility(View.GONE);
			break;
		case R.id.password_delete:
			mPassword.setText("");
			mPassDelete.setVisibility(View.GONE);
			break;
		case R.id.code_delete:
			mCode.setText("");
			mCodeDelete.setVisibility(View.GONE);
			break;
		case R.id.back:
			AndroidUtils.finishActivity(this);
			break;

		default:
			break;
		}
	}

	/**
	 * 校对用户输入是否合规
	 */
	private boolean proofInputInfo() {
		if (StringUtils.isEmpty(mPhoneStr)) {
			Toast.makeText(this, getString(R.string.please_input_phone_null),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!AndroidUtils.isPhoneNumberValid(mPhoneStr)) {
			Toast.makeText(this, getString(R.string.please_input_phone_again),
					Toast.LENGTH_SHORT).show();
			mAccount.setText("");
			mPhoneStr = null;
			return false;
		}
		if (StringUtils.isEmpty(mAuthcodeStr)) {
			Toast.makeText(this, getString(R.string.please_input_code_null),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (StringUtils.isEmpty(mPasswordStr)) {
			Toast.makeText(this,
					getString(R.string.please_input_password_null),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	private void resetPW(String phoneStr, String authcode, String pwcode) {
		NetHelper.resetPassword(phoneStr, authcode, pwcode,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "register+onSuccess:");
						resolveResetPassword(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "register+onFailure:");
					}
				});

	}

	private void resolveResetPassword(byte[] arg2) {
		try {
			String json = new String(arg2, "UTF-8");
			JSONObject obj = new JSONObject(json);
			String code = obj.optString("code");
			String msg = obj.optString("msg");
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			if ("0".equals(code)) {
				AndroidUtils.finishActivity(this);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void getCode() {
		if (StringUtils.isEmpty(mPhoneStr)) {
			Toast.makeText(this, getString(R.string.please_input_phone_null),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!AndroidUtils.isPhoneNumberValid(mPhoneStr)) {
			Toast.makeText(this, getString(R.string.please_input_phone_again),
					Toast.LENGTH_SHORT).show();
			mAccount.setText("");
			mPhoneStr = null;
			return;
		}
		NetHelper.forgotSendCode(mPhoneStr, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				LogUtils.logd(TAG, "getCode+onSuccess:");
				resolvegetCode(arg2);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				LogUtils.logd(TAG, "getCode+onFailure:");
				Toast.makeText(ResetPasswordActivity.this,
						getString(R.string.request_service_failure),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void resolvegetCode(byte[] responseBody) {
		try {
			String json = new String(responseBody, "UTF-8");
			LogUtils.logd(TAG, json);
			JSONObject obj = new JSONObject(json);
			String code = obj.optString("code");
			String msg = obj.optString("msg");
			Toast.makeText(ResetPasswordActivity.this, msg, Toast.LENGTH_SHORT)
					.show();
			if ("0".equals(code)) {
				mTime.start();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		mTime = new TimeCount(60000, 1000);

		mTitle = (TextView) findViewById(R.id.title);
		mAccount = (EditText) findViewById(R.id.account);
		mCode = (EditText) findViewById(R.id.code);
		mPassword = (EditText) findViewById(R.id.password);
		mAccountDelete = (ImageView) findViewById(R.id.account_delete);
		mPassDelete = (ImageView) findViewById(R.id.password_delete);
		mCodeDelete = (ImageView) findViewById(R.id.code_delete);
		mBack = (ImageView) findViewById(R.id.back);
		mResetPassword = (Button) findViewById(R.id.reset_password);
		mGetCode = (Button) findViewById(R.id.get_code);

		mTitle.setText(R.string.reset_password);

		mAccountDelete.setVisibility(View.GONE);
		mPassDelete.setVisibility(View.GONE);
		mCodeDelete.setVisibility(View.GONE);
		setTextChangedListener();

		mAccountDelete.setOnClickListener(this);
		mPassDelete.setOnClickListener(this);
		mCodeDelete.setOnClickListener(this);
		mResetPassword.setOnClickListener(this);
		mGetCode.setOnClickListener(this);
		mBack.setOnClickListener(this);

	}

	private void initData() {
		mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//		new Timer().schedule(new TimerTask() {
//			@Override
//			public void run() {
//				mImm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//			}
//		}, 500);
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
		mCode.addTextChangedListener(new TextWatcher() {

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
					mCodeDelete.setVisibility(View.VISIBLE);
				} else {
					mCodeDelete.setVisibility(View.GONE);
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

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			mGetCode.setText(getString(R.string.send_code));
			mGetCode.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			mGetCode.setClickable(false);
			mGetCode.setText(millisUntilFinished / 1000
					+ getString(R.string.get_code_s));
		}
	}

}