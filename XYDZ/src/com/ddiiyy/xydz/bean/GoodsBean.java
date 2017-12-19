package com.ddiiyy.xydz.bean;

import java.util.List;

import com.ddiiyy.xydz.base.BaseBean;

public class GoodsBean extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<BackgroundBean> ListBackground;
	private List<CanvasBean> ListCanvas;
	private List<DecorationBean> ListDecoration;
	private List<FrameBean> ListFrame;
	private List<PageBean> ListPage;
	private List<PhotoBean> ListPhoto;
	private List<SpecialBean> ListSpecial;
	private List<TextBean> ListText;
	
	public List<BackgroundBean> getListBackground() {
		return ListBackground;
	}
	public void setListBackground(List<BackgroundBean> listBackground) {
		ListBackground = listBackground;
	}
	public List<CanvasBean> getListCanvas() {
		return ListCanvas;
	}
	public void setListCanvas(List<CanvasBean> listCanvas) {
		ListCanvas = listCanvas;
	}
	public List<DecorationBean> getListDecoration() {
		return ListDecoration;
	}
	public void setListDecoration(List<DecorationBean> listDecoration) {
		ListDecoration = listDecoration;
	}
	public List<FrameBean> getListFrame() {
		return ListFrame;
	}
	public void setListFrame(List<FrameBean> listFrame) {
		ListFrame = listFrame;
	}
	public List<PageBean> getListPage() {
		return ListPage;
	}
	public void setListPage(List<PageBean> listPage) {
		ListPage = listPage;
	}
	public List<PhotoBean> getListPhoto() {
		return ListPhoto;
	}
	public void setListPhoto(List<PhotoBean> listPhoto) {
		ListPhoto = listPhoto;
	}
	public List<SpecialBean> getListSpecial() {
		return ListSpecial;
	}
	public void setListSpecial(List<SpecialBean> listSpecial) {
		ListSpecial = listSpecial;
	}
	public List<TextBean> getListText() {
		return ListText;
	}
	public void setListText(List<TextBean> listText) {
		ListText = listText;
	}
	
	

}
