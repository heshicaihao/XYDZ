package com.ddiiyy.xydz.net;

import java.io.File;
import java.io.FileNotFoundException;

import com.ddiiyy.xydz.utils.LogUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 联网接口
 * 
 * @author heshicaihao
 * 
 */
public class NetHelper {

	private static final String TAG = "NetHelper";

	/**
	 * 初始化商品信息请求 post
	 * 
	 * @param goods_id
	 *            商品Id
	 * @param handler
	 */
	public static void getGoodsInfo(String goods_id,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("goods_id", goods_id);
		HttpClient.post(MyURL.GOODS_URL, params, handler);
	}

	/**
	 * 初始化模板信息发起请求 post
	 * 
	 * @param template_id
	 *            模板Id
	 * @param handler
	 */
	public static void getTemplateInfo(String template_id,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("template_id", template_id);
		HttpClient.post(MyURL.TEMPLATE_URL, params, handler);
	}

	/**
	 * 上传作品信息 post请求
	 * 
	 * @param account_id
	 * @param account_token
	 * @param tmp_account_id
	 * @param work_id
	 * @param template_id
	 * @param work_name
	 * @param work_cover_id
	 * @param content
	 * @param handler
	 */
	public static void saveWorks(String account_id, String account_token,
			String tmp_account_id, String work_id, String template_id,
			String work_name, String work_cover_id, String content,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("tmp_account_id", tmp_account_id);
		params.put("work_id", work_id);
		params.put("template_id", template_id);
		params.put("work_name", work_name);
		params.put("work_cover_id", work_cover_id);
		params.put("content", content);
		HttpClient.post(MyURL.SAVE_WORKS_URL, params, handler);
	}

	/**
	 * 上传 图片 post请求
	 * 
	 * @param pic_id
	 * @param work_id
	 * @param pic_name
	 * @param pic_info
	 * @param file
	 * @param handler
	 */
	public static void savePicture(String pic_id, String work_id,
			String pic_name, String pic_info, File file,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("pic_id", pic_id);
		params.put("work_id", work_id);
		params.put("pic_name", pic_name);
		params.put("pic_info", pic_info);
		try {
			params.put("file", file);
		} catch (FileNotFoundException e) {
			LogUtils.logd(TAG, "没有文件");
		}
		HttpClient.post(MyURL.UPLOADPICTURE_URL, params, handler);
	}

	/**
	 * 获取临时账户申请临时作品id post请求
	 * 
	 * @param account_id
	 * @param account_token
	 * @param handler
	 */
	public static void getTempWorkId(String account_id, String account_token,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", "");
		HttpClient.post(MyURL.TEMP_WORK_URL, params, handler);
	}

	/**
	 * 申请生成临时新账户信息 post请求
	 * 
	 * @param handler
	 */
	public static void getTempAccountInfo(AsyncHttpResponseHandler handler) {
		HttpClient.post(MyURL.TEMP_ACCOUNT_URL, handler);
	}

	/**
	 * 地址列表 post请求
	 * 
	 * @param handler
	 */
	public static void getAddressList(String account_id, String account_token,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		HttpClient.post(MyURL.ADDRESS_LIST_URL, params, handler);
	}

	/**
	 * 添加新地址 post请求
	 * 
	 * @param handler
	 */
	public static void addAddress(String account_id, String account_token,
			String ship_name, String ship_area, String ship_addr,
			String ship_zip, String ship_tel, String ship_mobile,
			boolean is_default, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("ship_name", ship_name);
		params.put("ship_area", ship_area);
		params.put("ship_addr", ship_addr);
		params.put("ship_zip", ship_zip);
		params.put("ship_tel", ship_tel);
		params.put("ship_mobile", ship_mobile);
		params.put("is_default", is_default);
		HttpClient.post(MyURL.ADD_ADDRESS_URL, params, handler);
	}

	/**
	 * 更新单项地址簿信息 post请求
	 * 
	 * @param handler
	 */
	public static void reviseAddress(String account_id, String account_token,
			String ship_id, String ship_name, String ship_area,
			String ship_addr, String ship_zip, String ship_tel,
			String ship_mobile, boolean is_default,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("ship_id", ship_id);
		params.put("ship_name", ship_name);
		params.put("ship_area", ship_area);
		params.put("ship_addr", ship_addr);
		params.put("ship_zip", ship_zip);
		params.put("ship_tel", ship_tel);
		params.put("ship_mobile", ship_mobile);
		params.put("is_default", is_default);
		HttpClient.post(MyURL.ADD_ADDRESS_URL, params, handler);
	}

