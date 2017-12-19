package com.ddiiyy.xydz.tab;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddiiyy.xydz.MainActivity;
import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.activity.AboutMeActivity;
import com.ddiiyy.xydz.activity.LoginActivity;
import com.ddiiyy.xydz.activity.ManageAddressActivity;
import com.ddiiyy.xydz.activity.SettingActivity;
import com.ddiiyy.xydz.base.BaseFragment;
import com.ddiiyy.xydz.bean.UserBean;
import com.ddiiyy.xydz.cache.ACache;
import com.ddiiyy.xydz.common.UserController;
import com.ddiiyy.xydz.constants.MyConstants;
import com.ddiiyy.xydz.utils.AndroidUtils;
import com.ddiiyy.xydz.utils.FileUtil;
import com.ddiiyy.xydz.utils.JSONUtil;
import com.ddiiyy.xydz.utils.SharedpreferncesUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class TabMeFragment extends BaseFragment implements OnClickListener {

	private String codeStr = "10003";
	private View mView;
	private UserBean mUser;
	private ImageView mBack;
	private TextView mTitle;
	private LinearLayout mUnloginLL;
	private LinearLayout mLoginLL;
	private LinearLayout mMyOrderLL;
	private LinearLayout mMyWorksLL;
	private LinearLayout mManageAddressLL;
	private LinearLayout mSettingLL;
	private LinearLayout mAboutMeLL;
	private Button mLogoutLL;
	private TextView mName;
	
	private UMShareAPI mShareAPI = null;
	private ACache mCache;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_tab_me, null);
		}
		ViewGroup parent = (ViewGroup) mView.getParent();
		if (parent != null) {
			parent.removeView(mView);
		}
		mUser = UserController.getInstance(getActivity()).getUserInfo();
		initView(mView);
		if (!AndroidUtils.isNetworkAvailable(getActivity())) {
			Toast.makeText(getApplication(),
					this.getString(R.string.no_net), Toast.LENGTH_SHORT)
					.show();
		}
		mCache = ACache.get(getApplication());
		mCache.put(MyConstants.SELECTED_ADDRESS, 0+"");
		return mView;
	}

	@Override
	public void onResume() {
		super.onResume();
		showHead();
	}

	public void initView(View view) {
		mTitle = (TextView) view.findViewById(R.id.title);
		mBack = (ImageView) view.findViewById(R.id.back);
		mName = (TextView) view.findViewById(R.id.name_tv);
		mUnloginLL = (LinearLayout) view.findViewById(R.id.head_unlogin_ll);
		mLoginLL = (LinearLayout) view.findViewById(R.id.head_login_ll);
		mMyOrderLL = (LinearLayout) view.findViewById(R.id.my_order_ll);
		mMyWorksLL = (LinearLayout) view.findViewById(R.id.my_works_ll);
		mManageAddressLL = (LinearLayout) view
				.findViewById(R.id.manage_address_ll);
		mSettingLL = (LinearLayout) view.findViewById(R.id.setting_ll);
		mAboutMeLL = (LinearLayout) view.findViewById(R.id.about_me_ll);
		mLogoutLL = (Button) view.findViewById(R.id.logout_btn);

		showHead();
		mTitle.setText(R.string.tab_me);
		mBack.setVisibility(View.GONE);
		mUnloginLL.setOnClickListener(this);
		mLoginLL.setOnClickListener(this);
		mMyOrderLL.setOnClickListener(this);
		mMyWorksLL.setOnClickListener(this);
		mManageAddressLL.setOnClickListener(this);
		mSettingLL.setOnClickListener(this);
		mLogoutLL.setOnClickListener(this);
		mAboutMeLL.setOnClickListener(this);
		
		mShareAPI = UMShareAPI.get(getActivity());

	}

	@Override
	public void onClick(View view) {
		mUser = UserController.getInstance(getActivity()).getUserInfo();

		switch (view.getId()) {
		case R.id.head_unlogin_ll:
			gotoOther(getApplication(), LoginActivity.class);
			break;
		case R.id.head_login_ll:
			// gotoOther(getApplication(), LoginActivity.class);
			break;
		case R.id.my_order_ll:
			if (mUser.isIs_login()) {
				gotoOther(getApplication(), MainActivity.class, 2);
			} else {
				hintLogin();
			}
			break;
		case R.id.my_works_ll:
			if (mUser.isIs_login()) {
				gotoOther(getApplication(), MainActivity.class, 1);
			} else {
				hintLogin();
			}
			break;
		case R.id.manage_address_ll:
			if (mUser.isIs_login()) {
				String data = JSONUtil.getData(codeStr, "2");
				gotoOther(getApplication(), ManageAddressActivity.class, data);
			} else {
				hintLogin();
			}
			break;
		case R.id.setting_ll:
			gotoOther(getApplication(), SettingActivity.class);
			break;

		case R.id.about_me_ll:
			gotoOther(getApplication(), AboutMeActivity.class);
			break;

		case R.id.logout_btn:
			FileUtil.deleteOrderList();
			SharedpreferncesUtil.setOrder(getApplication(), false);
			if (mUser.isIs_login()) {
				String type = mUser.getType();
				if ("qzone".equals(type)) {
					deleteOauth(SHARE_MEDIA.QZONE);
				}else if ("wechat".equals(type)) {
					deleteOauth(SHARE_MEDIA.WEIXIN);
				}
			}
			gotoLogout();
			showHead();
			break;

		default:
			break;
		}
	}

	/**
	 * 提示登录
	 */
	private void hintLogin() {
		Toast.makeText(getApplication(),
				getApplication().getString(R.string.please_login_first),
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * 跳转到其他界面
	 */
	private void gotoOther(Context context, Class<?> activity) {
		Intent intent = new Intent(context, activity);
		startActivity(intent);
		AndroidUtils.enterActvityAnim(getActivity());
	}

	/**
	 * 跳转到其他界面
	 */
	private void gotoOther(Context context, Class<?> activity, String data) {
		Intent intent = new Intent(context, activity);
		intent.putExtra("data", data);
		startActivity(intent);

		AndroidUtils.enterActvityAnim(getActivity());
	}

	/**
	 * 跳转到其他界面
	 */
	private void gotoOther(Context context, Class<?> activity, int CurrentTabNum) {
		Intent intent = new Intent(context, activity);
		intent.putExtra("CurrentTabNum", CurrentTabNum);
		startActivity(intent);
		getActivity().finish();
		// AndroidUtils.finishActivity(getActivity());
		AndroidUtils.enterActvityAnim(getActivity());
	}

	/**
	 * 显示用户头像
	 */
	private void showHead() {
		mUser = UserController.getInstance(getActivity()).getUserInfo();
		if (mUser.isIs_login()) {
			mLoginLL.setVisibility(View.VISIBLE);
			mUnloginLL.setVisibility(View.GONE);
			mLogoutLL.setVisibility(View.VISIBLE);
			if (mUser.isIs_three_login()) {
				mName.setText(mUser.getUsername());
			} else {
				String mobile = mUser.getUname();
				String maskNumber = mobile.substring(0, 3) + "****"
						+ mobile.substring(7, mobile.length());
				mName.setText(maskNumber);
			}
		} else {
			mLoginLL.setVisibility(View.GONE);
			mUnloginLL.setVisibility(View.VISIBLE);
			mLogoutLL.setVisibility(View.GONE);
		}
	}

	/**
	 * 退出登录
	 */
	public void gotoLogout() {
		UserController mUserController = UserController
				.getInstance(getApplication());
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
		Toast.makeText(getApplication(),
				getApplication().getString(R.string.logout_ok),
				Toast.LENGTH_SHORT).show();
	}
	
	private void deleteOauth(SHARE_MEDIA platform) {
		mShareAPI.deleteOauth(getActivity(), platform,
				new UMAuthListener() {

					@Override
					public void onComplete(SHARE_MEDIA platform, int action,
							Map<String, String> data) {
//						Toast.makeText(getActivity(),
//								"delete Authorize succeed", Toast.LENGTH_SHORT)
//								.show();
					}

					public void onError(SHARE_MEDIA platform, int action,
							Throwable t) {
//						Toast.makeText(getActivity(),
//								"delete Authorize fail", Toast.LENGTH_SHORT)
//								.show();
					}

					@Override
					public void onCancel(SHARE_MEDIA platform, int action) {
//						Toast.makeText(getActivity(),
//								"delete Authorize cancel", Toast.LENGTH_SHORT)
//								.show();
					}

				});
	}

}
