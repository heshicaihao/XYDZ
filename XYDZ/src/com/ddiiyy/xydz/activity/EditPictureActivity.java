package com.ddiiyy.xydz.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.adapter.FrameAdapter;
import com.ddiiyy.xydz.base.BaseActivity;
import com.ddiiyy.xydz.bean.FrameBean;
import com.ddiiyy.xydz.bean.MobileShell;
import com.ddiiyy.xydz.bean.WorksInfoBean;
import com.ddiiyy.xydz.cache.ACache;
import com.ddiiyy.xydz.cache.SaveDataJson;
import com.ddiiyy.xydz.constants.MyConstants;
import com.ddiiyy.xydz.database.DataHelper;
import com.ddiiyy.xydz.dialog.CustomDialog;
import com.ddiiyy.xydz.dialog.CustomProgressDialog;
import com.ddiiyy.xydz.net.NetHelper;
import com.ddiiyy.xydz.utils.AndroidUtils;
import com.ddiiyy.xydz.utils.FileUtil;
import com.ddiiyy.xydz.utils.GUID;
import com.ddiiyy.xydz.utils.JSONUtil;
import com.ddiiyy.xydz.utils.LogUtils;
import com.ddiiyy.xydz.utils.MyBitmapUtils;
import com.ddiiyy.xydz.utils.StringUtils;
import com.ddiiyy.xydz.widget.ErrorHintView;
import com.ddiiyy.xydz.widget.ErrorHintView.OperateListener;
import com.ddiiyy.xydz.xutils.BitmapHelp;
import com.ddiiyy.xydz.xutils.BitmapUtils;
import com.ddiiyy.xydz.xutils.bitmap.BitmapCommonUtils;
import com.ddiiyy.xydz.xutils.bitmap.BitmapDisplayConfig;
import com.ddiiyy.xydz.xutils.bitmap.callback.BitmapLoadCallBack;
import com.ddiiyy.xydz.xutils.bitmap.callback.BitmapLoadFrom;
import com.ddiiyy.xydz.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 编辑图片页
 * 
 * @author heshicaihao
 */