	/**
	 * 删除地址post请求
	 * 
	 * @param handler
	 */
	public static void deleteAddress(String account_id, String account_token,
			String[] ship_id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("ship_id", ship_id);
		HttpClient.post(MyURL.DELETE_ADDRESS, params, handler);
	}

	/**
	 * 获取配送方式列表 post请求
	 * 
	 * @param handler
	 */
	public static void getShippingList(String account_id, String account_token,
			String area_id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("area_id", area_id);
		HttpClient.post(MyURL.SHIPPING_LIST_URL, params, handler);
	}

	/**
	 * 获取订单价钱信息 post请求
	 * 
	 * @param handler
	 */
	public static void getCheckCost(String account_id, String account_token,
			String cur, String shipping_id, String is_protect, String is_tax,
			String tax_type, String payment, String area, String area_id,
			String dis_point, String coupon, String objects,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("cur", cur);
		params.put("shipping_id", shipping_id);
		params.put("is_protect", is_protect);
		params.put("is_tax", is_tax);
		params.put("tax_type", tax_type);
		params.put("payment", payment);
		params.put("area", area);
		params.put("area_id", area_id);
		params.put("dis_point", dis_point);
		params.put("coupon", coupon);
		params.put("objects", objects);
		HttpClient.post(MyURL.CHECK_COST_URL, params, handler);
	}

	/**
	 * 提交新订单数据 post请求
	 * 
	 * @param handler
	 */
	public static void createOrders(String account_id, String account_token,
			String area, String addr, String zip, String name, String mobile,
			String shipping_id, String tel, String day, String special,
			String time, String payment_currency, String payment_pay_app_id,
			String payment_is_tax, String payment_tex_type,
			String payment_tax_company, String payment_tax_content,
			String coupon, String memo, String objects,
			AsyncHttpResponseHandler handler) {

		RequestParams params = new RequestParams();

		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("area", area);
		params.put("addr", addr);
		params.put("zip", zip);
		params.put("name", name);
		params.put("mobile", mobile);
		params.put("shipping_id", shipping_id);
		params.put("tel", tel);
		params.put("day", day);
		params.put("special", special);
		params.put("time", time);
		params.put("payment_currency", payment_currency);
		params.put("payment_pay_app_id", payment_pay_app_id);
		params.put("payment_is_tax", payment_is_tax);
		params.put("payment_tex_type", payment_tex_type);
		params.put("payment_tax_company", payment_tax_company);
		params.put("payment_tax_content", payment_tax_content);
		params.put("coupon", coupon);
		params.put("memo", memo);
		params.put("objects", objects);
		HttpClient.post(MyURL.CREATE_ORDERS_URL, params, handler);
	}

	/**
	 * 获取地区关系信息JSON数据 post请求
	 * 
	 * @param handler
	 */
	public static void getAreaInfo(AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		HttpClient.post(MyURL.AREA_INFO_URL, params, handler);
	}

	/**
	 * 设置默认地址信息 post请求
	 * 
	 * @param handler
	 */
	public static void setDefault(String account_id, String account_token,
			String ship_id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("ship_id", ship_id);
		HttpClient.post(MyURL.SET_DEFAULT_URL, params, handler);
	}

	/**
	 * 微信支付，接口 post请求
	 * 
	 * @param handler
	 */
	public static void wechatPay(String order_id, String body, String price,
			String account_id, String account_token,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("order_id", order_id);
		params.put("body", body);
		params.put("price", price);
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		HttpClient.post(MyURL.WECHAT_PAY_URL, params, handler);
	}

	/**
	 * 手机注册新账号验证码确认 post请求
	 * 
	 * @param handler
	 */
	public static void registerSendCode(String phonenum,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("phonenum", phonenum);
		HttpClient.post(MyURL.REGISTER_SEND_CODE_URL, params, handler);
	}

	/**
	 * 手机忘记密码,账号验证码确认 post请求
	 * 
	 * @param handler
	 */
	public static void forgotSendCode(String phonenum,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("phonenum", phonenum);
		HttpClient.post(MyURL.FORGOT_SEND_CODE_URL, params, handler);
	}

	/**
	 * 手机忘记密码, post请求
	 * 
	 * @param handler
	 */
	public static void registerUser(String phonenum, String authcode,
			String pwcode, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("phonenum", phonenum);
		params.put("authcode", authcode);
		params.put("pwcode", pwcode);
		HttpClient.post(MyURL.REGISTER_URL, params, handler);
	}

	/**
	 * 账号登录 post请求
	 * 
	 * @param handler
	 */
	public static void loginUser(String account_id, String account_pw,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_pw", account_pw);
		HttpClient.post(MyURL.LOGIN_URL, params, handler);
	}

