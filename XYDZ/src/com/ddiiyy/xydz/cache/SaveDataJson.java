package com.ddiiyy.xydz.cache;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ddiiyy.xydz.bean.DecorationBean;
import com.ddiiyy.xydz.bean.FrameBean;
import com.ddiiyy.xydz.bean.MobileShell;
import com.ddiiyy.xydz.bean.PageBean;
import com.ddiiyy.xydz.bean.PhotoBean;
import com.ddiiyy.xydz.bean.TextBean;
import com.ddiiyy.xydz.utils.JSONUtil;
import com.ddiiyy.xydz.utils.LogUtils;

public class SaveDataJson {
	public static String  TAG = "SaveDataJson";
	
	/**
	 * 获取作品数据json
	 * 
	 * @param mContext
	 * @return
	 */
	public static String SaveWorkData(MobileShell mShell) {
		DecorationBean decorationBean = mShell.getDecorationBean();
		FrameBean frameBean = mShell.getFrameBean();
		PageBean pageBean = mShell.getPageBean();
		TextBean textBean = mShell.getTextBean();
		PhotoBean photoBean = mShell.getPhotoBean();
		int cout = -1;
		String myJson = null;
		try {
			JSONArray jsonarray = new JSONArray();
			LogUtils.logd(TAG, "jsonarray:");
			if (decorationBean != null) {
				if ("decoration".equals(decorationBean.getElementType())) {
					JSONObject decoration = JSONUtil
							.objectToJson(decorationBean);
					cout++;
					jsonarray.put(cout, decoration);
				}
			}
			LogUtils.logd(TAG, "decoration:");
			if (photoBean != null) {
				if ("photo".equals(photoBean.getElementType())) {
					JSONObject photo = JSONUtil.objectToJson(photoBean);
					cout++;
					jsonarray.put(cout, photo);
				}
			}
			LogUtils.logd(TAG, "photo:");
			if (frameBean != null) {
				if ("frame".equals(frameBean.getElementType())) {
					JSONObject frame = JSONUtil.objectToJson(frameBean);
					cout++;
					jsonarray.put(cout, frame);
				}
			}
			LogUtils.logd(TAG, "frame:");
			if (textBean != null) {
				if ("text".equals(textBean.getElementType())) {
					JSONObject text = JSONUtil.objectToJson(textBean);
					cout++;
					jsonarray.put(cout, text);
				}
			}
			LogUtils.logd(TAG, "text:");
			if (pageBean != null) {
				if ("page".equals(pageBean.getElementType())) {
					JSONObject page = JSONUtil.objectToJson(pageBean);
					cout++;
					jsonarray.put(cout, page);
				}
			}
			LogUtils.logd(TAG, "page:");
			myJson = jsonarray.toString();
			LogUtils.logd(TAG, "myJson:"+myJson);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.logd(TAG, "e.printStackTrace();");
		}
		return myJson;
	}
}
