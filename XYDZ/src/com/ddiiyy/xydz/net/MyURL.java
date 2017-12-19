package com.ddiiyy.xydz.net;

/**
 * URL
 * 
 * @author heshicaihao
 */
public class MyURL {

	// 帮助指引H5 URL
	public static final String HELP_URL = "http://www.ddiiyy.com/phpamf-apsv-2.html?api=1";

	// 反馈建议 H5 URL
	public static final String SUGGESTIONS_URL = "http://www.ddiiyy.com/phpamf-convic-1.html?api=1?username=";

	// 联系我们 H5 URL
	public static final String CONTACT_US_URL = "http://www.ddiiyy.com/phpamf-convic-2.html?api=1";

	// 产品介绍 H5 URL
	public static final String APP_INTRODUCTION_URL = "http://www.ddiiyy.com/phpamf-convic-10.html?api=1";

	// 免责声明 H5 URL
	public static final String DISCLAIMER_URL = "http://www.ddiiyy.com/phpamf-convic-3.html?api=1";

	// 用户协议 H5 URL
	public static final String TREATY_URL = "http://www.ddiiyy.com/phpamf-convic-9.html?api=1";

	// logo URL
	public static final String LOGO_URL = "http://www.ddiiyy.com/public/images/4c/8d/e0/daad1be11d52d3ffee70f01b4b11fa157f35f6ba.png?1464080992#h";

	// App分享 URL
	public static final String SHARE_APP_URL = "http://www.ddiiyy.com/phpamf-convic-11.html?api=1";

	// 作品分享 URL
	public static final String SHARE_WORK_URL = "http://www.ddiiyy.com/phpamf-convic-11.html?api=1";

	// 服务器存储地址
	public static final String SERVER_APP_URL = "http://www.ddiiyy.com/download/XYDZ_V1.0.0.248.apk";

	// 主页H5 URL
	public static final String HOME_URL = ConfigURL.URL + "/index.php/wap/";

	// 订单 H5 URL
	public static final String ORDER_URL = ConfigURL.URL
			+ "/index.php/wap/member-orders.html";

	// 我的 H5 URL
	public static final String ME_URL = ConfigURL.URL
			+ "index.php/wap/member.html";

	// 商品信息
	public static final String GOODS_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_goods/get_item";

	// 模板信息
	public static final String TEMPLATE_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_template/get_template";

	// 图片上传
	public static final String UPLOADPICTURE_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_storage/save";

	// 保存作品信息
	public static final String SAVE_WORKS_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_works/save";

	// 蒙版列表
	public static final String LIST_MASK_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_template/get_template";

	// 临时账户申请临时作品id
	public static final String TEMP_WORK_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_member/get_temp_work_id";

	// 申请生成临时新账户信息
	public static final String TEMP_ACCOUNT_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_member/get_temp_user";

	// 添加新地址 或者 更新地址
	public static final String ADD_ADDRESS_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_address/save";

	// 地址列表
	public static final String ADDRESS_LIST_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_address/get_list";

	// 删除地址
	public static final String DELETE_ADDRESS = ConfigURL.URL
			+ "index.php/openapi/b2c_address/delete";

	// 获取配送方式列表
	public static final String SHIPPING_LIST_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_orders/get_dlytype";

	// 获取订单价钱信息
	public static final String CHECK_COST_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_orders/check_cost";

	// 提交新订单数据
	public static final String CREATE_ORDERS_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_orders/create";

	// 支付宝支付 回掉函数
	public static final String MALIPAY_CALLBACK_URL = ConfigURL.URL
			+ "index.php/openapi/ectools_payment/parse/ectools/ectools_payment_plugin_malipay_server/callback/";

	// 获取地区关系信息
	public static final String AREA_INFO_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_address/getAreaInfo";

	// 设置默认地址信息
	public static final String SET_DEFAULT_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_address/set_default";

	// 微信支付，接口
	public static final String WECHAT_PAY_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_orders/sendWechatPay";

	// 手机注册新账号验证码确认
	public static final String REGISTER_SEND_CODE_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_member/send_signup_vcode_sms";

	// 账号新注册
	public static final String REGISTER_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_member/create";

	// 手机忘记密码, 验证码确认
	public static final String FORGOT_SEND_CODE_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_member/send_forgot_vcode_sms";

	// 账号登录
	public static final String LOGIN_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_member/post_login";

	// 第三方登录接口
	public static final String THREE_PARTY_LOGIN_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_member/openid_login";

	// 退出登陆
	public static final String LOGOUT_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_member/logout";

	// 密码重置发送验证码确认
	public static final String FORGET_VCODE_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_member/send_forgot_vcode_sms";

	// 密码重置过程
	public static final String RESET_PASSWORD_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_member/resetpassword";

	// App 登录 代H5登录
	public static final String H5_LOGIN_URL = "http://diy-app.net-tactic.com/index.php/wap/passport-post_login.html";

	// App三方登录 代H5三方登录
	public static final String H5_PARTY_LOGIN_URL = "http://diy-app.net-tactic.com/index.php/wap/passport-partylogin.html";

	// 检查版本更新状态
	public static final String GET_VERSION_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_services/getversion";

	// H5订单详情网页
	public static final String H5_ORDER_DETAIL_URL = ConfigURL.URL
			+ "/index.php/wap/member-orderdetail-";

	// 获取全部订单列表信息
	public static final String ORDER_GET_LIST_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_orders/get_list";

	// 订单取消
	public static final String CANCEL_ORDER_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_orders/cancel";

	// 获取详细订单信息
	public static final String ORDER_DETAIL_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_orders/detail";

	// 查询商品更新列表
	public static final String GOODS_LIST_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_goods/get_modify_list";

	// 首页商品类获取接口
	public static final String CAT_LIST_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_goods/get_cat_list";

	// 单个品牌商品列表接口
	public static final String NEW_GOODS_LIST_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_goods/get_goods_list";

	// 广告列表接口
	public static final String AD_INFO_URL = ConfigURL.URL
			+ "index.php/openapi/b2c_services/getAdInfo";

}
