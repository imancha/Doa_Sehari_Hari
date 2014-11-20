package me.imancha.doa;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Window;
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

	private static ListView LV;
	private static Intent intent;
	private static Bundle bundle;
	private static Typeface type;
	private static DoaDB mydb;
	private static DoaDBB mydbb;
	private static String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mydb = new DoaDB(this);

		try {
			mydb.CopyDatabaseFromAssets();
		} catch (IOException e) {
			Log.e("copy", "Copy Failed");
		}

		mydb.OpenDatabase();

		ArrayList<String> result = mydb.GetAllData();

		LV = (ListView) findViewById(R.id.listView1);

		LV.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, result) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				final View view = super.getView(position, convertView, parent);
				final TextView text = (TextView) view
						.findViewById(android.R.id.text1);

				type = Typeface.createFromAsset(getAssets(), "KacstBook.ttf");

				text.setTypeface(type);

				return view;
			}
		});

		mydb.close();

		LV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				intent = new Intent(getApplicationContext(), DoaView.class);
				bundle = new Bundle();

				bundle.putString("nama", arg0.getItemAtPosition(arg2)
						.toString());

				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		LV.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				title = arg0.getItemAtPosition(arg2).toString();

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

		registerForContextMenu(LV);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.listView1) {
			menu.setHeaderTitle(title);
			menu.add(Menu.NONE, 0, 0, R.string.view);

			mydbb = new DoaDBB(getApplicationContext());

			if (mydbb.GetData(title).moveToFirst()) {
				menu.add(Menu.NONE, 1, 1, R.string.bookmark_on);
			} else {
				menu.add(Menu.NONE, 2, 1, R.string.bookmark_off);
			}

			mydbb.close();
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			bundle = new Bundle();
			bundle.putString("nama", title);
			intent = new Intent(getApplicationContext(), DoaView.class);
			intent.putExtras(bundle);
			startActivity(intent);

			return true;
		case 1:
			mydbb = new DoaDBB(getApplicationContext());
			mydbb.DeleteData(title);
			mydbb.close();

			Toast.makeText(getApplicationContext(), R.string.remove,
					Toast.LENGTH_SHORT).show();

			return true;
		case 2:
			mydbb = new DoaDBB(getApplicationContext());
			mydbb.InsertData(title);
			mydbb.close();

			Toast.makeText(getApplicationContext(), R.string.add,
					Toast.LENGTH_SHORT).show();

			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// Add icon in the overflow menu
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (NoSuchMethodException e) {
					Log.e("menu", "Show menu icon failed");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

		return super.onMenuOpened(featureId, menu);
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
				bundle = new Bundle();
				bundle.putString("key", arg0.toString());
				intent = new Intent(getApplicationContext(), DoaSearch.class);
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
			intent = new Intent(getApplicationContext(), DoaBookmark.class);
			startActivity(intent);

			return true;
		case R.id.action_help:
			intent = new Intent(getApplicationContext(), DoaHelp.class);
			startActivity(intent);

			return true;
		case R.id.action_about:
			intent = new Intent(getApplicationContext(), DoaAbout.class);
			startActivity(intent);

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
