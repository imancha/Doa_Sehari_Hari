package me.imancha.doa;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DoaView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_doa);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle extras = getIntent().getExtras();
		setTitle(extras.getString("nama"));

		String nama = extras.getString("nama");

		final TextView TV1 = (TextView) findViewById(R.id.textView1);
		final TextView TV2 = (TextView) findViewById(R.id.textView2);
		final TextView TV3 = (TextView) findViewById(R.id.TextView3);
		final TextView TV4 = (TextView) findViewById(R.id.TextView4);

		Typeface type1 = Typeface.createFromAsset(getAssets(), "KacstOffice.ttf");
		Typeface type2 = Typeface.createFromAsset(getAssets(), "KacstBook.ttf");

		TV1.setTypeface(type1);
		TV2.setTypeface(type2, Typeface.BOLD);
		TV3.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
		TV4.setTypeface(type1);

		DoaDB mydb = new DoaDB(this);
		Cursor res = mydb.GetData(nama);

		res.moveToFirst();
		String arab = res.getString(res.getColumnIndex(DoaDB.COLUMN_ARAB));
		String baca = res.getString(res.getColumnIndex(DoaDB.COLUMN_BACA));
		String arti = res.getString(res.getColumnIndex(DoaDB.COLUMN_ARTI));

		TV1.setText(R.string.top);
		TV2.setText(arab);
		TV3.setText(baca);
		TV4.setText("\" " + arti + " \"");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
