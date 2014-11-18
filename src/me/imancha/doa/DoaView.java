package me.imancha.doa;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class DoaView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_doa);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle extras = getIntent().getExtras();
		String nama = extras.getString("nama");
		setTitle(nama);

		final TextView TV1 = (TextView) findViewById(R.id.textView1);
		final TextView TV2 = (TextView) findViewById(R.id.textView2);
		final TextView TV3 = (TextView) findViewById(R.id.TextView3);
		final TextView TV4 = (TextView) findViewById(R.id.TextView4);

		// Custom fonts from assets folder
		Typeface type1 = Typeface.createFromAsset(getAssets(), "KacstOffice.ttf");
		Typeface type2 = Typeface.createFromAsset(getAssets(), "KacstBook.ttf");

		// Set the custom fonts
		TV1.setTypeface(type1);
		TV2.setTypeface(type2, Typeface.BOLD);
		TV3.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
		TV4.setTypeface(type1);

		// Get data from database
		DoaDB mydb = new DoaDB(this);
		Cursor res = mydb.GetData(nama);

		// Fetch result from database
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
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.bookmark:
				Toast.makeText(getApplicationContext(), R.string.bookmark,
						Toast.LENGTH_SHORT).show();
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
