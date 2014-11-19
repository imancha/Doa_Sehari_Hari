package me.imancha.doa;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class DoaSearch extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle extras = getIntent().getExtras();
		String key = extras.getString("key");

		DoaDB mydb = new DoaDB(this);

		mydb.OpenDatabase();

		ArrayList<String> result = mydb.GetMatchData(key);

		final ListView list = (ListView) findViewById(R.id.listView1);
		final TextView TV = (TextView) findViewById(R.id.textView5);
		final Typeface type = Typeface.createFromAsset(getAssets(),
				"KacstBook.ttf");

		if (result.isEmpty()) {
			TV.setTypeface(type);
			TV.setTextColor(Color.GRAY);
			TV.setText("No Match Found");
		} else {
			list.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, result) {

				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					final View view = super.getView(position, convertView,
							parent);
					final TextView text = (TextView) view
							.findViewById(android.R.id.text1);
					text.setTypeface(type);

					return view;
				}
			});
		}

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getApplicationContext(),
						DoaView.class);
				Bundle bundle = new Bundle();

				bundle.putString("nama", arg0.getItemAtPosition(arg2)
						.toString());

				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Toast.makeText(getApplicationContext(),
						arg0.getItemAtPosition(arg2).toString(),
						Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		mydb.close();
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

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
