package me.imancha.doa;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DoaDB mydb = new DoaDB(this);

		try {
			mydb.CopyDatabaseFromAssets();
		} catch (IOException e) {
			Log.e("copy", "Copy Failed");
		}

		mydb.OpenDatabase();

		ArrayList<String> result = mydb.GetAllData();

		final ListView list = (ListView) findViewById(R.id.listView1);

		list.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, result) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				final View view = super.getView(position, convertView, parent);
				final TextView text = (TextView) view
						.findViewById(android.R.id.text1);

				Typeface type = Typeface.createFromAsset(getAssets(),
						"KacstBook.ttf");

				text.setTypeface(type);

				return view;
			}
		});

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getApplicationContext(), DoaView.class);
				Bundle bundle = new Bundle();

				bundle.putString("nama", arg0.getItemAtPosition(arg2).toString());

				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Toast.makeText(getApplicationContext(),
						arg0.getItemAtPosition(arg2).toString(), Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		});

		// Force overflow menu on top right corner
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			Log.e("menu", "Create Menu Failed");
		}

		mydb.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		// Search action bar
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();

		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setSubmitButtonEnabled(false);
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String arg0) {
				Intent intent = new Intent(getApplicationContext(), DoaSearch.class);
				Bundle bundle = new Bundle();

				bundle.putString("key", arg0.toString());

				intent.putExtras(bundle);
				startActivity(intent);

				return true;
			}

			@Override
			public boolean onQueryTextChange(String arg0) {

				return false;
			}
		});

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case R.id.action_bookmark:
				Intent intent = new Intent(getApplicationContext(),
						DoaBookmark.class);
				startActivity(intent);

				return true;
			case R.id.action_help:
				Toast.makeText(getApplicationContext(), R.string.action_help,
						Toast.LENGTH_SHORT).show();
				return true;
			case R.id.action_about:
				Toast.makeText(getApplicationContext(), R.string.action_about,
						Toast.LENGTH_SHORT).show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
