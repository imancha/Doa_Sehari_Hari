package me.imancha.doa;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class DoaSearch extends Activity {

	private ListView LV;
	private TextView TV;
	private Typeface type;
	private Intent intent;
	private Bundle bundle;
	private DoaDB mydb;
	private DoaDBB mydbb;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		bundle = getIntent().getExtras();

		setTitle(getString(R.string.action_search) + ": "
				+ bundle.getString("key"));

		mydb = new DoaDB(this);
		mydb.OpenDatabase();

		ArrayList<String> result = mydb.GetMatchData(bundle.getString("key"));

		type = Typeface.createFromAsset(getAssets(), "KacstBook.ttf");

		if (mydb.GetMatchData(bundle.getString("key")).isEmpty()) {
			TV = (TextView) findViewById(R.id.textView5);
			TV.setTypeface(type);
			TV.setTextColor(Color.GRAY);
			TV.setText(R.string.none);
		} else {
			LV = (ListView) findViewById(R.id.listView1);
			LV.setAdapter(new ArrayAdapter<String>(this,
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

			LV.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent intent = new Intent(getApplicationContext(),
							DoaView.class);
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

			registerForContextMenu(LV);
		}

		mydb.close();
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