@SuppressLint({ "HandlerLeak", "ResourceAsColor" })
public class EditPictureActivity extends BaseActivity implements
		OnTouchListener, OnClickListener, OnItemClickListener {
	private static final int REQUESTCODE = 1;
	private static final int REQUESTCODE02 = 2;
	private static final int SCAN_OK = 2;
	private static final int DOWNLOAD_OK = 1000;
	private static final int VIEW_LIST = 1;
	private static final int VIEW_WIFIFAILUER = 2;
	private static final int VIEW_LOADFAILURE = 3;
	private static final int VIEW_LOADING = 4;

	private int mFirstCount = 0;
	private int mUploadCout = 0;
	private int mDownloadCout = 0;
	private int mRequestCout = 0;
	private int mMode = 0;
	private int mMaxPicWidth = MyConstants.INIT_MAXWIDTH;
	private int mMaxPicHeight = MyConstants.INIT_MAXHEIGHT;
	private int mPicHeight = 0;
	private int mPicWidth = 0;
	// private int mRotation = 0;
	private float mInitScale = 1;
	private float mScale = mInitScale;
	private float mTranslateX;
	private float mTranslateY;
	private float mLastMoveX = -1;
	private float mLastMoveY = -1;
	private float mStartDis;
	private float mDesignW;
	private float mDesignH;
	private float mInitX;
	private float mInitY;
	private float mPiMask = (float) 1.0;
	private boolean mIsMove = false;
	private boolean mIsShow = false;
	private boolean mIsSecondEdit = false;
	private boolean mIsLogin = false;

	private String mAccountId;
	private String mTempAccountId;
	private String mAccountToken;
	private String mTempToken;
	private String mWorkId = "0";
	private String mGoodsId;
	private String mProductId;
	private String mTemplateId;
	private String mPicId = GUID.getGUID();
	private String mCoverId = GUID.getGUID();
	private String mTitleStr;
	private String mPrice;
	private String mMaskName;
	private String mFrameName;
	private String mJsonStr;
	private String mQuantity;

	private RelativeLayout mEditMiddleRl;
	private RelativeLayout mContentView;
	private RelativeLayout mMiddleRl;
	private LinearLayout mEditBottomLl;
	private LinearLayout mMenu;
	private LinearLayout mMinMenu;
	private ImageView mResultImage;
	private ImageView mMobileImage;
	private ImageView mFrameImage;
	private TextView mTitle;
	private TextView mBack;
	private TextView mSave;

	// private ImageView mRightTurn;
	private ImageView mEnlarge;
	private ImageView mNarrow;
	private ImageView mChoicePicture;
	private ImageView mCollectMenu;
	private GridView mFrameGridView;
	private ErrorHintView mErrorHintView;
	private HorizontalScrollView mFrameScrollView;

	private Bitmap mPicBitmap;
	private Bitmap mMaskBitmap;
	private Bitmap mFrameBitmap;
	private Bitmap mResultBitmap;
	private JSONArray mFrameJSONArray;
	private Handler mFristHandler;
	private Handler mDownloadHandler;
	private Handler mRefreshPicHandler;
	private ACache mCache;
	private MobileShell mShell;
	private String mWorksDir;
	private ArrayList<String> mWorksIds = new ArrayList<String>();
	private FrameAdapter mFrameAdapter;

	private BitmapUtils bitmapUtils;
	private BitmapDisplayConfig bigPicDisplayConfig;
	private CustomProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IsShow();
		setContentView(R.layout.activity_edit_picture);
		initView();
		initData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		showFrameDialog();
		JSONArray jsonArray2 = JSONUtil.resolveFrameJSONArray(mFrameJSONArray);
		JSONObject maskJson = jsonArray2.optJSONObject(position);
		FrameBean frameBean = (FrameBean) JSONUtil.JSONToObj(
				maskJson.toString(), FrameBean.class);
		mShell.setFrameBean(frameBean);
		changFrameData(mShell.getFrameBean());
		mFrameAdapter.setSeclection(position);
		mFrameAdapter.notifyDataSetInvalidated();

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.save:
			mDialog.show();
			getUserInfo();
			if (mIsLogin) {
				if (AndroidUtils.isNoFastClick()) {
					upload();
				}
			} else {
				mDialog.dismiss();
				gotoLogin();
			}
			break;
		case R.id.change_picture:
			gotoChoicePicture();
			break;
		// case R.id.right_turn:
		// mRotation += MyConstants.EVERY_TIME_ROTATION;
		// getUserSee();
		// break;
		case R.id.enlarge:
			mScale += mInitScale * MyConstants.EVERY_TIME_SCALE;
			limitMaxScale();
			getUserSee();
			break;
		case R.id.narrow:
			mScale -= mInitScale * MyConstants.EVERY_TIME_SCALE;
			limitMinScale();
			getUserSee();
			break;
		case R.id.collect_menu:
			int visibility = mMenu.getVisibility();
			if (View.GONE == visibility) {
				mMenu.setVisibility(View.VISIBLE);
				mCollectMenu.setImageResource(R.drawable.lower);
			} else if (View.VISIBLE == visibility) {
				mMenu.setVisibility(View.GONE);
				mCollectMenu.setImageResource(R.drawable.upper);
			} else if (View.INVISIBLE == visibility) {
				mMenu.setVisibility(View.GONE);
				mCollectMenu.setImageResource(R.drawable.upper);
			}
			break;
		case R.id.back:
			showDialog(this);
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mMode = 1;
			mIsMove = false;
			break;
		case MotionEvent.ACTION_UP:
			mMode = 0;
			mLastMoveX = -1;
			mLastMoveY = -1;
			LogUtils.logd(TAG, "changeMenu");
			changeMenu();
			mIsMove = true;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mRequestCout != 0) {
				onTouchMove(event);
			}
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mMode -= 1;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			mMode += 1;
			mStartDis = AndroidUtils.distance(event);
			break;
		}
		return true;
	}

	// private float getPX(float x) {
	// float y;
	// y = (float) ((x / 300 * 25.4) * 0.039370078740157 * 200);
	// return y;
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUESTCODE:
			if (resultCode == RESULT_OK) {
				String mPath = data.getStringExtra("Path");
				if (StringUtils.isEmpty(mPath)) {
					mPath = FileUtil.getFilePath(MyConstants.CACHE_DIR,
							MyConstants.DEFAULT_PIC, MyConstants.JPEG);
					mSave.setVisibility(View.GONE);
					mMinMenu.setVisibility(View.GONE);
				} else {
					mRequestCout += 1;
					mSave.setVisibility(View.VISIBLE);
					mEditBottomLl.setVisibility(View.VISIBLE);
					mMinMenu.setVisibility(View.VISIBLE);
				}
				synResizePic(mPath);
				refreshPic();
			}
			break;
		case REQUESTCODE02:
			if (resultCode == RESULT_OK) {
				// String mdata = data.getStringExtra("data");
				getUserInfo();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog(this);
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mPicBitmap != null && (!mPicBitmap.isRecycled())) {
			mPicBitmap.recycle();
			mPicBitmap = null;
			System.gc();
		}
		if (mMaskBitmap != null && (!mMaskBitmap.isRecycled())) {
			mMaskBitmap.recycle();
			mMaskBitmap = null;
			System.gc();
		}
		if (mResultBitmap != null && (!mResultBitmap.isRecycled())) {
			mResultBitmap.recycle();
			mMaskBitmap = null;
			System.gc();
		}
		System.gc();
	}

	private void initData() {
		getUserInfo();
		getDataIntent();
		if ("0".equals(mWorkId)) {
			if (mIsLogin) {
				getWorkId(mAccountId, mAccountToken);
			} else {
				getTempAccount();
			}
		}
		initCache();
		showLoading(VIEW_LOADING);

	}

	private void getUserInfo() {
		mAccountId = mUserController.getUserInfo().getId();
		mAccountToken = mUserController.getUserInfo().getToken();
		mIsLogin = mUserController.getUserInfo().isIs_login();
		mTempAccountId = mUserController.getUserInfo().getTemp_id();
		mTempToken = mUserController.getUserInfo().getTemp_token();

	}

	/**
	 * 从H5界面获取数据，处理网络请求。
	 */
	private void getDataIntent() {
		Intent intent = getIntent();
		String data = intent.getStringExtra("data");
		try {
			JSONObject jsonobject = new JSONObject(data);
			String code = jsonobject.getString("code");
			JSONObject content = jsonobject.getJSONObject("content");
			mGoodsId = content.getString("goods_id");
			mQuantity = content.getString("number");
			if ("10001".equals(code)) {
				mMinMenu.setVisibility(View.GONE);
				initGoodsInfo();
			} else if ("10002".equals(code)) {
				mWorkId = content.getString("work_id");
				mPicId = content.getString("pic_id");
				mCoverId = content.getString("cover_id");
				mWorksDir = FileUtil.getWorksDir(mWorkId);

				mSave.setVisibility(View.VISIBLE);
				mEditBottomLl.setVisibility(View.VISIBLE);
				mIsSecondEdit = true;
				mRequestCout = 1;
				initGoodsInfo();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initCache() {
		mCache = ACache.get(this);

		if (bitmapUtils == null) {
			bitmapUtils = BitmapHelp.getBitmapUtils(this);
		}
		bigPicDisplayConfig = new BitmapDisplayConfig();
		bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
		bigPicDisplayConfig.setBitmapMaxSize(BitmapCommonUtils
				.getScreenSize(EditPictureActivity.this));
		bitmapUtils.configDefaultLoadingImage(R.drawable.blank_bg);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.blank_bg);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);

		// MemoryCache memoryCache = new MemoryCache();// 内存缓存
		// FileCache fcache = new FileCache(this, new
		// File(FileUtil.getSDPath()),
		// MyConstants.CACHE_DIR);// 文件缓存
		// mImageLoader = new AsyncImageLoader(this, memoryCache, fcache);

	}

	private void initView() {

		mContentView = (RelativeLayout) findViewById(R.id.content_view);
		mFrameGridView = (GridView) findViewById(R.id.gridview);
		mFrameScrollView = (HorizontalScrollView) findViewById(R.id.first_content);
		mEditMiddleRl = (RelativeLayout) findViewById(R.id.middle_rl_touch);
		mMiddleRl = (RelativeLayout) findViewById(R.id.middle_rl);
		mEditBottomLl = (LinearLayout) findViewById(R.id.edit_content);
		mMenu = (LinearLayout) findViewById(R.id.menu);
		mMinMenu = (LinearLayout) findViewById(R.id.min_menu);
		mMobileImage = (ImageView) findViewById(R.id.mobile_image);
		mResultImage = (ImageView) findViewById(R.id.result_image);
		mFrameImage = (ImageView) findViewById(R.id.frame_image);
		mEnlarge = (ImageView) findViewById(R.id.enlarge);
		mNarrow = (ImageView) findViewById(R.id.narrow);
		// mRightTurn = (ImageView) findViewById(R.id.right_turn);
		mChoicePicture = (ImageView) findViewById(R.id.change_picture);
		mCollectMenu = (ImageView) findViewById(R.id.collect_menu);
		mBack = (TextView) findViewById(R.id.back);
		mTitle = (TextView) findViewById(R.id.title);
		mSave = (TextView) findViewById(R.id.save);
		mErrorHintView = (ErrorHintView) findViewById(R.id.hint_view);
		setListener();
		mDialog = new CustomProgressDialog(this, this.getResources().getString(
				R.string.dialog_info_save_works));

	}

	private void setListener() {

		mFrameScrollView.setHorizontalScrollBarEnabled(false);

		mFrameGridView.setOnItemClickListener(this);
		mBack.setOnClickListener(this);
		mChoicePicture.setOnClickListener(this);
		// mRightTurn.setOnClickListener(this);
		mEnlarge.setOnClickListener(this);
		mNarrow.setOnClickListener(this);
		mSave.setOnClickListener(this);
		mCollectMenu.setOnClickListener(this);
		mEditMiddleRl.setOnTouchListener(this);
	}

	private void gotoSubmitOrder() {
		mWorksIds.add(mWorkId);
		if (mWorksIds.size() == 0) {
			Toast.makeText(this, getString(R.string.choice_works),
					Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(this, SubmitOrderActivity.class);
		intent.putExtra(MyConstants.WORKS_IDS, mWorksIds);
		startActivity(intent);
		AndroidUtils.enterActvityAnim(this);
	}

	/**
	 * 是否显示任务栏
	 */
	private void IsShow() {
		mIsShow = !mIsShow;
		if (mIsShow) {
			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			getWindow().setAttributes(params);
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().setAttributes(params);
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}

	}

	/**
	 * 显示对话框
	 * 
	 * @param context
	 */
	private void showDialog(Context context) {
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setMessage(context
				.getString(R.string.do_you_want_to_quit_editing));
		// builder.setTitle("提示");
		builder.setPositiveButton(context.getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (AndroidUtils.isNoFastClick()) {
							dialog.dismiss();
							AndroidUtils
									.finishActivity(EditPictureActivity.this);

						}
					}
				});

		builder.setNegativeButton(context.getString(R.string.go_on_editing),
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (AndroidUtils.isNoFastClick()) {
							dialog.dismiss();

						}
					}
				});

		builder.create().show();
	}

	/**
	 * ChoicePictureActivity 回调切换界面
	 */
	private void changeData() {
		initPicLocation(mPicBitmap, mMaskBitmap);
		getUserSee();

	}

	/**
	 * 触屏改变菜单显示
	 */
	private void changeMenu() {
		if (!mIsMove) {
			if (mRequestCout == 0) {
				gotoChoicePicture();
			} else {
				int visibility = mMinMenu.getVisibility();
				if (View.GONE == visibility) {
					mMinMenu.setVisibility(View.VISIBLE);
				} else if (View.VISIBLE == visibility) {
					mMinMenu.setVisibility(View.GONE);
				} else if (View.INVISIBLE == visibility) {
					mMinMenu.setVisibility(View.GONE);
				}
			}
		}

	}

	/**
	 * 触屏操作方法
	 * 
	 * @param event
	 */
	private void onTouchMove(MotionEvent event) {
		if (mMode == 1) {
			float xMove = event.getX();
			float yMove = event.getY();
			if (mLastMoveX == -1 && mLastMoveY == -1) {
				mLastMoveX = xMove;
				mLastMoveY = yMove;
			}
			float movedDistanceX = xMove - mLastMoveX;
			float movedDistanceY = yMove - mLastMoveY;
			if (Math.abs(movedDistanceX) > 3 || Math.abs(movedDistanceX) > 3) {
				mIsMove = true;
			}
			mTranslateX = mTranslateX + movedDistanceX;
			mTranslateY = mTranslateY + movedDistanceY;
			getUserSee();
			mLastMoveX = xMove;
			mLastMoveY = yMove;
		} else if (mMode >= 2) {
			if (mRequestCout != 0) {
				float endDis = AndroidUtils.distance(event);
				mScale = mScale * (endDis / mStartDis);
				limitMaxScale();
				limitMinScale();
				mIsMove = true;
				getUserSee();
				mStartDis = endDis;
			}
		}
	}

	/**
	 * 显示加载页面
	 * 
	 * @param i
	 */
	private void showLoading(int i) {
		mErrorHintView.setVisibility(View.GONE);
		mContentView.setVisibility(View.GONE);
		switch (i) {
		case 1:
			mErrorHintView.hideLoading();
			mContentView.setVisibility(View.VISIBLE);
			break;

		case 2:
			mErrorHintView.hideLoading();
			mErrorHintView.netError(new OperateListener() {
				@Override
				public void operate() {
					showLoading(VIEW_LOADING);
					initGoodsInfo();
				}
			});
			break;

		case 3:
			mErrorHintView.hideLoading();
			mErrorHintView.loadFailure(new OperateListener() {
				@Override
				public void operate() {
					showLoading(VIEW_LOADING);
					initGoodsInfo();
				}
			});

		case 4:
			mErrorHintView.loadingData();
			break;
		}
	}

	/**
	 * 初始化用户图片居中，铺满mask。
	 * 
	 * @param mOldBitmap
	 * @param mNewBitmap
	 */
	@SuppressWarnings("unused")
	private void changeTranslate(Bitmap mOldBitmap, Bitmap mNewBitmap) {
		// int OldW = mOldBitmap.getWidth();
		// int OldH = mOldBitmap.getHeight();
		// int NewW = mNewBitmap.getWidth();
		// int NewH = mNewBitmap.getHeight();
		// float sW = OldW / NewW;
		// float sH = OldH / NewH;
		// mInitScale = BitmapUtils.getInitScale(mPicBitmap, mMaskBitmap);
		// if (sW > sH) {
		// mTranslateX = mTranslateX * sW;
		// mTranslateY = mTranslateY * sW;
		// } else if (sW == sH) {
		// mTranslateX = mTranslateX * sW;
		// mTranslateY = mTranslateY * sW;
		// } else {
		// mTranslateX = mTranslateX * sW;
		// mTranslateY = mTranslateY * sW;
		// }
		if (mTranslateX > 0) {
			mTranslateX = 0;
		}
		if (mTranslateY > 0) {
			mTranslateY = 0;
		}
	}

	/**
	 * 初始化用户图片居中，铺满mask。
	 * 
	 * @param mPicBitmap
	 * @param mMaskBitmap
	 */
	private void initPicLocation(Bitmap mPicBitmap, Bitmap mMaskBitmap) {
		int PicW = mPicBitmap.getWidth();
		int PicH = mPicBitmap.getHeight();
		int MaskW = mMaskBitmap.getWidth();
		int MaskH = mMaskBitmap.getHeight();
		mScale = MyBitmapUtils.getInitScale(mPicBitmap, mMaskBitmap);
		mInitScale = mScale;
		if (PicW > PicH) {
			mTranslateX = -(PicW * mScale - MaskW) / (float) 2;
			mTranslateY = 0;
		} else if (PicW == PicH) {
			mTranslateX = -(PicW * mScale - MaskW) / (float) 2;
			mTranslateY = 0;
		} else {
			mTranslateX = 0;
			mTranslateY = -(PicH * mScale - MaskH) / (float) 2;
		}
		// mRotation = 0;

	}

	/**
	 * 显示图片编辑结果
	 */
	private void getUserSee() {
		// mResultImage.setImageBitmap(setMask(mPicBitmap, mMaskBitmap,
		// mTranslateX, mTranslateY, mScale, mRotation));
		mResultBitmap = setMask(mPicBitmap, mMaskBitmap, mTranslateX,
				mTranslateY, mScale);
		mResultImage.setImageBitmap(mResultBitmap);
	}

	/**
	 * @param pic
	 *            用户图片
	 * @param mask
	 *            蒙版
	 * @param picX
	 *            用户图片偏移量X
	 * @param picY
	 *            用户图片偏移量Y
	 * @param scale
	 *            缩放比例
	 * @param degree
	 *            旋转角度
	 * @return Bitmap
	 */

	private Bitmap setMask(Bitmap pic, Bitmap mask, float picX, float picY,
			float scale) {
		Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(),
				Config.ARGB_8888);
		Canvas mCanvas = new Canvas(result);
		Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		Matrix mMatrix = new Matrix();
		// mMatrix.postRotate(degree);
		mMatrix.postScale(scale, scale,
				Integer.parseInt(mShell.getFrameBean().getFrameWidth()) / 2,
				Integer.parseInt(mShell.getFrameBean().getFrameHeight()) / 2);
		pic = Bitmap.createBitmap(pic, 0, 0, pic.getWidth(), pic.getHeight(),
				mMatrix, true);
		picX = limitX(mask, picX, pic);
		picY = limitY(mask, picY, pic);
		mTranslateX = picX;
		mTranslateY = picY;
		mCanvas.drawBitmap(pic, picX, picY, null);
		mCanvas.drawBitmap(mask, 0, 0, mPaint);
		mPaint.setXfermode(null);

		return result;
	}

	/**
	 * 限制横向图片不出界
	 * 
	 * @param mask
	 * @param picX
	 * @param resizeBitmap
	 * @return
	 */
	private float limitX(Bitmap mask, float picX, Bitmap resizeBitmap) {
		float maxX = resizeBitmap.getWidth() - mask.getWidth();
		if (picX > 0) {
			picX = 0;
		} else if (picX < -maxX) {
			picX = -maxX;
		}
		return picX;
	}

	/**
	 * 限制纵向图片不出界
	 * 
	 * @param mask
	 * @param picY
	 * @param resizeBitmap
	 * @return
	 */
	private float limitY(Bitmap mask, float picY, Bitmap resizeBitmap) {
		float maxY = resizeBitmap.getHeight() - mask.getHeight();
		if (picY > 0) {
			picY = 0;
		} else if (picY < -maxY) {
			picY = -maxY;
		}
		return picY;
	}

	/**
	 * 限制最小缩小倍数
	 */
	private void limitMinScale() {
		if (mScale < mInitScale * MyConstants.MIN_SCALE) {
			mScale = mInitScale * MyConstants.MIN_SCALE;
		}
	}

	/**
	 * 限制最大放大倍数
	 */
	private void limitMaxScale() {
		if (mScale > mInitScale * MyConstants.MAX_SCALE) {
			mScale = mInitScale * MyConstants.MAX_SCALE;
		}
	}

	/**
	 * 保存作品并上传
	 */
	private void upload() {
		saveChangeWorkInfo();
		mJsonStr = SaveDataJson.SaveWorkData(mShell);
		if ("0".equals(mWorkId)) {
			if (mIsLogin) {
				getWorkId(mAccountId, mAccountToken);
			} else {
				getTempAccount();
			}
		} else {
			FileUtil.saveBitmap2Jpeg(mPicBitmap, mWorksDir, mPicId,
					MyConstants.JPEG);
			FileUtil.saveFile(mJsonStr, mWorksDir, mWorkId, MyConstants.TXT);
			FileUtil.saveBitmap2Png(getPicView(), mWorksDir, mCoverId,
					MyConstants.PNG);
			uploadWorkInfo(mJsonStr);
		}

	}

	/**
	 * 跳转到选择图片界面
	 */
	private void gotoChoicePicture() {
		Intent intent = new Intent(EditPictureActivity.this,
				ChoicePictureActivity.class);
		mPicBitmap.recycle();
		EditPictureActivity.this.startActivityForResult(intent, REQUESTCODE);
		AndroidUtils.enterActvityAnim(this);

	}

	/**
	 * 跳转到选择登录
	 */
	private void gotoLogin() {
		Intent intent = new Intent(EditPictureActivity.this,
				LoginActivity.class);
		intent.putExtra("code", MyConstants.EDITPICTURE2LOGIN);
		EditPictureActivity.this.startActivityForResult(intent, REQUESTCODE02);
		AndroidUtils.enterActvityAnim(this);
	}

	/**
	 * 保存作品信息到缓存
	 */
	private void saveChangeWorkInfo() {
		int mMaskBitmapheight = mMaskBitmap.getHeight();
		int height = Integer.parseInt(mShell.getFrameBean().getHeight());
		mPiMask = (float) height / (float) mMaskBitmapheight;
		LogUtils.logd(TAG, "saveChangeWorkInfo+mPiMask:" + mPiMask);

		mShell.getFrameBean().setCutx("" + mTranslateX * mPiMask);
		mShell.getFrameBean().setCuty("" + mTranslateY * mPiMask);
		mShell.getFrameBean().setUrl("imageid://" + mPicId);
		mShell.getFrameBean().setRemark(
				getMyScale("" + mInitScale, "" + mScale, "" + mCoverId, ""
						+ mPiMask));
		mShell.getFrameBean().setCutwidth("" + mPicWidth * mScale * mPiMask);
		mShell.getFrameBean().setCutheight("" + mPicHeight * mScale * mPiMask);
		mShell.getPageBean().setRemark(MyConstants.ANDROID);
	}

	/**
	 * 设置GridView的Adapter
	 */
	private void setAdapterGridView() {
		float screenensity = AndroidUtils.getScreenensity(this);
		int cWidth = (int) (MyConstants.EDITBOTTOM_WIDTH * screenensity);
		int hSpacing = (int) (2 * screenensity);
		mFrameAdapter = new FrameAdapter(this, EditPictureActivity.this,
				mFrameJSONArray);
		mFrameGridView.setAdapter(mFrameAdapter);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				mFrameAdapter.getCount() * (cWidth + hSpacing * 2),
				LinearLayout.LayoutParams.WRAP_CONTENT);
		mFrameGridView.setLayoutParams(params);
		mFrameGridView.setColumnWidth(cWidth + hSpacing);
		mFrameGridView.setHorizontalSpacing(hSpacing);
		mFrameGridView.setStretchMode(GridView.NO_STRETCH);
		mFrameGridView.setNumColumns(mFrameAdapter.getCount());

	}

	BitmapLoadCallBack<ImageView> callback = new DefaultBitmapLoadCallBack<ImageView>() {
		@Override
		public void onLoadStarted(ImageView container, String uri,
				BitmapDisplayConfig config) {
			super.onLoadStarted(container, uri, config);
		}

		@Override
		public void onLoadCompleted(ImageView container, String uri,
				Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
			super.onLoadCompleted(container, uri, bitmap, config, from);
			showLoading(VIEW_LIST);
		}
	};

	/**
	 * 获取手机壳信息 设置
	 * 
	 * @param myJsonObject
	 */
	private void setMobileData(JSONArray myJsonObject) {
		try {
			mShell = MobileShell.initMobileShellData(myJsonObject);
			int mMobileHeight = Integer.parseInt(mShell.getDecorationBean()
					.getHeight());
			int mMobileWidth = Integer.parseInt(mShell.getDecorationBean()
					.getWidth());
			int mMobileX = Integer.parseInt(mShell.getDecorationBean().getX());
			int mMobileY = Integer.parseInt(mShell.getDecorationBean().getY());
			String mMobileUrl = mShell.getDecorationBean().getUrl();
			setView(mMobileImage, mMobileWidth, mMobileHeight, mMobileX,
					mMobileY);
			if (!StringUtils.isEmpty(mMobileUrl)) {
				bitmapUtils.display(mMobileImage, mMobileUrl,
						bigPicDisplayConfig, callback);
				// bitmapUtils.display(mMobileImage, mMobileUrl,
				// bigPicDisplayConfig);
			}
			setFrameData(mShell.getFrameBean());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置蒙版
	 * 
	 * @param mask
	 * @throws JSONException
	 */
	private void changFrameData(FrameBean mask) {
		getFrameAddMaskBitmap(mask);
		refreshChangFrameUI(mask);
	}

	/**
	 * 设置蒙版
	 * 
	 * @param mask
	 * @throws JSONException
	 */
	private void setFrameData(FrameBean mask) {
		try {
			int mFrameWidth = Integer.parseInt(mask.getFrameWidth());
			int mFrameHeight = Integer.parseInt(mask.getFrameHeight());
			int mFrameX = Integer.parseInt(mask.getX());
			int mFrameY = Integer.parseInt(mask.getY());
			String mFrameUrl = mask.getFrameUrl();
			String mMaskUrl = mask.getFrameMaskUrl();
			mMaskName = FileUtil.getenURl(mMaskUrl);
			mFrameName = FileUtil.getenURl(mFrameUrl);
			if (mIsSecondEdit) {
				String PicIdUrl = mask.getUrl();
				String[] split = PicIdUrl.split("\\://");
				mPicId = split[1];
				JSONObject mRemark = new JSONObject(mask.getRemark());
				mInitScale = Float.parseFloat(mRemark.getString("mInitScale"));
				mScale = Float.parseFloat(mRemark.getString("mScale"));
				// mCoverId = mRemark.getString("mCoverId");
				mPiMask = Float.parseFloat(mRemark.getString("mPiMask"));
				mTranslateX = Float.parseFloat(mask.getCutx()) / mPiMask;
				mTranslateY = Float.parseFloat(mask.getCuty()) / mPiMask;
			}
			setView(mFrameImage, mFrameWidth, mFrameHeight, mFrameX, mFrameY);
			if (!StringUtils.isEmpty(mFrameUrl)) {
				bitmapUtils
						.display(mFrameImage, mFrameUrl, bigPicDisplayConfig);
			}
			setView(mResultImage, mFrameWidth, mFrameHeight, mFrameX, mFrameY);
			synGetMask();
			refreshUI();
		} catch (Exception e) {
		}
	}

	/**
	 * 获取画框和蒙版 资源
	 */
	private void getFrameAddMaskBitmap(FrameBean mask) {

		mDownloadCout = 0;
		// Mask资源
		String mMaskUrl = mask.getFrameMaskUrl();
		mMaskName = FileUtil.getenURl(mMaskUrl);
		final String mMaskpath = FileUtil.getFilePath(MyConstants.CACHE_DIR,
				mMaskName, MyConstants.PNG);
		if (FileUtil.fileIsExists(mMaskpath)) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					mMaskBitmap = FileUtil.getSDCardImg(mMaskpath);
					mDownloadCout++;
					if (mDownloadCout == 2) {
						mDownloadHandler.sendEmptyMessage(DOWNLOAD_OK);
					}
				}
			}).start();
		} else {
			downloadMask(mMaskUrl);
		}
		// Frame资源
		final String mFrameUrl = mask.getFrameUrl();
		mFrameName = FileUtil.getenURl(mFrameUrl);
		final String mFramepath = FileUtil.getFilePath(MyConstants.CACHE_DIR,
				mFrameName, MyConstants.PNG);
		if (FileUtil.fileIsExists(mFramepath)) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					mFrameBitmap = FileUtil.getSDCardImg(mFramepath);
					mDownloadCout++;
					if (mDownloadCout == 2) {
						mDownloadHandler.sendEmptyMessage(DOWNLOAD_OK);
					}
				}
			}).start();
		} else {
			downloadFrame(mFrameUrl);
		}

	}

	/**
	 * 切换画框 更新UI
	 */
	private void refreshChangFrameUI(FrameBean mask) {

		final int mFrameWidth = Integer.parseInt(mask.getFrameWidth());
		final int mFrameHeight = Integer.parseInt(mask.getFrameHeight());
		final int mFrameX = Integer.parseInt(mask.getX());
		final int mFrameY = Integer.parseInt(mask.getY());

		mDownloadHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case DOWNLOAD_OK:
					setView(mFrameImage, mFrameWidth, mFrameHeight, mFrameX,
							mFrameY);
					mFrameImage.setImageBitmap(mFrameBitmap);
					setView(mResultImage, mFrameWidth, mFrameHeight, mFrameX,
							mFrameY);
					initPicLocation(mPicBitmap, mMaskBitmap);
					getUserSee();
					dismissFrameDialog();
					break;
				}
			}

		};
	}

	/**
	 * 下载 mask 保存mask
	 * 
	 * @param mMaskUrl
	 */
	private void downloadMask(String mMaskUrl) {
		ImageLoader.getInstance().loadImage(mMaskUrl,
				new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {

					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {

					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {
						mDownloadCout++;
						mMaskBitmap = arg2;
						FileUtil.saveBitmap2Png(arg2, MyConstants.CACHE_DIR,
								mMaskName, MyConstants.PNG);
						if (mDownloadCout == 2) {
							mDownloadHandler.sendEmptyMessage(DOWNLOAD_OK);
						}
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {

					}
				});
	}

	/**
	 * 下载 frame 保存frame
	 * 
	 * @param mFrameUrl
	 */
	private void downloadFrame(String mFrameUrl) {
		ImageLoader.getInstance().loadImage(mFrameUrl,
				new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {

					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {

					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {
						mDownloadCout++;
						mFrameBitmap = arg2;
						FileUtil.saveBitmap2Png(arg2, MyConstants.CACHE_DIR,
								mFrameName, MyConstants.PNG);
						if (mDownloadCout == 2) {
							mDownloadHandler.sendEmptyMessage(DOWNLOAD_OK);
						}

					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {

					}
				});
	}

	private void synResizePic(final String mPath) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Bitmap sdCardImg = FileUtil.getSDCardImg(mPath);
				mPicBitmap = MyBitmapUtils.ResizePiImage(sdCardImg,
						mMaxPicWidth, mMaxPicHeight);
				// if (mMaxPicWidth > mMaxPicHeight) {
				// mPicBitmap = MyBitmapUtils.ResizePiImage(sdCardImg,
				// mMaxPicHeight, mMaxPicHeight);
				// } else {
				// mPicBitmap = MyBitmapUtils.ResizePiImage(sdCardImg,
				// mMaxPicWidth, mMaxPicWidth);
				// }
				mPicWidth = mPicBitmap.getWidth();
				mPicHeight = mPicBitmap.getHeight();
				mRefreshPicHandler.sendEmptyMessage(SCAN_OK);
			}
		}).start();
	}

	/**
	 * 拼字符串存服务器
	 * 
	 * @param mInitScale
	 *            初始比例
	 * @param mScale
	 *            最终比例
	 * @return
	 */
	private String getMyScale(String mInitScale, String mScale,
			String mCoverId, String mPiMask) {
		JSONObject object = new JSONObject();
		try {
			object.put("mInitScale", mInitScale);
			object.put("mScale", mScale);
			object.put("mCoverId", mCoverId);
			object.put("mPiMask", mPiMask);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String data = object.toString();
		return data;
	}

	/**
	 * 第一次加载默认图片完成刷新UI
	 */
	private void refreshUI() {
		mFristHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case SCAN_OK:
					if (!mIsSecondEdit) {
						initPicLocation(mPicBitmap, mMaskBitmap);
					}
					getUserSee();
					// showLoading(VIEW_LIST);
					break;
				}
			}
		};
	}

	/**
	 * 第一次加载默认图片完成刷新UI
	 */
	private void refreshPic() {
		mRefreshPicHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case SCAN_OK:
					changeData();
					break;
				}
			}

		};
	}

	/**
	 * 设置控件 的位置
	 * 
	 * @param view
	 * @param viewHeight
	 * @param viewWidth
	 * @param y
	 * @param x
	 */
	private void setView(View view, int viewWidth, int viewHeight, int x, int y) {
		float pi = getPro();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				view.getLayoutParams());
		params.width = (int) (viewWidth * pi);
		params.height = (int) (viewHeight * pi);
		params.leftMargin = (int) (mInitX + x * pi);
		params.topMargin = (int) (mInitY + y * pi);
		view.setLayoutParams(params);
	}

	/**
	 * 起始左边间距
	 * 
	 */
	private float getMarginleft(float viewWidth) {
		int mScreenWidth = AndroidUtils.getScreenWidth(this);
		float Padd = (mScreenWidth - viewWidth * getPro()) / 2;
		return Padd;
	}

	/**
	 * 获取图片缩放比例
	 * 
	 * @param mMobileHeight
	 * @return
	 */
	private float getPro() {
		int mScreenHeight = AndroidUtils.getScreenHeight(this);
		float viewHeight = mDesignH;
		float pi = mScreenHeight / viewHeight;
		return pi;
	}

	/**
	 * 截取view视图屏幕
	 * 
	 * @return viewBitmap
	 */
	private Bitmap getPicView() {
		int topMargin = 0;
		int bottomMargin = 0;
		int leftMargin = (int) mInitX;
		int rightMargin = leftMargin;
		Bitmap viewBitmap = AndroidUtils.takeScreenShot(
				EditPictureActivity.this, mMiddleRl, topMargin, bottomMargin,
				rightMargin, leftMargin);
		return viewBitmap;
	}

	/**
	 * 同步加载默认的蒙版和默认的用户图片
	 */
	private void synGetMask() {
		if (!mIsSecondEdit) {
			ImageLoader.getInstance().loadImage(mShell.getFrameBean().getUrl(),
					new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String arg0, View arg1) {
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1,
								FailReason arg2) {
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							mPicBitmap = loadedImage;
							mFirstCount += 1;
							if (mFirstCount == 2) {
								mFristHandler.sendEmptyMessage(SCAN_OK);
							}
							FileUtil.saveBitmap2Jpeg(loadedImage,
									MyConstants.CACHE_DIR,
									MyConstants.DEFAULT_PIC, MyConstants.JPEG);

						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
						}
					});
		} else {
			String filePath = FileUtil.getFilePath(mWorksDir, mPicId,
					MyConstants.JPEG);
			mPicBitmap = FileUtil.getSDCardImg(filePath);
			mPicWidth = mPicBitmap.getWidth();
			mPicHeight = mPicBitmap.getHeight();
			if (mPicBitmap == null) {
				Toast.makeText(this, getString(R.string.picture_lose),
						Toast.LENGTH_SHORT).show();
				LogUtils.logd(TAG, "mPicBitmap=null");
				AndroidUtils.finishActivity(EditPictureActivity.this);
			} else {
				mFirstCount += 1;
				if (mFirstCount == 2) {
					mFristHandler.sendEmptyMessage(SCAN_OK);
				}
			}
		}

		ImageLoader.getInstance().loadImage(
				mShell.getFrameBean().getFrameMaskUrl(),
				new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						mMaskBitmap = loadedImage;
						mFirstCount += 1;
						if (mFirstCount == 2) {
							mFristHandler.sendEmptyMessage(SCAN_OK);
						}
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
					}
				});
	}

	/**
	 * 从网上获取Mask 图片 ，并保存到SD卡。
	 * 
	 * @param mMaskUrl
	 */
	// private void loadMask(String mMaskUrl) {
	// ImageLoader.getInstance().loadImage(mMaskUrl,
	// new ImageLoadingListener() {
	// @Override
	// public void onLoadingStarted(String arg0, View arg1) {
	//
	// }
	//
	// @Override
	// public void onLoadingFailed(String arg0, View arg1,
	// FailReason arg2) {
	//
	// }
	//
	// @Override
	// public void onLoadingComplete(String arg0, View arg1,
	// Bitmap arg2) {
	// mMaskBitmap = arg2;
	// // changeTranslate(mPicBitmap, mMaskBitmap);
	// initPicLocation(mPicBitmap, mMaskBitmap);
	// getUserSee();
	// FileUtil.saveBitmap2Png(arg2, MyConstants.CACHE_DIR,
	// mMaskName, MyConstants.PNG);
	//
	// }
	//
	// @Override
	// public void onLoadingCancelled(String arg0, View arg1) {
	//
	// }
	// });
	// }

	/**
	 * 自动获取临时新账户信息
	 */
	private void getTempAccount() {
		NetHelper.getTempAccountInfo(new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				LogUtils.logd(TAG, "initTempAccountInfoSuccess");
				resolveTempAccountResponse(arg2);

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				LogUtils.logd(TAG, "initTempAccountInfoFailure");
			}
		});
	}

	/**
	 * 从网络获取临时账户申请临时作品id
	 */
	private void getWorkId(String id, String token) {
		NetHelper.getTempWorkId(id, token, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				LogUtils.logd(TAG, "initTempWorkIdSuccess");
				resolveInitTempWorkIdResponse(responseBody);

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				mWorksDir = FileUtil.getWorksDir(mWorkId);
				LogUtils.logd(TAG, "initTempWorkIdFailure");
			}
		});

	}

	/**
	 * 初始化商品信息
	 */
	private void initGoodsInfo() {
		NetHelper.getGoodsInfo(mGoodsId, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				LogUtils.logd(TAG, "initGoodsInfoonSuccess");
				resolveGoodsInfo(responseBody);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				LogUtils.logd(TAG, "initGoodsInfoonFailure");
				showLoading(VIEW_WIFIFAILUER);
			}
		});
	}

	/**
	 * 初始化模板信息
	 */
	private void initTemplateInfo(String template_id) {
		NetHelper.getTemplateInfo(template_id, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				LogUtils.logd(TAG, "initTemplateInfoSuccess");
				resolveTemplateInfo(responseBody);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				LogUtils.logd(TAG, "initTemplateInfoFailure");
				showLoading(VIEW_WIFIFAILUER);
			}
		});

	}

	/**
	 * 上传作品信息
	 * 
	 */
	private void uploadWorkInfo(String myContent) {
		String work_name = mWorkId;
		NetHelper.saveWorks(mAccountId, mAccountToken, mTempAccountId, mWorkId,
				mTemplateId, work_name, mCoverId, myContent,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "uploadWorkInfoonSuccess");
						try {
							JSONObject resolveResult = JSONUtil
									.resolveResult(arg2);
							if (resolveResult != null) {
								LogUtils.logd(TAG,
										"uploadWorkInfo+resolveResult:"
												+ resolveResult.toString());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						String filePath = FileUtil.getFilePath(mWorksDir,
								mCoverId, MyConstants.PNG);
						if (FileUtil.fileIsExists(filePath)) {
							File file = new File(filePath);
							if (file != null) {
								uploadUserPicture(mCoverId, file);
							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "uploadWorkInfoonFailure");
						mDialog.dismiss();
					}
				});
	}

	/**
	 * 上传图片
	 */
	private void uploadUserPicture(String pic_id, File file) {
		String pic_name = "a001";
		String pic_info = "";
		NetHelper.savePicture(pic_id, mWorkId, pic_name, pic_info, file,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						mUploadCout += 1;
						LogUtils.logd(TAG, "" + mUploadCout
								+ "uploadUserPictureonSuccess");
						if (mUploadCout == 1) {
							String filePath = FileUtil.getFilePath(mWorksDir,
									mPicId, MyConstants.JPEG);
							if (FileUtil.fileIsExists(filePath)) {
								File file = new File(filePath);
								if (file != null) {
									if (mIsSecondEdit && mRequestCout == 0) {
										mUploadCout += 1;
										LogUtils.logd(
												TAG,
												""
														+ mUploadCout
														+ "NOuploadUserPictureonSuccess");
										if (mUploadCout == 2) {
											saveToDB();
											gotoSubmitOrder();
											mDialog.dismiss();
											AndroidUtils
													.finishActivity(EditPictureActivity.this);
										}
										return;
									}
								}
								uploadUserPicture(mPicId, file);
							}
						}
						if (mUploadCout == 2) {
							saveToDB();
							gotoSubmitOrder();
							mDialog.dismiss();
							AndroidUtils
									.finishActivity(EditPictureActivity.this);
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "uploadUserPictureonFailure");
						mDialog.dismiss();
					}
				});
	}

	/**
	 * 保存到数据库
	 */
	private void saveToDB() {
		// String type = "iPhne6S";
		String type = "";
		String lasttime = System.currentTimeMillis() + "";
		WorksInfoBean works = new WorksInfoBean(mWorkId, mJsonStr, mTitleStr,
				mQuantity, mPrice, mCoverId, mGoodsId, type, mPicId,
				mProductId, mAccountId, "0", lasttime);
		DataHelper dh = DataHelper.getInstance(getApplicationContext());
		dh.addWorks(works);
	}

	/**
	 * 解析 临时账户信息 返回信息
	 * 
	 * @param response
	 */
	private void resolveTempAccountResponse(byte[] response) {
		try {
			JSONObject result = JSONUtil.resolveResult(response);
			JSONObject account_info = result.getJSONObject("account_info");
			mTempAccountId = account_info.getString("id");
			mTempToken = account_info.getString("token");
			getWorkId(mTempAccountId, mTempToken);
			if (mTempAccountId != null) {
				user.setTemp_id(mTempAccountId);
				user.setTemp_token(mTempToken);
				user.setIs_temp_login(true);
				mUserController.saveUserInfo(user);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析申请临时作品id 返回信息
	 * 
	 * @param responseBody
	 */
	private void resolveInitTempWorkIdResponse(byte[] responseBody) {
		try {
			JSONObject result = JSONUtil.resolveResult(responseBody);
			mWorkId = result.getString("work_id");
			mWorksDir = FileUtil.getWorksDir(mWorkId);
			File oleFile = new File(FileUtil.getWorksDir("0"));
			File newFile = new File(mWorksDir);
			oleFile.renameTo(newFile);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析商品信息 返回信息
	 * 
	 * @param responseBody
	 */
	private void resolveGoodsInfo(byte[] responseBody) {
		try {
			JSONObject result = JSONUtil.resolveResult(responseBody);
			LogUtils.logd(TAG, "resolveGoodsInfo+result:" + result);

			JSONArray skus = result.optJSONArray("skus");
			JSONObject object2 = (JSONObject) skus.get(0);
			JSONArray skus_content = object2.optJSONArray("skus_content");
			JSONObject object3 = (JSONObject) skus_content.get(0);
			mProductId = object3.optString("product_id");
			mPrice = StringUtils.format2point(object3.optString("price"));

			mTitleStr = result.optString("title");
			mTitle.setText(mTitleStr);

			JSONArray template_list = result.optJSONArray("template_list");
			JSONObject object = (JSONObject) template_list.get(0);
			mTemplateId = object.getString("template_id");
			initTemplateInfo(mTemplateId);
			if (result.optString("maxWidth") != null) {
				mMaxPicWidth = Integer.parseInt(result.getString("maxWidth"));
			}
			if (result.optString("maxHeight") != null) {
				mMaxPicHeight = Integer.parseInt(result.getString("maxHeight"));
			}
			if (result.optString("minWidth") != null) {
				mCache.put(MyConstants.MINWIDTH, result.getString("minWidth"));
			}
			if (result.optString("minHeight") != null) {
				mCache.put(MyConstants.MINWIDTH, result.getString("minHeight"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析模板信息 返回信息
	 * 
	 * @param responseBody
	 */
	private void resolveTemplateInfo(byte[] responseBody) {
		try {
			JSONObject result = JSONUtil.resolveResult(responseBody);
			if (result != null) {
				JSONArray jsonArray = result.optJSONObject("content")
						.optJSONArray("inpage");
				JSONObject template_info = result
						.optJSONObject("template_info");
				mDesignW = Float.parseFloat(template_info
						.optString("designw_inside"));
				mDesignH = Float.parseFloat(template_info
						.optString("designh_inside"));
				// mMaxPicWidth = (int) mDesignW;
				// mMaxPicHeight = (int) mDesignH;
				// mCache.put(MyConstants.MINWIDTH, mMaxPicWidth + "");
				// mCache.put(MyConstants.MINHEIGHT, mMaxPicHeight + "");
				mInitX = getMarginleft(mDesignW);
				mInitY = 0;
				mFrameJSONArray = JSONUtil.resolveNofJsonSONArray(jsonArray);
				setAdapterGridView();
				if (!mIsSecondEdit) {
					JSONObject jsonArray01 = (JSONObject) jsonArray.get(0);
					JSONArray firstJSONArray = new JSONArray(
							jsonArray01.getString("inpage_content"));
					setMobileData(firstJSONArray);
				} else {
					String firstWorkInfo = FileUtil.readFile(mWorksDir,
							mWorkId, MyConstants.TXT);
					if (firstWorkInfo == null) {
						Toast.makeText(this, getString(R.string.picture_lose),
								Toast.LENGTH_SHORT).show();
						LogUtils.logd(TAG, "firstWorkInfo=null");
						AndroidUtils.finishActivity(EditPictureActivity.this);
					} else {
						JSONArray firstJSONArray = new JSONArray(firstWorkInfo);
						setMobileData(firstJSONArray);
					}
				}
			} else {
				showLoading(VIEW_LOADFAILURE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			showLoading(VIEW_LOADFAILURE);
		}
	}

}
