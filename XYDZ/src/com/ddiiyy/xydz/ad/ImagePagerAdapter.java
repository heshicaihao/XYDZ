/*
 * Copyright 2014 trinea.cn All right reserved. This software is the
 * confidential and proprietary information of trinea.cn
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with trinea.cn.
 */
package com.ddiiyy.xydz.ad;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.activity.OtherWebActivity;
import com.ddiiyy.xydz.bean.ADBean;
import com.ddiiyy.xydz.utils.StringUtils;
import com.ddiiyy.xydz.xutils.BitmapHelp;
import com.ddiiyy.xydz.xutils.BitmapUtils;
import com.ddiiyy.xydz.xutils.bitmap.BitmapCommonUtils;
import com.ddiiyy.xydz.xutils.bitmap.BitmapDisplayConfig;

/**
 * ImagePagerAdapter
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class ImagePagerAdapter extends RecyclingPagerAdapter {

	private Context context;
	private List<ADBean> imageIdList;
	private int size;
	private boolean isInfiniteLoop;
	private Activity mActivity;
	private BitmapUtils bitmapUtils;
	private BitmapDisplayConfig bigPicDisplayConfig;

	public ImagePagerAdapter(Activity mActivity, Context context,
			List<ADBean> imageIdList) {
		this.mActivity = mActivity;
		this.context = context;
		this.imageIdList = imageIdList;
		this.size = ListUtils.getSize(imageIdList);
		isInfiniteLoop = false;
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
		// Infinite loop
		return isInfiniteLoop ? Integer.MAX_VALUE : ListUtils
				.getSize(imageIdList);
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
		return isInfiniteLoop ? position % size : position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup container) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = holder.imageView = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			holder.imageView.setLayoutParams(params);
			holder.imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.i("TAG", "position " + position);
					String url = imageIdList.get(getPosition(position))
							.getUrl();
					String title = imageIdList.get(getPosition(position))
							.getTitle();
					if (!StringUtils.isEmpty(url)) {
						startOtherWeb(context, title, url);
					}
				}
			});
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		String url = imageIdList.get(getPosition(position)).getImage_url();
		if (!StringUtils.isEmpty(url)) {
			bitmapUtils.display(holder.imageView, url);
		}
		holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		return view;
	}


	/**
	 * 打开H5界面
	 * 
	 * @param context
	 */
	public void startOtherWeb(Context context, String title, String url) {
		Intent intent = new Intent(context, OtherWebActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("url", url);
		mActivity.startActivity(intent);
	}

	private static class ViewHolder {

		ImageView imageView;
	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop
	 *            the isInfiniteLoop to set
	 */
	public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}
}
