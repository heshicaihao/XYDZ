package com.ddiiyy.xydz.bean;

import org.json.JSONException;
import org.json.JSONObject;

import com.ddiiyy.xydz.base.BaseBean;

public class FrameBean extends BaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String elementType;//类型special,text,frame,frame,background,photo,page,smart,canvas
	private String id;//唯一ID
	private String x;//元素x坐标
	private String y ;//元素y坐标
	private String width;//元素整体宽度
	private String height;//元素整体高度
	private String rotation;//旋转度数
	private String alpha;//透明度
	private String imgid;//图片表ID
	private String url;//用户图片地址
	private String theOrder;//排序(层叠索引)
	private String distortion;//是否失真
	private String pageID;//当前模板页ID
	private String cutx;//裁剪中的x坐标
	private String cuty;//裁剪中的y坐标
	private String cutwidth;//裁剪中的宽
	private String cutheight;//裁剪中的高
	private String tier;//是否在最上层 0设计区，1上层效果区，2底层效果区
	private String type;//图片类型：0=正常图片，1=封面中的大图，2=作者头像，3=封底中的图片，4=扉页大图，5=扉页小图
	private String frameUrl;//边框地址
	private String frameMaskUrl;//边框蒙板地址
	private String frameHeight;//边框图片高度
	private String frameWidth;//边框图片宽度
	private String frameID;//边框ID
	private String isEdit;//当前内容给用户是否可以编辑：1=可编辑内容，0=不能编辑内容
	private String relateID  ;//关联ID
	private String relateFID  ;//关联值为relateID
	private String left ;//相对父元素左对齐，为空代表不设置，内容一定是数字类型
	private String top  ;//相对父元素上对齐
	private String right ;//相对父元素右对齐
	private String bottom ;//相对父元素底对齐
	private String remark ;//相对父元素底对齐
//	private String rotationExif;//图片中exif的旋转度数
	public String getElementType() {
		return elementType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
//	public String getRotationExif() {
//		return rotationExif;
//	}
//	public void setRotationExif(String rotationExif) {
//		this.rotationExif = rotationExif;
//	}
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
	public String getFrameUrl() {
		return frameUrl;
	}
	public void setFrameUrl(String frameUrl) {
		this.frameUrl = frameUrl;
	}
	public String getFrameMaskUrl() {
		return frameMaskUrl;
	}
	public void setFrameMaskUrl(String frameMaskUrl) {
		this.frameMaskUrl = frameMaskUrl;
	}
	public String getFrameHeight() {
		return frameHeight;
	}
	public void setFrameHeight(String frameHeight) {
		this.frameHeight = frameHeight;
	}
	public String getFrameWidth() {
		return frameWidth;
	}
	public void setFrameWidth(String frameWidth) {
		this.frameWidth = frameWidth;
	}
	public String getFrameID() {
		return frameID;
	}
	public void setFrameID(String frameID) {
		this.frameID = frameID;
	}
	public String getIsEdit() {
		return isEdit;
	}
	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
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
	public FrameBean(String elementType, String id, String x, String y,
			String width, String height, String rotation, String rotationExif,
			String alpha, String imgid, String url, String theOrder,
			String distortion, String pageID, String cutx, String cuty,
			String cutwidth, String cutheight, String tier, String type,
			String frameUrl, String frameMaskUrl, String frameHeight,
			String frameWidth, String frameID, String isEdit, String relateID,
			String relateFID, String left, String top, String right,
			String bottom) {
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
		this.frameUrl = frameUrl;
		this.frameMaskUrl = frameMaskUrl;
		this.frameHeight = frameHeight;
		this.frameWidth = frameWidth;
		this.frameID = frameID;
		this.isEdit = isEdit;
		this.relateID = relateID;
		this.relateFID = relateFID;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	public FrameBean() {
		super();
	}
	
	public static FrameBean getDataToJson(String json) {
		FrameBean frame = null;
		try {
			frame = new FrameBean();
			JSONObject object = new JSONObject(json);
			frame.setElementType(object.getString("elementType"));
			frame.setId(object.getString("id"));
			frame.setX(object.getString("x"));
			frame.setY(object.getString("y"));
			frame.setWidth(object.getString("width"));
			frame.setHeight(object.getString("height"));
			frame.setRotation(object.getString("rotation"));
			frame.setAlpha(object.getString("alpha"));
			frame.setImgid(object.getString("imgid"));
			frame.setUrl(object.getString("url"));
			frame.setTheOrder(object.getString("theOrder"));
			frame.setDistortion(object.getString("distortion"));
			frame.setPageID(object.getString("pageID"));
			frame.setCutx(object.getString("cutx"));
			frame.setCuty(object.getString("cuty"));
			frame.setCutwidth(object.getString("cutwidth"));
			frame.setCutheight(object.getString("cutheight"));
			frame.setTier(object.getString("tier"));
			frame.setType(object.getString("type"));
			frame.setFrameUrl(object.getString("frameUrl"));
			frame.setFrameMaskUrl(object.getString("frameMaskUrl"));
			frame.setFrameHeight(object.getString("frameHeight"));
			frame.setFrameWidth(object.getString("frameWidth"));
			frame.setFrameID(object.getString("frameID"));
			frame.setIsEdit(object.getString("isEdit"));
			frame.setRelateID(object.getString("relateID"));
			frame.setRelateFID(object.getString("relateFID"));
			frame.setLeft(object.getString("left"));
			frame.setTop(object.getString("top"));
			frame.setRight(object.getString("right"));
			frame.setBottom(object.getString("bottom"));
			frame.setRemark(object.getString("remark"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return frame;
	}
	
	

}
