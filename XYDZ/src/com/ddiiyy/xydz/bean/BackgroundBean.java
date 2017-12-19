package com.ddiiyy.xydz.bean;

import com.ddiiyy.xydz.base.BaseBean;

public class BackgroundBean extends BaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String elementType;//����special,text,decoration,frame,background,photo,page,smart,canvas
	private String id;//ΨһID
	private String x;//Ԫ��x����
	private String y;//Ԫ��y����
	private String width;//Ԫ��������
	private String height;//Ԫ������߶�
	private String imgid;//������ID
	private String url;//����ͼƬ��ַ
	private String theOrder;//���򱳾�Ԫ�ص�������ԶΪ0
	private String pageID;//��ǰģ��ҳID
	
	public String getElementType() {
		return elementType;
	}
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getImgid() {
		return imgid;
	}
	public void setImgid(String imgid) {
		this.imgid = imgid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTheOrder() {
		return theOrder;
	}
	public void setTheOrder(String theOrder) {
		this.theOrder = theOrder;
	}
	public String getPageID() {
		return pageID;
	}
	public void setPageID(String pageID) {
		this.pageID = pageID;
	}
	public BackgroundBean(String elementType, String id, String x, String y,
			String width, String height, String imgid, String url,
			String theOrder, String pageID) {
		super();
		this.elementType = elementType;
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.imgid = imgid;
		this.url = url;
		this.theOrder = theOrder;
		this.pageID = pageID;
	}
	public BackgroundBean() {
		super();
	}
	
	
	
	
	
	
}
