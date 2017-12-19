package com.ddiiyy.xydz.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.ddiiyy.xydz.R;


/**
 * @Description:自定义对话框
 * @author http://blog.csdn.net/finddreams
 */
public class FrameProgressDialog extends ProgressDialog {

	public FrameProgressDialog(Context context) {
		super(context);
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_dialog_progress);
	}

}
