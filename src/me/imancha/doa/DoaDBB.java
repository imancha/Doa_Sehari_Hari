package me.imancha.doa;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DoaDBB extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "bookmark.db";
	public static final String TABLE_NAME = "bookmark";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAMA = "nama";

	public DoaDBB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		arg0.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAMA
				+ " TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(arg0);
	}

	public Cursor GetData(String nama) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
				+ COLUMN_NAMA + " = '" + nama + "' LIMIT 1", null);

		return res;
	}

	public boolean InsertData(String nama) {
		SQLiteDatabase db = this.getWritableDatabase();

		if (!GetData(nama).moveToFirst()) {
			ContentValues insertValue = new ContentValues();

			insertValue.put(COLUMN_NAMA, nama);

			db.insert(TABLE_NAME, null, insertValue);
		}

		return true;
	}

	public int DeleteData(String nama) {
		SQLiteDatabase db = this.getWritableDatabase();
		String[] args = { nama };

		return (db.delete(TABLE_NAME, COLUMN_NAMA + " = ?", args));
	}

	public ArrayList<String> GetAllData() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT " + COLUMN_NAMA + " FROM "
				+ TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC", null);
		ArrayList<String> list = new ArrayList<String>();

		res.moveToFirst();
		while (res.isAfterLast() == false) {
			list.add(res.getString(res.getColumnIndex(COLUMN_NAMA)));
			res.moveToNext();
		}

		return list;
	}
}
