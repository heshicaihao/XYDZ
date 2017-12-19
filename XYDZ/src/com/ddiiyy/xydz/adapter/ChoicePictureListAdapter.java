package com.ddiiyy.xydz.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.bean.ImageBean;
/**
 * 选择图片日期的List 的适配器
 * 
 * @author heshicaihao
 */
public class ChoicePictureListAdapter extends BaseAdapter {

	private List<ImageBean> mList;
	private TreeMap<Integer, List<String>> mGroup;
	private List<String> mChildList = new ArrayList<String>();

	private Activity mActivity;
	private Context mContext;
	private LayoutInflater mInflater;
	private ChoicePictureGridAdapter mChildAdapter;

	public ChoicePictureListAdapter(Activity activity,Context context, List<ImageBean> list,
			TreeMap<Integer, List<String>> gruopMap) {
		this.mList = list;
		this.mGroup = gruopMap;
		this.mContext = context;
		this.mActivity = activity;
		mInflater = LayoutInflater.from(context);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_choice_picture_list,
					null);
			holder = new ViewHolder();
			holder.mTitle = (TextView) convertView.findViewById(R.id.date);
			holder.mGrid = (GridView) convertView.findViewById(R.id.mGrid);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageBean mImageBean = mList.get(position);
		String folderName = mImageBean.getFolderName();
		String mTitleString = folderName.substring(0, 4) + "\n"
				+ folderName.substring(4, folderName.length());
		holder.mTitle.setText(mTitleString);
		holder.mGrid.setClickable(false);
		holder.mGrid.setPressed(false);
		holder.mGrid.setEnabled(false);
		mChildList = mGroup.get(Integer.parseInt(mList.get(position)
				.getFolderName()));
		mChildAdapter = new ChoicePictureGridAdapter(mActivity,mContext, mChildList,
				holder.mGrid, mImageBean.getPaths());
		holder.mGrid.setAdapter(mChildAdapter);
		return convertView;

	}

	public static class ViewHolder {
		public TextView mTitle;
		public GridView mGrid;
	}

}
