package com.ddiiyy.xydz.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.activity.EditPictureActivity;
import com.ddiiyy.xydz.activity.LoginActivity;
import com.ddiiyy.xydz.activity.OrderPayActivity;
import com.ddiiyy.xydz.activity.RegisterActivity;
import com.ddiiyy.xydz.bean.UserBean;
import com.ddiiyy.xydz.common.UserController;
import com.ddiiyy.xydz.constants.MyConstants;
import com.ddiiyy.xydz.utils.AndroidUtils;
/**
 * H5 调用的类
 * @author heshicaihao
 *
 */
public class JavaScriptObject {
	private Context mContxt;
	private Activity mActivity;

	public JavaScriptObject(Context mContxt, Activity mActivity) {
		this.mContxt = mContxt;
		this.mActivity = mActivity;
	}

	@JavascriptInterface
	public void gotoEditPicture(String data) {
		Intent intent = new Intent(mContxt, EditPictureActivity.class);
		intent.putExtra("data", data);
		mActivity.startActivity(intent);
		AndroidUtils.enterActvityAnim(mActivity);
	}
	
	@JavascriptInterface
	public void gotoLogin(String data) {
		Intent intent = new Intent(mContxt, LoginActivity.class);
		intent.putExtra("data", data);
		intent.putExtra("code", MyConstants.H52LOGIN);
		mActivity.startActivity(intent);
		AndroidUtils.enterActvityAnim(mActivity);
	}
	
	@JavascriptInterface
	public void gotoRegister(String data) {
		Intent intent = new Intent(mContxt, RegisterActivity.class);
		intent.putExtra("data", data);
		mActivity.startActivity(intent);
		AndroidUtils.enterActvityAnim(mActivity);
	}
	
	@JavascriptInterface
	public void gotoLogout(String data) {
		UserController mUserController = UserController.getInstance(mContxt);
		UserBean user = mUserController.getUserInfo();
		user.setUname("");
		user.setPassword("");
		user.setId("");
		user.setToken("");
		user.setIs_login(false);
		user.setTemp_id("");
		user.setTemp_token("");
		user.setIs_temp_login(false);
		user.setOpen_id("");
		user.setType("");
		user.setIs_three_login(false);
		mUserController.saveUserInfo(user);
		Toast.makeText(mContxt, mContxt.getString(R.string.logout_ok), Toast.LENGTH_SHORT)
		.show();
	}
	
	
	@JavascriptInterface
	public void gotoOrderPay(String data) {
		Intent intent = new Intent(mContxt, OrderPayActivity.class);
		intent.putExtra("data", data);
		mActivity.startActivity(intent);
		AndroidUtils.enterActvityAnim(mActivity);
	}

}
