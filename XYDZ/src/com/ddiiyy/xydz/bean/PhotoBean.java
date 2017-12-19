package com.ddiiyy.xydz.bean;

import com.ddiiyy.xydz.base.BaseBean;

public class PhotoBean extends BaseBean{

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
	private String rotationExif;//ͼƬ��exif����ת����
	private String alpha;//͸����
	private String imgid;//ͼƬ��ID
	private String url;//�û�ͼƬ��ַ
	private String theOrder;//����(�������)
	private String distortion;//�û�ͼƬ�Ƿ�ʧ��
	private String pageID;//��ǰģ��ҳID

	private String cutx;//�ü��е�x����
	private String cuty;//�ü��е�y����
	private String cutwidth;//�ü��еĿ�
	private String cutheight;//�ü��еĸ�
	private String tier;//�Ƿ������ϲ� 0�������1�ϲ�Ч������2�ײ�Ч����
	private String type;//ͼƬ���ͣ�0=����ͼƬ��1=�����еĴ�ͼ��2=����ͷ������û�û�ϴ�ͷ��Ͳ���ʾ��ӡˢ��ͷ�񣩣�3=����е�ͼƬ��4=��ҳ��ͼ
	private String isEdit;//��ǰ���ݸ��û��Ƿ���Ա༭��1=�ɱ༭���ݣ�0=���ܱ༭����

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
