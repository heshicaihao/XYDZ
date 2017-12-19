package com.ddiiyy.xydz.tab;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddiiyy.xydz.MainActivity;
import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.activity.OrderDetailsActivity;
import com.ddiiyy.xydz.activity.OrderPayActivity;
import com.ddiiyy.xydz.adapter.OrderHolder;
import com.ddiiyy.xydz.adapter.TabOrderChildAdapter;
import com.ddiiyy.xydz.base.BaseFragment;
import com.ddiiyy.xydz.bean.OrderBean;
import com.ddiiyy.xydz.bean.UserBean;
import com.ddiiyy.xydz.common.UserController;
import com.ddiiyy.xydz.constants.MyConstants;
import com.ddiiyy.xydz.dialog.CustomDialog;
import com.ddiiyy.xydz.listener.OnRefreshListener;
import com.ddiiyy.xydz.net.NetHelper;
import com.ddiiyy.xydz.utils.AndroidUtils;
import com.ddiiyy.xydz.utils.FileUtil;
import com.ddiiyy.xydz.utils.JSONUtil;
import com.ddiiyy.xydz.utils.LogUtils;
import com.ddiiyy.xydz.utils.SharedpreferncesUtil;
import com.ddiiyy.xydz.utils.StringUtils;
import com.ddiiyy.xydz.widget.MyListView;
import com.ddiiyy.xydz.widget.RefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class TabOrderFragment extends BaseFragment implements OnClickListener,
		OnRefreshListener {

	private View mView;
	private RefreshListView mListView;
	private RelativeLayout mViewNull;
	private LinearLayout mContentLl;
	private TextView mTitle;
	private ImageView mBack;
	private Button mGoSee;
	private ImageView mPromptImage;
	private TextView mPromptInfo;
	private boolean mIsNoNull = false;
	private TabOrderAdapter mAdapter;
	private int mPage = 1;
	private int mPageCout = 5;
	private String pager_total = "1";
	private List<OrderBean> orderList = new ArrayList<OrderBean>();
	private UserBean mUser;
	private String goods_list = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_tab_order, null);
		}
		ViewGroup parent = (ViewGroup) mView.getParent();
		if (parent != null) {
			parent.removeView(mView);
		}
		mUser = UserController.getInstance(getActivity()).getUserInfo();
		mIsNoNull = SharedpreferncesUtil.getOrder(getApplication());
		LogUtils.logd(TAG, "mUser.isIs_login()" + mUser.isIs_login());
		LogUtils.logd(TAG, "mIsNoNull:" + mIsNoNull);
		initView(mView);
		if (!AndroidUtils.isNetworkAvailable(getActivity())) {
			Toast.makeText(getApplication(), this.getString(R.string.no_net),
					Toast.LENGTH_SHORT).show();
		}
		initData();

		return mView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mPage = 1;
		initData();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.go_see:
			Intent intent = new Intent(getApplication(), MainActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	public void initView(View view) {
		mTitle = (TextView) view.findViewById(R.id.title);
		mListView = (RefreshListView) view.findViewById(R.id.listview);
		mViewNull = (RelativeLayout) view.findViewById(R.id.in_view_null);
		mGoSee = (Button) view.findViewById(R.id.go_see);
		mPromptImage = (ImageView) view.findViewById(R.id.prompt_image);
		mBack = (ImageView) view.findViewById(R.id.back);
		mPromptInfo = (TextView) view.findViewById(R.id.prompt_info);
		mContentLl = (LinearLayout) view.findViewById(R.id.content_ll);

		mTitle.setText(R.string.my_order);
		mBack.setVisibility(View.GONE);
		mPromptImage.setImageResource(R.drawable.order_null);
		mPromptInfo.setText(this.getString(R.string.no_order_info));
		if (mUser.isIs_login() && mIsNoNull) {
			mViewNull.setVisibility(View.GONE);
			mContentLl.setVisibility(View.VISIBLE);
		} else {
			mViewNull.setVisibility(View.VISIBLE);
			mContentLl.setVisibility(View.GONE);
		}

		mAdapter = new TabOrderAdapter(getActivity(), getApplication());
		mListView.setAdapter(mAdapter);

		mGoSee.setOnClickListener(this);
		mListView.setOnRefreshListener(this);

	}



	private void initData() {
		if (mUser.isIs_login() && mIsNoNull) {
			String order_listfilepath = FileUtil.getFilePath(
					MyConstants.ORDERLIST, MyConstants.ORDER_LIST_INFO,
					MyConstants.TXT);
			boolean fileIsExists = FileUtil.fileIsExists(order_listfilepath);
			if (!fileIsExists) {
				if (AndroidUtils.isNetworkAvailable(getActivity())) {
					showmeidialog();
					initData(true);
				} else {
					Toast.makeText(getApplication(),
							this.getString(R.string.no_net), Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				goods_list = FileUtil.readFile(MyConstants.ORDERLIST,
						MyConstants.ORDER_LIST_INFO, MyConstants.TXT);
				try {
					JSONArray arr = new JSONArray(goods_list);
					List<OrderBean> list = JSONUtil.getOrderList(arr);
					if (list != null && list.size() > 0) {
						if (orderList.isEmpty()) {
							orderList.addAll(list);
						}
						mAdapter.setData(orderList);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void initData(boolean flag) {

		if (mUser.isIs_login()) {
			getOrderInfo(flag);
		}
	}

	private void getOrderInfo(final boolean flag) {
		NetHelper.getOrderList(mUser.getId(), mUser.getToken(), "all", mPage,
				mPageCout, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "getOrderList+onSuccess");
						resolveOrderList(flag, arg2);
						dismissmeidialog();
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "getOrderList+onFailure");
						dismissmeidialog();
					}
				});
	}

	private void resolveOrderList(boolean flag, byte[] arg2) {
		try {
			String json = new String(arg2, "UTF-8");
			JSONObject obj = new JSONObject(json);
			JSONObject result = obj.optJSONObject("result");
			pager_total = result.optString("pager_total");
			JSONArray arr = result.getJSONArray("orderData");
			if (mPage == 1) {
				FileUtil.saveFile(arr.toString(), MyConstants.ORDERLIST,
						MyConstants.ORDER_LIST_INFO, MyConstants.TXT);
			}
			List<OrderBean> list = JSONUtil.getOrderList(arr);
			if (list != null && list.size() > 0) {
				if (flag) {
					orderList.clear();
				}
				mPage++;

				orderList.addAll(list);
				mAdapter.setData(orderList);
				mAdapter.notifyDataSetChanged();
				mListView.hideHeaderView();
				mListView.hideFooterView();
			} else {
				mPage = 0;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			dismissmeidialog();
		} catch (JSONException e) {
			e.printStackTrace();
			dismissmeidialog();
		}
	}

	@Override
	public void onDownPullRefresh() {
		mPage = 1;
		initData(true);
	}

	@Override
	public void onLoadingMore() {
		if (mPage > Integer.parseInt(pager_total)) {
			mListView.hideFooterView();
			if (AndroidUtils.isNoFastClick()) {
				Toast.makeText(getApplication(),
						this.getString(R.string.no_more), Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			initData(false);
		}
	}

	public class TabOrderAdapter extends BaseAdapter {
		public String TAG = getClass().getName();

		private Context mContext;
		private Activity mActivity;
		private List<OrderBean> mData;
		private LayoutInflater mInflater;
		private OrderHolder mHolder;
		private TabOrderChildAdapter mChildAdapter;

		public TabOrderAdapter(Activity mActivity, Context context) {
			this.mContext = context;
			this.mActivity = mActivity;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			int cout = 0;
			if (mData == null) {
				cout = 0;
			} else {
				cout = mData.size();
			}
			return cout;
		}

		/**
		 * 设置数据
		 * 
		 * @param mData
		 */
		public void setData(List<OrderBean> mData) {
			this.mData = mData;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {

				convertView = mInflater.inflate(R.layout.item_tab_order_list,
						null);
				mHolder = new OrderHolder();
				getItemView(convertView);
				convertView.setTag(mHolder);
			} else {
				mHolder = (OrderHolder) convertView.getTag();
			}
			setData2UI(position);
			setListener(position);

			return convertView;
		}

		/**
		 * 找到Item 的 控件
		 * 
		 * @param convertView
		 */
		private void getItemView(View convertView) {
			mHolder.time = (TextView) convertView.findViewById(R.id.time);
			mHolder.payment_status = (TextView) convertView
					.findViewById(R.id.payment_status);
			mHolder.child_listview = (MyListView) convertView
					.findViewById(R.id.child_listview);
			mHolder.num = (TextView) convertView.findViewById(R.id.num);
			mHolder.total = (TextView) convertView.findViewById(R.id.total);
			mHolder.cancel_order_btn = (TextView) convertView
					.findViewById(R.id.cancel_order_btn);
			mHolder.pay_btn = (TextView) convertView.findViewById(R.id.pay_btn);

		}

		/**
		 * 给UI加载数据
		 * 
		 * @param position
		 * @param deleteListener
		 * @param editListener
		 */
		private void setData2UI(int position) {
			try {
				OrderBean object = mData.get(position);
				String createtimeStr = object.getCreatetime();
				long createtime = Long.parseLong(createtimeStr);
				String time = StringUtils.longToDate(createtime);
				mHolder.time.setText(time);
				String pay_status = object.getPay_status();
				String order_status = object.getStatus();
				if (mContext.getString(R.string.cancelled).equals(order_status)) {
					mHolder.payment_status.setText(order_status);
					mHolder.cancel_order_btn.setVisibility(View.GONE);
					mHolder.pay_btn.setVisibility(View.GONE);
				} else {
					mHolder.payment_status.setText(pay_status);
					if (mContext.getString(R.string.paymented).equals(
							pay_status)) {
						mHolder.cancel_order_btn.setVisibility(View.GONE);
						mHolder.pay_btn.setVisibility(View.GONE);
					} else {
						mHolder.cancel_order_btn.setVisibility(View.VISIBLE);
						mHolder.pay_btn.setVisibility(View.VISIBLE);
					}
				}
				String itemnum = object.getItemnum();
				mHolder.num.setText(itemnum);
				String amount = object.getAmount();
				String format2point = StringUtils.format2point(amount);
				mHolder.total.setText("¥" + format2point);
				String itemStr = object.getItem();
				JSONArray itemArray = new JSONArray(itemStr);
				mChildAdapter = new TabOrderChildAdapter(mActivity, mContext,
						itemArray);
				mHolder.child_listview.setAdapter(mChildAdapter);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		/**
		 * item 中的控件设置点击事件
		 * 
		 * @param position
		 */
		private void setListener(final int position) {
			mHolder.cancel_order_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showDialog(position);
				}

			});
			mHolder.pay_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					gotoOrderPayActivity(position);
				}

			});
			mHolder.child_listview
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							gotoOrderDetailsActivity(position);
						}
					});
		}

		/**
		 * 跳转到 OrderDetailsActivity
		 * 
		 * @param position
		 */
		private void gotoOrderDetailsActivity(final int position) {
			OrderBean object = mData.get(position);
			String mOutTradeNo = object.getOrder_id();
			Intent intent = new Intent(mContext, OrderDetailsActivity.class);
			intent.putExtra("mOutTradeNo", mOutTradeNo);

			mActivity.startActivity(intent);
			AndroidUtils.enterActvityAnim(mActivity);
		}

		/**
		 * 跳转到 OrderPayActivity
		 * 
		 * @param position
		 */
		private void gotoOrderPayActivity(final int position) {
			OrderBean object = mData.get(position);
			String amount = object.getAmount();
			String mTotalStr = StringUtils.format2point(amount);
			String mOutTradeNo = object.getOrder_id();

			Intent intent = new Intent(mContext, OrderPayActivity.class);
			intent.putExtra("mTotalStr", mTotalStr);
			intent.putExtra("mGoodsNameStr", "");
			intent.putExtra("mGoodsInfoStr", "");
			intent.putExtra("mOutTradeNo", mOutTradeNo);

			mActivity.startActivity(intent);
			AndroidUtils.enterActvityAnim(mActivity);
		}

		/**
		 * 显示对话框
		 * 
		 * @param context
		 */
		private void showDialog(final int position) {
			CustomDialog.Builder builder = new CustomDialog.Builder(mActivity);
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
								netCancelOrder(position);
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
		private void netCancelOrder(int position) {
			OrderBean object = mData.get(position);
			String order_id = object.getOrder_id();
			UserBean mUser = UserController.getInstance(mContext).getUserInfo();
			NetHelper.cancelOrder(mUser.getId(), mUser.getToken(), order_id,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							LogUtils.logd(TAG, "onSuccess");
							resolvecancelOrder(arg2);

						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
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
					mPage = 1;
					initData(true);
					Toast.makeText(mContext,
							mContext.getString(R.string.cancel_order_ok),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
