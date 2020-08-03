package com.npdevs.healthcastle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "Food.db";
	public static final String TABLE_NAME = "food_table";
	public static final String COL_1 = "ID";
	public static final String COL_2 = "NAME";
	public static final String COL_3 = "AMOUNT";
	public static final String COL_4 = "CALORIE";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,AMOUNT INTEGER,CALORIE INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	public boolean insertData(String name, int amount, int calorie) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(COL_2, name);
		contentValues.put(COL_3, amount);
		contentValues.put(COL_4, calorie);
		long result = db.insert(TABLE_NAME, null, contentValues);
		return result != -1;
	}

	public Cursor getAllData() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
		return res;
	}
}