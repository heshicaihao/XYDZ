package com.ddiiyy.xydz.database;

import com.ddiiyy.xydz.constants.DBConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		if (newVersion > oldVersion) {
			removeTables(db);
			createTables(db);
		}
	}

	/**
	 * 创建表
	 * 
	 * @param db
	 */
	private void createTables(SQLiteDatabase db) {
		createTableWorks(db);
		createTablePhotos(db);
	}

	/**
	 * 移除表
	 * 
	 * @param db
	 */
	private void removeTables(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLENAME_WORKS);
		db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLENAME_PHOTOS);
	}

	/**
	 * 创建 作品表
	 * 
	 * @param db
	 */
	private void createTableWorks(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + DBConstants.TABLENAME_WORKS + " ("
				+ DBConstants.TableColumn.WorksInfo.ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ DBConstants.TableColumn.WorksInfo.WORKID + " varchar(20),"
				+ DBConstants.TableColumn.WorksInfo.JSONSTRING + " TEXT,"
				+ DBConstants.TableColumn.WorksInfo.GOODSNAME + " varchar(100),"
				+ DBConstants.TableColumn.WorksInfo.QUANTITY + " varchar(20),"
				+ DBConstants.TableColumn.WorksInfo.PRICE + " varchar(20),"
				+ DBConstants.TableColumn.WorksInfo.IMAGEURL + " varchar(100),"
				+ DBConstants.TableColumn.WorksInfo.GOODSID + " varchar(20),"
				+ DBConstants.TableColumn.WorksInfo.TYPE + " varchar(100),"
				+ DBConstants.TableColumn.WorksInfo.PICURL + " varchar(100),"
				+ DBConstants.TableColumn.WorksInfo.PRODUCTID + " varchar(100),"
				+ DBConstants.TableColumn.WorksInfo.USERID + " varchar(100),"
				+ DBConstants.TableColumn.WorksInfo.EDITSTATE + " varchar(100),"
				+ DBConstants.TableColumn.WorksInfo.LASTTIME + " varchar(50)" + ");");
	}

	/**
	 * 创建 相片表
	 * 
	 * @param db
	 */
	private void createTablePhotos(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + DBConstants.TABLENAME_PHOTOS + " ("
				+ DBConstants.TableColumn.PhotosInfo.ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ DBConstants.TableColumn.PhotosInfo.IMGID + " TEXT,"
				+ DBConstants.TableColumn.PhotosInfo.IMGPATH + " TEXT,"
				+ DBConstants.TableColumn.PhotosInfo.IMGCOMPRESSWIDTH
				+ " TEXT,"
				+ DBConstants.TableColumn.PhotosInfo.IMGCOMPRESSHEIGHT
				+ " TEXT," + DBConstants.TableColumn.PhotosInfo.IMGCREATTIME
				+ " TEXT" + ");");
	}
	
	// 更新列
	public void updateColumn(SQLiteDatabase db, String tableName,
			String oldColumn, String newColumn, String typeColumn) {
		try {
			db.execSQL("ALTER TABLE " + tableName + " CHANGE " + oldColumn
					+ " " + newColumn + " " + typeColumn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
