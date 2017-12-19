package com.ddiiyy.xydz.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.adapter.FirstPopupHolder;
import com.ddiiyy.xydz.adapter.GoodsDetailsListAdapter;
import com.ddiiyy.xydz.adapter.SecondPopupHolder;
import com.ddiiyy.xydz.base.BaseActivity;
import com.ddiiyy.xydz.net.NetHelper;
import com.ddiiyy.xydz.utils.AndroidUtils;
import com.ddiiyy.xydz.utils.LogUtils;
import com.ddiiyy.xydz.utils.StringUtils;
import com.ddiiyy.xydz.widget.SildingFinishLayout;
import com.ddiiyy.xydz.widget.SildingFinishLayout.OnSildingFinishListener;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 商品详情
 * 
 * @author heshicaihao
 * 
 */
public class GoodsDetailsActivity extends BaseActivity implements
		OnClickListener {

	private int mFirstPosition = 0;
	private int mSecondPosition = 0;
	private String mCatId;
	private String mDataIntent;

	private View mMenuView;
	private ImageView mBack;
	private TextView mTitle;
	private TextView mCustomMade;
	private TextView mOKBtn;
	private ListView mListView;
	private GridView mFirstGridview;
	private GridView mSecondGridview;

	private MyPopupWindow mPopupWindow;
	private JSONArray mDataPopupWindow = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_details);
		initView();
		initData();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			AndroidUtils.finishActivity(this);
			break;
		case R.id.custom_made_btn:
			if (mDataPopupWindow != null) {
				mPopupWindow = new MyPopupWindow(GoodsDetailsActivity.this,
						itemsOnClick);
				mPopupWindow.showAtLocation(GoodsDetailsActivity.this
						.findViewById(R.id.custom_made_btn), Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0);
			} else {
				Toast.makeText(GoodsDetailsActivity.this,
						getString(R.string.temporary_no_data),
						Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}

	private void initView() {
		mTitle = (TextView) findViewById(R.id.title);
		mBack = (ImageView) findViewById(R.id.back);
		mCustomMade = (TextView) findViewById(R.id.custom_made_btn);
		mListView = (ListView) findViewById(R.id.goods_details_listview);

		mCustomMade.setClickable(false);
		mCustomMade.setBackgroundColor(Color.parseColor("#c3c3c3"));

		setSildingFinish();

		mCustomMade.setOnClickListener(this);
		mBack.setOnClickListener(this);
	}


	private void initData() {
		getDataIntent();
		initGoodsList();
	}

	private void getDataIntent() {
		Intent intent = getIntent();
		mCatId = intent.getStringExtra("cat_id");
		LogUtils.logd(TAG, "cat_id:" + mCatId);

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
						GoodsDetailsActivity.this.finish();
					}
				});

		mSildingFinishLayout.setTouchView(mListView);
	}

	private void initGoodsList() {
		NetHelper.getNewGoodsList(mCatId, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				LogUtils.logd(TAG, "getNewGoodsList+onSuccess");
				resolveNewGoodsList(responseBody);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				LogUtils.logd(TAG, "getNewGoodsList+onFailure");
			}
		});
	}

	private void resolveNewGoodsList(byte[] responseBody) {
		try {
			String json = new String(responseBody, "UTF-8");
			LogUtils.logd(TAG, "NewGoodsList+json:" + json);
			JSONObject JSONObject = new JSONObject(json);
			JSONObject result = JSONObject.optJSONObject("result");
			JSONObject cat = result.optJSONObject("cat");
			String cat_name = cat.optString("cat_name");
			mTitle.setText(cat_name);
			JSONArray content = cat.optJSONArray("content");
			GoodsDetailsListAdapter mAdapter = new GoodsDetailsListAdapter(
					this, GoodsDetailsActivity.this, content);
			mListView.setAdapter(mAdapter);
			mDataPopupWindow = result.optJSONArray("items");
			if (mDataPopupWindow.length() != 0) {
				mCustomMade.setClickable(true);
				mCustomMade.setBackgroundColor(Color.parseColor("#E34250"));
			} else {
				Toast.makeText(GoodsDetailsActivity.this,
						getString(R.string.temporary_no_data),
						Toast.LENGTH_SHORT).show();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.ok_btn:
				LogUtils.logd(TAG, "ok_btn:" + mSecondPosition);
				LogUtils.logd(TAG, "_ok_btn_data:" + mDataIntent);
				if (!StringUtils.isEmpty(mDataIntent)) {
					gotoEditPicture(mDataIntent);
					mPopupWindow.dismiss();
					mDataIntent = null;
				} else {
					Toast.makeText(GoodsDetailsActivity.this,
							getString(R.string.please_choose_model),
							Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
		}
	};

	private void gotoEditPicture(String data) {
		Intent intent = new Intent(this, EditPictureActivity.class);
		intent.putExtra("data", data);
		startActivity(intent);
		AndroidUtils.enterActvityAnim(this);
	}

	/**
	 * 弹出层 选材质 选型号
	 * 
	 * @author heshicaihao
	 * 
	 */
	class MyPopupWindow extends PopupWindow {

		public FirstPopupGridAdapter firstAdapter;
		public SecondPopupGridAdapter secondAdapter;
		public Activity context;

		public MyPopupWindow(Activity context, OnClickListener itemsOnClick) {
			super(context);
			this.context = context;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mMenuView = inflater.inflate(
					R.layout.view_popup_window_goods_detaile, null);
			initView(mMenuView, itemsOnClick);
		}

		/**
		 * 找ID
		 * 
		 * @param view
		 * @param itemsOnClick
		 */
		private void initView(View view, OnClickListener itemsOnClick) {
			mOKBtn = (TextView) view.findViewById(R.id.ok_btn);
			mFirstGridview = (GridView) view
					.findViewById(R.id.material_gridview);
			mSecondGridview = (GridView) view.findViewById(R.id.model_gridview);
			mOKBtn.setOnClickListener(itemsOnClick);
			setPopupWindow();
			setMenuTouch();

			if (mDataPopupWindow.length() != 0) {
				firstAdapter = new FirstPopupGridAdapter(context,
						mDataPopupWindow);
				mFirstGridview.setAdapter(firstAdapter);

			}

			try {
				JSONObject object = (JSONObject) mDataPopupWindow.get(0);
				JSONArray goods_items = object.optJSONArray("goods_items");
				if (goods_items.length() != 0) {
					secondAdapter = new SecondPopupGridAdapter(context,
							goods_items);
					mSecondGridview.setAdapter(secondAdapter);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			setFristOnItemClick();
			setSecondOnItemClick();

		}

		/**
		 * 设置 型号的 ItemClick 事件
		 */
		private void setSecondOnItemClick() {

			mSecondGridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					for (int i = 0; i < parent.getCount(); i++) {
						TextView v = (TextView) parent.getChildAt(i);
						if (position == i) {// 当前选中的Item改变背景颜色
							v.setBackgroundResource(R.drawable.popup_window_btn_bg);
							v.setTextColor(Color.parseColor("#FFFFFF"));
						} else {
							v.setBackgroundColor(Color.TRANSPARENT);
							v.setTextColor(Color.parseColor("#3B3B3B"));
						}
					}
					mSecondPosition = position;
					LogUtils.logd(TAG, "mSecondPosition:" + mSecondPosition);
					try {
						JSONObject object = (JSONObject) mDataPopupWindow
								.get(mFirstPosition);
						JSONArray goods_items = object
								.optJSONArray("goods_items");
						JSONObject object02 = (JSONObject) goods_items
								.get(position);
						String goods_id = object02.optString("goods_id");
						mDataIntent = getData("10001", goods_id, "1");
						LogUtils.logd(TAG, "data:" + mDataIntent);

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}

		/**
		 * 设置 材质的 ItemClick 事件
		 */
		private void setFristOnItemClick() {
			mFirstGridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					mDataIntent = null;
					mFirstPosition = position;
					LogUtils.logd(TAG, "mFirstPosition:" + mFirstPosition);
					try {
						JSONObject object = (JSONObject) mDataPopupWindow
								.get(position);
						JSONArray goods_items = object
								.optJSONArray("goods_items");
						if (goods_items != null) {
							if (goods_items.length() != 0) {
								secondAdapter = new SecondPopupGridAdapter(
										context, goods_items);
								mSecondGridview.setAdapter(secondAdapter);
							}
						}
						firstAdapter.setSeclection(position);
						firstAdapter.notifyDataSetInvalidated();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}

		/**
		 * 拼Json 给编辑图片页
		 * 
		 * @param code
		 * @param goods_id
		 * @return
		 */
		private String getData(String code, String goods_id, String number) {
			JSONObject object = new JSONObject();
			try {
				object.put("code", code);
				JSONObject contentJson = new JSONObject();
				contentJson.put("goods_id", goods_id);
				contentJson.put("number", number);
				object.put("content", contentJson);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			String data = object.toString();
			return data;
		}

		/**
		 * 设置Touch PopupWindow
		 */
		private void setMenuTouch() {
			// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
			mMenuView.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {

					int height = mMenuView.findViewById(R.id.pop_layout)
							.getTop();
					int y = (int) event.getY();
					if (event.getAction() == MotionEvent.ACTION_UP) {
						if (y < height) {
							mDataIntent = null;
							dismiss();
						}
					}
					return true;
				}
			});
		}

		/**
		 * 设置PopupWindow
		 */
		@SuppressWarnings("deprecation")
		private void setPopupWindow() {
			// 设置SelectPicPopupWindow的View
			this.setContentView(mMenuView);
			// 设置SelectPicPopupWindow弹出窗体的宽
			this.setWidth(LayoutParams.FILL_PARENT);
			// 设置SelectPicPopupWindow弹出窗体的高
			this.setHeight(LayoutParams.WRAP_CONTENT);
			// 设置SelectPicPopupWindow弹出窗体可点击
			this.setFocusable(true);
			// 设置SelectPicPopupWindow弹出窗体动画效果
			this.setAnimationStyle(R.style.AnimBottom);
			// 实例化一个ColorDrawable颜色为半透明
			ColorDrawable dw = new ColorDrawable(0xb0000000);
			// 设置SelectPicPopupWindow弹出窗体的背景
			this.setBackgroundDrawable(dw);
		}

	}

	/**
	 * 材质 内容的 适配器
	 * 
	 * @author PC
	 * 
	 */
	class FirstPopupGridAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		@SuppressWarnings("unused")
		private Context mContext;
		private JSONArray mList;
		private int clickTemp = 0;// 选中的位置

		public FirstPopupGridAdapter(Context context, JSONArray list) {
			this.mContext = context;
			this.mList = list;
			this.mInflater = LayoutInflater.from(context);
		}

		public void setSeclection(int position) {
			clickTemp = position;
		}

		@Override
		public int getCount() {
			int count = mList == null ? 0 : mList.length();
			return count;
		}

		@Override
		public Object getItem(int position) {
			try {
				return mList == null ? null : mList.get(position);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FirstPopupHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.item_popup_window_grid, null);
				holder = new FirstPopupHolder();
				holder.firstContentTv = (TextView) convertView
						.findViewById(R.id.content);
				convertView.setTag(holder);
			} else {
				holder = (FirstPopupHolder) convertView.getTag();
			}
			if (clickTemp == position) {
				holder.firstContentTv
						.setBackgroundResource(R.drawable.popup_window_btn_bg);
				holder.firstContentTv.setTextColor(Color.parseColor("#FFFFFF"));
			} else {
				holder.firstContentTv.setBackgroundColor(Color.TRANSPARENT);
				holder.firstContentTv.setTextColor(Color.parseColor("#3B3B3B"));
			}

			try {
				JSONObject object = (JSONObject) mList.get(position);
				JSONObject cat_item = object.optJSONObject("cat_item");
				String cat_name = cat_item.optString("cat_name");
				if (!StringUtils.isEmpty(cat_name)) {
					holder.firstContentTv.setText(cat_name);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return convertView;
		}

	}

	/**
	 * 型号 内容的 适配器
	 * 
	 * @author PC
	 * 
	 */
	class SecondPopupGridAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		@SuppressWarnings("unused")
		private Context mContext;
		private JSONArray mList;

		public SecondPopupGridAdapter(Context context, JSONArray list) {
			this.mContext = context;
			this.mList = list;
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			int count = mList == null ? 0 : mList.length();
			return count;
		}

		@Override
		public Object getItem(int position) {
			try {
				return mList == null ? null : mList.get(position);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SecondPopupHolder holder = null;

			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.item_popup_window_grid_two, null);
				holder = new SecondPopupHolder();
				holder.secondContentTv = (TextView) convertView
						.findViewById(R.id.content);
				convertView.setTag(holder);
			} else {
				holder = (SecondPopupHolder) convertView.getTag();
			}
			try {
				JSONObject object = (JSONObject) mList.get(position);
				String title = object.optString("title");
				if (!StringUtils.isEmpty(title)) {
					holder.secondContentTv.setText(title);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return convertView;
		}
	}

}
