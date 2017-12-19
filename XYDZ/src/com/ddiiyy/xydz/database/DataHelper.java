package com.ddiiyy.xydz.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ddiiyy.xydz.bean.WorksInfoBean;
import com.ddiiyy.xydz.constants.DBConstants;
import com.ddiiyy.xydz.utils.LogUtils;

/**
 * SQLite本地数据库操作（增删改查） 工具类
 * 
 */
public class DataHelper {
	private static String TAG = "DataHelper";
	private static DataHelper mInstance;
	private SQLiteDatabase db;
	private DBHelper dbHelper;

	private DataHelper(Context context) {
		// 定义一个SQLite数据库
		dbHelper = new DBHelper(context, DBConstants.DATABASENAME, null,
				DBConstants.DATABASE_VERSION);
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * 初始化数据库操作DBUtil类
	 */
	public static DataHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DataHelper(context);

		}
		return mInstance;
	}

	public void Close() {
		db.close();
		dbHelper.close();
	}

	/**
	 * 最近作品插入 保留20条记录
	 * 
	 * @param data
	 */
	public void addWorks(WorksInfoBean data) {
		SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
		Cursor cursor = null;
		try {
			LogUtils.logd(TAG, "insertWorks");
			insertWorks(sqlite, cursor, data);
		} catch (Exception e) {
			LogUtils.logd(TAG, "Exception" + e);
		} finally {
			if (sqlite != null) {
				sqlite.close();
				sqlite = null;
			}
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}
	}

	private void insertWorks(SQLiteDatabase sqlite, Cursor cursor,
			WorksInfoBean data) {
		cursor = sqlite.rawQuery("select * from works where workid=?",
				new String[] { data.getWorkid() });
		if (cursor != null && cursor.moveToFirst()) {
			LogUtils.logd(TAG, "存在 则修改" + data.getQuantity());
			String Sql = "update works set jsonstring=?,imageurl=?,picurl=?,quantity=?,userid=?,editstate=?,lasttime=? where workid=?";
			// 存在 则修改
			sqlite.execSQL(
					Sql,
					new String[] { data.getJsonstring(), data.getImageurl(),
							data.getPicurl(), data.getQuantity(),
							data.getUserid(), data.getEditstate(),
							System.currentTimeMillis() + "", data.getWorkid() });
		} else {
			String sql = "insert into " + DBConstants.TABLENAME_WORKS;
			sql += "(workid, jsonstring, goodsname, quantity, price, imageurl, goodsid, type, picurl, productid,userid, editstate, lasttime) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?)";
			sqlite.execSQL(
					sql,
					new String[] { data.getWorkid(), data.getJsonstring(),
							data.getGoodsname(), data.getQuantity(),
							data.getPrice(), data.getImageurl(),
							data.getGoodsid(), data.getType(),
							data.getPicurl(), data.getProductid(),
							data.getUserid(), data.getEditstate(),
							System.currentTimeMillis() + "" });
			LogUtils.logd(TAG, "execSQL" + data.getWorkid());
		}
	}

	/**
	 * 作品全部 查询
	 * 
	 * @return
	 */
	public List<WorksInfoBean> queryWorks() {
		SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
		ArrayList<WorksInfoBean> lists = new ArrayList<WorksInfoBean>();
		Cursor cursor = sqlite.rawQuery("select * from "
				+ DBConstants.TABLENAME_WORKS + " order by workid desc", null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			WorksInfoBean data = new WorksInfoBean();
			data.setWorkid(cursor.getString(cursor.getColumnIndex("workid")));
			data.setJsonstring(cursor.getString(cursor
					.getColumnIndex("jsonstring")));
			data.setGoodsname(cursor.getString(cursor
					.getColumnIndex("goodsname")));
			data.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
			data.setPrice(cursor.getString(cursor.getColumnIndex("price")));
			data.setImageurl(cursor.getString(cursor.getColumnIndex("imageurl")));
			data.setGoodsid(cursor.getString(cursor.getColumnIndex("goodsid")));
			data.setType(cursor.getString(cursor.getColumnIndex("type")));
			data.setPicurl(cursor.getString(cursor.getColumnIndex("picurl")));
			data.setProductid(cursor.getString(cursor
					.getColumnIndex("productid")));
			data.setUserid(cursor.getString(cursor.getColumnIndex("userid")));
			data.setEditstate(cursor.getString(cursor
					.getColumnIndex("editstate")));
			data.setLasttime(cursor.getString(cursor.getColumnIndex("lasttime")));
			lists.add(data);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		sqlite.close();
		this.Close();
		return lists;
	}

	/**
	 * 此用户 作品 查询多个
	 * 
	 * @return
	 */
	public ArrayList<WorksInfoBean> queryWorks(ArrayList<String> work_ids) {
		SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
		ArrayList<WorksInfoBean> lists = new ArrayList<WorksInfoBean>();

		String ids = "";
		for (int i = 0; i < work_ids.size(); i++) {
			if (i == 0) {
				ids += work_ids.get(i);
			} else {
				ids += "," + work_ids.get(i);
			}
		}
		Cursor cursor = sqlite.rawQuery("select * from "
				+ DBConstants.TABLENAME_WORKS + " where workid in (" + ids
				+ ") order by workid desc", null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			WorksInfoBean data = new WorksInfoBean();
			data.setWorkid(cursor.getString(cursor.getColumnIndex("workid")));
			data.setJsonstring(cursor.getString(cursor
					.getColumnIndex("jsonstring")));
			data.setGoodsname(cursor.getString(cursor
					.getColumnIndex("goodsname")));
			data.setQuantity(cursor.getString(cursor.getColumnIndex("quantity")));
			data.setPrice(cursor.getString(cursor.getColumnIndex("price")));
			data.setImageurl(cursor.getString(cursor.getColumnIndex("imageurl")));
			data.setGoodsid(cursor.getString(cursor.getColumnIndex("goodsid")));
			data.setType(cursor.getString(cursor.getColumnIndex("type")));
			data.setPicurl(cursor.getString(cursor.getColumnIndex("picurl")));
			data.setProductid(cursor.getString(cursor
					.getColumnIndex("productid")));
			data.setUserid(cursor.getString(cursor.getColumnIndex("userid")));
			data.setEditstate(cursor.getString(cursor
					.getColumnIndex("editstate")));
			data.setLasttime(cursor.getString(cursor.getColumnIndex("lasttime")));
			lists.add(data);
		}
		LogUtils.logd(TAG, "lists:" + lists.size());
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		sqlite.close();
		this.Close();
		return lists;
	}

	/**
	 * 此用户 作品 查询多个
	 * 
	 * @return
	 */
	public void reviseEditState(ArrayList<String> work_ids) {
		SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
		String ids = "";
		for (int i = 0; i < work_ids.size(); i++) {
			if (i == 0) {
				ids += work_ids.get(i);
			} else {
				ids += "," + work_ids.get(i);
			}
		}
		sqlite.execSQL("update " + DBConstants.TABLENAME_WORKS
				+ " set editstate = 1 where workid in (" + ids
				+ ") ");
		sqlite.close();
		this.Close();
	}

	/**
	 * 根据作品id 查询 作品信息
	 * 
	 * @param id
	 */
	public WorksInfoBean queryWorksById(String work_id) {
		LogUtils.logd(TAG, "work_id:" + work_id);
		SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
		WorksInfoBean data = new WorksInfoBean();
		Cursor cursor = sqlite.rawQuery("select * from works where workid=?",
				new String[] { work_id });
		LogUtils.logd(TAG, "cursor += null" + cursor);
		if (cursor != null && cursor.moveToFirst()) {
			LogUtils.logd(TAG, "cursor != null");
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				LogUtils.logd(TAG, "!cursor.isAfterLast()");
				data.setWorkid(cursor.getString(cursor.getColumnIndex("workid")));
				data.setJsonstring(cursor.getString(cursor
						.getColumnIndex("jsonstring")));
				data.setGoodsname(cursor.getString(cursor
						.getColumnIndex("goodsname")));
				data.setQuantity(cursor.getString(cursor
						.getColumnIndex("quantity")));
				data.setPrice(cursor.getString(cursor.getColumnIndex("price")));
				data.setImageurl(cursor.getString(cursor
						.getColumnIndex("imageurl")));
				data.setGoodsid(cursor.getString(cursor
						.getColumnIndex("goodsid")));
				data.setType(cursor.getString(cursor.getColumnIndex("type")));
				data.setPicurl(cursor.getString(cursor.getColumnIndex("picurl")));
				data.setProductid(cursor.getString(cursor
						.getColumnIndex("productid")));
				data.setUserid(cursor.getString(cursor.getColumnIndex("userid")));
				data.setEditstate(cursor.getString(cursor
						.getColumnIndex("editstate")));
				data.setLasttime(cursor.getString(cursor
						.getColumnIndex("lasttime")));
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		sqlite.close();
		this.Close();
		return data;
	}

	/**
	 * 根据user_id 查询 作品信息
	 * 
	 * @param id
	 */
	public ArrayList<WorksInfoBean> queryWorksByUserId(String user_id) {
		LogUtils.logd(TAG, "user_id:" + user_id);
		SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
		ArrayList<WorksInfoBean> lists = new ArrayList<WorksInfoBean>();

		Cursor cursor = sqlite.rawQuery("select * from works where userid=?",
				new String[] { user_id });
		LogUtils.logd(TAG, "cursor += null" + cursor);
		if (cursor != null && cursor.moveToFirst()) {
			LogUtils.logd(TAG, "cursor != null");
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				LogUtils.logd(TAG, "!cursor.isAfterLast()");
				WorksInfoBean data = new WorksInfoBean();
				data.setWorkid(cursor.getString(cursor.getColumnIndex("workid")));
				data.setJsonstring(cursor.getString(cursor
						.getColumnIndex("jsonstring")));
				data.setGoodsname(cursor.getString(cursor
						.getColumnIndex("goodsname")));
				data.setQuantity(cursor.getString(cursor
						.getColumnIndex("quantity")));
				data.setPrice(cursor.getString(cursor.getColumnIndex("price")));
				data.setImageurl(cursor.getString(cursor
						.getColumnIndex("imageurl")));
				data.setGoodsid(cursor.getString(cursor
						.getColumnIndex("goodsid")));
				data.setType(cursor.getString(cursor.getColumnIndex("type")));
				data.setPicurl(cursor.getString(cursor.getColumnIndex("picurl")));
				data.setProductid(cursor.getString(cursor
						.getColumnIndex("productid")));
				data.setUserid(cursor.getString(cursor.getColumnIndex("userid")));
				data.setEditstate(cursor.getString(cursor
						.getColumnIndex("editstate")));
				data.setLasttime(cursor.getString(cursor
						.getColumnIndex("lasttime")));
				lists.add(data);
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		sqlite.close();
		this.Close();
		return lists;
	}

	/**
	 * 作品 删单条
	 * 
	 * @param id
	 */
	public int updateWorks(WorksInfoBean data) {
		int id = 0;
		SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
		Cursor cursor = null;
		try {
			id = sqlite.delete(DBConstants.TABLENAME_WORKS, "workid=?",
					new String[] { data.getWorkid() });
		} catch (Exception e) {
			LogUtils.logd(TAG, "Exception" + e);
		} finally {
			if (sqlite != null) {
				sqlite.close();
				sqlite = null;
			}
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}

		return id;
	}

	/**
	 * 作品 删
	 * 
	 * @param id
	 */
	public int deleteWorksTable() {
		SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
		int id = sqlite.delete(DBConstants.TABLENAME_WORKS, null, null);
		sqlite.close();
		this.Close();
		return id;
	}

	/**
	 * 作品 删单条
	 * 
	 * @param id
	 */
	public int deleteWorks(WorksInfoBean data) {
		int id = 0;
		SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
		Cursor cursor = null;
		try {
			id = sqlite.delete(DBConstants.TABLENAME_WORKS, "workid=?",
					new String[] { data.getWorkid() });
		} catch (Exception e) {
			LogUtils.logd(TAG, "Exception" + e);
		} finally {
			if (sqlite != null) {
				sqlite.close();
				sqlite = null;
			}
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
			}
		}

		return id;
	}

}