	/**
	 * 第三方登录 post请求 type 第三方登录类型标识（微博、微信或者QQ） 新浪微博:sina,微信:wechat,QQ:qzone
	 * 
	 * @param handler
	 */
	public static void threePartyLogin(String openid, String type,
			String username, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("openid", openid);
		params.put("type", type);
		params.put("username", username);
		HttpClient.post(MyURL.THREE_PARTY_LOGIN_URL, params, handler);
	}

	/**
	 * 退出登陆 post请求
	 * 
	 * @param handler
	 */
	public static void logoutUser(String account_id, String account_token,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		HttpClient.post(MyURL.LOGOUT_URL, params, handler);
	}

	/**
	 * 密码重置发送验证码确认 post请求
	 * 
	 * @param handler
	 */
	public static void forgetSendCode(String phonenum,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("phonenum", phonenum);
		HttpClient.post(MyURL.FORGET_VCODE_URL, params, handler);
	}

	/**
	 * 密码重置 post请求
	 * 
	 * @param handler
	 */
	public static void resetPassword(String phonenum, String authcode,
			String pwcode_new, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("phonenum", phonenum);
		params.put("authcode", authcode);
		params.put("pwcode_new", pwcode_new);
		HttpClient.post(MyURL.RESET_PASSWORD_URL, params, handler);
	}

	/**
	 * App 登录 代H5登录 post请求
	 * 
	 * @param handler
	 */
	public static void h5loginUser(String uname, String password,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("uname", uname);
		params.put("password", password);
		HttpClient.post(MyURL.H5_LOGIN_URL, params, handler);
	}

	/**
	 * App三方登录 代H5三方登录 post请求
	 * 
	 * @param handler
	 */
	public static void h5ThreePartyLogin(String open_id, String type,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("open_id", open_id);
		params.put("type", type);
		HttpClient.post(MyURL.H5_PARTY_LOGIN_URL, params, handler);
	}

	/**
	 * 检查版本更新状态 post请求 app_type：(String类型, 分别 "android", 或者是"ios")
	 * 
	 * @param handler
	 */
	public static void getVersionFromNet(String version, String app_type,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("version", version);
		params.put("app_type", app_type);
		HttpClient.post(MyURL.GET_VERSION_URL, params, handler);
	}

	/**
	 * 获取账户全部订单列表信息 order_status, 订单支付状态：全部-all、待付款-unpaid、待收货-paid
	 * 
	 * @param handler
	 */
	public static void getOrderList(String account_id, String account_token,
			String order_status, int page_no, int page_size,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("order_status", order_status);
		params.put("page_no", page_no);
		params.put("page_size", page_size);

		HttpClient.post(MyURL.ORDER_GET_LIST_URL, params, handler);
	}

	/**
	 * 订单取消 post请求
	 * 
	 * @param handler
	 */
	public static void cancelOrder(String account_id, String account_token,
			String order_id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("order_id", order_id);

		HttpClient.post(MyURL.CANCEL_ORDER_URL, params, handler);
	}

	/**
	 * 获取详细订单信息
	 * 
	 * @param handler
	 */
	public static void getOrderDetail(String account_id, String account_token,
			String order_id, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("account_id", account_id);
		params.put("account_token", account_token);
		params.put("order_id", order_id);
		HttpClient.post(MyURL.ORDER_DETAIL_URL, params, handler);
	}

	/**
	 * 查询商品更新列表 post
	 * 
	 * @param handler
	 */
	public static void getGoodsList(String goods_updatetime,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("goods_updatetime", goods_updatetime);
		HttpClient.post(MyURL.GOODS_LIST_URL, params, handler);
	}

	/**
	 * 查询商品更新列表 post
	 * 
	 * @param handler
	 */
	public static void getCatList(AsyncHttpResponseHandler handler) {
		HttpClient.post(MyURL.CAT_LIST_URL, handler);
	}

	/**
	 * 查询商品更新列表 post
	 * 
	 * @param handler
	 */
	public static void getAdInfo(String width, String height,
			String updatetime, String app_type, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("width", width);
		params.put("height", height);
		params.put("updatetime", updatetime);
		params.put("app_type", app_type);
		HttpClient.post(MyURL.AD_INFO_URL, params, handler);
	}

	/**
	 * 商品列表接口 参数 post
	 * 
	 * @param handler
	 */
	public static void getNewGoodsList(String cat_id,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("cat_id", cat_id);
		HttpClient.post(MyURL.NEW_GOODS_LIST_URL, params, handler);
	}
	
	

}
