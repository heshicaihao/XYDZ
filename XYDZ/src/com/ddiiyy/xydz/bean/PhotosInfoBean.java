package com.ddiiyy.xydz.bean;

import com.ddiiyy.xydz.base.BaseBean;

public class PhotosInfoBean extends BaseBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String imgid;
	private String imgpath;
	private String imgcompresswidth;
	private String imgcompressheight;
	private String imgcreattime;
	
	public PhotosInfoBean() {
		super();
	}

	public PhotosInfoBean(String id, String imgid, String imgpath,
			String imgcompresswidth, String imgcompressheight,
			String imgcreattime) {
		super();
		this.id = id;
		this.imgid = imgid;
		this.imgpath = imgpath;
		this.imgcompresswidth = imgcompresswidth;
		this.imgcompressheight = imgcompressheight;
		this.imgcreattime = imgcreattime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImgid() {
		return imgid;
	}

	public void setImgid(String imgid) {
		this.imgid = imgid;
	}

	public String getImgpath() {
		return imgpath;
	}

	public void setImgpath(String imgpath) {
		this.imgpath = imgpath;
	}

	public String getImgcompresswidth() {
		return imgcompresswidth;
	}

	public void setImgcompresswidth(String imgcompresswidth) {
		this.imgcompresswidth = imgcompresswidth;
	}

	public String getImgcompressheight() {
		return imgcompressheight;
	}

	public void setImgcompressheight(String imgcompressheight) {
		this.imgcompressheight = imgcompressheight;
	}

	public String getImgcreattime() {
		return imgcreattime;
	}

	public void setImgcreattime(String imgcreattime) {
		this.imgcreattime = imgcreattime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((imgcompressheight == null) ? 0 : imgcompressheight
						.hashCode());
		result = prime
				* result
				+ ((imgcompresswidth == null) ? 0 : imgcompresswidth.hashCode());
		result = prime * result
				+ ((imgcreattime == null) ? 0 : imgcreattime.hashCode());
		result = prime * result + ((imgid == null) ? 0 : imgid.hashCode());
		result = prime * result + ((imgpath == null) ? 0 : imgpath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhotosInfoBean other = (PhotosInfoBean) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imgcompressheight == null) {
			if (other.imgcompressheight != null)
				return false;
		} else if (!imgcompressheight.equals(other.imgcompressheight))
			return false;
		if (imgcompresswidth == null) {
			if (other.imgcompresswidth != null)
				return false;
		} else if (!imgcompresswidth.equals(other.imgcompresswidth))
			return false;
		if (imgcreattime == null) {
			if (other.imgcreattime != null)
				return false;
		} else if (!imgcreattime.equals(other.imgcreattime))
			return false;
		if (imgid == null) {
			if (other.imgid != null)
				return false;
		} else if (!imgid.equals(other.imgid))
			return false;
		if (imgpath == null) {
			if (other.imgpath != null)
				return false;
		} else if (!imgpath.equals(other.imgpath))
			return false;
		return true;
	}
	
	
}
