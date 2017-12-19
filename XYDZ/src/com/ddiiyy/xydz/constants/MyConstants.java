package com.ddiiyy.xydz.constants;

public class MyConstants {
	
	//App 是否运营
	public static final boolean ISOPERATE = false;
	// SD卡app夹名
	public static final String ANDROID = "android";
	// SD卡app夹名
	public static final String APP_DIR = "heshicaihao_cache_img/";
	// SD卡cache夹名
	public static final String CACHE_DIR = APP_DIR + ".cache";
	// SD卡work夹名
	public static final String WORK_DIR = APP_DIR + ".work";
	// SD卡area_data夹名
	public static final String AREA_DATA_DIR = APP_DIR + ".area_data";
	// SD卡work夹名
	public static final String DOWNLOAD = APP_DIR + ".download";
	// SD卡time夹名
	public static final String TIME = APP_DIR + ".time";
	// SD卡catList夹名
	public static final String CATLIST = APP_DIR + ".cat_list";
	// SD卡OrderList夹名
	public static final String ORDERLIST = APP_DIR + ".order_list";
	// SD卡首页广告夹名
	public static final String HOMEAD = APP_DIR + ".home_ad";

	// 后缀txt
	public static final String TXT = ".txt";
	// 后缀png
	public static final String PNG = ".png";
	// 后缀jpeg
	public static final String JPEG = ".jpeg";

	// 全国省市区数据
	public static final String AREA_DATA = "areaData";
	// 时间梭文件名
	public static final String UPDATE_TIME = "update_time";
	// 商品分类列表信息文件名
	public static final String CAT_LIST_INFO = "cat_list_info";
	// 订单列表信息文件名
	public static final String ORDER_LIST_INFO = "order_list_info";
	// 首页广告文件名
	public static final String HOME_AD_INFO = "home_ad_info";
	// 作品id集
	public static final String WORKS_IDS = "mWorksIds";

	// 默认图片名字
	public static final String DEFAULT_PIC = "heshicaihao_default_pic";

	// ACache的Key
	public static final String MINWIDTH = "minWidth";
	public static final String MINHEIGHT = "minHeight";
	public static final String MAXWIDTH = "maxWidth";
	public static final String MAXHEIGHT = "maxHeight";
	public static final String ORDERID = "orderIdStr";
	public static final String GOODSNAME = "goodsNameStr";
	public static final String ORDERMONEY = "orderMoneyStr";
	public static final String EFFECTPICURL = "effectPicUrl";
	
	public static final String SELECTED_ADDRESS = "selected_address";

	// 每次缩放最小倍数
	public static final float EVERY_TIME_SCALE = (float) 0.2;
	// 缩放最大倍数
	public static final float MAX_SCALE = (float) 2;
	// 缩放最小倍数
	public static final float MIN_SCALE = (float) 1;
	// 编辑页标题高 dip
	public static final int EDITTITLE_WIDTH = 50;
	// 编辑页底部高 dip
	public static final int EDITBOTTOM_WIDTH = 70;
	// 每次旋转最小角度
	public static final int EVERY_TIME_ROTATION = 90;
	// 可编辑用户图片最小宽
	public static final int INIT_MINWIDTH = 858;
	// 可编辑用户图片最小高
	public static final int INIT_MINHEIGHT = 858;
	// 可编辑用户图片最大宽
	public static final int INIT_MAXWIDTH = 1717;
	// 可编辑用户图片最大高
	public static final int INIT_MAXHEIGHT = 1717;

	// 可编辑用户图片最大高
	public static final double MB_MAX_CACHE = 6;

	// 我的跳转到登录
	public static final int ME2LOGIN = 1;

	// 图片编辑跳转到登录
	public static final int EDITPICTURE2LOGIN = 2;

	// 注册跳转到登录
	public static final int REGISTER2LOGIN = 3;

	// H5跳转到登录
	public static final int H52LOGIN = 4;

	// ShareSDK的App Key
	public static final String SHARE_APP_KEY = "f4c7c1f82626";
	// ShareSDK的App Secret
	public static final String SHARE_APP_SECRET = "6d796e5dea21756629fa1e24d94a5e2f";

