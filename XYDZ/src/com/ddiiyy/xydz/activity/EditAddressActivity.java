package com.ddiiyy.xydz.activity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.base.BaseActivity;
import com.ddiiyy.xydz.constants.MyConstants;
import com.ddiiyy.xydz.net.NetHelper;
import com.ddiiyy.xydz.utils.AndroidUtils;
import com.ddiiyy.xydz.utils.FileUtil;
import com.ddiiyy.xydz.utils.LogUtils;
import com.ddiiyy.xydz.widget.WheelView;
import com.ddiiyy.xydz.widget.wheelview.OnWheelChangedListener;
import com.ddiiyy.xydz.widget.wheelview.adapters.ArrayWheelAdapter;
import com.ddiiyy.xydz.widget.wheelview.bean.CityModel;
import com.ddiiyy.xydz.widget.wheelview.bean.DistrictModel;
import com.ddiiyy.xydz.widget.wheelview.bean.ProvinceModel;
import com.ddiiyy.xydz.widget.wheelview.utils.JSONUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ta.utdid2.android.utils.StringUtils;

/**
 * 编辑地址
 * 
 * @author heshicaihao
 * 
 */
public class EditAddressActivity extends BaseActivity implements
		OnClickListener, OnWheelChangedListener {

	private int dCurrent = 0;
	private boolean mIsDefault = false;
	private boolean mIsNoAddress = false;
	private boolean mIsEditAddress = false;
	private String mNameStr;
	private String mPhoneStr;
	private String mHomeTelStr;
	private String mPostcodeStr;
	private String mAreaStr;
	private String mAreaNetStr;
	private String mDetaileAreaStr;
	private String mAccountId;
	private String mAccountToken;
	private String mShipId;
	private String mCurrentProviceName;
	private String mCurrentCityName;
	private String mCurrentDistrictName;
	private String mCurrentZipCode;
	private String[] mShipIdArr = new String[1];
	private String[] mProvinceDatas;
	private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	private Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
	private Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

	private LinearLayout mAreaLL;
	private RelativeLayout mEditRL;
	private RelativeLayout mAreaView;
	private TextView mTitle;
	private ImageView mBack;
	private TextView mSave;
	private TextView mArea;
	private TextView mDelete;
	private TextView mIsDefaultTV;
	private EditText mName;
	private EditText mPhone;
	private EditText mPostcode;
	private EditText mDetaileArea;
	private Button mConfirm;
	private Button mCancel;
	private WheelView mProvinceWV;
	private WheelView mCityWV;
	private WheelView mDistrictWV;
	private InputMethodManager mImm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_address);

		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setIsDefault();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {

		if (wheel == mProvinceWV) {
			showProvince();
		} else if (wheel == mCityWV) {
			showCity();
		} else if (wheel == mDistrictWV) {
			showDistrict(newValue);
			dCurrent = newValue;
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.area:
			mImm.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), 0);
			mAreaView.setVisibility(View.VISIBLE);
			break;
		case R.id.save:
			mImm.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), 0);
			if (AndroidUtils.isNoFastClick()) {
				getInfoInput();
				if (mIsEditAddress) {
					updateAddress();
				} else {
					addAddress2Net();
				}
			}
			break;
		case R.id.delete_text:
			mImm.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), 0);
			doDeleteAddress();
			
			break;
		case R.id.default_address:
			mImm.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), 0);
			if (!mIsDefault) {
				setAddressDefault();
			}
			break;
		case R.id.btn_confirm:
			mAreaNetStr = getShipAreaData(mCurrentProviceName,
					mCurrentCityName, mCurrentDistrictName, mCurrentZipCode);
			String[] arr = mAreaNetStr.split("\\:");
			String arr1 = arr[1];
			mAreaStr = arr1.replaceAll("/", " ");
			mAreaView.setVisibility(View.GONE);
			mArea.setText(mAreaStr);
			LogUtils.logd(TAG, "省:" + mCurrentProviceName + ",市:"
					+ mCurrentCityName + ",区:" + mCurrentDistrictName + ",邮编:"
					+ mCurrentZipCode);
			break;
		case R.id.btn_cancel:
			mAreaView.setVisibility(View.GONE);
			break;
		case R.id.back:
			AndroidUtils.finishActivity(this);
			break;
		default:
			break;
		}
	}

	private void initView() {

		mTitle = (TextView) findViewById(R.id.title);
		mBack = (ImageView) findViewById(R.id.back);
		mSave = (TextView) findViewById(R.id.save);
		mArea = (TextView) findViewById(R.id.area_edit);
		mDelete = (TextView) findViewById(R.id.delete_text);
		mIsDefaultTV = (TextView) findViewById(R.id.default_address);
		mName = (EditText) findViewById(R.id.name_edit);
		mPhone = (EditText) findViewById(R.id.phone_edit);
		mPostcode = (EditText) findViewById(R.id.postcode_edit);
		mDetaileArea = (EditText) findViewById(R.id.detailed_area_edit);
		mConfirm = (Button) findViewById(R.id.btn_confirm);
		mCancel = (Button) findViewById(R.id.btn_cancel);
		mEditRL = (RelativeLayout) findViewById(R.id.edit_rl);
		mAreaView = (RelativeLayout) findViewById(R.id.area_view);
		mAreaLL = (LinearLayout) findViewById(R.id.area);
		mProvinceWV = (WheelView) findViewById(R.id.id_province);
		mCityWV = (WheelView) findViewById(R.id.id_city);
		mDistrictWV = (WheelView) findViewById(R.id.id_district);

		setTextListener();
		mTitle.setText(R.string.add_address);
		mSave.setText(R.string.save);
		mSave.setVisibility(View.VISIBLE);
		mSave.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mDelete.setOnClickListener(this);
		mAreaLL.setOnClickListener(this);
		mIsDefaultTV.setOnClickListener(this);
		mConfirm.setOnClickListener(this);
		mCancel.setOnClickListener(this);
		mProvinceWV.addChangingListener(this);
		mCityWV.addChangingListener(this);
		mDistrictWV.addChangingListener(this);
	}


	private void initData() {
		getDataIntent();
		mAccountId = mUserController.getUserInfo().getId();
		mAccountToken = mUserController.getUserInfo().getToken();
		mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		initAreaViewData();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				mImm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 500);
	}

	private void getDataIntent() {
		Intent intent = getIntent();
		mIsNoAddress = intent.getBooleanExtra("mIsNoAddress", false);
		mIsEditAddress = intent.getBooleanExtra("mIsEditAddress", false);
		if (mIsEditAddress) {
			mEditRL.setVisibility(View.VISIBLE);
			mTitle.setText(R.string.revise_address);
		} else {
			mEditRL.setVisibility(View.GONE);
		}
		String data = intent.getStringExtra("data");
		if (data != null) {
			JSONObject obj;
			try {
				obj = new JSONObject(data);
				showAddress(obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void initAreaViewData() {
		String asString = FileUtil.readFile(MyConstants.AREA_DATA_DIR,
				MyConstants.AREA_DATA, MyConstants.TXT);
		// JSONObject JSONObject;
		try {
			// JSONObject = new JSONObject(asString);
			// JSONArray result = JSONObject.optJSONArray("result");
			JSONArray result = new JSONArray(asString);
			changeArray(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setDefaultArea();
	}

	private void showAddress(JSONObject object) throws JSONException {
		String ship_name = object.optString("ship_name");
		String ship_mobile = object.optString("ship_mobile");
		String ship_zip = object.optString("ship_zip");
		mAreaNetStr = object.optString("ship_area");
		String[] arr = mAreaNetStr.split("\\:");
		String arr1 = arr[1];
		String ship_area = arr1.replaceAll("/", " ");
		// String mCode = arr[2];
		String ship_addr = object.optString("ship_addr");
		mShipId = object.optString("ship_id");
		mIsDefault = object.optBoolean("is_default");
		mShipIdArr[0] = mShipId;
		mName.setText(ship_name);
		mPhone.setText(ship_mobile);
		mPostcode.setText(ship_zip);
		mArea.setText(ship_area);
		mDetaileArea.setText(ship_addr);
		setIsDefault();
	}

	private void setIsDefault() {
		if (mIsDefault) {
			mIsDefaultTV.setText(getString(R.string.default_address_ed));
			mIsDefaultTV.setTextColor(android.graphics.Color
					.parseColor("#9e9e9e"));
			Drawable drawable = getResources().getDrawable(
					R.drawable.default_address_normal);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			mIsDefaultTV.setCompoundDrawables(drawable, null, null, null);
		} else {
			mIsDefaultTV.setText(getString(R.string.setting_default_address));
			mIsDefaultTV.setTextColor(android.graphics.Color
					.parseColor("#E34250"));
			Drawable drawable = getResources().getDrawable(
					R.drawable.default_address_pressed);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			mIsDefaultTV.setCompoundDrawables(drawable, null, null, null);
		}
	}

	private void getInfoInput() {

		mNameStr = mName.getText().toString().trim();
		mPhoneStr = mPhone.getText().toString().trim();
		mHomeTelStr = "";
		mPostcodeStr = mPostcode.getText().toString().trim();
		mAreaStr = mArea.getText().toString().trim();
		mDetaileAreaStr = mDetaileArea.getText().toString().trim();
		if (!mIsEditAddress) {
			mIsDefault = true;
		}

	}
	

	private void setTextListener() {
		mName.addTextChangedListener(new TextWatcher() {
			String tmp = "";
			String digits = "/\\:*?<>|\"\n\t";

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mName.setSelection(s.length());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				tmp = s.toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				if (str.equals(tmp)) {
					return;
				}
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < str.length(); i++) {
					if (digits.indexOf(str.charAt(i)) < 0) {
						sb.append(str.charAt(i));
					}
				}
				tmp = sb.toString();
				mName.setText(tmp);
			}
		});
	}

	/**
	 * 将数据转化为 省、市、区 数组
	 */
	private void changeArray(JSONArray result) throws Exception {
		List<ProvinceModel> provinceList = JSONUtil.getJson2Data(result);

		mProvinceDatas = new String[provinceList.size()];
		for (int i = 0; i < provinceList.size(); i++) {
			// 将全国省份数据转成数组保存到 mProvinceDatas
			mProvinceDatas[i] = provinceList.get(i).getName();
			List<CityModel> cityList = provinceList.get(i).getCityList();
			String[] cityNames = new String[cityList.size()];
			for (int j = 0; j < cityList.size(); j++) {
				// 遍历省下面的所有市的数据
				cityNames[j] = cityList.get(j).getName();
				List<DistrictModel> districtList = cityList.get(j)
						.getDistrictList();
				String[] distrinctNameArray = new String[districtList.size()];
				DistrictModel[] distrinctArray = new DistrictModel[districtList
						.size()];
				for (int k = 0; k < districtList.size(); k++) {
					// 遍历市下面所有区/县的数据
					DistrictModel districtModel = new DistrictModel(
							districtList.get(k).getName(), districtList.get(k)
									.getZipcode());
					// 将全国各省市区邮编数据转成数组保存到 mZipcodeDatasMap
					mZipcodeDatasMap.put(provinceList.get(i).getName()
							+ cityNames[j] + districtList.get(k).getName(),
							districtList.get(k).getZipcode());
					distrinctArray[k] = districtModel;
					distrinctNameArray[k] = districtModel.getName();
				}
				// 将全国各省市区 的区数据转成数组保存到 mDistrictDatasMap
				mDistrictDatasMap.put(provinceList.get(i).getName()
						+ cityNames[j], distrinctNameArray);
			}
			// 将全国各省市 的市数据转成数组保存到 mCitisDatasMap
			mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
		}
	}

	/**
	 * 初始化默认选中的省、市、区
	 */
	private void setDefaultArea() {
		mProvinceWV.setViewAdapter(new ArrayWheelAdapter<String>(
				EditAddressActivity.this, mProvinceDatas));
		// 设置可见条目数量
		mProvinceWV.setVisibleItems(7);
		mCityWV.setVisibleItems(7);
		mDistrictWV.setVisibleItems(7);
		showProvince();
	}

	/**
	 * 显示 省 WheelView的信息，并给当前省对应的 市添加数据 和设置初始值
	 */
	private void showProvince() {
		int pCurrent = mProvinceWV.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mCityWV.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mCityWV.setCurrentItem(0);
		showCity();
	}

	/**
	 * 显示 市 WheelView的信息，并给当前市对应的 区添加数据 和设置初始值
	 */
	private void showCity() {
		int cCurrent = mCityWV.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[cCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentProviceName
				+ mCurrentCityName);
		if (areas == null) {
			areas = new String[] { "" };
		}
		mDistrictWV.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mDistrictWV.setCurrentItem(0);

		showDistrict(dCurrent);
	}

	/**
	 * 显示 区 WheelView的信息，并给当前区对应的 邮编
	 */
	private void showDistrict(int newValue) {
		mCurrentDistrictName = mDistrictDatasMap.get(mCurrentProviceName
				+ mCurrentCityName)[newValue];
		mCurrentZipCode = mZipcodeDatasMap.get(mCurrentProviceName
				+ mCurrentCityName + mCurrentDistrictName);
	}

	private String getShipAreaData(String ProviceName, String CityName,
			String DistrictName, String Code) {
		String data = "mainland:";
		data += ProviceName + "/";
		data += CityName + "/";
		data += DistrictName + ":";
		data += Code;
		return data;
	}

	private void goBackResult() {
		String data = getData("", mPhoneStr, mNameStr, mHomeTelStr, "true",
				mDetaileAreaStr, mAreaNetStr, mPostcodeStr);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("address", data);
		this.setResult(SubmitOrderActivity.RESULT_OK, intent);

	}

	private String getData(String ship_id, String ship_mobile,
			String ship_name, String ship_tel, String is_default,
			String ship_addr, String ship_area, String ship_zip) {
		JSONObject object = new JSONObject();
		try {
			object.put("ship_id", ship_id);
			object.put("ship_mobile", ship_mobile);
			object.put("ship_name", ship_name);
			object.put("ship_tel", ship_tel);
			object.put("is_default", is_default);
			object.put("ship_addr", ship_addr);
			object.put("ship_area", ship_area);
			object.put("ship_zip", ship_zip);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String data = object.toString();
		return data;
	}

	/**
	 * 设置 默认地址
	 */
	private void setAddressDefault() {
		NetHelper.setDefault(mAccountId, mAccountToken, mShipId,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						LogUtils.logd(TAG, "setDefault+Success");
						resolvSetDefault(responseBody);	
						
					}


					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "setDefault+Failure");

					}
				});
	}

	

	private void resolvSetDefault(byte[] responseBody) {
		try {
			String json = new String(responseBody, "UTF-8");
			LogUtils.logd(TAG, "resolvSetDefault+json:" + json);
			JSONObject JSONObject = new JSONObject(json);
			String code = JSONObject.optString("code");
			String msg = JSONObject.optString("msg");
			if ("0".equals(code)) {
				Toast.makeText(this, getString(R.string.set_default_address_success),
						Toast.LENGTH_SHORT).show();
				mIsDefaultTV.setTextColor(android.graphics.Color
						.parseColor("#9e9e9e"));
				mIsDefaultTV
						.setText(getString(R.string.default_address_ed));
				Drawable drawable = getResources().getDrawable(
						R.drawable.default_address_normal);
				// / 这一步必须要做,否则不会显示.
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				mIsDefaultTV.setCompoundDrawables(drawable, null, null,
						null);
				mIsDefault = true;
			} else {
				Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 删除地址
	 */
	private void doDeleteAddress() {
		NetHelper.deleteAddress(mAccountId, mAccountToken, mShipIdArr,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						LogUtils.logd(TAG, "initGetAddressListonSuccess");
						resolvEdeleteAddress(responseBody);		

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "initGetAddressListonFailure");
					}
				});
	}

	private void resolvEdeleteAddress(byte[] responseBody) {
		try {
			String json = new String(responseBody, "UTF-8");
			LogUtils.logd(TAG, "EdeleteAddress+json:" + json);
			JSONObject JSONObject = new JSONObject(json);
			String code = JSONObject.optString("code");
			String msg = JSONObject.optString("msg");
			if ("0".equals(code)) {
				Toast.makeText(this, getString(R.string.delete_address_success),
						Toast.LENGTH_SHORT).show();
				mDelete.setTextColor(android.graphics.Color
						.parseColor("#9e9e9e"));
				Drawable drawable = getResources().getDrawable(
						R.drawable.delete_address_normal);
				// / 这一步必须要做,否则不会显示.
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				mDelete.setCompoundDrawables(drawable, null, null, null);
				AndroidUtils.finishActivity(EditAddressActivity.this);
			} else {
				Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 修改地址
	 */
	private void updateAddress() {
		NetHelper.reviseAddress(mAccountId, mAccountToken, mShipId, mNameStr,
				mAreaNetStr, mDetaileAreaStr, mPostcodeStr, mHomeTelStr,
				mPhoneStr, mIsDefault, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "reviseAddress+onSuccess:");
						resolveUpdateAddress(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "reviseAddress+onFailure:");

					}
				});
	}

	/**
	 * 新增地址
	 */
	private void addAddress2Net() {
		if (StringUtils.isEmpty(mNameStr)) {
			Toast.makeText(this, getString(R.string.please_input_name),
					Toast.LENGTH_SHORT).show();
			LogUtils.logd(TAG, "mNameStr" + "Null");
			return;
		}
		if (StringUtils.isEmpty(mPhoneStr)) {
			Toast.makeText(this, getString(R.string.please_input_phone),
					Toast.LENGTH_SHORT).show();
			LogUtils.logd(TAG, "mPhoneStr" + "Null");
			return;
		}
		if (!AndroidUtils.isPhoneNumberValid(mPhoneStr)) {
			Toast.makeText(this, getString(R.string.please_input_phone_again),
					Toast.LENGTH_SHORT).show();
			mPhone.setText("");
			mPhoneStr = null;
			return;
		}
		if (StringUtils.isEmpty(mAreaStr)) {
			Toast.makeText(this, getString(R.string.please_input_area),
					Toast.LENGTH_SHORT).show();
			LogUtils.logd(TAG, "mAreaStr" + "Null");
			return;
		}
		if (StringUtils.isEmpty(mDetaileAreaStr)) {
			Toast.makeText(this, getString(R.string.please_input_detaile_area),
					Toast.LENGTH_SHORT).show();
			LogUtils.logd(TAG, "mDetaileAreaStr" + "Null");
			return;
		}
		NetHelper.addAddress(mAccountId, mAccountToken, mNameStr, mAreaNetStr,
				mDetaileAreaStr, mPostcodeStr, mHomeTelStr, mPhoneStr,
				mIsDefault, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "addAddress2Net+onSuccess:");
						resolveaddAddress2Net(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "addAddress2Net+onFailure:");

					}
				});
	}

	private void resolveUpdateAddress(byte[] arg2) {
		try {
			String json = new String(arg2, "UTF-8");
			LogUtils.logd(TAG, "json:" + json);
			JSONObject JSONObject = new JSONObject(json);
			String code = JSONObject.optString("code");
			String msg = JSONObject.optString("msg");
			if ("0".equals(code)) {
				Toast.makeText(this, getString(R.string.add_address_success),
						Toast.LENGTH_SHORT).show();
				AndroidUtils.finishActivity(EditAddressActivity.this);
			} else {
				Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void resolveaddAddress2Net(byte[] arg2) {
		try {
			String json = new String(arg2, "UTF-8");
			LogUtils.logd(TAG, "json:" + json);
			JSONObject JSONObject = new JSONObject(json);
			String code = JSONObject.optString("code");
			String msg = JSONObject.optString("msg");
			if ("0".equals(code)) {
				Toast.makeText(this, getString(R.string.add_address_success),
						Toast.LENGTH_SHORT).show();
				if (mIsNoAddress) {
					goBackResult();
				}
				AndroidUtils.finishActivity(EditAddressActivity.this);
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
