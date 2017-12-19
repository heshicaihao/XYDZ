package com.ddiiyy.xydz.tab;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.ad.AutoScrollViewPager;
import com.ddiiyy.xydz.ad.CirclePageIndicator;
import com.ddiiyy.xydz.ad.ImagePagerAdapter;
import com.ddiiyy.xydz.adapter.TabHomeListAdapter;
import com.ddiiyy.xydz.base.BaseFragment;
import com.ddiiyy.xydz.bean.ADBean;
import com.ddiiyy.xydz.constants.MyConstants;
import com.ddiiyy.xydz.net.NetHelper;
import com.ddiiyy.xydz.utils.AndroidUtils;
import com.ddiiyy.xydz.utils.FileUtil;
import com.ddiiyy.xydz.utils.JSONUtil;
import com.ddiiyy.xydz.utils.LogUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class TabHomeFragment extends BaseFragment {

	private View mView;
	private View mHeader;
	private ImageView mBack;
	private TextView mTitle;
	private ListView mListView;
	private AutoScrollViewPager mViewPager;
	private CirclePageIndicator mIndicator;

	private int mCout = 0;
	private String mTime;
	private JSONArray mAdItems;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_tab_home, null);
		}
		ViewGroup parent = (ViewGroup) mView.getParent();
		if (parent != null) {
			parent.removeView(mView);
		}
		initView(mView);
		if (!AndroidUtils.isNetworkAvailable(getActivity())) {
			Toast.makeText(getApplication(), this.getString(R.string.no_net),
					Toast.LENGTH_SHORT).show();
			getSDinfo();
		} else {
			initData();
		}
		return mView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getSDinfo();
		mViewPager.startAutoScroll();
	}


	@Override
	public void onPause() {
		super.onPause();
		mViewPager.stopAutoScroll();
	}

	public void initView(View view) {
		mCout++;
		mTitle = (TextView) view.findViewById(R.id.title);
		mBack = (ImageView) view.findViewById(R.id.back);
		mListView = (ListView) view.findViewById(R.id.goods_listview);
		if (mCout == 1) {
			mHeader = getActivity().getLayoutInflater().inflate(
					R.layout.view_header_home, null);
			getActivity().getLayoutInflater();
			mViewPager = (AutoScrollViewPager) mHeader
					.findViewById(R.id.view_pager);
			mIndicator = (CirclePageIndicator) mHeader
					.findViewById(R.id.indicator);
			mListView.addHeaderView(mHeader);
			// setAdapter();
		}
		mTitle.setText(R.string.tab_home);
		mBack.setVisibility(View.GONE);

	}

	private void initData() {
		mTime = getTime();
		initAdInfo();
		initCatList();

	}

	private void initAdInfo() {
		int screenWidth = AndroidUtils.getScreenWidth(getActivity());
		int screenHeight = AndroidUtils.getScreenHeight(getActivity());
		String app_type = MyConstants.ANDROID;

		NetHelper.getAdInfo(screenWidth + "", screenHeight + "", mTime,
				app_type, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "getAdInfo+onSuccess");
						resolveGetAdInfo(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "getAdInfo+onFailure");
					}
				});

	}

	private void resolveGetAdInfo(byte[] responseBody) {
		try {
			String json = new String(responseBody, "UTF-8");
			LogUtils.logd(TAG, "GetAdInfo+json：" + json);

			JSONObject JSONObject = new JSONObject(json);

			String code = JSONObject.optString("code");
			if ("0".equals(code)) {
				JSONObject resolve = JSONUtil.resolveResult(responseBody);
				mAdItems = resolve.getJSONArray("carousel");
				FileUtil.saveFile(mAdItems.toString(), MyConstants.HOMEAD,
						MyConstants.HOME_AD_INFO, MyConstants.TXT);
				AdListSetAdapter(mAdItems);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initCatList() {
		NetHelper.getCatList(new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				LogUtils.logd(TAG, "getCatList+onSuccess");
				resolveGetCatList(arg2);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				LogUtils.logd(TAG, "getCatList+onFailure");
			}
		});

	}

	private void resolveGetCatList(byte[] responseBody) {

		try {
			String json = new String(responseBody, "UTF-8");
			JSONObject JSONObject = new JSONObject(json);
			JSONArray mItems = JSONObject.getJSONArray("result");
			ListSetAdapter(mItems);
			FileUtil.saveFile(mItems.toString(), MyConstants.CATLIST,
					MyConstants.CAT_LIST_INFO, MyConstants.TXT);
			LogUtils.logd(TAG, "json：" + json);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 从SD读取 缓存Json数据
	 */
	private void getSDinfo() {
		String home_adPath = FileUtil.getFilePath(MyConstants.HOMEAD,
				MyConstants.HOME_AD_INFO, MyConstants.TXT);
		if (FileUtil.fileIsExists(home_adPath)) {
			String ad_info = FileUtil.readFile(MyConstants.HOMEAD,
					MyConstants.HOME_AD_INFO, MyConstants.TXT);
			try {
				mAdItems = new JSONArray(ad_info);
				AdListSetAdapter(mAdItems);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		String goods_listpath = FileUtil.getFilePath(MyConstants.CATLIST,
				MyConstants.CAT_LIST_INFO, MyConstants.TXT);
		if (FileUtil.fileIsExists(goods_listpath)) {
			String goods_list_info = FileUtil.readFile(MyConstants.CATLIST,
					MyConstants.CAT_LIST_INFO, MyConstants.TXT);
			try {
				JSONArray mItems = new JSONArray(goods_list_info);
				ListSetAdapter(mItems);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * ListView 添加数据
	 * 
	 * @param result
	 * @throws JSONException
	 */
	private void ListSetAdapter(JSONArray items) throws JSONException {

		TabHomeListAdapter mAdapter = new TabHomeListAdapter(getActivity(),
				getApplication(), items);
		LogUtils.logd(TAG, items.toString());
		mListView.setAdapter(mAdapter);
	}

	/**
	 * 获取时间梭
	 * 
	 * @return
	 */
	private String getTime() {
		String time = null;
		String timefilePath = FileUtil.getFilePath(MyConstants.TIME,
				MyConstants.UPDATE_TIME, MyConstants.TXT);
		boolean fileIsExists = FileUtil.fileIsExists(timefilePath);
		if (fileIsExists) {
			time = FileUtil.readFile(MyConstants.TIME, MyConstants.UPDATE_TIME,
					MyConstants.TXT);
		} else {
			time = "0";
		}
		return time;
	}

	/**
	 * ListView 添加数据
	 * 
	 * @param result
	 * @throws JSONException
	 */
	private void AdListSetAdapter(JSONArray items) throws JSONException {
		setAdapter(ADBean.getDataToJson(items));
	}

	private void setAdapter(List<ADBean> imageIdList) {
		ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(
				getActivity(), getApplication(), imageIdList);
		mViewPager.setAdapter(imagePagerAdapter);
		mIndicator.setViewPager(mViewPager);
		mIndicator.setRadius(5);
		mIndicator.setOrientation(LinearLayout.HORIZONTAL);
		mIndicator.setStrokeWidth(2);
		mIndicator.setSnap(true);
		mViewPager.setInterval(5000);
		mViewPager
				.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);
		// viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_NONE);
		// viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
		mViewPager.setCycle(true);
		mViewPager.setBorderAnimation(true);
	}

}
