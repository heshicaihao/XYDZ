package com.ddiiyy.xydz.utils;

import java.util.UUID;

public class GUID {
	public static String getGUID() {
		String guid = UUID.randomUUID().toString();
		return guid;
	}

}
