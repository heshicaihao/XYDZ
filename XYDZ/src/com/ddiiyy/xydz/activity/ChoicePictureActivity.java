package com.ddiiyy.xydz.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.widget.ListView;

import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.adapter.ChoicePictureListAdapter;
import com.ddiiyy.xydz.base.BaseActivity;
import com.ddiiyy.xydz.bean.ImageBean;
import com.ddiiyy.xydz.utils.AndroidUtils;
import com.ddiiyy.xydz.utils.DateFormatUtils;
/**
 * 选择用户图片页
 * 
 * @author heshicaihao
 */
public class ChoicePictureActivity extends BaseActivity {

	private static final int SCAN_OK = 1;
	private TreeMap<Integer, List<String>> mGruopMap = new TreeMap<Integer, List<String>>();
	private Handler mHandler;
	private ListView mListView;
	private ChoicePictureListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choice_picture);
		init();

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("Path", "");
			this.setResult(EditPictureActivity.RESULT_OK,
					intent);
			AndroidUtils.finishActivity(this);
		}
		return false;
	}
	

	@SuppressLint("HandlerLeak")
	private void init() {
		mListView = (ListView) findViewById(R.id.list_view);
		// 获取图片信息
		getImagesFromSD(); 
		mHandler = new Handler() {
			@Override
			// 扫描完成之后，发送的消息被handleMessage接收
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case SCAN_OK:
					mAdapter = new ChoicePictureListAdapter(
							ChoicePictureActivity.this,
							getApplicationContext(),
							subGroupOfImage(mGruopMap), mGruopMap);
					mListView.setAdapter(mAdapter);
					break;
				}
			}
		};
	}

	protected List<ImageBean> subGroupOfImage(
			TreeMap<Integer, List<String>> gruopMap) {
		if (gruopMap.size() == 0) {
			return null;
		}
		List<ImageBean> list = new ArrayList<ImageBean>();
		Iterator<Entry<Integer, List<String>>> it = gruopMap.entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<Integer, List<String>> entry = it.next();
			ImageBean mImageBean = new ImageBean();
			Integer key = entry.getKey();
			List<String> value = entry.getValue();
			mImageBean.setFolderName(key + "");
			mImageBean.setImageCount(value.size());
			mImageBean.setPaths(value);
			mImageBean.setTopImagePath(value.get(0));// 获取该组的第一张图片
			list.add(mImageBean);
		}
		List<ImageBean> mListImage = new ArrayList<ImageBean>();
		for (int i = 0; i < list.size(); i++) {
			mListImage.add(list.get(list.size() - i - 1));
		}

		return mListImage;
	}

	private void getImagesFromSD() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return;
		}
		// 显示进度条
		new Thread(new Runnable() {
			@Override
			public void run() {
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = getApplicationContext()
						.getContentResolver();
				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);

				while (mCursor.moveToNext()) {
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
//					String is_private = mCursor.getString(mCursor
//							.getColumnIndex(MediaStore.Images.Media.IS_PRIVATE));
//					long longitude = mCursor.getLong(mCursor
//							.getColumnIndex(MediaStore.Images.Media.LONGITUDE));
//					long latitude = mCursor.getLong(mCursor
//							.getColumnIndex(MediaStore.Images.Media.LATITUDE));
					long longDate = mCursor.getLong(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
					String transferLongToDate = DateFormatUtils
							.transferLongToDate("yyyyMMdd", longDate);
					int date = Integer.parseInt(transferLongToDate);
					// 根据父路径名将图片放入到mGruopMap中
					if (!mGruopMap.containsKey(date)) {
						List<String> chileList = new ArrayList<String>();
						chileList.add(path);
						mGruopMap.put(date, chileList);
					} else {
						mGruopMap.get(date).add(path);
					}
				}
				mCursor.close();
				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(SCAN_OK);
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

}
