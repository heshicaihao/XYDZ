package com.ddiiyy.xydz.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.activity.EditPictureActivity;
import com.ddiiyy.xydz.cache.ACache;
import com.ddiiyy.xydz.constants.MyConstants;
import com.ddiiyy.xydz.utils.AndroidUtils;
import com.ddiiyy.xydz.utils.LogUtils;
import com.ddiiyy.xydz.xutils.BitmapHelp;
import com.ddiiyy.xydz.xutils.BitmapUtils;
import com.ddiiyy.xydz.xutils.bitmap.BitmapDisplayConfig;
import com.ddiiyy.xydz.xutils.bitmap.core.BitmapSize;

/**
 * 选择图片每一个日期的Grid 的适配器
 * 
 * @author heshicaihao
 */
public class ChoicePictureGridAdapter extends BaseAdapter {
	private String TAC = "ChoicePictureGridAdapter";

	private int mMinPicWidth = MyConstants.INIT_MINWIDTH;
	private int mMinPicHeight = MyConstants.INIT_MINHEIGHT;
	private int mScreenWidth;
	private String mPath;
	
	private LayoutInflater mInflater;
	private List<String> mList;
	private List<String> mPaths;
	private Activity mActivity;
	private Context mContext;
	private ACache mCache;
	private BitmapUtils bitmapUtils;
	private BitmapDisplayConfig bigPicDisplayConfig;
	@SuppressWarnings("unused")
	private GridView mGridView;

	public ChoicePictureGridAdapter(Activity activity, Context context,
			List<String> list, GridView gridView, List<String> Paths) {
		this.mList = list;
		this.mGridView = gridView;
		this.mContext = context;
		this.mActivity = activity;
		this.mPaths = Paths;
		this.mInflater = LayoutInflater.from(context);
		this.mCache = ACache.get(mContext);
		this.mScreenWidth = AndroidUtils.getScreenWidth(mActivity);

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
		int count = mList == null ? 0 : mList.size();
		return count;
	}

	@Override
	public Object getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_choice_picture_grid,
					null);
			holder = new ViewHolder();
			holder.mImageView = (ImageView) convertView
					.findViewById(R.id.grid_image);
			holder.mGridItem = (LinearLayout) convertView
					.findViewById(R.id.grid_item);

			LayoutParams lp = holder.mImageView.getLayoutParams();
			lp.width = mScreenWidth / 4;
			lp.height = lp.width;
			holder.mImageView.setLayoutParams(lp);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		mPath = mList.get(position);
		holder.mGridItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (AndroidUtils.isNoFastClick()) {
//					Bitmap mPicBitmap = FileUtil.getSDCardImg02(mPaths
//							.get(position));
//					int outWidth = mPicBitmap.getWidth() * 30;
//					int outHeight = mPicBitmap.getHeight() * 30;
					
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(mPaths.get(position), options);
					int outWidth = options.outWidth;
					int outHeight = options.outHeight;

					if (mCache.getAsString(MyConstants.MINWIDTH) != null) {
						mMinPicWidth = Integer.parseInt(mCache
								.getAsString(MyConstants.MINWIDTH));
					}
					if (mCache.getAsString(MyConstants.MINHEIGHT) != null) {
						mMinPicHeight = Integer.parseInt(mCache
								.getAsString(MyConstants.MINHEIGHT));
					}
					if (outWidth > mMinPicWidth && outHeight > mMinPicHeight) {
						Intent intent = new Intent();
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("Path", mPaths.get(position));
						mActivity.setResult(EditPictureActivity.RESULT_OK,
								intent);
						AndroidUtils.finishActivity(mActivity);
					} else {
						Toast.makeText(
								mContext,
								mContext.getString(R.string.choice_picture_again),
								Toast.LENGTH_SHORT).show();
					}
					LogUtils.logd(TAC, "outWidth:" + outWidth);
					LogUtils.logd(TAC, "outHeight:" + outHeight);
					LogUtils.logd(TAC, "mMinPicWidth:" + mMinPicWidth);
					LogUtils.logd(TAC, "mMinPicHeight:" + mMinPicHeight);

				}
			}
		});

		bitmapUtils.display(holder.mImageView, mPath, bigPicDisplayConfig);
		return convertView;
	}

	public static class ViewHolder {
		private ImageView mImageView;
		private LinearLayout mGridItem;
	}

}
