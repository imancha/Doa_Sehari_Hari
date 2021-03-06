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

	private TextView TV1, TV2, TV3, TV4;
	private Typeface type1, type2;
	private DoaDB mydb;
	private DoaDBB mydbb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_doa);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle extras = getIntent().getExtras();
		String nama = extras.getString("nama");
		setTitle(nama);

		TV1 = (TextView) findViewById(R.id.textView1);
		TV2 = (TextView) findViewById(R.id.textView2);
		TV3 = (TextView) findViewById(R.id.TextView3);
		TV4 = (TextView) findViewById(R.id.TextView4);

		// Custom fonts from assets folder
		type1 = Typeface.createFromAsset(getAssets(), "KacstOffice.ttf");
		type2 = Typeface.createFromAsset(getAssets(), "KacstBook.ttf");

		// Set the custom fonts
		TV1.setTypeface(type1);
		TV2.setTypeface(type2, Typeface.BOLD);
		TV3.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
		TV4.setTypeface(type1);

		// Get data from database
		mydb = new DoaDB(this);
		mydb.OpenDatabase();

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

		mydb.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view, menu);

		mydbb = new DoaDBB(getApplicationContext());

		if (mydbb.GetData(getTitle().toString()).moveToFirst()) {
			menu.findItem(R.id.bookmark_off).setVisible(false);
		} else {
			menu.findItem(R.id.bookmark_on).setVisible(false);
		}

		mydbb.close();

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
		case R.id.bookmark_on:
			mydbb = new DoaDBB(getApplicationContext());
			mydbb.DeleteData(getTitle().toString());
			mydbb.close();

			Toast.makeText(getApplicationContext(), R.string.remove,
					Toast.LENGTH_SHORT).show();

			return true;
		case R.id.bookmark_off:
			mydbb = new DoaDBB(getApplicationContext());
			mydbb.InsertData(getTitle().toString());
			mydbb.close();

			Toast.makeText(getApplicationContext(), R.string.add,
					Toast.LENGTH_SHORT).show();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
