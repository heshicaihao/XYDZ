package com.ddiiyy.xydz.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.utils.AndroidUtils;
import com.ddiiyy.xydz.utils.StringUtils;
import com.ddiiyy.xydz.xutils.BitmapHelp;
import com.ddiiyy.xydz.xutils.BitmapUtils;
import com.ddiiyy.xydz.xutils.bitmap.BitmapDisplayConfig;
import com.ddiiyy.xydz.xutils.bitmap.core.BitmapSize;

public class OrderDetailsAdapter extends BaseAdapter {
	private JSONArray mData;

	private Context mContext;
	private LayoutInflater mInflater;
	private ViewHolder mHolder;
	
	private BitmapUtils bitmapUtils;
	private BitmapDisplayConfig bigPicDisplayConfig;

	public OrderDetailsAdapter(Activity mActivity, Context context,
			JSONArray mData) {
		this.mData = mData;
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		
		int  mScreenWidth = AndroidUtils.getScreenWidth(mActivity);
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
//		bitmapUtils.configDiskCacheEnabled(false);

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
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_tab_order_child_list,
					null);
			mHolder = new ViewHolder();
			getItemView(convertView);

			convertView.setTag(mHolder);

		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		setData2UI(position);

		return convertView;

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
			JSONObject object = (JSONObject) mData.get(position);
			String url = object.optString("goods_pic");
			if (!StringUtils.isEmpty(url)) {
				bitmapUtils.display(mHolder.goods_image, url, bigPicDisplayConfig);
//				String enURlurl = FileUtil.getenURl(url);
//				String mPath = FileUtil.getFilePath(MyConstants.CACHE_DIR,
//						enURlurl, MyConstants.JPEG);
//				if (FileUtil.fileIsExists(mPath)) {
//					Bitmap mBitmap = FileUtil.getSDCardImg(mPath);
//					mHolder.goods_image.setImageBitmap(mBitmap);
//				} else {
//					loadImageView(url, enURlurl, mHolder.goods_image);
//				}
			}

			String goods_name = object.optString("goods_name");
			mHolder.goods_name.setText(goods_name);
			mHolder.goods_type.setText("");
			String goods_price = object.optString("price");
			String format2point = StringUtils.format2point(goods_price);
			String quantity = object.optString("quantity");
			mHolder.num_add_price.setText("¥" + format2point + "x" + quantity);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 找到Item 的 控件
	 * 
	 * @param convertView
	 */
	private void getItemView(View convertView) {
		mHolder.goods_image = (ImageView) convertView
				.findViewById(R.id.goods_image);
		mHolder.goods_name = (TextView) convertView
				.findViewById(R.id.goods_name);
		mHolder.goods_type = (TextView) convertView
				.findViewById(R.id.goods_type);
		mHolder.num_add_price = (TextView) convertView
				.findViewById(R.id.num_add_price);
	}

	/**
	 * 从网上获取Mask 图片 ，并保存到SD卡。
	 * 
	 * @param mMaskUrl
	 */
//	private void loadImageView(String mMaskUrl, final String enURlurl,
//			final ImageView imageview) {
//		ImageLoader.getInstance().loadImage(mMaskUrl,
//				new ImageLoadingListener() {
//					@Override
//					public void onLoadingStarted(String arg0, View arg1) {
//
//					}
//
//					@Override
//					public void onLoadingFailed(String arg0, View arg1,
//							FailReason arg2) {
//
//					}
//
//					@Override
//					public void onLoadingComplete(String arg0, View arg1,
//							Bitmap arg2) {
//						imageview.setImageBitmap(arg2);
//						FileUtil.saveBitmap2Jpeg(arg2, MyConstants.CACHE_DIR,
//								enURlurl, MyConstants.JPEG);
//
//					}
//
//					@Override
//					public void onLoadingCancelled(String arg0, View arg1) {
//
//					}
//
//				});
//	}

	public class ViewHolder {

		public ImageView goods_image = null;
		public TextView goods_name = null;
		public TextView goods_type = null;
		public TextView num_add_price = null;

	}

}
