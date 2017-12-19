package com.ddiiyy.xydz.common;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.constants.MyConstants;
import com.ddiiyy.xydz.dialog.UpdateDialog;
import com.ddiiyy.xydz.net.NetHelper;
import com.ddiiyy.xydz.update.UIHelper;
import com.ddiiyy.xydz.utils.AndroidUtils;
import com.ddiiyy.xydz.utils.JSONUtil;
import com.ddiiyy.xydz.utils.LogUtils;
import com.ddiiyy.xydz.utils.StringUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 更新管理类
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年11月18日 下午4:21:00
 * 
 */

public class UpdateManager {
	private String TAG = "UpdateManager";
	private String mAppType = MyConstants.ANDROID;
	private boolean isShow = false;
	private double curVersionCode;
	private double netVersionCode;

	private Context mContext;

	private String version;
	private String download;
	@SuppressWarnings("unused")
	private String content;

	public UpdateManager(Context context, boolean isShow) {
		this.mContext = context;
		this.isShow = isShow;
		curVersionCode = AndroidUtils.getVersionCode(AppContext.getInstance()
				.getPackageName());
		LogUtils.logd(TAG, "curVersionCode:"+curVersionCode);
		getNetVersionInfo();
	}

	public boolean haveNew() {
		boolean haveNew = false;
		if (curVersionCode < netVersionCode) {
			haveNew = true;
		}
		return haveNew;
	}

	public void checkUpdate() {
		if (curVersionCode < netVersionCode) {
			showAlertDialog(mContext);
		}

	}

	public void AppUpdate() {
		if (curVersionCode < netVersionCode) {
			
			if (!StringUtils.isEmpty(download)) {
				UIHelper.openDownLoadService(mContext, download, version);
			}
			
		} else {
			Toast.makeText(mContext,
					mContext.getString(R.string.is_the_latest_version),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void getNetVersionInfo() {
		NetHelper.getVersionFromNet("" + curVersionCode, mAppType,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						LogUtils.logd(TAG, "getVersionFromNet+onSuccess");
						resolveVersionFromNetResponse(arg2);
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						LogUtils.logd(TAG, "getVersionFromNet+onFailure");
					}
				});
	}

	private void resolveVersionFromNetResponse(byte[] responseBody) {
		try {
			JSONObject result = JSONUtil.resolveResult(responseBody);
			LogUtils.logd(TAG, "result:" + result.toString());
			if (result != null) {
				version = result.optString("version");
				download = result.optString("download");
				content = result.optString("content");
				if (!StringUtils.isEmpty(version)) {
					netVersionCode = Double.parseDouble(version);
					LogUtils.logd(TAG, "netVersionCode:"+netVersionCode);
				}else {
					netVersionCode = 1;
				}
				
				if (isShow) {
					checkUpdate();
				} else {
					AppUpdate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showAlertDialog(Context context) {

		UpdateDialog.Builder builder = new UpdateDialog.Builder(context);
		builder.setMessage(context.getString(R.string.no_immediate_update));
		builder.setTitle(context.getString(R.string.check_for_updates));
		builder.setPositiveButton(context.getString(R.string.later_on),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 设置你的操作事项
					}
				});

		builder.setNegativeButton(context.getString(R.string.now_updates),
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						LogUtils.logd(TAG, "download:" + download);
						LogUtils.logd(TAG, "version:" + version);
						if (!StringUtils.isEmpty(download)) {
							UIHelper.openDownLoadService(mContext, download,
									version);
						}
						dialog.dismiss();
					}
				});

		builder.create().show();

	}

}
