package com.ddiiyy.xydz.adapter;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.xutils.BitmapHelp;
import com.ddiiyy.xydz.xutils.BitmapUtils;
import com.ddiiyy.xydz.xutils.bitmap.BitmapCommonUtils;
import com.ddiiyy.xydz.xutils.bitmap.BitmapDisplayConfig;

/**
 * 商品详情 List 的适配器
 * 
 * @author heshicaihao
 */
public class GoodsDetailsListAdapter extends BaseAdapter {

	private JSONArray mData;

	private LayoutInflater mInflater;

	private BitmapUtils bitmapUtils;
	private BitmapDisplayConfig bigPicDisplayConfig;

	public GoodsDetailsListAdapter(Context context, Activity mActivity,
			JSONArray jsonArray) {
		this.mData = jsonArray;
		mInflater = LayoutInflater.from(context);
		if (bitmapUtils == null) {
			bitmapUtils = BitmapHelp.getBitmapUtils(context);
		}

		bigPicDisplayConfig = new BitmapDisplayConfig();
		// 图片太大时容易OOM。
		bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
		bigPicDisplayConfig.setBitmapMaxSize(BitmapCommonUtils
				.getScreenSize(mActivity));
		bitmapUtils.configDefaultLoadingImage(R.drawable.wait_im);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.wait_im);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		// AlphaAnimation 在一些android系统上表现不正常, 造成图片列表中加载部分图片后剩余无法加载, 目前原因不明.
		// 可以模仿下面示例里的fadeInDisplay方法实现一个颜色渐变动画。
		AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(1000);
		bitmapUtils.configDefaultImageLoadAnimation(animation);

	}

	@Override
	public int getCount() {
		return mData.length();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_goods_details_list,
					null);
			holder = new ViewHolder();
			holder.imageview = (ImageView) convertView
					.findViewById(R.id.goods_imageview);
			holder.imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
			holder.imageview.setAdjustViewBounds(true);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
			if (mData.length() != 0) {
				String imgUrl = (String) mData.get(position);
				if (!TextUtils.isEmpty(imgUrl)) {
					bitmapUtils.display(holder.imageview, imgUrl);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return convertView;

	}

	public static class ViewHolder {
		public ImageView imageview;
	}

}
