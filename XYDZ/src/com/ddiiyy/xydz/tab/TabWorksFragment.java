package com.ddiiyy.xydz.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddiiyy.xydz.MainActivity;
import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.activity.EditPictureActivity;
import com.ddiiyy.xydz.activity.SubmitOrderActivity;
import com.ddiiyy.xydz.adapter.WorksHolder;
import com.ddiiyy.xydz.base.BaseFragment;
import com.ddiiyy.xydz.bean.UserBean;
import com.ddiiyy.xydz.bean.WorksInfoBean;
import com.ddiiyy.xydz.common.UserController;
import com.ddiiyy.xydz.constants.MyConstants;
import com.ddiiyy.xydz.database.DataHelper;
import com.ddiiyy.xydz.dialog.CustomDialog;
import com.ddiiyy.xydz.utils.AndroidUtils;
import com.ddiiyy.xydz.utils.FileUtil;
import com.ddiiyy.xydz.utils.LogUtils;
import com.ddiiyy.xydz.utils.StringUtils;
import com.ddiiyy.xydz.xutils.BitmapHelp;
import com.ddiiyy.xydz.xutils.BitmapUtils;
import com.ddiiyy.xydz.xutils.bitmap.BitmapDisplayConfig;
import com.ddiiyy.xydz.xutils.bitmap.core.BitmapSize;

