package com.ddiiyy.xydz.activity;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.adapter.ChoiceShippingListAdapter;
import com.ddiiyy.xydz.adapter.SubmitOrderHolder;
import com.ddiiyy.xydz.base.BaseActivity;
import com.ddiiyy.xydz.bean.ShoppingInfoBean;
import com.ddiiyy.xydz.bean.WorksInfoBean;
import com.ddiiyy.xydz.cache.ACache;
import com.ddiiyy.xydz.constants.MyConstants;
import com.ddiiyy.xydz.database.DataHelper;
import com.ddiiyy.xydz.dialog.CustomDialog;
import com.ddiiyy.xydz.dialog.CustomProgressDialog;
import com.ddiiyy.xydz.net.NetHelper;
import com.ddiiyy.xydz.utils.AndroidUtils;
import com.ddiiyy.xydz.utils.FileUtil;
import com.ddiiyy.xydz.utils.JSONUtil;
import com.ddiiyy.xydz.utils.LogUtils;
import com.ddiiyy.xydz.utils.PictureUtil;
import com.ddiiyy.xydz.utils.SharedpreferncesUtil;
import com.ddiiyy.xydz.utils.StringUtils;
import com.ddiiyy.xydz.widget.AddAndSubView;
import com.ddiiyy.xydz.widget.AddAndSubView.OnNumChangeListener;
import com.ddiiyy.xydz.xutils.BitmapHelp;
import com.ddiiyy.xydz.xutils.BitmapUtils;
import com.ddiiyy.xydz.xutils.bitmap.BitmapDisplayConfig;
import com.ddiiyy.xydz.xutils.bitmap.core.BitmapSize;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 提交定单
 * 
 * @author heshicaihao
 * 
 */
