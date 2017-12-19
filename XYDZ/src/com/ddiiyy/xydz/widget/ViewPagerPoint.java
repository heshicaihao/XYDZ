package com.ddiiyy.xydz.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Բ��ؼ� ��Viewpager���ʹ��
 * 
 * 
 * @author zhou.ni 2015��4��25��
 */
public class ViewPagerPoint extends LinearLayout {

	/**
	 * Բ������:Ĭ��Ϊ0
	 * */
	private int count = 0;

	/**
	 * Բ��ѡ��������Ĭ��Ϊ0
	 * */
	private int index = 0;

	/**
	 * ����Բ�㱳����δѡ��
	 * */
	private Bitmap noBitmap;

	/**
	 * ����Բ�㱳������ѡ��
	 * */
	private Bitmap yesBitmap;
	/**
	 * ������
	 * */
	private Context context;
	/**
	 * ���ֲ���
	 * */
	private LinearLayout.LayoutParams layoutParamsWH = null;

	/***/
	private int margin = 5;

	public ViewPagerPoint(Context context) {
		this(context, null);
	}

	public ViewPagerPoint(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initItem();
	}

	/**
	 * ��ʼ�������Լ�����
	 * */
	private void initItem() {
		layoutParamsWH = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(layoutParamsWH);
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setGravity(Gravity.CENTER_HORIZONTAL);

	}

	/**
	 * ��������Բ�����ʾͼƬ
	 * 
	 * @param no
	 *            Ĭ��ͼƬ
	 * @param yes
	 *            ����ͼƬ
	 * */
	public void setBitmap(int no, int yes) {
		try {
			noBitmap = BitmapFactory.decodeResource(getResources(), no);
			yesBitmap = BitmapFactory.decodeResource(getResources(), yes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ����ͼƬ
	private void writeRound() {
		this.removeAllViews(); // �ػ�ʱ�Ƴ�������View
		if (yesBitmap != null && noBitmap != null) {
			for (int i = 0; i < count; i++) {
				ImageView img = new ImageView(context);
				LinearLayout.LayoutParams img_Params = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				// ����Բ��֮��ļ��
				img_Params.leftMargin = margin;
				img_Params.rightMargin = margin;
				img.setLayoutParams(img_Params);
				this.addView(img, i);
				if (index == i) { // ��������ֵ�ı�Բ����ʾͼƬ
					img.setImageBitmap(yesBitmap);
				} else {
					img.setImageBitmap(noBitmap);
				}
			}
		}
	}

	/**
	 * ������ʾUI
	 * 
	 * @param index
	 *            ����
	 * */
	public void notifyDataSetChanged(int index) {
		if (index < 0)
			index = 0; // ����ֵ����С��0
		setIndex(index); // ��������
		writeRound(); // ���»���
	}

	/**
	 * ������Ҫ��ʾ������
	 * */
	public void setCount(int count) {
		if (count < 0)
			count = 0; // ��������С��0
		this.count = count;
	}

	/**
	 * ������Ҫ�ĸı��Item������:��0��ʼ
	 * */
	public void setIndex(int index) {
		if (index < 0)
			index = 0; // ����ֵ����С��0
		this.index = index;
	}

	/**
	 * ����Բ��֮��ľ���
	 * */
	public void setMargin(int margin) {
		this.margin = margin;
	}

}
