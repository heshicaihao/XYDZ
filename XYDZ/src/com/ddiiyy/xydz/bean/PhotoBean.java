package com.ddiiyy.xydz.bean;

import com.ddiiyy.xydz.base.BaseBean;

public class PhotoBean extends BaseBean{

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
	private String rotationExif;//图片中exif的旋转度数
	private String alpha;//透明度
	private String imgid;//图片表ID
	private String url;//用户图片地址
	private String theOrder;//排序(层叠索引)
	private String distortion;//用户图片是否失真
	private String pageID;//当前模板页ID

	private String cutx;//裁剪中的x坐标
	private String cuty;//裁剪中的y坐标
	private String cutwidth;//裁剪中的宽
	private String cutheight;//裁剪中的高
	private String tier;//是否在最上层 0设计区，1上层效果区，2底层效果区
	private String type;//图片类型：0=正常图片，1=封面中的大图，2=作者头像（如果用户没上传头像就不显示和印刷此头像），3=封底中的图片，4=扉页大图
	private String isEdit;//当前内容给用户是否可以编辑：1=可编辑内容，0=不能编辑内容

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
	public String getRotationExif() {
		return rotationExif;
	}
	public void setRotationExif(String rotationExif) {
		this.rotationExif = rotationExif;
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
	public String getDistortion() {
		return distortion;
	}
	public void setDistortion(String distortion) {
		this.distortion = distortion;
	}
	public String getPageID() {
		return pageID;
	}
	public void setPageID(String pageID) {
		this.pageID = pageID;
	}
	public String getCutx() {
		return cutx;
	}
	public void setCutx(String cutx) {
		this.cutx = cutx;
	}
	public String getCuty() {
		return cuty;
	}
	public void setCuty(String cuty) {
		this.cuty = cuty;
	}
	public String getCutwidth() {
		return cutwidth;
	}
	public void setCutwidth(String cutwidth) {
		this.cutwidth = cutwidth;
	}
	public String getCutheight() {
		return cutheight;
	}
	public void setCutheight(String cutheight) {
		this.cutheight = cutheight;
	}
	public String getTier() {
		return tier;
	}
	public void setTier(String tier) {
		this.tier = tier;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIsEdit() {
		return isEdit;
	}
	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
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
	public PhotoBean(String elementType, String id, String x, String y,
			String width, String height, String rotation, String rotationExif,
			String alpha, String imgid, String url, String theOrder,
			String distortion, String pageID, String cutx, String cuty,
			String cutwidth, String cutheight, String tier, String type,
			String isEdit, String relateFID, String left, String top,
			String right, String bottom) {
		super();
		this.elementType = elementType;
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.rotation = rotation;
		this.rotationExif = rotationExif;
		this.alpha = alpha;
		this.imgid = imgid;
		this.url = url;
		this.theOrder = theOrder;
		this.distortion = distortion;
		this.pageID = pageID;
		this.cutx = cutx;
		this.cuty = cuty;
		this.cutwidth = cutwidth;
		this.cutheight = cutheight;
		this.tier = tier;
		this.type = type;
		this.isEdit = isEdit;
		this.relateFID = relateFID;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	public PhotoBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
