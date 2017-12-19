package com.ddiiyy.xydz.frame;

import com.ddiiyy.xydz.R;
import com.ddiiyy.xydz.tab.TabHomeFragment;
import com.ddiiyy.xydz.tab.TabMeFragment;
import com.ddiiyy.xydz.tab.TabOrderFragment;
import com.ddiiyy.xydz.tab.TabWorksFragment;

public enum MainTab {

	HOME(0, R.string.tab_home, R.drawable.tab_home_btn, TabHomeFragment.class),

	WORKS(1, R.string.tab_works, R.drawable.tab_works_btn, TabWorksFragment.class),

	ORDER(2, R.string.tab_order, R.drawable.tab_order_btn,
			TabOrderFragment.class),

	ME(3, R.string.tab_me, R.drawable.tab_me_btn, TabMeFragment.class);

	private int idx;
	private int resName;
	private int resIcon;
	private Class<?> clz;

	private MainTab(int idx, int resName, int resIcon, Class<?> clz) {
		this.idx = idx;
		this.resName = resName;
		this.resIcon = resIcon;
		this.clz = clz;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getResName() {
		return resName;
	}

	public void setResName(int resName) {
		this.resName = resName;
	}

	public int getResIcon() {
		return resIcon;
	}

	public void setResIcon(int resIcon) {
		this.resIcon = resIcon;
	}

	public Class<?> getClz() {
		return clz;
	}

	public void setClz(Class<?> clz) {
		this.clz = clz;
	}

}
