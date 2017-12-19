package com.ddiiyy.xydz.bean;

public class QQInfo {

	private String is_yellow_year_vip, vip, level, province, yellow_vip_level,
			is_yellow_vip, gender, openid, screen_name, msg, profile_image_url,
			city;

	public String getIs_yellow_year_vip() {
		return is_yellow_year_vip;
	}

	public void setIs_yellow_year_vip(String is_yellow_year_vip) {
		this.is_yellow_year_vip = is_yellow_year_vip;
	}

	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getYellow_vip_level() {
		return yellow_vip_level;
	}

	public void setYellow_vip_level(String yellow_vip_level) {
		this.yellow_vip_level = yellow_vip_level;
	}

	public String getIs_yellow_vip() {
		return is_yellow_vip;
	}

	public void setIs_yellow_vip(String is_yellow_vip) {
		this.is_yellow_vip = is_yellow_vip;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "QQInfo [is_yellow_year_vip=" + is_yellow_year_vip + ", vip="
				+ vip + ", level=" + level + ", province=" + province
				+ ", yellow_vip_level=" + yellow_vip_level + ", is_yellow_vip="
				+ is_yellow_vip + ", gender=" + gender + ", openid=" + openid
				+ ", screen_name=" + screen_name + ", msg=" + msg
				+ ", profile_image_url=" + profile_image_url + ", city=" + city
				+ "]";
	}

	public QQInfo(String is_yellow_year_vip, String vip, String level,
			String province, String yellow_vip_level, String is_yellow_vip,
			String gender, String openid, String screen_name, String msg,
			String profile_image_url, String city) {
		super();
		this.is_yellow_year_vip = is_yellow_year_vip;
		this.vip = vip;
		this.level = level;
		this.province = province;
		this.yellow_vip_level = yellow_vip_level;
		this.is_yellow_vip = is_yellow_vip;
		this.gender = gender;
		this.openid = openid;
		this.screen_name = screen_name;
		this.msg = msg;
		this.profile_image_url = profile_image_url;
		this.city = city;
	}

	public QQInfo() {
		super();
	}

	
}
