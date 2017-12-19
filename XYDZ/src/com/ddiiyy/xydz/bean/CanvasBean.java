package com.ddiiyy.xydz.bean;

import com.ddiiyy.xydz.base.BaseBean;

public class CanvasBean  extends BaseBean{
	
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
	private String rotation;//��ת����
	private String alpha;//͸����
	private String imgid;//����ͼƬID
	private String theOrder;//����(�������)
	private String pageID;//��ǰģ��ҳID
	private String tier;//�Ƿ������ϲ� 0�������1�ϲ�Ч������2�ײ�Ч����
	private String layOut;//H=���򲼾֣�V=���򲼾�
	private String type;//�������ͣ�0=����������1=��ҳͼƬ����

	private String relateID;//����ID
	private String relateFID;//����ֵΪrelateID
	private String left;//��Ը�Ԫ������룬Ϊ�մ������ã�����һ������������
	private String top;//��Ը�Ԫ���϶���
	private String right;//��Ը�Ԫ���Ҷ���
	private String bottom;//��Ը�Ԫ�ص׶���
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
