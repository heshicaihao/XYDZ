package com.ddiiyy.xydz.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class YSYBitmapUtil {

	public static Bitmap getBitmap(String path, int width, int height) {
		Options option = new Options();
		option.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, option);
		option.inSampleSize = calculateSampleSize(option, width, height);

		option.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, option);
	}

	private static int calculateSampleSize(Options option, int width, int height) {
		int sampleSize = 1;
		if (option.outWidth > width && option.outHeight > height) {
			int widthRate = Math.round((float) option.outWidth / (float) width);
			int heightRate = Math.round((float) option.outHeight
					/ (float) height);

			sampleSize = Math.max(widthRate, heightRate);
			if (sampleSize <= 0) {
				sampleSize = 1;
			}
		}
		return sampleSize;
	}
}
