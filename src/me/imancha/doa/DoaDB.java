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

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_PATH = Environment.getDataDirectory()
			+ "/data/me.imancha.doa/databases/";
	public static final String DATABASE_NAME = "doa";
	public static final String TABLE_NAME = "doa";
	public static final String COLUMN_NAMA = "nama";
	public static final String COLUMN_ARAB = "arab";
	public static final String COLUMN_BACA = "baca";
	public static final String COLUMN_ARTI = "arti";

	private Context context;

	public DoaDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated constructor stub
	}

	public void CopyDatabaseFromAssets() throws IOException {
		String OutputFilename = DATABASE_PATH + DATABASE_NAME;
		File DBFile = new File(DATABASE_PATH);

		Log.e("copy", "Start Copying");

		if (!DBFile.exists()) {
			DBFile.mkdirs();
			DBFile.createNewFile();
		}

		InputStream in = context.getAssets().open("doa");
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
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
				+ COLUMN_NAMA + " = '" + nama + "' LIMIT 1", null);

		return res;
	}

	public ArrayList<String> GetAllDoa() {
		ArrayList<String> list = new ArrayList<String>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY "
				+ COLUMN_NAMA + " ASC", null);

		res.moveToFirst();
		while (res.isAfterLast() == false) {
			list.add(res.getString(res.getColumnIndex(COLUMN_NAMA)));
			res.moveToNext();
		}

		return list;
	}
}
