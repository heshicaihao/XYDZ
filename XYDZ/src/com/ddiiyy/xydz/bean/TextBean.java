package com.ddiiyy.xydz.bean;

import org.json.JSONException;
import org.json.JSONObject;

import com.ddiiyy.xydz.base.BaseBean;

public class TextBean extends BaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String elementType;//类型special,text,decoration,text,background,photo,page,smart,canvas
	private String maxChar;//文字内容最大长度
	private String imgid;//图片关联ID
	private String id;//唯一ID
	private String x;//元素x坐标
	private String y;//元素y坐标
	private String width;//元素整体宽度
	private String height;//元素整体高度
	private String rotation;//旋转度数
	private String alpha;//透明度
	private String text;//文字内容
	private String theOrder;//排序(层叠索引)
	private String fontfamily;//字体类型
	private String color;//文字颜色
	private String bgcolor;//文字背景颜色
	private String size;//字体大小
	private String pageID;//当前模板页ID
	private String isBold;//是否加粗
	private String align;//left=居左，center=居中，right=居右 ，V=竖排文字
	private String isEdit;//当前内容给用户是否可以编辑：1=可编辑内容，0=不能编辑内容
	private String type;//0=其他内容，1=姓名，2=年龄（阿拉伯数字:2岁,如果有生日程序自动加上(生日)），3=年龄（中文的数字:二岁），4=性别，5=生日，6=生辰八字，7=属相，8=星座，9=作品标题，10=宝宝时间树开始时间，11=宝宝时间树结束时间，12=图片数量，13=图片的日期，14=每组图片的开始时间+结束时间，format=格式6，15=所有图片的开始时间+结束时间，format=格式7，16=获取当前时间（暂时不用），17=相机品牌，18=相机光圈信息，19=图片描述信息，20=图片的地理位置，21=作者名称（摄影类），22=作者简介（摄影类），23=台历的日期
	private String format;//日期文字格式，如{yyyy-MM-dd},格式1={yyyy-MM-dd},格式2={yyyy.MM.dd},格式3={MM.dd},格式4={dd},格式5={week},格式6={yyyy.MM.dd}-{MM.dd},格式7={yyyy年MM月dd日}-{yyyy年MM月dd日},格式8={yyyy.MM},格式9={yyyy}，格式10=年龄：{0}，格式10=性别：{0}，格式10=生日：{yyyy.MM.dd}，格式10=属相{0}，格式10=星座{0},格式11={yyyy年MM月dd日},格式12英文的月份={month},格式13={yyyy年MM月dd日}-{MM月dd日},格式13={holiday}
	private String relateID;//关联ID
	private String relateFID;//关联值为relateID
	private String left;//相对父元素左对齐，为空代表不设置，内容一定是数字类型
	private String top;//相对父元素上对齐，为空代表不设置，内容一定是数字类型
	private String right;//相对父元素右对齐，为空代表不设置，内容一定是数字类型
	private String bottom;//相对父元素底对齐，为空代表不设置，内容一定是数字类型
	private String remark;
//	private String LineSpace;//多行文字的行间距
//	private String RowMaxChar;//如：每行最多15个字
//	private String tier;//是否在最上层 0设计区，1上层效果区，2底层效果区
//	private String lines;//0=可换行，大于0就是说明最大多少行

	public String getElementType() {
		return elementType;
	}


	public void setElementType(String elementType) {
		this.elementType = elementType;
	}


	public String getMaxChar() {
		return maxChar;
	}


	public void setMaxChar(String maxChar) {
		this.maxChar = maxChar;
	}


	public String getImgid() {
		return imgid;
	}


	public void setImgid(String imgid) {
		this.imgid = imgid;
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


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public String getTheOrder() {
		return theOrder;
	}


	public void setTheOrder(String theOrder) {
		this.theOrder = theOrder;
	}


	public String getFontfamily() {
		return fontfamily;
	}


	public void setFontfamily(String fontfamily) {
		this.fontfamily = fontfamily;
	}


	public String getColor() {
		return color;
	}


	public void setColor(String color) {
		this.color = color;
	}


	public String getBgcolor() {
		return bgcolor;
	}


	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}


	public String getSize() {
		return size;
	}


	public void setSize(String size) {
		this.size = size;
	}


	public String getPageID() {
		return pageID;
	}


	public void setPageID(String pageID) {
		this.pageID = pageID;
	}


	public String getIsBold() {
		return isBold;
	}


	public void setIsBold(String isBold) {
		this.isBold = isBold;
	}


	public String getAlign() {
		return align;
	}


	public void setAlign(String align) {
		this.align = align;
	}


	public String getIsEdit() {
		return isEdit;
	}


	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getFormat() {
		return format;
	}


	public void setFormat(String format) {
		this.format = format;
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


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public TextBean() {
		super();
		// TODO Auto-generated constructor stub
	}


	public TextBean(String elementType, String maxChar, String imgid,
			String id, String x, String y, String width, String height,
			String rotation, String alpha, String text, String theOrder,
			String fontfamily, String color, String bgcolor, String size,
			String pageID, String isBold, String align, String isEdit,
			String type, String format, String relateID, String relateFID,
			String left, String top, String right, String bottom, String remark) {
		super();
		this.elementType = elementType;
		this.maxChar = maxChar;
		this.imgid = imgid;
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.rotation = rotation;
		this.alpha = alpha;
		this.text = text;
		this.theOrder = theOrder;
		this.fontfamily = fontfamily;
		this.color = color;
		this.bgcolor = bgcolor;
		this.size = size;
		this.pageID = pageID;
		this.isBold = isBold;
		this.align = align;
		this.isEdit = isEdit;
		this.type = type;
		this.format = format;
		this.relateID = relateID;
		this.relateFID = relateFID;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.remark = remark;
	}
	
	public static TextBean getDataToJson(String json) {
		TextBean text = null;
		try {
			text = new TextBean();
			JSONObject object = new JSONObject(json);
			text.setElementType(object.getString("elementType"));
			text.setMaxChar(object.getString("maxChar"));
			text.setImgid(object.getString("imgid"));
			text.setId(object.getString("id"));
			text.setX(object.getString("x"));
			text.setY(object.getString("y"));
			text.setWidth(object.getString("width"));
			text.setHeight(object.getString("height"));
			text.setRotation(object.getString("rotation"));
			text.setAlpha(object.getString("alpha"));
			text.setText(object.getString("text"));
			text.setTheOrder(object.getString("theOrder"));
			text.setFontfamily(object.getString("fontfamily"));
			text.setColor(object.getString("color"));
			text.setBgcolor(object.getString("bgcolor"));
			text.setSize(object.getString("size"));
			text.setPageID(object.getString("pageID"));
			text.setIsBold(object.getString("isBold"));
			text.setAlign(object.getString("align"));
			text.setIsEdit(object.getString("isEdit"));
			text.setType(object.getString("type"));
			text.setFormat(object.getString("format"));
			text.setIsEdit(object.getString("isEdit"));
			text.setRelateID(object.getString("relateID"));
			text.setRelateFID(object.getString("relateFID"));
			text.setLeft(object.getString("left"));
			text.setTop(object.getString("top"));
			text.setRight(object.getString("right"));
			text.setBottom(object.getString("bottom"));
			text.setRemark(object.getString("remark"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return text;
	}
	

}
