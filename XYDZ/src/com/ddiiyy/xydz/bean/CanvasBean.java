package com.ddiiyy.xydz.bean;

import com.ddiiyy.xydz.base.BaseBean;

public class CanvasBean  extends BaseBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String elementType;//类型special,text,decoration,frame,background,photo,page,smart,canvas
	private String id;//唯一ID
	private String x;//元素x坐标
	private String y;//元素y坐标
	private String width;//元素整体宽度
	private String height;//元素整体高度
	private String rotation;//旋转度数
	private String alpha;//透明度
	private String imgid;//关联图片ID
	private String theOrder;//排序(层叠索引)
	private String pageID;//当前模板页ID
	private String tier;//是否在最上层 0设计区，1上层效果区，2底层效果区
	private String layOut;//H=横向布局，V=竖向布局
	private String type;//容器类型：0=其他容器，1=扉页图片容器

	private String relateID;//关联ID
	private String relateFID;//关联值为relateID
	private String left;//相对父元素左对齐，为空代表不设置，内容一定是数字类型
	private String top;//相对父元素上对齐
	private String right;//相对父元素右对齐
	private String bottom;//相对父元素底对齐
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
	public String getRotation() {
		return rotation;
	}
	public void setRotation(String rotation) {
		this.rotation = rotation;
	}
	public String getAlpha() {
		return alpha;
	}
	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}
	public String getImgid() {
		return imgid;
	}
	public void setImgid(String imgid) {
		this.imgid = imgid;
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
	public String getTier() {
		return tier;
	}
	public void setTier(String tier) {
		this.tier = tier;
	}
	public String getLayOut() {
		return layOut;
	}
	public void setLayOut(String layOut) {
		this.layOut = layOut;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRelateID() {
		return relateID;
	}
	public void setRelateID(String relateID) {
		this.relateID = relateID;
	}
	public String getRelateFID() {
		return relateFID;
	}
	public void setRelateFID(String relateFID) {
		this.relateFID = relateFID;
	}
	public String getLeft() {
		return left;
	}
	public void setLeft(String left) {
		this.left = left;
	}
	public String getTop() {
		return top;
	}
	public void setTop(String top) {
		this.top = top;
	}
	public String getRight() {
		return right;
	}
	public void setRight(String right) {
		this.right = right;
	}
	public String getBottom() {
		return bottom;
	}
	public void setBottom(String bottom) {
		this.bottom = bottom;
	}
	public CanvasBean(String elementType, String id, String x, String y,
			String width, String height, String rotation, String alpha,
			String imgid, String theOrder, String pageID, String tier,
			String layOut, String type, String relateID, String relateFID,
			String left, String top, String right, String bottom) {
		super();
		this.elementType = elementType;
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.rotation = rotation;
		this.alpha = alpha;
		this.imgid = imgid;
		this.theOrder = theOrder;
		this.pageID = pageID;
		this.tier = tier;
		this.layOut = layOut;
		this.type = type;
		this.relateID = relateID;
		this.relateFID = relateFID;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	public CanvasBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
