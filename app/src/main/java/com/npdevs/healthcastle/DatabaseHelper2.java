package com.npdevs.healthcastle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper2 extends SQLiteOpenHelper {
	public static final String DATABASE_NAME_1 = "Exercise.db";
	public static final String TABLE_NAME_1 = "exercise_table";
	public static final String COL_11 = "ID";
	public static final String COL_21 = "NAME";
	public static final String COL_31 = "DURATION";
	public static final String COL_41 = "CALORIE";

	public DatabaseHelper2(Context context) {
		super(context, DATABASE_NAME_1, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_NAME_1 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,DURATION INTEGER,CALORIE INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
		onCreate(db);
	}

	public boolean insertData(String name, int duration, int calorie) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(COL_21, name);
		contentValues.put(COL_31, duration);
		contentValues.put(COL_41, calorie);
		long result = db.insert(TABLE_NAME_1, null, contentValues);
		return result != -1;
	}

	public Cursor getAllData() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor res = db.rawQuery("select * from " + TABLE_NAME_1, null);
		return res;
	}
}