package com.ddiiyy.xydz.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ddiiyy.xydz.base.BaseBean;
import com.ddiiyy.xydz.utils.JSONUtil;

public class MobileShell extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DecorationBean decorationBean;
	private PhotoBean photoBean;
	private FrameBean frameBean;
	private TextBean textBean;
	private PageBean pageBean;

	public DecorationBean getDecorationBean() {
		return decorationBean;
	}

	public void setDecorationBean(DecorationBean decorationBean) {
		this.decorationBean = decorationBean;
	}

	public PhotoBean getPhotoBean() {
		return photoBean;
	}

	public void setPhotoBean(PhotoBean photoBean) {
		this.photoBean = photoBean;
	}

	public FrameBean getFrameBean() {
		return frameBean;
	}

	public void setFrameBean(FrameBean frameBean) {
		this.frameBean = frameBean;
	}

	public TextBean getTextBean() {
		return textBean;
	}

	public void setTextBean(TextBean textBean) {
		this.textBean = textBean;
	}

	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	public MobileShell() {
		super();
	}

	public MobileShell(DecorationBean decorationBean, PhotoBean photoBean,
			FrameBean frameBean, TextBean textBean, PageBean pageBean) {
		super();
		this.decorationBean = decorationBean;
		this.photoBean = photoBean;
		this.frameBean = frameBean;
		this.textBean = textBean;
		this.pageBean = pageBean;
	}

	@Override
	public String toString() {
		return "MobileShell [decorationBean=" + decorationBean + ", photoBean="
				+ photoBean + ", frameBean=" + frameBean + ", textBean="
				+ textBean + ", pageBean=" + pageBean + "]";
	}

	public static MobileShell initMobileShellData(JSONArray myJsonObject)
			throws JSONException {
		MobileShell MmobileShell = new MobileShell();
		int indexMobile = JSONUtil.getIndex(myJsonObject, 0, 1, "decoration");
		int indexFrame = JSONUtil.getIndex(myJsonObject, 0, 3, "frame");
		int indexPage = JSONUtil.getIndex(myJsonObject, "page");
		int indexText = JSONUtil.getIndex(myJsonObject, "text");
		int indexPhoto = JSONUtil.getIndex(myJsonObject, "photo");
		if (-1 != indexMobile) {
			JSONObject mobile = (JSONObject) myJsonObject.get(indexMobile);
			DecorationBean mobileBean = (DecorationBean) JSONUtil.JSONToObj(
					mobile.toString(), DecorationBean.class);
			MmobileShell.setDecorationBean(mobileBean);
		}
		if (-1 != indexFrame) {
			JSONObject mask = (JSONObject) myJsonObject.get(indexFrame);
			FrameBean frameBean = (FrameBean) JSONUtil.JSONToObj(
					mask.toString(), FrameBean.class);
			MmobileShell.setFrameBean(frameBean);
		}
		if (-1 != indexText) {
			JSONObject text = (JSONObject) myJsonObject.get(indexText);
			TextBean textBean = (TextBean) JSONUtil.JSONToObj(text.toString(),
					TextBean.class);
			MmobileShell.setTextBean(textBean);
		}
		if (-1 != indexPage) {
			JSONObject page = (JSONObject) myJsonObject.get(indexPage);
			PageBean pageBean = (PageBean) JSONUtil.JSONToObj(page.toString(),
					PageBean.class);
			MmobileShell.setPageBean(pageBean);
		}
		if (-1 != indexPhoto) {
			JSONObject photo = (JSONObject) myJsonObject.get(indexPhoto);
			PhotoBean photoBean = (PhotoBean) JSONUtil.JSONToObj(
					photo.toString(), PhotoBean.class);
			MmobileShell.setPhotoBean(photoBean);
		}

		return MmobileShell;
	}

}