@SuppressLint("UseSparseArrays")
public class TabWorksFragment extends BaseFragment implements OnClickListener,
		OnItemClickListener {

	private int mCoutChoice = 0;

	private View mView;
	private TextView mTitle;
	private TextView mChoiceTV;
	private TextView mNowBuy;
	private ImageView mBack;
	private ImageView mChoiceBtn;
	private Button mGoSee;
	private RelativeLayout mChoiceRL;
	private static RelativeLayout mViewNull;
	private static RelativeLayout mViewBuy;
	private static ListView mListView;

	private ArrayList<String> mWorksIds = new ArrayList<String>();
	private List<WorksInfoBean> mlist;
	private WorksAdapter mAdapter;
	private UserBean mUser;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_tab_works, null);
		}
		ViewGroup parent = (ViewGroup) mView.getParent();
		if (parent != null) {
			parent.removeView(mView);
		}
		mUser = UserController.getInstance(getActivity()).getUserInfo();
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
		initData();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.now_buy:
			if (AndroidUtils.isNoFastClick()) {
				if (!AndroidUtils.isNetworkAvailable(getActivity())) {
					Toast.makeText(getApplication(),
							this.getString(R.string.no_net), Toast.LENGTH_SHORT)
							.show();
				} else {
					gotoSubmitOrder();
				}
			}
			break;
		case R.id.choice_rl:
			ChoiceWorks();
			break;
		case R.id.go_see:
			Intent intent = new Intent(getApplication(), MainActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		WorksHolder holder = (WorksHolder) view.getTag();
		// 在每次获取点击的item时改变checkbox的状态
		holder.checkbox.toggle();
		WorksAdapter.mIsSelecteds.put(position, holder.checkbox.isChecked()); // 同时修改map的值保存状态
		if (holder.checkbox.isChecked() == true) {
			mWorksIds.add(mlist.get(position).getWorkid());
		} else {
			mWorksIds.remove(mlist.get(position).getWorkid());
		}
		
		int allKey = WorksAdapter.mIsSelecteds.size();
		int trueCount = 0;
		
		for (int i = 0; i < allKey; i++) {
			if (WorksAdapter.mIsSelecteds.get(i)) {
				trueCount++;
			}
		}
		if (allKey <= trueCount) {
			// 所有的value都是true
			mChoiceTV.setText(R.string.all_no_choice);
			mChoiceBtn.setImageResource(R.drawable.checkbox_pressed);
		}
		if (trueCount==0) {
			mChoiceTV.setText(R.string.all_choice);
			mChoiceBtn.setImageResource(R.drawable.checkbox_normal);
		}

	}

	public void initView(View view) {
		mTitle = (TextView) view.findViewById(R.id.title);
		mChoiceTV = (TextView) view.findViewById(R.id.checkbox_text);
		mNowBuy = (TextView) view.findViewById(R.id.now_buy);
		mBack = (ImageView) view.findViewById(R.id.back);
		mChoiceBtn = (ImageView) view.findViewById(R.id.all_choice);
		mChoiceRL = (RelativeLayout) view.findViewById(R.id.choice_rl);
		mViewNull = (RelativeLayout) view.findViewById(R.id.in_view_null);
		mViewBuy = (RelativeLayout) view.findViewById(R.id.in_view_buy);
		mGoSee = (Button) view.findViewById(R.id.go_see);
		mListView = (ListView) view.findViewById(R.id.listview);

		mViewNull.setVisibility(View.VISIBLE);
		mViewBuy.setVisibility(View.GONE);
		mListView.setVisibility(View.GONE);
		mTitle.setText(R.string.tab_works);
		mBack.setVisibility(View.GONE);

		mNowBuy.setOnClickListener(this);
		mChoiceRL.setOnClickListener(this);
		mGoSee.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
	}

	private void initData() {
		if (mUser.isIs_login()) {
			mlist = DataHelper.getInstance(getActivity()).queryWorksByUserId(
					mUser.getId());
			if (mlist.size() != 0) {
				mAdapter = new WorksAdapter(getActivity(), getApplication(),
						mlist);
				mListView.setAdapter(mAdapter);
				mChoiceTV.setText(R.string.all_choice);
				mChoiceBtn.setImageResource(R.drawable.checkbox_normal);

				mViewNull.setVisibility(View.GONE);
				mViewBuy.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.VISIBLE);
			}
		} else {
			mViewNull.setVisibility(View.VISIBLE);
			mViewBuy.setVisibility(View.GONE);
			mListView.setVisibility(View.GONE);
		}
	}

	private void ChoiceWorks() {
		mCoutChoice++;
		if (mCoutChoice % 2 == 1) {
			mWorksIds = new ArrayList<String>();
			for (int i = 0; i < mlist.size(); i++) {
				WorksAdapter.mIsSelecteds.put(i, true);
				mWorksIds.add(mlist.get(i).getWorkid());
			}
			mAdapter.notifyDataSetChanged();
			mChoiceTV.setText(R.string.all_no_choice);
			mChoiceBtn.setImageResource(R.drawable.checkbox_pressed);
		} else if (mCoutChoice % 2 == 0) {
			for (int i = 0; i < mlist.size(); i++) {
				if (WorksAdapter.mIsSelecteds.get(i) == true) {
					WorksAdapter.mIsSelecteds.put(i, false);
					mWorksIds.remove(mlist.get(i).getWorkid());
				}
			}
			mAdapter.notifyDataSetChanged();
			mChoiceTV.setText(R.string.all_choice);
			mChoiceBtn.setImageResource(R.drawable.checkbox_normal);
		}
	}

	private void gotoSubmitOrder() {

		if (mWorksIds.size() == 0) {
			Toast.makeText(getActivity(), getString(R.string.choice_works),
					Toast.LENGTH_SHORT).show();
			return;
		}
		showmeidialog();
		Intent intent = new Intent(getActivity(), SubmitOrderActivity.class);
		intent.putExtra(MyConstants.WORKS_IDS, mWorksIds);
		// ArrayList<WorksInfoBean> mListData = (ArrayList<WorksInfoBean>)
		// DataHelper
		// .getInstance(getApplication()).queryWorks(mWorksIds);
		startActivity(intent);
		LogUtils.logd(TAG, "end");
		// mListData.clear();
		mWorksIds.clear();
		for (int i = 0; i < mlist.size(); i++) {
			if (WorksAdapter.mIsSelecteds.get(i) == true) {
				WorksAdapter.mIsSelecteds.put(i, false);
				mWorksIds.remove(mlist.get(i).getWorkid());
			}
		}
		mAdapter.notifyDataSetChanged();
		mChoiceTV.setText(R.string.all_choice);
		mChoiceBtn.setImageResource(R.drawable.checkbox_normal);
		dismissmeidialog();
		AndroidUtils.enterActvityAnim(getActivity());

	}

	public static class WorksAdapter extends BaseAdapter {
		private String TAG = "CartAdapter";
		public static HashMap<Integer, Boolean> mIsSelecteds;
		private List<WorksInfoBean> mData;

		private Context mContext;
		private Activity mActivity;
		private LayoutInflater mInflater;
		private WorksHolder mHolder;

		private BitmapUtils bitmapUtils;
		private BitmapDisplayConfig bigPicDisplayConfig;

		public WorksAdapter(Activity mActivity, Context context,
				List<WorksInfoBean> mData) {
			this.mData = mData;
			this.mContext = context;
			this.mActivity = mActivity;
			mInflater = LayoutInflater.from(context);
			initCheckBox();

			int mScreenWidth = AndroidUtils.getScreenWidth(mActivity);
			if (bitmapUtils == null) {
				bitmapUtils = BitmapHelp.getBitmapUtils(mContext);
			}
			bigPicDisplayConfig = new BitmapDisplayConfig();
			bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
			bigPicDisplayConfig.setBitmapMaxSize(new BitmapSize(
					mScreenWidth / 5, mScreenWidth / 5));
			bitmapUtils.configDefaultLoadingImage(R.drawable.blank_bg);
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.blank_bg);
			bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
			bitmapUtils.configDiskCacheEnabled(false);

			// AlphaAnimation 在一些android系统上表现不正常, 造成图片列表中加载部分图片后剩余无法加载, 目前原因不明.
			// 可以模仿下面示例里的fadeInDisplay方法实现一个颜色渐变动画。
			AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
			animation.setDuration(1000);
			bitmapUtils.configDefaultImageLoadAnimation(animation);
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
		public View getView(int position, View convertView, ViewGroup parent) {
			DeleteOnClickListener deleteListener = null;
			EditOnClickListener editListener = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_works_list, null);
				mHolder = new WorksHolder();
				getItemView(convertView);

				convertView.setTag(mHolder);
				deleteListener = new DeleteOnClickListener(position);
				editListener = new EditOnClickListener(position);
				mHolder.delete_Iv.setOnClickListener(deleteListener);
				mHolder.edit_Iv.setOnClickListener(editListener);

			} else {
				mHolder = (WorksHolder) convertView.getTag();
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
			WorksInfoBean works = mData.get(position);

			String mCoverId = works.getImageurl();
			String imageurl = FileUtil.getFilePath(
					FileUtil.getWorksDir(works.getWorkid()), mCoverId,
					MyConstants.PNG);
			// mHolder.goods_imageIv.setImageURI(Uri.parse(imageurl));
			bitmapUtils.display(mHolder.goods_imageIv, imageurl,
					bigPicDisplayConfig);

			String goods_name = works.getGoodsname();
			mHolder.goods_nameTv.setText(goods_name);

			String goods_type = works.getType();
			mHolder.goods_typeTv.setText(goods_type);

			String editstate = works.getEditstate();
			if ("0".equals(editstate)) {
				mHolder.edit_Iv.setVisibility(View.VISIBLE);
			} else {
				mHolder.edit_Iv.setVisibility(View.GONE);
			}

			double price = Double.parseDouble(works.getPrice());
			String subtotalStr = StringUtils.format2point(price + "");
			String[] arr = subtotalStr.split("\\.");
			mHolder.goods_subtotal_bigTv.setText(arr[0] + ".");
			mHolder.goods_subtotal_smTv.setText(arr[1]);
			mHolder.checkbox.setChecked(mIsSelecteds.get(position));

		}

		/**
		 * 找到Item 的 控件
		 * 
		 * @param convertView
		 */
		private void getItemView(View convertView) {
			mHolder.checkbox = (CheckBox) convertView
					.findViewById(R.id.checkbox);
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
			mHolder.edit_Iv = (TextView) convertView.findViewById(R.id.edit_iv);
			mHolder.delete_Iv = (TextView) convertView
					.findViewById(R.id.delete_iv);
		}

		// 初始化 设置所有checkbox都为未选择
		private void initCheckBox() {
			mIsSelecteds = new HashMap<Integer, Boolean>();
			for (int i = 0; i < mData.size(); i++) {
				mIsSelecteds.put(i, false);
			}
		}

		/**
		 * 拼Json 给编辑图片页
		 * 
		 * @param code
		 * @param work_id
		 * @param goods_id
		 * @param number
		 * @param pic_id
		 * @param cover_id
		 * @return
		 */
		private String getData(String code, String work_id, String goods_id,
				String number, String pic_id, String cover_id) {
			JSONObject object = new JSONObject();
			try {
				object.put("code", code);
				JSONObject contentJson = new JSONObject();
				contentJson.put("work_id", work_id);
				contentJson.put("goods_id", goods_id);
				contentJson.put("number", number);
				contentJson.put("pic_id", pic_id);
				contentJson.put("cover_id", cover_id);
				object.put("content", contentJson);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			String data = object.toString();
			return data;
		}

		/**
		 * 删除对话框
		 * 
		 * @param position
		 */
		private void showAlertDialog(final int position) {
			CustomDialog.Builder builder = new CustomDialog.Builder(mActivity);
			builder.setMessage(mContext.getString(R.string.delete_works));
			builder.setPositiveButton(mContext.getString(R.string.ok),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							DataHelper.getInstance(mContext).deleteWorks(
									mData.get(position));
							FileUtil.deleteWorks(mData.get(position)
									.getWorkid());
							mData.remove(position);
							if (mData.size() == 0) {
								mViewNull.setVisibility(View.VISIBLE);
								mViewBuy.setVisibility(View.GONE);
								mListView.setVisibility(View.GONE);
							}
							notifyDataSetChanged();
							dialog.dismiss();

						}
					});

			builder.setNegativeButton(mContext.getString(R.string.cancel),
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							LogUtils.logd(TAG, "position:");
							dialog.dismiss();
						}
					});
			builder.create().show();

		}

		private class DeleteOnClickListener implements OnClickListener {
			int mPosition;

			public DeleteOnClickListener(int inPosition) {
				mPosition = inPosition;
			}

			@Override
			public void onClick(View v) {
				showAlertDialog(mPosition);
			}

		}

		private class EditOnClickListener implements OnClickListener {
			int mPosition;

			public EditOnClickListener(int inPosition) {
				mPosition = inPosition;
			}

			@Override
			public void onClick(View v) {
				WorksInfoBean works = mData.get(mPosition);
				String data = getData("10002", works.getWorkid(),
						works.getGoodsid(), works.getQuantity(),
						works.getPicurl(), works.getImageurl());
				LogUtils.logd(TAG, data);
				Intent intent = new Intent(mContext, EditPictureActivity.class);
				intent.putExtra("data", data);
				mActivity.startActivity(intent);
				AndroidUtils.enterActvityAnim(mActivity);
			}
		}
	}
}