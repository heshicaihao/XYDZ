package com.ddiiyy.xydz.constants;

public class DBConstants {
	public static final String DATABASENAME = "xydz.db";
	public static final int DATABASE_VERSION = 1;
	public static final String TABLENAME_WORKS = "works";
	public static final String TABLENAME_PHOTOS = "photos";

	public static class TableColumn {
		public static class WorksInfo {
			public static final String ID = "id";
			public static final String WORKID = "workid";
			public static final String JSONSTRING = "jsonstring";
			public static final String GOODSNAME = "goodsname";
			public static final String QUANTITY = "quantity";
			public static final String PRICE = "price";
			public static final String IMAGEURL = "imageurl";
			public static final String GOODSID = "goodsid";
			public static final String TYPE = "type";
			public static final String PICURL = "picurl";
			public static final String PRODUCTID = "productid";
			public static final String USERID = "userid";
			public static final String EDITSTATE = "editstate";
			public static final String LASTTIME = "lasttime";
			
		}
		
		public static class PhotosInfo {
			public static final String ID = "id";
			public static final String IMGID = "imgid";
			public static final String IMGPATH = "imgpath";
			public static final String IMGCOMPRESSWIDTH = "imgcompresswidth";
			public static final String IMGCOMPRESSHEIGHT = "imgcompressheight";
			public static final String IMGCREATTIME = "imgcreattime";
		}
	}
	
}