public class SubmitOrderActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private static final int REQUESTCODE = 1001;
	private static final int REQUESTCODE2 = 1002;

	private int mCout = 0;
	private int mCoutGetPrice = 0;
	private int mCoutPay = 0;
	private String mNameStr;
	private String mPhoneStr;
	private String mHomeTelStr;
	private String mPostcodeStr;
	private String mAreaStr;
	private String mDetaileAreaStr;
	private String mGoodsNameStr;
	private String mGoodsInfoStr;
	private String mAreaId;
	private String mAreaNetStr;
	private String mShippingId;
	private String mAccountId;
	private String mAccountToken;
	private String mObjects;
	private String mTotalStr;
	private String mOutTradeNo;

	private String code04 = "10004";
	private String data = JSONUtil.getData(code04, "2");
	private boolean mIsNoAddress = false;

	private RelativeLayout mShippingView;
	private RelativeLayout mTotalMoneyRL;
	private LinearLayout mTrafficLL;
	private LinearLayout mAddressLL;
	private EditText mRemarks;
	private ImageView mBack;
	private TextView mTitle;
	private TextView mPay;
	private TextView mConsignee;
	private TextView mConsigneePhone;
	private TextView mShippingAddress;
	private TextView mShipping;
	private TextView mNum;
	private TextView mTotalMoney;
	private TextView mTotalContent;
	private TextView mNoAddressTV;
	private ProgressBar mTotalPBar;
	private ListView mShippingList;
	private ListView mListView;
	private View mHeader;
	private View mFooter;

	private ACache mCache;
	private JSONArray mShippingArray;
	private ArrayList<String> mWorksIds = new ArrayList<String>();
	private ArrayList<WorksInfoBean> mListData;
	private ChoiceShippingListAdapter mChoiceShippingAdapter;
	private CustomProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_order);
		initView();
		initData();

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.address:
			if (mIsNoAddress) {
				gotoAddAddress();
			} else {
				gotoManageAddress(data);
			}

			break;
		case R.id.traffic:
			if (mIsNoAddress) {
				Toast.makeText(this, getString(R.string.no_address),
						Toast.LENGTH_SHORT).show();
			} else {
				mShippingView.setVisibility(View.VISIBLE);
			}
			break;

		case R.id.pay_text:
			if (MyConstants.ISOPERATE) {
				if (mCoutPay == 0) {
					if (mIsNoAddress) {
						Toast.makeText(this, getString(R.string.no_address),
								Toast.LENGTH_SHORT).show();
					} else {
						if (StringUtils.isEmpty(mShippingId)) {
							Toast.makeText(this,
									getString(R.string.please_express_mode),
									Toast.LENGTH_SHORT).show();
						} else {
							mCache.put("mConsignee", mConsignee.getText()
									.toString());
							mCache.put("mConsigneePhone", mConsigneePhone.getText()
									.toString());
							mCache.put("mShippingAddress", mShippingAddress
									.getText().toString());
							mDialog.show();
							submitOrder();
						}
					}
				} else {
					if (AndroidUtils.isNoFastClick()) {
						Toast.makeText(this,
								getString(R.string.you_place_an_order),
								Toast.LENGTH_SHORT).show();
					}
				}
			}else {
				showSystemDialog(this);
			}
			break;
		case R.id.no_address_tv:
			if (mIsNoAddress) {
				gotoAddAddress();
			} else {
				gotoManageAddress(data);
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		try {
			JSONObject object = mShippingArray.getJSONObject(position);
			setShowVelivery(object);
			mChoiceShippingAdapter.setSeclection(position);
			mChoiceShippingAdapter.notifyDataSetInvalidated();
			mShippingView.setVisibility(View.GONE);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUESTCODE:
			if (resultCode == RESULT_OK) {
				String obj = data.getExtras().getString("address");
				JSONObject object;
				try {
					object = new JSONObject(obj);
					showAddress(object);
					mIsNoAddress = false;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			break;
		case REQUESTCODE2:
			if (resultCode == RESULT_OK) {
				String obj = data.getExtras().getString("obj");
				JSONObject object;
				try {
					object = new JSONObject(obj);
					showAddress(object);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			break;
		default:
			break;
		}
	}

	private void setShowVelivery(JSONObject object) throws JSONException {
		String dt_name = object.optString("dt_name");
		String money = object.optString("money");
		mShippingId = object.optString("dt_id");
		String Str;
		if ("0".equals(money)) {
			Str = this.getResources().getString(R.string.free_shipping);
			mShipping.setText(Str);
		} else {
			Str = dt_name + "  (" + money + "元)";
			CharSequence charSequence = Html.fromHtml(Str);
			mShipping.setText(charSequence);
		}
		mObjects = getObjects(mListData);
		if (!mIsNoAddress) {
			initGetPrice();
		}
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.title);
		mBack = (ImageView) findViewById(R.id.back);

		mListView = (ListView) findViewById(R.id.listview);
		mHeader = getLayoutInflater().inflate(
				R.layout.view_header_submit_order, null);
		getLayoutInflater();
		mFooter = LayoutInflater.from(this).inflate(
				R.layout.view_footer_submit_order, null);
		mListView.addHeaderView(mHeader);
		mListView.addFooterView(mFooter);
		mAddressLL = (LinearLayout) mHeader.findViewById(R.id.address);
		mNoAddressTV = (TextView) mHeader.findViewById(R.id.no_address_tv);
		mTrafficLL = (LinearLayout) mFooter.findViewById(R.id.traffic);
		mRemarks = (EditText) mFooter.findViewById(R.id.remarks_edit);

		mShippingView = (RelativeLayout) findViewById(R.id.shipping_view);
		mTotalMoneyRL = (RelativeLayout) findViewById(R.id.total_money_num_rl);
		mPay = (TextView) findViewById(R.id.pay_text);
		mConsignee = (TextView) findViewById(R.id.consignee);
		mConsigneePhone = (TextView) findViewById(R.id.consignee_phone);
		mShippingAddress = (TextView) findViewById(R.id.shipping_address);
		mShipping = (TextView) findViewById(R.id.shipping_text);
		mNum = (TextView) findViewById(R.id.num_text);
		mTotalMoney = (TextView) findViewById(R.id.total_money);
		mTotalContent = (TextView) findViewById(R.id.total_content);

		mTotalPBar = (ProgressBar) findViewById(R.id.total_money_progressbar);
		mShippingList = (ListView) findViewById(R.id.shipping_list);

		mTitle.setText(R.string.submit_order);
		mPay.setClickable(false);
		mPay.setBackgroundColor(Color.parseColor("#c3c3c3"));
		mBack.setOnClickListener(this);
		mPay.setOnClickListener(this);
		mAddressLL.setOnClickListener(this);
		mTrafficLL.setOnClickListener(this);
		mNoAddressTV.setOnClickListener(this);
		mShippingList.setOnItemClickListener(this);
		
		mDialog = new CustomProgressDialog(this, this.getResources().getString(
				R.string.dialog_info_create_order));
	}

	private void initData() {
		mCache = ACache.get(this);
		mAccountId = mUserController.getUserInfo().getId();
		mAccountToken = mUserController.getUserInfo().getToken();
		getDataFromEditPicture();
		initGetAddressList();
	}

	@SuppressWarnings("unchecked")
	private void getDataFromEditPicture() {
		Intent intent = getIntent();
		mWorksIds = (ArrayList<String>) intent
				.getSerializableExtra(MyConstants.WORKS_IDS);
		LogUtils.logd(TAG, "mWorksIds" + mWorksIds.toString());
		mListData = (ArrayList<WorksInfoBean>) DataHelper.getInstance(this)
				.queryWorks(mWorksIds);
		LogUtils.logd(TAG, "mListData" + mListData.toString());
		SubmitOrderAdapter mAdapter = new SubmitOrderAdapter(
				getApplicationContext(), mListData);
		mListView.setAdapter(mAdapter);

	}

	/**
	 * 修改DB 提交订单成功的 作品编辑状态
	 */
	private void reviseEditState2DB() {
		DataHelper.getInstance(this).reviseEditState(mWorksIds);
	}

	private void initGetAddressList() {
		NetHelper.getAddressList(mAccountId, mAccountToken,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						LogUtils.logd(TAG, "initGetAddressListonSuccess");
						resolveGetAddressList(responseBody);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "initGetAddressListonFailure");
					}
				});
	}

	private void resolveGetAddressList(byte[] responseBody) {
		try {
			String json = new String(responseBody, "UTF-8");
			JSONObject JSONObject = new JSONObject(json);
			JSONArray result = JSONObject.optJSONArray("result");
			if (result == null) {
				Toast.makeText(this, getString(R.string.no_address),
						Toast.LENGTH_SHORT).show();
				mIsNoAddress = true;
				int total = PictureUtil.getTotal(mListData);
				mNum.setText(total + "");
				mNoAddressTV.setVisibility(View.VISIBLE);
				mTotalContent.setVisibility(View.GONE);
				mTotalMoneyRL.setVisibility(View.GONE);
				mTotalPBar.setVisibility(View.GONE);
				return;
			} else if (result != null) {
				int d = -1;
				for (int i = 0; i < result.length(); i++) {
					JSONObject object = result.getJSONObject(i);
					boolean is_default = object.optBoolean("is_default");
					if (is_default) {
						d = i;
					}
				}
				JSONObject objectAdd = null;
				if (d != -1) {
					objectAdd = result.getJSONObject(d);
					mCache.put(MyConstants.SELECTED_ADDRESS, d+"");
				} else if (d == -1) {
					objectAdd = result.getJSONObject(0);
					mCache.put(MyConstants.SELECTED_ADDRESS, 0+"");
				}
				showAddress(objectAdd);
			}

		} catch (Exception e) {
			e.printStackTrace();
			mIsNoAddress = true;
			Toast.makeText(this, getString(R.string.no_address),
					Toast.LENGTH_SHORT).show();
			mNoAddressTV.setVisibility(View.VISIBLE);
		}

	}

	private void showAddress(JSONObject object) throws JSONException {
		LogUtils.logd(TAG, "object:" + object.toString());
		mNameStr = object.optString("ship_name");
		mPhoneStr = object.optString("ship_mobile");
		mDetaileAreaStr = object.optString("ship_addr");
		mHomeTelStr = object.optString("ship_tel");
		mPostcodeStr = object.optString("ship_zip");
		mAreaNetStr = object.getString("ship_area");
		String[] arr = mAreaNetStr.split("\\:");
		String arr1 = arr[1];
		mAreaStr = arr1.replaceAll("/", " ");
		mAreaId = arr[2];
		mConsignee.setText(mNameStr);
		mConsigneePhone.setText(mPhoneStr);
		mShippingAddress.setText(mAreaStr + mDetaileAreaStr);
		mIsNoAddress = false;
		mNoAddressTV.setVisibility(View.GONE);
		initGetShippingList();
	}

	private void resolveGetCheckCost(byte[] responseBody) {
		try {
			mTotalPBar.setVisibility(View.GONE);
			int total = PictureUtil.getTotal(mListData);
			mNum.setText(total + "");
			String json = new String(responseBody, "UTF-8");
			LogUtils.logd(TAG, "GetCheckCostjson:" + json);
			JSONObject jsonobject = new JSONObject(json);
			String code = jsonobject.getString("code");
			String msg = jsonobject.getString("msg");
			JSONObject result = jsonobject.optJSONObject("result");
			if ("0".equals(code)) {
				JSONObject payment_detail = result
						.optJSONObject("payment_detail");
				mTotalStr = StringUtils.format2point(payment_detail
						.optString("total_amount"));
				mTotalMoneyRL.setVisibility(View.VISIBLE);
				mTotalContent.setVisibility(View.VISIBLE);
				mPay.setClickable(true);
				mPay.setBackgroundColor(Color.parseColor("#23aeed"));
				mTotalMoney.setText(mTotalStr);
				mCache.put(MyConstants.ORDERMONEY, mTotalStr);
			} else {
				mTotalContent.setVisibility(View.GONE);
				mTotalMoneyRL.setVisibility(View.GONE);
				Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void resolveSubmitOrder(byte[] responseBody) {
		try {
			String json = new String(responseBody, "UTF-8");
			LogUtils.logd(TAG, "SubmitOrderjson:" + json);

			JSONObject jsonobject = new JSONObject(json);
			String code = jsonobject.getString("code");
			String msg = jsonobject.getString("msg");
			if ("0".equals(code)) {
				JSONObject result = jsonobject.optJSONObject("result");
				mOutTradeNo = result.optString("order_id");
				mCache.put(MyConstants.ORDERID, mOutTradeNo);
				reviseEditState2DB();

				gotoOrderPay();
			} else {
				mDialog.dismiss();
				mCoutPay = 0;
				Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initGetPrice() {
		mPay.setClickable(false);
		mPay.setBackgroundColor(Color.parseColor("#c3c3c3"));
		mCoutGetPrice++;
		LogUtils.logd(TAG, "mShippingId:" + mShippingId);
		mTotalPBar.setVisibility(View.VISIBLE);
		mTotalMoneyRL.setVisibility(View.GONE);
		String cur = "CNY";
		String is_protect = "false";
		String is_tax = "false";
		String tax_type = "false";
		String dis_point = "0";
		String payment = "malipay";
		String coupon = "";
		LogUtils.logd(TAG, "mAccountId:" + mAccountId + ";" + "mAccountToken:"
				+ mAccountToken + ";" + "cur:" + cur + ";" + "mShippingId:"
				+ mShippingId + ";" + "is_protect:" + is_protect + ";"
				+ "+ is_tax:" + is_tax + ";" + "tax_type:" + tax_type + ";"
				+ "payment:" + payment + ";" + "mAreaNetStr:" + mAreaNetStr
				+ ";" + "mAreaId:" + mAreaId + ";" + "dis_point:" + dis_point
				+ ";" + "coupon:" + coupon + ";" + "mObjects:" + mObjects);

		NetHelper.getCheckCost(mAccountId, mAccountToken, cur, mShippingId,
				is_protect, is_tax, tax_type, payment, mAreaNetStr, mAreaId,
				dis_point, coupon, mObjects, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						LogUtils.logd(TAG, "initGetAddressListonSuccess");
						resolveGetCheckCost(responseBody);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "initGetAddressListonFailure");
					}
				});
	}

	private void submitOrder() {
		mCoutPay++;
		String day = "任意日期";
		String special = "";
		String time = "任意时段";
		String payment_currency = "CNY";
		String payment_pay_app_id = "malipay";
		String payment_tex_type = "false";
		String payment_is_tax = "false";
		String payment_tax_company = "";
		String payment_tax_content = "";
		String coupon = "";
		String memo = mRemarks.getText().toString().trim();
		LogUtils.logd(TAG, "mAccountId:" + mAccountId);
		LogUtils.logd(TAG, "mAccountToken:" + mAccountToken);
		LogUtils.logd(TAG, "mAreaNetStr:" + mAreaNetStr);
		LogUtils.logd(TAG, "mDetaileAreaStr:" + mDetaileAreaStr);
		LogUtils.logd(TAG, "mPostcodeStr:" + mPostcodeStr);
		LogUtils.logd(TAG, "mNameStr:" + mNameStr);

		LogUtils.logd(TAG, "mPhoneStr:" + mPhoneStr);
		LogUtils.logd(TAG, "mShippingId:" + mShippingId);
		LogUtils.logd(TAG, "mHomeTelStr:" + mHomeTelStr);
		LogUtils.logd(TAG, "mObjects:" + mObjects);

		NetHelper.createOrders(mAccountId, mAccountToken, mAreaNetStr,
				mDetaileAreaStr, mPostcodeStr, mNameStr, mPhoneStr,
				mShippingId, mHomeTelStr, day, special, time, payment_currency,
				payment_pay_app_id, payment_is_tax, payment_tex_type,
				payment_tax_company, payment_tax_content, coupon, memo,
				mObjects, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						LogUtils.logd(TAG, "SubmitOrderSuccess");
						resolveSubmitOrder(responseBody);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "SubmitOrderFailure");
						mDialog.dismiss();
						mCoutPay = 0;
					}
				});
	}

	private void initGetShippingList() {
		NetHelper.getShippingList(mAccountId, mAccountToken, mAreaId,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						LogUtils.logd(TAG, "initGetShippingListSuccess");
						resolveShippingList(responseBody);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "initGetShippingListFailure");
					}
				});
	}

	private void resolveShippingList(byte[] responseBody) {
		try {
			JSONObject result = JSONUtil.resolveResult(responseBody);
			LogUtils.logd(TAG, result.toString());
			mShippingArray = result.optJSONArray("delivery");

			if (mShippingArray.length() != 0) {
				JSONObject object = (JSONObject) mShippingArray.get(0);
				String dt_name = object.optString("dt_name");
				String money = object.optString("money");
				mShippingId = object.optString("dt_id");
				String moneyStr;
				if ("0".equals(money)) {
					moneyStr = dt_name + "  ("
							+ this.getString(R.string.free_shipping) + ")";
				} else {
					moneyStr = dt_name + "  (" + money + "元)";
				}
				mShipping.setText(moneyStr);
				mObjects = getObjects(mListData);

				initGetPrice();

				mChoiceShippingAdapter = new ChoiceShippingListAdapter(this,
						mShippingArray);
				mShippingList.setAdapter(mChoiceShippingAdapter);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void gotoManageAddress(String data) {
		Intent intent = new Intent(SubmitOrderActivity.this,
				ManageAddressActivity.class);
		intent.putExtra("data", data.toString());
		SubmitOrderActivity.this.startActivityForResult(intent, REQUESTCODE2);
		AndroidUtils.enterActvityAnim(this);
	}

	private void gotoOrderPay() {
		Intent intent = new Intent(this, OrderPayActivity.class);
		intent.putExtra("mTotalStr", mTotalStr);
		intent.putExtra("mGoodsNameStr", mGoodsNameStr);
		intent.putExtra("mGoodsInfoStr", mGoodsInfoStr);
		intent.putExtra("mOutTradeNo", mOutTradeNo);
		SharedpreferncesUtil.setOrder(getApplication(), true);
		startActivity(intent);
		AndroidUtils.enterActvityAnim(this);
		mDialog.dismiss();
		FileUtil.deleteOrderList();
		AndroidUtils.finishActivity(this);
	}

	private void gotoAddAddress() {
		String data = null;
		Intent intent = new Intent(this, EditAddressActivity.class);
		intent.putExtra("mIsNoAddress", true);
		intent.putExtra("mIsEditAddress", false);
		intent.putExtra("data", data);
		SubmitOrderActivity.this.startActivityForResult(intent, REQUESTCODE);
		AndroidUtils.enterActvityAnim(this);
	}

	private String getObjects(ArrayList<WorksInfoBean> mListData) {
		String json = null;
		JSONArray jsonarray = new JSONArray();
		for (int i = 0; i < mListData.size(); i++) {

			WorksInfoBean worksBean = mListData.get(i);

			String goods_id = worksBean.getGoodsid();
			String product_id = worksBean.getProductid();
			String obj_ident = "goods_" + goods_id + "_" + product_id;
			String quantity = worksBean.getQuantity();
			if (StringUtils.isEmpty(quantity)) {
				quantity = "1";
			}
			String works_id = worksBean.getWorkid();

			ShoppingInfoBean shoppinginfo = new ShoppingInfoBean();
			shoppinginfo.setObj_ident(obj_ident);
			shoppinginfo.setQuantity(quantity);
			shoppinginfo.setWork_id(works_id);
			JSONObject object = JSONUtil.objectToJson(shoppinginfo);

			jsonarray.put(object);
		}
		json = jsonarray.toString();

		return json;
	}

	public class SubmitOrderAdapter extends BaseAdapter {

		private Context mContext;
		private LayoutInflater mInflater;
		private SubmitOrderHolder mHolder;
		private ArrayList<WorksInfoBean> mData;
		
		private BitmapUtils bitmapUtils;
		private BitmapDisplayConfig bigPicDisplayConfig;

		public SubmitOrderAdapter(Context mContext,
				ArrayList<WorksInfoBean> mData) {
			this.mContext = mContext;
			this.mData = mData;
			LogUtils.logd(TAG, mData.toString());
			mInflater = LayoutInflater.from(mContext);
			
			int mScreenWidth = AndroidUtils.getScreenWidth(SubmitOrderActivity.this);
			if (bitmapUtils == null) {
				bitmapUtils = BitmapHelp.getBitmapUtils(mContext);
			}

			bigPicDisplayConfig = new BitmapDisplayConfig();
			bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
			bigPicDisplayConfig.setBitmapMaxSize(new BitmapSize(mScreenWidth / 5,
					mScreenWidth / 5));
			bitmapUtils.configDefaultLoadingImage(R.drawable.blank_bg);
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.blank_bg);
			bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
			bitmapUtils.configDiskCacheEnabled(false);
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			mCout++;
			LogUtils.logd(TAG, "getViewmCout" + mCout);
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.item_submit_order_list, null);
				mHolder = new SubmitOrderHolder();
				getItemView(convertView);

				convertView.setTag(mHolder);
			} else {
				mHolder = (SubmitOrderHolder) convertView.getTag();
			}
			setData2UI(position);
			mHolder.goods_num_editTv
					.setOnNumChangeListener(new OnNumChangeListener() {

						@Override
						public void onNumChange(View view, int num) {
							WorksInfoBean works = mData.get(position);
							works.setQuantity(num + "");
							DataHelper.getInstance(mContext).addWorks(works);

							ArrayList<WorksInfoBean> mlist = DataHelper
									.getInstance(mContext)
									.queryWorks(mWorksIds);
							int total = PictureUtil.getTotal(mlist);
							mNum.setText(total + "");
							mObjects = getObjects(mlist);
							if (mCoutGetPrice > 0) {
								initGetPrice();
							}

						}
					});
			return convertView;

		}

		/**
		 * 给UI加载数据
		 * 
		 * @param position
		 */
		private void setData2UI(int position) {
			WorksInfoBean works = mData.get(position);

			String mCoverId = works.getImageurl();
			String imageurl = FileUtil.getFilePath(
					FileUtil.getWorksDir(works.getWorkid()), mCoverId,
					MyConstants.PNG);
//			mHolder.goods_imageIv.setImageURI(Uri.parse(imageurl));
			bitmapUtils.display(mHolder.goods_imageIv, imageurl, bigPicDisplayConfig);

			String goods_name = works.getGoodsname();
			mHolder.goods_nameTv.setText(goods_name);

			String goods_type = works.getType();
			mHolder.goods_typeTv.setText(goods_type);

			String quantityStr = works.getQuantity();
			int quantity = 1;
			if (!StringUtils.isEmpty(quantityStr)) {
				quantity = Integer.parseInt(works.getQuantity());
			} else {
				quantity = 1;
			}
			mHolder.goods_num_editTv.setNum(quantity);

			double price = Double.parseDouble(works.getPrice());
			String subtotalStr = StringUtils.format2point(price + "");
			String[] arr = subtotalStr.split("\\.");
			mHolder.goods_subtotal_bigTv.setText(arr[0] + ".");
			mHolder.goods_subtotal_smTv.setText(arr[1]);
		}

		/**
		 * 找到Item 的 控件
		 * 
		 * @param convertView
		 */
		private void getItemView(View convertView) {
			mHolder.goods_imageIv = (ImageView) convertView
					.findViewById(R.id.goods_image);
			mHolder.goods_nameTv = (TextView) convertView
					.findViewById(R.id.goods_name);
			mHolder.goods_typeTv = (TextView) convertView
					.findViewById(R.id.goods_type);
			mHolder.goods_subtotal_bigTv = (TextView) convertView
					.findViewById(R.id.goods_subtotal_big);
			mHolder.goods_subtotal_smTv = (TextView) convertView
					.findViewById(R.id.goods_subtotal_sm);
			mHolder.goods_num_editTv = (AddAndSubView) convertView
					.findViewById(R.id.goods_num_edit);
			mHolder.goods_num_editTv.setFocusable(false);
			mHolder.goods_num_editTv.setFocusableInTouchMode(false);
		}

	}
	
	/**
	 * 显示对话框
	 * 
	 * @param context
	 */
	private void showSystemDialog(Context context) {
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setMessage(context
				.getString(R.string.system_maintenance));
		builder.setPositiveButton(context.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (AndroidUtils.isNoFastClick()) {
							dialog.dismiss();
						}
					}
				});

		builder.setNegativeButton(context.getString(R.string.back_last_page),
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (AndroidUtils.isNoFastClick()) {
							dialog.dismiss();
							finish();
						}
					}
				});

		builder.create().show();
	}
	
}