	// 友盟的App Key
	public static final String UM_APP_KEY = "56b027b2e0f55adcc8003086";

	public static final String TEMPIMG = "tempimg";
	// 微信分享的App ID(分享、支付)
	public static final String WX_APP_ID = "wx67a076a71b44e24e";
	// 微信分享的App Secret(分享、支付)
	public static final String WX_APP_SECRET = "e02c199cdfcb59f6ce9cd7683de85b14";
	// QQ分享的App ID
	public static final String QQ_APP_ID = "1105084229";
	// QQ分享的App Key
	public static final String QQ_APP_KEY = "Lsk6yMVqi64Zyofd";

	// 新浪分享的App ID
	public static final String SINA_APP_KEY = "44430004";
	// 新浪分享的App Secret
	public static final String SINA_APP_SECRET = "1276b71cd1882ac518a7e30c113fc507";
	// 支付宝 商户PID
	public static final String ALI_PARTNER = "2088121066370602";
	// 支付宝 商户收款账号
	public static final String ALI_SELLER = "nettactic@163.com";
	// 支付宝 商户私钥，pkcs8格式
	public static final String ALI_RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMjo9WuKtGME2JXd"
			+ "xjfoYYn/Zq2fJO0f3n8pmGD6oO0Y9nANHEix8LCqUpd4hwaOZE9IWjxXFobKWK5U"
			+ "vXYU+Z5wgUeDe8qckDeYY4JCDpIRKaS7musUJe9ik2FemKkpOzC+t69f5fHR4SeJ"
			+ "Daaini/XFmKKalzhbbcTZuU+GvMXAgMBAAECgYB3vgCnu9+PNJAxoiBFqgaYTNRR"
			+ "noxPl7Lwb0YWZsoLBdm7JtU4rnUZlinrhTZUvgEjD6hYVj9izq7UGEVjX1T1/p2t"
			+ "pOBxhF6YhnQOFMF0AfxcT2MUUjVbuuMyDOLXs6SkNQWJ5tbmF+//2iZPibfiG0oS"
			+ "k7yKVjhrqwVpxQ9LQQJBAPDKg4lGMAORhiz+aQNGha3yQk4yExd/3+jyf3cju0Zp"
			+ "gmJDLoQ1qR24kY1CShxV506aIyk5a4DQ9fgStVoK/A8CQQDVmZXYt8BDyroXFIh5"
			+ "iOOL7XCqE9pj7dPOQtAhDDExTskDErK+sDm3PQJznShatwwjAHFqDtkwJZ23/TJq"
			+ "6TB5AkEAqY6hg1fWVZzy89oOjozpc2zFfhTK1uBoBrxEP9v8cv+3HYSbS+QRe2lN"
			+ "rUABueRP5kyp98yERAhhPFfINmjHxwJAeNekO/BxGKlXsHkQYNm+Ckrxo3cJ5eEG"
			+ "JDoOb7PrHL86378zWshuTql4epod23yVpDO69kqiOyf827umS2/T2QJACAjkWqEU"
			+ "6SJWQx6AabLsWJT9GT/PZxoeTlo9QcRb9QDzY/vWzNblR+nOXSgYqcrTUNKCkvdR"
			+ "Vgkkk07y6oGgow==";
	
	// 支付宝公钥
//	public static final String ALI_RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDI6PVrirRjBNiV3cY36GGJ/2atnyTtH95/KZhg+qDtGPZwDRxIsfCwqlKXeIcGjmRPSFo8VxaGyliuVL12FPmecIFHg3vKnJA3mGOCQg6SESmku5rrFCXvYpNhXpipKTswvrevX+Xx0eEniQ2mop4v1xZiimpc4W23E2blPhrzFwIDAQAB";

	// 支付宝 安全校验码(Key)
//	public static final String ALI_KEY = "u9qt1w7x5qi85kyiq13s43i1asxcygng";
	
	// 支付宝 APP_ID
//	public static final String ALI_APP_ID = "2016012801126895";

}
