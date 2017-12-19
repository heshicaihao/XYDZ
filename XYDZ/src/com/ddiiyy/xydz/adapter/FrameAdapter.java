package com.ddiiyy.xydz.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.xutils.BitmapHelp;
import com.ddiiyy.xydz.xutils.BitmapUtils;
import com.ddiiyy.xydz.xutils.bitmap.BitmapCommonUtils;
import com.ddiiyy.xydz.xutils.bitmap.BitmapDisplayConfig;

/**
 * 蒙版的单行适配器
 * 
 * @author heshicaihao
 */
@SuppressLint("ResourceAsColor")
public class FrameAdapter extends BaseAdapter {
	public String TAG = getClass().getName();
	private Context mContext;
	LayoutInflater mInflater;
	JSONArray Data;
	private int clickTemp = -1;// 选中的位置

	private BitmapUtils bitmapUtils;
	private BitmapDisplayConfig bigPicDisplayConfig;

	public FrameAdapter(Context c, Activity mActivity, JSONArray jsonArray
			) {
		mContext = c;
		Data = jsonArray;
		mInflater = LayoutInflater.from(mContext);

		if (bitmapUtils == null) {
			bitmapUtils = BitmapHelp.getBitmapUtils(mContext);
		}

		bigPicDisplayConfig = new BitmapDisplayConfig();
		// 图片太大时容易OOM。
		bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
		bigPicDisplayConfig.setBitmapMaxSize(BitmapCommonUtils
				.getScreenSize(mActivity));
		bitmapUtils.configDefaultLoadingImage(R.drawable.blank_bg);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.blank_bg);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		// AlphaAnimation 在一些android系统上表现不正常, 造成图片列表中加载部分图片后剩余无法加载, 目前原因不明.
		// 可以模仿下面示例里的fadeInDisplay方法实现一个颜色渐变动画。
		AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(1000);
		bitmapUtils.configDefaultImageLoadAnimation(animation);

	}

	@Override
	public int getCount() {

		return Data.length();
	}

	public void setSeclection(int position) {
		clickTemp = position;
	}

	@Override
	public Object getItem(int position) {
		try {
			return Data == null ? null : Data.get(position);
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

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_mask_grid, null);
			holder.mImg = (ImageView) convertView
					.findViewById(R.id.mask_grid_image);
			holder.mMask_ll = (LinearLayout) convertView
					.findViewById(R.id.mask_ll);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setTag(holder);
		if (clickTemp == position) {
			convertView.setBackgroundResource(R.color.main_color);
		} else {
			convertView.setBackgroundColor(Color.TRANSPARENT);
		}
		JSONObject object = Data.optJSONObject(position);
		String mImguRL = object.optString("script_setting");
//		Bitmap loadBitmap = mImageLoader.loadBitmap(holder.mImg, mImguRL);
		
//		holder.mImg.setImageBitmap(loadBitmap);
		bitmapUtils.display(holder.mImg, mImguRL,bigPicDisplayConfig);

		return convertView;
	}

	static class ViewHolder {
		ImageView mImg;
		LinearLayout mMask_ll;
	}

}
