package me.imancha.doa;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DoaDB extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 2;
	public static final String DATABASE_PATH = Environment.getDataDirectory()
			+ "/data/me.imancha.doa/databases/";
	public static final String DATABASE_NAME = "doa.db";
	public static final String TABLE_NAME = "doa";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAMA = "nama";
	public static final String COLUMN_ARAB = "arab";
	public static final String COLUMN_BACA = "baca";
	public static final String COLUMN_ARTI = "arti";

	private Context context;
	private SQLiteDatabase db;
	private Cursor res;
	private ArrayList<String> list;

	public DoaDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// Do nothing
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// Do nothing
	}

	public void CopyDatabaseFromAssets() throws IOException {
		String OutputFilename = DATABASE_PATH + DATABASE_NAME;
		File DBFile = new File(DATABASE_PATH);

		Log.e("copy", "Start Copying");

		if (!DBFile.exists()) {
			DBFile.mkdirs();
			DBFile.createNewFile();
		}

		InputStream in = context.getAssets().open(DATABASE_NAME);
		OutputStream out = new FileOutputStream(OutputFilename);
		byte[] buffer = new byte[1024];
		int length;

		while ((length = in.read(buffer)) > 0)
			out.write(buffer, 0, length);

		Log.e("copy", "Copying Completed");

		out.flush();
		out.close();
		in.close();
	}

	public void OpenDatabase() throws SQLException {
		SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null,
				SQLiteDatabase.NO_LOCALIZED_COLLATORS
						| SQLiteDatabase.CREATE_IF_NECESSARY);
	}

	public Cursor GetData(String nama) {
		db = this.getReadableDatabase();
		res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
				+ COLUMN_NAMA + " = '" + nama + "' LIMIT 1", null);

		return res;
	}

	public ArrayList<String> GetMatchData(String nama) {
		list = new ArrayList<String>();
		db = this.getReadableDatabase();
		res = db.rawQuery("SELECT " + COLUMN_NAMA + " FROM " + TABLE_NAME
				+ " WHERE " + COLUMN_NAMA + " LIKE '%" + nama + "%' ORDER BY "
				+ COLUMN_NAMA + " ASC", null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			list.add(res.getString(res.getColumnIndex(COLUMN_NAMA)));
			res.moveToNext();
		}

		return list;
	}

	public ArrayList<String> GetAllData() {
		list = new ArrayList<String>();
		db = this.getReadableDatabase();
		res = db.rawQuery("SELECT " + COLUMN_NAMA + " FROM " + TABLE_NAME
				+ " ORDER BY " + COLUMN_NAMA + " ASC", null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			list.add(res.getString(res.getColumnIndex(COLUMN_NAMA)));
			res.moveToNext();
		}

		return list;
	}
}
